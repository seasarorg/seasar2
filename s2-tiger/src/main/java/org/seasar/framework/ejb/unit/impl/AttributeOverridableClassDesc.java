package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
class AttributeOverridableClassDesc implements PersistentClassDesc {

    private final Class<?> persistentClass;

    private final String primaryTableName;

    private final boolean propertyAccessed;

    private List<PersistentStateDesc> stateDescs = new ArrayList<PersistentStateDesc>();

    private Map<String, PersistentStateDesc> stateDescsByName = new HashMap<String, PersistentStateDesc>();

    private Map<String, Column> attribOverrides;

    private final List<String> tableNames = new ArrayList<String>();

    public AttributeOverridableClassDesc(Class persistentClass,
            String primaryTableName, boolean propertyAccessed,
            Map<String, Column> overridingAttribute) {

        if (persistentClass == null) {
            throw new EmptyRuntimeException("persistentClass");
        }
        this.persistentClass = persistentClass;
        this.primaryTableName = primaryTableName;
        this.propertyAccessed = propertyAccessed;
        this.attribOverrides = overridingAttribute;
        setupPersistenceState();
    }

    private void setupPersistenceState() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(persistentClass);
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            PersistentStateDesc ps = null;
            if (propertyAccessed) {
                if (beanDesc.hasPropertyDesc(field.getName())) {
                    PropertyDesc pd = beanDesc.getPropertyDesc(field.getName());
                    ps = new PersistentPropertyDesc(pd, primaryTableName);
                } else {
                    continue;
                }
            } else {
                ps = new PersistentFieldDesc(field, primaryTableName);
            }

            if (ps != null) {
                PersistentStateDesc newPs = null;
                if (attribOverrides.containsKey(ps.getStateName())) {
                    newPs = override(ps, attribOverrides
                            .get(ps.getStateName()));
                } else {
                    newPs = ps;
                }
                stateDescs.add(newPs);
                stateDescsByName.put(newPs.getStateName(), newPs);
            }
        }
    }

    private PersistentStateDesc override(PersistentStateDesc original,
            Column column) {

        AttributeOverridedStateDesc aos = new AttributeOverridedStateDesc(
                original);
        aos.setColumnName(column.name().toUpperCase());
        aos.setTableName(column.table().toUpperCase());
        return aos;
    }

    public String getName() {
        return ClassUtil.getShortClassName(persistentClass);
    }

    public Class<?> getPersistentClass() {
        return persistentClass;
    }

    public PersistentStateDesc getStateDesc(int index) {
        return stateDescs.get(index);
    }

    public PersistentStateDesc getStateDesc(String entityStateName) {
        return stateDescsByName.get(entityStateName);
    }

    public int getStateDescSize() {
        return stateDescs.size();
    }

    public String getTableName(int index) {
        return tableNames.get(index);
    }

    public int getTableSize() {
        return tableNames.size();
    }

    public boolean isPropertyAccessed() {
        return propertyAccessed;
    }

    private static class AttributeOverridedStateDesc implements
            PersistentStateDesc {

        private PersistentStateDesc original;

        private String columnName;

        private String tableName;

        public AttributeOverridedStateDesc(PersistentStateDesc original) {
            this.original = original;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Class<?> getCollectionType() {
            return original.getCollectionType();
        }

        public String getColumnName() {
            if (StringUtil.isEmpty(columnName)) {
                return original.getColumnName();
            }
            return columnName;
        }

        public String getStateName() {
            return original.getStateName();
        }

        public Class<?> getPersistentStateType() {
            return original.getPersistentStateType();
        }

        public String getTableName() {
            if (StringUtil.isEmpty(tableName)) {
                return original.getTableName();
            }
            return tableName;
        }

        public Object getValue(Object target) {
            return original.getValue(target);
        }

        public boolean hasColumnName() {
            return getColumnName() != null;
        }

        public boolean hasTableName() {
            return getTableName() != null;
        }

        public boolean isCollection() {
            return original.isCollection();
        }

        public boolean isEmbedded() {
            return original.isEmbedded();
        }

        public boolean isPersistent() {
            return original.isPersistent();
        }

        public boolean isProperty() {
            return original.isProperty();
        }

        public boolean isRelationship() {
            return original.isRelationship();
        }

        public void setValue(Object target, Object value) {
            original.setValue(target, value);
        }

        public PersistentClassDesc createPersistentClass() {
            return original.createPersistentClass();
        }
    }
}