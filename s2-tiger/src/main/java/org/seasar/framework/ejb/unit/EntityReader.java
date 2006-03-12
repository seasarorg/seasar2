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
import org.seasar.framework.ejb.unit.impl.EntityClassDesc;

/**
 * @author taedium
 * 
 */
public class EntityReader implements DataReader {

    private final DataSet dataSet = new DataSetImpl();

    private final Collection<Class<?>> processedClasses = new ArrayList<Class<?>>();

    private final Collection<Object> processedEntities = new ArrayList<Object>();

    private final Collection<Path> paths = new HashSet<Path>();

    protected EntityReader() {
    }

    public EntityReader(Object entity) {
        PersistentClassDesc psh = new EntityClassDesc(entity.getClass());
        setupColumns(psh);
        setupRow(psh, entity);
    }

    protected void setupColumns(PersistentClassDesc psh) {
        if (processedClasses.contains(psh.getPersistentClass())) {
            return;
        }
        processedClasses.add(psh.getPersistentClass());

        for (int i = 0; i < psh.getStateDescSize(); ++i) {
            PersistentStateDesc ps = psh.getStateDesc(i);
            if (ps.isPersistent()) {
                if (ps.isRelationship() || ps.isEmbedded()) {
                    setupColumns(ps.createPersistentClass());
                    continue;
                }
                DataTable dataTable = null;
                String tableName = ps.getTableName();
                if (dataSet.hasTable(tableName)) {
                    dataTable = dataSet.getTable(tableName);
                } else {
                    dataTable = dataSet.addTable(tableName);
                }
                Class<?> type = ps.getPersistentStateType();
                dataTable.addColumn(ps.getColumnName(), ColumnTypes
                        .getColumnType(type));
            }
        }
    }

    protected void setupRow(PersistentClassDesc me, Object entity) {
        if (processedEntities.contains(entity)) {
            return;
        }
        processedEntities.add(entity);

        List<PersistentClassDesc> relationships = new ArrayList<PersistentClassDesc>();
        Map<PersistentClassDesc, Object> relationshipValues = new HashMap<PersistentClassDesc, Object>();

        DataRow row = null;
        for (int i = 0; i < me.getStateDescSize(); ++i) {
            PersistentStateDesc ps = me.getStateDesc(i);
            if (ps.isPersistent()) {
                Class type = ps.getPersistentStateType();
                Object value = ps.getValue(entity);
                if (ps.isRelationship()) {
                    PersistentClassDesc you = ps.createPersistentClass();
                    Class from = me.getPersistentClass();
                    Class to = you.getPersistentClass();
                    if (!paths.contains(new Path(to, from))
                            && isNotEmpty(value)) {
                        paths.add(new Path(from, to));
                        relationships.add(you);
                        relationshipValues.put(you, value);
                    }
                    continue;
                }
                DataTable dataTable = dataSet.getTable(ps.getTableName());
                row = (row == null) ? dataTable.addRow() : row;
                if (ps.isEmbedded()) {
                    setupRow(row, ps.createPersistentClass(), value);
                } else {
                    ColumnType ct = ColumnTypes.getColumnType(type);
                    row.setValue(ps.getColumnName(), ct.convert(value, null));
                }
            }
        }
        row.setState(RowStates.UNCHANGED);

        for (PersistentClassDesc holder : relationships) {
            Object value = relationshipValues.get(holder);
            if (value instanceof Collection) {
                for (Object element : (Collection) value) {
                    setupRow(holder, element);
                }
            } else {
                setupRow(holder, value);
            }
        }
    }

    private boolean isNotEmpty(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
            return false;
        }
        return true;
    }

    private void setupRow(DataRow row, PersistentClassDesc psh, Object obj) {
        for (int j = 0; j < psh.getStateDescSize(); ++j) {
            PersistentStateDesc ps = psh.getStateDesc(j);
            if (ps.isPersistent()) {
                Class type = ps.getPersistentStateType();
                Object value = ps.getValue(obj);
                ColumnType ct = ColumnTypes.getColumnType(type);
                row.setValue(ps.getColumnName(), ct.convert(value, null));
            }
        }
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
