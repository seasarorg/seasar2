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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.ejb.unit.impl.EntityClassDescFactory;

/**
 * @author taedium
 * 
 */
public class EntityReader implements DataReader {

    private final DataSet dataSet = new DataSetImpl();

    private final Collection<Class<?>> processedClasses = new HashSet<Class<?>>();

    private final Collection<Object> processedEntities = new HashSet<Object>();

    private final Collection<Path> paths = new HashSet<Path>();

    protected EntityReader() {
    }

    public EntityReader(Object entity) {
        PersistentClassDesc classDesc = EntityClassDescFactory
                .getEntityClassDesc(entity.getClass());
        setupColumns(classDesc);
        setupRow(classDesc, entity);
    }

    protected void setupColumns(PersistentClassDesc classDesc) {
        if (processedClasses.contains(classDesc.getPersistentClass())) {
            return;
        }
        processedClasses.add(classDesc.getPersistentClass());

        for (int i = 0; i < classDesc.getPersistentStateDescSize(); ++i) {
            PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(i);
            Class<?> stateType = stateDesc.getPersistentStateType();

            if (stateDesc.isEmbedded()) {
                setupColumns(stateDesc.getEmbeddedClassDesc());
            } else if (stateDesc.isToManyRelationship()) {
                setupColumns(stateDesc.getRelationshipClassDesc());
            } else if (stateDesc.isToOneRelationship()) {
                setupColumns(stateDesc.getRelationshipClassDesc());
                for (int j = 0; j < stateDesc.getForeignKeyColumnSize(); j++) {
                    PersistentColumn fk = stateDesc.getForeignKeyColumn(j);
                    String tableName = fk.getTableName();
                    String columnName = fk.getName();
                    setupColumns(tableName, columnName, stateType);
                }
            } else {
                PersistentColumn column = stateDesc.getColumn();
                setupColumns(column.getTableName(), column.getName(), stateType);
            }
        }
    }

    protected void setupColumns(String tableName, String columnName,
            Class<?> stateType) {

        DataTable dataTable = null;
        if (dataSet.hasTable(tableName)) {
            dataTable = dataSet.getTable(tableName);
        } else {
            dataTable = dataSet.addTable(tableName);
        }
        ColumnType ct = ColumnTypes.getColumnType(stateType);
        dataTable.addColumn(columnName, ct);
    }

    protected void setupRow(PersistentClassDesc me, Object entity) {
        if (processedEntities.contains(entity)) {
            return;
        }
        processedEntities.add(entity);

        Map<Object, PersistentClassDesc> relationshipValues = new LinkedHashMap<Object, PersistentClassDesc>();

        DataRow row = null;
        for (int i = 0; i < me.getPersistentStateDescSize(); ++i) {
            PersistentStateDesc stateDesc = me.getPersistentStateDesc(i);
            String curr = stateDesc.getColumn().getTableName();
            if (i == 0) {
                DataTable dataTable = dataSet.getTable(curr);
                row = dataTable.addRow();
            } else {
                String prev = me.getPersistentStateDesc(i - 1).getColumn()
                        .getTableName();
                if (!curr.equals(prev)) {
                    DataTable dataTable = dataSet.getTable(curr);
                    row = dataTable.addRow();
                }
            }

            Object state = stateDesc.getValue(entity);
            if (stateDesc.isToOneRelationship()
                    || stateDesc.isToManyRelationship()) {
                PersistentClassDesc you = stateDesc.getRelationshipClassDesc();
                if (!paths.contains(new Path(you, me)) && !isStateEmpty(state)) {
                    paths.add(new Path(me, you));
                    relationshipValues.put(state, you);
                }
                if (stateDesc.isToOneRelationship()) {
                    setupForeignKeys(row, stateDesc, state);
                }
            } else if (stateDesc.isEmbedded()) {
                PersistentClassDesc embDesc = stateDesc.getEmbeddedClassDesc();
                for (int j = 0; j < embDesc.getPersistentStateDescSize(); j++) {
                    PersistentStateDesc embeddedState = embDesc
                            .getPersistentStateDesc(j);
                    Object embValue = embeddedState.getValue(state);
                    setupRow(row, embeddedState, embValue);
                }
            } else {
                setupRow(row, stateDesc, state);
            }
        }
        row.setState(RowStates.UNCHANGED);

        setupRelationshipRows(relationshipValues);
    }

    protected void setupRow(DataRow row, PersistentStateDesc stateDesc,
            Object value) {
        PersistentColumn column = stateDesc.getColumn();
        Class stateType = stateDesc.getPersistentStateType();
        ColumnType ct = ColumnTypes.getColumnType(stateType);
        row.setValue(column.getName(), ct.convert(value, null));
    }

    protected void setupForeignKeys(DataRow row, PersistentStateDesc stateDesc,
            Object value) {
        for (int i = 0; i < stateDesc.getForeignKeyColumnSize(); i++) {
            PersistentColumn fk = stateDesc.getForeignKeyColumn(i);
            Object converted = null;
            if (value != null) {
                PersistentClassDesc rel = stateDesc.getRelationshipClassDesc();
                for (int j = 0; j < rel.getPersistentStateDescSize(); j++) {
                    PersistentStateDesc refState = rel.getPersistentStateDesc(j);
                    if (refState.hasColumn(fk.getReferencedColumnName())) {
                        Class refStateType = refState.getPersistentStateType();
                        ColumnType ct = ColumnTypes.getColumnType(refStateType);
                        converted = ct.convert(refState.getValue(value), null);
                    }
                }
            }
            row.setValue(fk.getName(), converted);
        }
    }

    protected void setupRelationshipRows(
            Map<Object, PersistentClassDesc> relationshipValues) {

        for (Object value : relationshipValues.keySet()) {
            PersistentClassDesc classDesc = relationshipValues.get(value);
            if (value instanceof Collection) {
                for (Object element : (Collection) value) {
                    setupRow(classDesc, element);
                }
            } else {
                setupRow(classDesc, value);
            }
        }
    }

    protected boolean isStateEmpty(Object state) {
        if (state == null) {
            return true;
        }
        if (state instanceof Collection && ((Collection) state).isEmpty()) {
            return true;
        }
        return false;
    }

    protected void release(Object entity) {
        processedEntities.remove(entity);
    }

    public DataSet read() {
        return dataSet;
    }

    private static class Path {
        private final PersistentClassDesc from;

        private final PersistentClassDesc to;

        Path(PersistentClassDesc from, PersistentClassDesc to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Path))
                return false;
            Path castOther = (Path) other;
            return this.from == castOther.from && this.to == castOther.to;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 37 * result + from.hashCode();
            result = 37 * result + to.hashCode();
            return result;
        }

    }
}
