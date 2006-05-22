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
import org.seasar.framework.ejb.unit.impl.DefaultProxiedObjectResolver;
import org.seasar.framework.ejb.unit.impl.EntityIntrospector;

/**
 * @author taedium
 * 
 */
public class EntityReader implements DataReader {

    private static final Object PRESENT = new Object();

    private final Map<Object, Object> processedEntities = new IdentityHashMap<Object, Object>();

    private final Stack<EntityClassDesc> processingClassDescs = new Stack<EntityClassDesc>();

    private final DataSet dataSet = new DataSetImpl();

    private final ProxiedObjectResolver resolver;

    private final EntityIntrospector introspector;

    private final boolean readsRelationships;

    protected EntityReader(EntityIntrospector introspector,
            boolean readsRelationships, ProxiedObjectResolver resolver) {
        this.introspector = introspector;
        this.readsRelationships = readsRelationships;
        this.resolver = resolver;
    }

    public EntityReader(Object entity, boolean readsRelationships) {
        this(entity, readsRelationships, DefaultProxiedObjectResolver.INSTANCE);
    }

    public EntityReader(Object entity, boolean readsRelationships,
            ProxiedObjectResolver resolver) {

        this(new EntityIntrospector(entity, readsRelationships, resolver),
                readsRelationships, resolver);

        EntityClassDesc classDesc = introspector.getEntityClassDesc(entity);

        if (readsRelationships) {
            for (EntityClassDesc each : introspector.getAllEntityClassDescs()) {
                setupColumns(each);
            }
        } else {
            setupColumns(classDesc);
        }

        startSetupRows(classDesc, entity);
    }

    protected void setupColumns(EntityClassDesc classDesc) {
        for (String tableName : classDesc.getTableNames()) {
            List<PersistentStateDesc> stateDescs = classDesc
                    .getPersistentStateDescsByTableName(tableName);

            for (PersistentStateDesc stateDesc : stateDescs) {
                Class<?> stateClass = stateDesc.getPersistenceTargetClass();

                if (stateDesc instanceof BasicStateDesc) {
                    setupColumn(stateDesc.getColumn(), stateClass);

                } else if (stateDesc instanceof EmbeddedStateDesc) {
                    EmbeddedStateDesc embeddedStateDesc = (EmbeddedStateDesc) stateDesc;
                    for (PersistentStateDesc embedded : embeddedStateDesc
                            .getPersistentStateDesc()) {
                        Class<?> type = embedded.getPersistenceTargetClass();
                        setupColumn(embedded.getColumn(), type);
                    }

                } else if (stateDesc instanceof ToOneRelationshipStateDesc) {
                    ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) stateDesc;
                    for (ForeignKey fk : toOne.getForeignKeys()) {
                        setupColumn(fk.getColumn(), stateClass);
                    }
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

    protected void startSetupRows(EntityClassDesc classDesc, Object entity) {
        if (isProcessing(classDesc) || isProcessed(entity)) {
            return;
        }
        pushProcessingClassDesc(classDesc);
        setupRows(classDesc, entity);
        popProcessingClassDesc();
        putProcessedEntity(entity);
    }

    protected void setupRows(EntityClassDesc classDesc, Object entity) {
        Map<Object, PersistentStateDesc> relationships = new IdentityHashMap<Object, PersistentStateDesc>();

        for (String tableName : classDesc.getTableNames()) {
            List<PersistentStateDesc> descs = classDesc
                    .getPersistentStateDescsByTableName(tableName);
            DataRow row = null;

            for (PersistentStateDesc stateDesc : descs) {
                Object state = stateDesc.getValue(entity, resolver);
                if (state == null) {
                    continue;
                }

                if (stateDesc instanceof BasicStateDesc) {
                    row = getRow(tableName, row);
                    Class<?> type = stateDesc.getPersistenceTargetClass();
                    setupRowValue(row, stateDesc.getColumn(), type, state);

                } else if (stateDesc instanceof EmbeddedStateDesc) {
                    EmbeddedStateDesc embeddedStateDesc = (EmbeddedStateDesc) stateDesc;
                    for (PersistentStateDesc each : embeddedStateDesc
                            .getPersistentStateDesc()) {
                        row = getRow(tableName, row);
                        Class<?> type = each.getPersistenceTargetClass();
                        Object value = each.getValue(state, resolver);
                        setupRowValue(row, each.getColumn(), type, value);
                    }

                } else if (stateDesc instanceof ToOneRelationshipStateDesc) {
                    ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) stateDesc;
                    for (ForeignKey fk : toOne.getForeignKeys()) {
                        row = getRow(tableName, row);
                        Class<?> type = fk.getPersistenceTargetClass();
                        Object value = fk.getValue(state, resolver);
                        setupRowValue(row, fk.getColumn(), type, value);
                    }
                    
                }

                if (readsRelationships) {
                    if (stateDesc instanceof ToManyRelationshipStateDesc
                            || stateDesc instanceof ToOneRelationshipStateDesc) {
                        relationships.put(state, stateDesc);
                    }
                }
            }

            if (row != null) {
                PersistentDiscriminatorColumn dc = classDesc
                        .getDiscriminatorColumn(tableName);
                if (dc != null) {
                    Class<?> type = dc.getPersistenceTargetClass();
                    setupRowValue(row, dc, type, dc.getValue());
                }
                row.setState(RowStates.UNCHANGED);
            }
        }

        for (Object each : relationships.keySet()) {
            setupRelationshipRows(classDesc.getPersistentClass(), relationships
                    .get(each), each);
        }
    }

    protected void setupRowValue(DataRow row, PersistentColumn column,
            Class stateType, Object value) {

        ColumnType ct = ColumnTypes.getColumnType(stateType);
        row.setValue(column.getName(), ct.convert(value, null));
    }

    protected void setupRelationshipRows(Class source,
            PersistentStateDesc stateDesc, Object value) {

        if (stateDesc instanceof ToManyRelationshipStateDesc) {
            for (Object element : introspector.getElements(value)) {
                if (element == null) {
                    continue;
                }
                Object real = resolver.unproxy(element);
                EntityClassDesc relationship = introspector
                        .getEntityClassDesc(real);
                startSetupRows(relationship, real);
            }
        } else {
            EntityClassDesc relationship = introspector
                    .getEntityClassDesc(value);
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

    protected void pushProcessingClassDesc(EntityClassDesc classDesc) {
        processingClassDescs.push(classDesc.getRoot());
    }

    protected boolean isProcessing(EntityClassDesc classDesc) {
        return processingClassDescs.contains(classDesc.getRoot());
    }

    protected void popProcessingClassDesc() {
        processingClassDescs.pop();
    }

    protected EntityIntrospector getEntityIntrospector() {
        return introspector;
    }

    protected DataRow getRow(String tableName, DataRow row) {
        if (row == null) {
            return dataSet.getTable(tableName).addRow();
        }
        return row;
    }

    public DataSet read() {
        return dataSet;
    }
}
