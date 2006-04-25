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
package org.seasar.framework.ejb.unit.impl;

import static org.seasar.framework.ejb.unit.PersistentStateType.BASIC;
import static org.seasar.framework.ejb.unit.PersistentStateType.EMBEDDED;
import static org.seasar.framework.ejb.unit.PersistentStateType.NONE;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_MANY;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_ONE;

import static javax.persistence.EnumType.ORDINAL;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescImpl implements PersistentStateDesc {

    private static final Set<Class<?>> BASICS = new HashSet<Class<?>>();

    private final PersistentClassDesc persistentClassDesc;

    private final String primaryTableName;

    private final PersistentStateAccessor accessor;

    private final String name;

    private final AnnotatedElement element;

    private Class<?> persistentStateClass;

    private boolean identifier;

    private boolean oneToOne;

    private boolean owningSide;

    private Class collectionClass;

    private PersistentClassDesc embeddedClassDesc;

    private PersistentStateType stateType = NONE;

    private Enumerated enumerated;

    private Map<String, PersistentColumn> attribOverrides = new HashMap<String, PersistentColumn>();

    private PersistentColumn persistentColumn;

    private List<PersistentColumn> fkColumns = new ArrayList<PersistentColumn>();

    private List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    private List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();

    static {
        BASICS.add(byte.class);
        BASICS.add(short.class);
        BASICS.add(int.class);
        BASICS.add(long.class);
        BASICS.add(double.class);
        BASICS.add(float.class);
        BASICS.add(Byte.class);
        BASICS.add(Short.class);
        BASICS.add(Integer.class);
        BASICS.add(Long.class);
        BASICS.add(Double.class);
        BASICS.add(Float.class);
        BASICS.add(String.class);
        BASICS.add(BigInteger.class);
        BASICS.add(BigDecimal.class);
        BASICS.add(java.util.Date.class);
        BASICS.add(Calendar.class);
        BASICS.add(java.sql.Date.class);
        BASICS.add(java.sql.Time.class);
        BASICS.add(Timestamp.class);
        BASICS.add(byte[].class);
        BASICS.add(Byte[].class);
        BASICS.add(char[].class);
        BASICS.add(Character[].class);
        BASICS.add(Enum.class);
        BASICS.add(Serializable.class);
    }

    public PersistentStateDescImpl(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {

        if (persistentClassDesc == null) {
            throw new EmptyRuntimeException("persistentClassDesc");
        }
        if (primaryTableName == null) {
            throw new EmptyRuntimeException("primaryTableName");
        }
        if (accessor == null) {
            throw new EmptyRuntimeException("accessor");
        }
        this.persistentClassDesc = persistentClassDesc;
        this.primaryTableName = primaryTableName;
        this.accessor = accessor;
        this.name = accessor.getName();
        this.persistentStateClass = accessor.getPersistentStateClass();
        this.element = accessor.getAnnotatedElement();
        introspection();
    }

    private void introspection() {
        identifier = element.isAnnotationPresent(Id.class)
                || element.isAnnotationPresent(EmbeddedId.class);

        if (isCollection()) {
            collectionClass = extractCollectionType(accessor.getGenericType());
        }

        setupColumn();
        setupJoinColumns();
        setupPkJoinColumns();
        setupAttributeOverrides();
        setupEnumerated();
        setupStateType();
    }

    private void setupEnumerated() {
        enumerated = element.getAnnotation(Enumerated.class);
        if (Enum.class.isAssignableFrom(persistentStateClass)) {
            if (enumerated == null || enumerated.value() == ORDINAL) {
                persistentStateClass = int.class;
            } else {
                persistentStateClass = String.class;
            }
        }
    }

    private void setupStateType() {
        OneToOne oneToOneAnn = element.getAnnotation(OneToOne.class);
        OneToMany oneToManyAnn = element.getAnnotation(OneToMany.class);
        ManyToOne manyToOneAnn = element.getAnnotation(ManyToOne.class);
        ManyToMany manyToManayAnn = element.getAnnotation(ManyToMany.class);

        oneToOne = oneToOneAnn != null;

        if (manyToOneAnn != null || oneToOneAnn != null
                && StringUtil.isEmpty(oneToOneAnn.mappedBy())
                || oneToManyAnn != null
                && StringUtil.isEmpty(oneToManyAnn.mappedBy())
                || manyToManayAnn != null
                && StringUtil.isEmpty(manyToManayAnn.mappedBy())) {
            owningSide = true;
        }

        if (oneToOneAnn != null || manyToOneAnn != null) {
            stateType = TO_ONE;
        } else if (oneToManyAnn != null || manyToManayAnn != null) {
            stateType = TO_MANY;
        } else if (element.isAnnotationPresent(Embedded.class)
                || element.isAnnotationPresent(EmbeddedId.class)) {
            stateType = EMBEDDED;
            setupEmbeddedClassDesc();
        } else if (isBasic()) {
            stateType = BASIC;
        } else {
            stateType = NONE;
        }
    }

    private boolean isBasic() {
        if (BASICS.contains(persistentStateClass)) {
            return true;
        }
        for (Class<?> basicClass : BASICS) {
            if (basicClass.isAssignableFrom(persistentStateClass)) {
                return true;
            }
        }
        return false;
    }

    private void setupColumn() {
        if (isBasic()) {
            Column columnAnn = element.getAnnotation(Column.class);
            if (columnAnn != null) {
                String tableName = StringUtil.isEmpty(columnAnn.table()) ? primaryTableName
                        : columnAnn.table();
                String columnName = StringUtil.isEmpty(columnAnn.name()) ? name
                        : columnAnn.name();
                persistentColumn = new PersistentColumnImpl(columnName,
                        tableName);
            } else {
                persistentColumn = new PersistentColumnImpl(name,
                        primaryTableName);
            }
        } else {
            persistentColumn = new PersistentColumnImpl(null, primaryTableName);
        }
    }

    private void setupAttributeOverrides() {
        AttributeOverride ao = element.getAnnotation(AttributeOverride.class);
        AttributeOverrides aos = element
                .getAnnotation(AttributeOverrides.class);
        if (ao != null) {
            attribOverrides.put(ao.name(),
                    new PersistentColumnImpl(ao.column()));
        } else if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                attribOverrides.put(each.name(), new PersistentColumnImpl(each
                        .column()));
            }
        }
    }

    private void setupJoinColumns() {
        JoinColumn jColumn = element.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = element.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(new PersistentColumnImpl(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(new PersistentColumnImpl(column));
            }
        }
    }

    private void setupPkJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = element
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = element
                .getAnnotation(PrimaryKeyJoinColumns.class);

        if (pkColumn != null) {
            pkJoinColumns.add(new PersistentColumnImpl(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(new PersistentColumnImpl(column));
            }
        }
    }

    private Class extractCollectionType(Type t) {
        if (t != null && t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] genTypes = pt.getActualTypeArguments();
            if (genTypes.length == 1 && genTypes[0] instanceof Class) {
                return (Class) genTypes[0];
            } else if (genTypes.length == 2 && genTypes[1] instanceof Class) {
                return (Class) genTypes[1];
            }
        }
        return null;
    }

    private void setupEmbeddedClassDesc() {
        embeddedClassDesc = new AttributeOverridableClassDesc(
                persistentStateClass, primaryTableName, isPropertyAccessed(),
                Collections.unmodifiableMap(attribOverrides));
    }

    public PersistentClassDesc getPersistentClassDesc() {
        return persistentClassDesc;
    }

    public PersistentColumn getColumn() {
        return persistentColumn;
    }

    public void setColumn(PersistentColumn column) {
        if (column == null) {
            throw new EmptyRuntimeException("column");
        }
        this.persistentColumn = column;
    }

    public String getName() {
        return name;
    }

    public Class<?> getPersistentStateClass() {
        return persistentStateClass;
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(persistentStateClass)
                || Map.class.isAssignableFrom(persistentStateClass);
    }

    public Class<?> getCollectionClass() {
        return collectionClass;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public PersistentStateAccessor getAccessor() {
        return accessor;
    }

    public void setupForeignKeyColumns(PersistentClassDesc relationship) {
        if (!owningSide || (!pkJoinColumns.isEmpty() && oneToOne)) {
            return;
        }

        List<PersistentColumn> refIdColumns = new ArrayList<PersistentColumn>();
        for (PersistentStateDesc stateDesc : relationship.getIdentifiers()) {
            refIdColumns.add(stateDesc.getColumn());
        }

        if (!joinColumns.isEmpty()) {
            if (hasReferencedColumnName(joinColumns)) {
                List<PersistentColumn> refAllColumns = new ArrayList<PersistentColumn>();
                for (PersistentStateDesc stateDesc : relationship
                        .getPersistentStateDescs()) {
                    refAllColumns.add(stateDesc.getColumn());
                }
                setupFkColumsByReferencedColumnName(joinColumns, refAllColumns);
            } else {
                setupFkColums(joinColumns, refIdColumns);
            }

        } else {
            setupDefaultFkColumn(refIdColumns);
        }
    }

    private boolean hasReferencedColumnName(List<PersistentColumn> columns) {
        for (PersistentColumn column : columns) {
            if (column.getReferencedColumnName() == null) {
                return false;
            }
        }
        return true;
    }

    private void setupFkColumsByReferencedColumnName(
            List<PersistentColumn> columns, List<PersistentColumn> refColumns) {

        for (PersistentColumn column : columns) {
            PersistentColumn referencedColumn = null;

            for (PersistentColumn refColumn : refColumns) {
                if (column.getReferencedColumnName()
                        .equals(refColumn.getName())) {
                    referencedColumn = refColumn;
                }
            }

            if (referencedColumn != null) {
                PersistentColumn fk = createFkColumn(column, referencedColumn);
                addForeignKeyColumn(fk);
            }
        }
    }

    private void setupFkColums(List<PersistentColumn> columns,
            List<PersistentColumn> refIdColumns) {

        if (columns.size() != refIdColumns.size()) {
            return;
        }

        for (int i = 0; i < columns.size(); i++) {
            PersistentColumn column = columns.get(i);
            PersistentColumn referencedColumn = refIdColumns.get(i);

            if (referencedColumn != null) {
                PersistentColumn fk = createFkColumn(column, referencedColumn);
                addForeignKeyColumn(fk);
            }
        }
    }

    private PersistentColumn createFkColumn(PersistentColumn column,
            PersistentColumn referencedColumn) {

        PersistentColumn newColumn = new PersistentColumnImpl(column);
        String referencedName = referencedColumn.getName();
        newColumn.setNameIfNull(getFkColumnName(referencedName));
        newColumn.setTableIfNull(primaryTableName);
        newColumn.setReferencedColumnNameIfNull(referencedName);
        return newColumn;
    }

    private void setupDefaultFkColumn(List<PersistentColumn> referencedIdColumns) {

        for (PersistentColumn referencedColumn : referencedIdColumns) {
            String referencedName = referencedColumn.getName();
            PersistentColumn fk = new PersistentColumnImpl(
                    getFkColumnName(referencedName), primaryTableName,
                    referencedName);
            addForeignKeyColumn(fk);
        }
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        return embeddedClassDesc;
    }

    public List<PersistentStateDesc> getEmbeddedStateDescs() {
        return embeddedClassDesc.getPersistentStateDescs();
    }

    private void addForeignKeyColumn(PersistentColumn column) {
        fkColumns.add(column);
    }

    public boolean hasColumn(String columnName) {
        if (getColumn().getName() == null) {
            return false;
        }
        return this.getColumn().getName().equalsIgnoreCase(columnName);
    }

    public List<PersistentColumn> getForeignKeyColumns() {
        return fkColumns;
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("name=");
        buf.append(name);
        buf.append(",stateClass=");
        buf.append(persistentStateClass.getName());
        return buf.toString();
    }

    public Object getValue(Object target) {
        Object value = accessor.getValue(target);
        if (value instanceof Enum) {
            Enum enumValue = (Enum) value;
            if (persistentStateClass == int.class) {
                return enumValue.ordinal();
            } else {
                return enumValue.toString();
            }
        }
        return value;
    }

    public void setValue(Object target, Object value) {
        accessor.setValue(target, value);
    }

    public boolean isRelationOwningSide() {
        return owningSide;
    }

    private boolean isPropertyAccessed() {
        return accessor instanceof PropertyAccessor;
    }

    public PersistentStateType getPersistentStateType() {
        return stateType;
    }

    private String getFkColumnName(String referencedColumnName) {
        return name + "_" + referencedColumnName;
    }

}
