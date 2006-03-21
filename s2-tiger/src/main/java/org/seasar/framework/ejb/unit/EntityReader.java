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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

        List<PersistentClassDesc> relationships = new ArrayList<PersistentClassDesc>();
        Map<PersistentClassDesc, Object> relationshipValues = new HashMap<PersistentClassDesc, Object>();

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
            Object stateValue = stateDesc.getValue(entity);
            if (stateDesc.isToOneRelationship()
                    || stateDesc.isToManyRelationship()) {
                PersistentClassDesc you = stateDesc.getRelationshipClassDesc();
                Class from = me.getPersistentClass();
                Class to = you.getPersistentClass();
                if (!paths.contains(new Path(to, from)) && !isEmpty(stateValue)) {
                    paths.add(new Path(from, to));
                    relationships.add(you);
                    relationshipValues.put(you, stateValue);
                }
            } else if (stateDesc.isEmbedded()) {
                PersistentClassDesc embDesc = stateDesc.getEmbeddedClassDesc();
                for (int j = 0; j < embDesc.getPersistentStateDescSize(); j++) {
                    PersistentStateDesc embeddedState = embDesc
                            .getPersistentStateDesc(j);
                    Object embValue = embeddedState.getValue(stateValue);
                    setupRow(row, embeddedState, embValue);
                }
            }
            if (stateDesc.isEmbedded() || stateDesc.isToManyRelationship()) {
                continue;
            }
            setupRow(row, stateDesc, stateValue);
        }
        row.setState(RowStates.UNCHANGED);

        setupRelationshipRows(relationships, relationshipValues);
    }

    protected void setupRow(DataRow row, PersistentStateDesc stateDesc,
            Object value) {
        if (stateDesc.isToOneRelationship()) {
            for (int i = 0; i < stateDesc.getForeignKeyColumnSize(); i++) {
                PersistentColumn fk = stateDesc.getForeignKeyColumn(i);
                Object converted = null;
                if (value != null) {
                    PersistentClassDesc rel = stateDesc
                            .getRelationshipClassDesc();
                    if (rel
                            .hasReferencedStateDesc(fk
                                    .getReferencedColumnName())) {
                        PersistentStateDesc refState = rel
                                .getReferencedStateDesc(fk
                                        .getReferencedColumnName());
                        Class refStateType = refState.getPersistentStateType();
                        ColumnType ct = ColumnTypes.getColumnType(refStateType);
                        converted = ct.convert(refState.getValue(value), null);
                    }
                }
                row.setValue(fk.getName(), converted);
            }
        } else {
            PersistentColumn column = stateDesc.getColumn();
            Class stateType = stateDesc.getPersistentStateType();
            ColumnType ct = ColumnTypes.getColumnType(stateType);
            row.setValue(column.getName(), ct.convert(value, null));
        }
    }

    protected void setupRelationshipRows(
            List<PersistentClassDesc> relationships,
            Map<PersistentClassDesc, Object> relationshipValues) {

        for (PersistentClassDesc classDesc : relationships) {
            Object value = relationshipValues.get(classDesc);
            if (value instanceof Collection) {
                for (Object element : (Collection) value) {
                    setupRow(classDesc, element);
                }
            } else {
                setupRow(classDesc, value);
            }
        }
    }

    protected boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
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
        private final Class from;

        private final Class to;

        Path(Class from, Class to) {
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
