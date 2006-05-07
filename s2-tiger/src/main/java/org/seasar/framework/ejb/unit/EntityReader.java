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

import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.seasar.framework.ejb.unit.PersistentStateType.TO_MANY;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.ejb.unit.impl.DefaultProxiedObjectResolver;
import org.seasar.framework.ejb.unit.impl.EntityIntrospector;

/**
 * @author taedium
 * 
 */
public class EntityReader implements DataReader {

    private static final Object PRESENT = new Object();

    private final Map<Object, Object> processedEntities = new IdentityHashMap<Object, Object>();

    private final Stack<PersistentClassDesc> processingClassDescs = new Stack<PersistentClassDesc>();

    private final DataSet dataSet = new DataSetImpl();
    
    private final ProxiedObjectResolver resolver;

    private final EntityIntrospector introspector;

    protected EntityReader(EntityIntrospector introspector, ProxiedObjectResolver resolver) {
        this.introspector = introspector;
        this.resolver = resolver;
    }
    
    public EntityReader(Object entity) {
        this(entity, DefaultProxiedObjectResolver.INSTANCE);
    }

    public EntityReader(Object entity, ProxiedObjectResolver resolver) {
        this(new EntityIntrospector(entity, resolver), resolver);
        
        for (PersistentClassDesc classDesc : introspector
                .getAllPersistentClassDescs()) {
            setupColumns(classDesc);
        }
        PersistentClassDesc classDesc = introspector
                .getPersistentClassDesc(entity);
        startSetupRows(classDesc, entity);
    }

    protected void setupColumns(PersistentClassDesc classDesc) {
        for (String tableName : classDesc.getTableNames()) {
            List<PersistentStateDesc> stateDescs = classDesc
                    .getPersistentStateDescsByTableName(tableName);

            for (PersistentStateDesc stateDesc : stateDescs) {
                Class<?> stateClass = stateDesc.getPersistenceTargetClass();

                switch (stateDesc.getPersistentStateType()) {
                case BASIC:
                    setupColumn(stateDesc.getColumn(), stateClass);
                    break;
                case EMBEDDED:
                    for (PersistentStateDesc embedded : stateDesc
                            .getEmbeddedStateDescs()) {
                        Class<?> type = embedded.getPersistenceTargetClass();
                        setupColumn(embedded.getColumn(), type);
                    }
                    break;
                case TO_ONE:
                    for (PersistentColumn fkColumn : stateDesc
                            .getForeignKeyColumns()) {
                        setupColumn(fkColumn, stateClass);
                    }
                    break;
                }
            }

            PersistentDiscriminatorColumn dc = classDesc
                    .getDiscriminatorColumn(tableName);
            if (dc != null) {
                setupColumn(dc, dc.getPersistenceTargetClass());
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

    protected void startSetupRows(PersistentClassDesc classDesc, Object entity) {
        if (isProcessing(classDesc) || isProcessed(entity)) {
            return;
        }
        pushProcessingClassDesc(classDesc);
        setupRows(classDesc, entity);
        popProcessingClassDesc();
        putProcessedEntity(entity);
    }

    protected void setupRows(PersistentClassDesc classDesc, Object entity) {
        Map<Object, PersistentStateDesc> relationships = new LinkedHashMap<Object, PersistentStateDesc>();

        for (String tableName : classDesc.getTableNames()) {
            List<PersistentStateDesc> descs = classDesc
                    .getPersistentStateDescsByTableName(tableName);
            DataRow row = null;

            for (PersistentStateDesc stateDesc : descs) {
                Object state = stateDesc.getValue(entity, resolver);
                if (state == null) {
                    continue;
                }

                row = row == null ? dataSet.getTable(tableName).addRow() : row;

                switch (stateDesc.getPersistentStateType()) {
                case BASIC:
                    PersistentColumn column = stateDesc.getColumn();
                    Class<?> type = stateDesc.getPersistenceTargetClass();
                    setupRowValue(row, column, type, state);
                    break;
                case EMBEDDED:
                    for (PersistentStateDesc embState : stateDesc
                            .getEmbeddedStateDescs()) {
                        PersistentColumn embcolumn = embState.getColumn();
                        Class<?> embType = embState.getPersistenceTargetClass();
                        Object value = embState.getValue(state, resolver);
                        setupRowValue(row, embcolumn, embType, value);
                    }
                    break;
                case TO_ONE:
                    setupForeignKeys(row, stateDesc, state);
                    relationships.put(state, stateDesc);
                    break;
                case TO_MANY:
                    relationships.put(state, stateDesc);
                    break;
                }
            }

            PersistentDiscriminatorColumn dc = classDesc
                    .getDiscriminatorColumn(tableName);
            if (row != null && dc != null) {
                Class<?> type = dc.getPersistenceTargetClass();
                setupRowValue(row, dc, type, dc.getValue());
            }
            row.setState(RowStates.UNCHANGED);
        }

        for (Object each : relationships.keySet()) {
            setupRelationshipRows(classDesc.getPersistentClass(), relationships
                    .get(each), each);
        }
    }

    protected void setupForeignKeys(DataRow row, PersistentStateDesc stateDesc,
            Object relEntity) {

        for (PersistentJoinColumn fk : stateDesc.getForeignKeyColumns()) {
            Class<?> type = null;
            Object value = null;
            if (relEntity != null) {
                PersistentClassDesc relationship = introspector
                        .getPersistentClassDesc(relEntity);
                PersistentStateDesc referenced = relationship
                        .getPersistentStateDescByColumnName(fk
                                .getReferencedColumnName());

                type = referenced.getPersistenceTargetClass();
                value = referenced.getValue(relEntity, resolver);
            }
            setupRowValue(row, fk, type, value);
        }
    }

    protected void setupRowValue(DataRow row, PersistentColumn column,
            Class stateType, Object value) {

        ColumnType ct = ColumnTypes.getColumnType(stateType);
        row.setValue(column.getName(), ct.convert(value, null));
    }

    protected void setupRelationshipRows(Class source,
            PersistentStateDesc stateDesc, Object value) {

        if (stateDesc.getPersistentStateType() == TO_MANY) {
            for (Object element : introspector.getElements(value)) {
                if (element == null) {
                    continue;
                }
                Object real = resolver.unproxy(element);
                PersistentClassDesc relationship = introspector
                        .getPersistentClassDesc(real);
                startSetupRows(relationship, real);
            }
        } else {
            PersistentClassDesc relationship = introspector
                    .getPersistentClassDesc(value);
            startSetupRows(relationship, value);
        }
    }

    protected void putProcessedEntity(Object entity) {
        processedEntities.put(entity, PRESENT);
    }

    protected boolean isProcessed(Object entity) {
        return processedEntities.containsKey(entity);
    }

    protected void releaseProcessedEntity(Object entity) {
        processedEntities.remove(entity);
    }

    protected void pushProcessingClassDesc(PersistentClassDesc classDesc) {
        processingClassDescs.push(classDesc.getRoot());
    }

    protected boolean isProcessing(PersistentClassDesc classDesc) {
        return processingClassDescs.contains(classDesc.getRoot());
    }

    protected void popProcessingClassDesc() {
        processingClassDescs.pop();
    }
    
    protected EntityIntrospector getEntityIntrospector() {
        return introspector;
    }

    public DataSet read() {
        return dataSet;
    }
}
