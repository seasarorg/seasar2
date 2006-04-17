/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.ejb.unit;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.ejb.unit.impl.EntityIntrospector;

/**
 * @author taedium
 * 
 */
public class EntityReader implements DataReader {

    private static final Object PRESENT = new Object();

    private final Map<Object, Object> processedEntities = new IdentityHashMap<Object, Object>();

    private final Stack<PersistentClassDesc> stack = new Stack<PersistentClassDesc>();

    private final DataSet dataSet = new DataSetImpl();

    protected EntityIntrospector introspector;

    protected EntityReader(EntityIntrospector introspector) {
        this.introspector = introspector;
    }

    public EntityReader(Object entity) {
        this(entity, null);
    }
    
    public EntityReader(Object entity, ProxiedObjectResolver resolver) {
        this(new EntityIntrospector(entity, resolver));

        for (PersistentClassDesc classDesc : introspector
                .getAllPersistentClassDescs()) {
            setupColumns(classDesc);
        }
        PersistentClassDesc classDesc = introspector
                .getPersistentClassDesc(entity.getClass());
        setupRows(classDesc, entity);
    }

    protected void setupColumns(PersistentClassDesc classDesc) {
        for (PersistentStateDesc stateDesc : classDesc
                .getPersistentStateDescs()) {
            Class<?> stateClass = stateDesc.getPersistentStateClass();

            switch (stateDesc.getPersistentStateType()) {
            case NONE:
            case TO_MANY:
                continue;
            case BASIC:
                setupColumn(stateDesc.getColumn(), stateClass);
                break;
            case EMBEDDED:
                setupColumns(stateDesc.getEmbeddedClassDesc());
                break;
            case TO_ONE:
                for (PersistentColumn fkColumn : stateDesc
                        .getForeignKeyColumns()) {
                    setupColumn(fkColumn, stateClass);
                }
                break;
            }
        }
    }

    protected void setupColumn(PersistentColumn column, Class<?> stateType) {
        DataTable dataTable = null;
        if (dataSet.hasTable(column.getTable())) {
            dataTable = dataSet.getTable(column.getTable());
        } else {
            dataTable = dataSet.addTable(column.getTable());
        }
        if (dataTable.hasColumn(column.getName())) {
            return;
        }
        ColumnType ct = ColumnTypes.getColumnType(stateType);
        dataTable.addColumn(column.getName(), ct);
    }

    protected void setupRows(PersistentClassDesc classDesc, Object entity) {
        if (isProcessed(entity)) {
            return;
        }
        stack.push(classDesc.getRoot());

        Map<Object, PersistentStateDesc> relationships = new LinkedHashMap<Object, PersistentStateDesc>();

        for (String tableName : classDesc.getTableNames()) {
            List<PersistentStateDesc> descs = classDesc
                    .getPersistentStateDescsByTableName(tableName);
            DataRow row = null;
            for (PersistentStateDesc stateDesc : descs) {
                Object state = introspector.unproxy(stateDesc.getValue(entity));
                if (state == null) {
                    continue;
                }

                if (row == null) {
                    row = dataSet.getTable(tableName).addRow();
                }

                switch (stateDesc.getPersistentStateType()) {
                case NONE:
                    continue;
                case BASIC:
                    setupRowValue(row, stateDesc, state);
                    break;
                case EMBEDDED:
                    for (PersistentStateDesc embeddedState : stateDesc
                            .getEmbeddedClassDesc().getPersistentStateDescs()) {
                        Object embValue = embeddedState.getValue(state);
                        setupRowValue(row, embeddedState, embValue);
                    }
                    break;
                case TO_ONE:
                    relationships.put(state, stateDesc);
                    setupForeignKeys(row, stateDesc, state);
                    break;
                case TO_MANY:
                    relationships.put(state, stateDesc);
                    break;
                }
                
                row.setState(RowStates.UNCHANGED);
            }
        }

        for (Object each : relationships.keySet()) {
            setupRelationshipRows(classDesc.getPersistentClass(), relationships
                    .get(each), each);
        }

        stack.pop();
    }

    protected void setupForeignKeys(DataRow row, PersistentStateDesc stateDesc,
            Object value) {
        for (PersistentColumn fk : stateDesc.getForeignKeyColumns()) {
            if (value == null) {
                continue;
            }
            PersistentClassDesc rel = introspector.getPersistentClassDesc(value
                    .getClass());
            Object converted = null;
            for (PersistentStateDesc refState : rel.getPersistentStateDescs()) {
                if (!refState.hasColumn(fk.getReferencedColumnName())) {
                    continue;
                }
                Class refStateType = refState.getPersistentStateClass();
                ColumnType ct = ColumnTypes.getColumnType(refStateType);
                converted = ct.convert(refState.getValue(value), null);
                break;
            }
            row.setValue(fk.getName(), converted);
        }
    }

    protected void setupRowValue(DataRow row, PersistentStateDesc stateDesc,
            Object value) {
        PersistentColumn column = stateDesc.getColumn();
        Class stateType = stateDesc.getPersistentStateClass();
        ColumnType ct = ColumnTypes.getColumnType(stateType);
        row.setValue(column.getName(), ct.convert(value, null));
    }

    protected void setupRelationshipRows(Class source,
            PersistentStateDesc stateDesc, Object value) {

        if (stateDesc.isCollection()) {

            for (Object element : (Collection) value) {
                if (element == null) {
                    continue;
                }
                Object real = introspector.unproxy(element);
                PersistentClassDesc relationship = introspector
                        .getPersistentClassDesc(real.getClass());
                if (isValidRelationship(relationship.getRoot())) {
                    setupRows(relationship, real);
                }
            }
        } else {
            PersistentClassDesc relationship = introspector
                    .getPersistentClassDesc(value.getClass());
            if (isValidRelationship(relationship.getRoot())) {
                setupRows(relationship, value);
            }
        }
    }

    protected boolean isProcessed(Object entity) {
        return processedEntities.put(entity, PRESENT) != null;
    }

    protected boolean isValidRelationship(PersistentClassDesc relationship) {
        return relationship.equals(stack.peek())
                || !stack.contains(relationship);
    }

    protected void release(Object entity) {
        processedEntities.remove(entity);
    }

    public DataSet read() {
        return dataSet;
    }
}
