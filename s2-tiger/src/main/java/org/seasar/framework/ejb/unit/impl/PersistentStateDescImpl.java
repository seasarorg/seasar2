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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
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
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescImpl implements PersistentStateDesc {

    protected final PersistentClassDesc persistentClassDesc;

    protected final String primaryTableName;

    protected final PersistentStateAccessor accessor;

    protected final AnnotatedElement element;

    protected boolean embedded;

    protected boolean identifier;

    protected boolean toOneRelationship;

    protected boolean toManyRelationship;

    protected boolean owningSide;

    protected Class collectionType;

    protected PersistentColumn column;

    protected PersistentClassDesc embeddedClassDesc;

    protected PersistentClassDesc relationshipClassDesc;

    protected Map<String, PersistentColumn> attribOverrides = new HashMap<String, PersistentColumn>();

    protected List<PersistentColumn> fkColumns = new ArrayList<PersistentColumn>();

    protected List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    protected List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();

    protected PersistentStateDescImpl(PersistentClassDesc persistentClassDesc,
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
        this.element = accessor.getAnnotatedElement();
        introspection();
    }

    protected void introspection() {

        identifier = element.isAnnotationPresent(Id.class)
                || element.isAnnotationPresent(EmbeddedId.class);
        embedded = element.isAnnotationPresent(Embedded.class)
                || element.isAnnotationPresent(EmbeddedId.class);

        setupRelationship();
        setupColumn();
        setupAttributeOverrides();
        setupJoinColumns();
        setupPkJoinColumns();

        if (embedded) {
            setupEmbeddedClassDesc();
        }
    }

    protected void setupRelationship() {
        OneToOne oneToOne = element.getAnnotation(OneToOne.class);
        OneToMany oneToMany = element.getAnnotation(OneToMany.class);
        ManyToOne manyToOne = element.getAnnotation(ManyToOne.class);
        ManyToMany manyToMany = element.getAnnotation(ManyToMany.class);

        toOneRelationship = oneToOne != null || manyToOne != null;
        toManyRelationship = oneToMany != null || manyToMany != null;

        if (oneToOne != null && StringUtil.isEmpty(oneToOne.mappedBy())
                || oneToMany != null && StringUtil.isEmpty(oneToMany.mappedBy())
                || manyToOne != null
                || manyToMany != null && StringUtil.isEmpty(manyToMany.mappedBy())) {
            owningSide = true;
        }
    }

    protected void setupColumn() {
        Column columnAnn = element.getAnnotation(Column.class);
        String tableName;
        String columnName;
        if (columnAnn == null) {
            tableName = primaryTableName;
            columnName = accessor.getName();
        } else {
            tableName = StringUtil.isEmpty(columnAnn.table()) ? primaryTableName
                    : columnAnn.table();
            columnName = StringUtil.isEmpty(columnAnn.name()) ? accessor
                    .getName() : columnAnn.name();
        }
        column = new PersistentColumnImpl(tableName, columnName);
    }

    protected void setupAttributeOverrides() {
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

    protected Class extractCollectionType(Type t) {
        if (t != null && t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] genTypes = pt.getActualTypeArguments();
            if (genTypes.length == 1 && genTypes[0] instanceof Class) {
                return (Class) genTypes[0];
            }
        }
        return null;
    }

    protected void setupEmbeddedClassDesc() {
        embeddedClassDesc = new AttributeOverridableClassDesc(accessor
                .getPersistentStateType(), primaryTableName, isProperty(),
                Collections.unmodifiableMap(attribOverrides));
    }

    public PersistentClassDesc getOwner() {
        return persistentClassDesc;
    }

    public PersistentColumn getColumn() {
        return column;
    }

    public void setColumn(PersistentColumn column) {
        if (column == null) {
            throw new EmptyRuntimeException("column");
        }
        this.column = column;
    }

    public String getStateName() {
        return accessor.getName();
    }

    public Class<?> getPersistentStateType() {
        return accessor.getPersistentStateType();
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(accessor
                .getPersistentStateType());
    }

    public Class<?> getCollectionType() {
        return extractCollectionType(accessor.getGenericType());
    }

    public boolean isToOneRelationship() {
        return toOneRelationship;
    }

    public boolean isToManyRelationship() {
        return toManyRelationship;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public void setRelationshipClassDesc(PersistentClassDesc persistentClassDesc) {
        this.relationshipClassDesc = persistentClassDesc;
    }

    public PersistentStateAccessor getAccessor() {
        return accessor;
    }

    public void setupForeignKeyColumns() {
        List<PersistentStateDesc> identifiers = relationshipClassDesc
                .getIdentifiers();
        if (identifiers.size() == 1 && identifiers.get(0).isEmbedded()) {
            PersistentClassDesc embedded = identifiers.get(0)
                    .getEmbeddedClassDesc();
            identifiers = embedded.getIdentifiers();
        }

        if (!pkJoinColumns.isEmpty()) {
            setupPKJoinForeignKeyColums(identifiers);
        } else if (!joinColumns.isEmpty()) {
            setupJoinForeignKeyColums(identifiers);
        } else {
            setupDefaultForeignKeyColumns(identifiers);
        }
    }

    protected void setupPKJoinForeignKeyColums(
            List<PersistentStateDesc> identifiers) {
        // not yet supported
    }

    protected void setupJoinForeignKeyColums(
            List<PersistentStateDesc> identifiers) {

        if (joinColumns.size() == 1) {
            PersistentColumn jc = joinColumns.get(0);
            String column = null;
            String table = StringUtil.isEmpty(jc.getTableName()) ? primaryTableName
                    : jc.getTableName();
            String referencedColumn = jc.getReferencedColumnName();

            if (StringUtil.isEmpty(referencedColumn)) {
                for (PersistentStateDesc idDesc : identifiers) {
                    PersistentColumn id = idDesc.getColumn();
                    column = StringUtil.isEmpty(jc.getName()) ? accessor
                            .getName()
                            + "_" + id.getName() : jc.getName();
                    addForeignKeyColumn(new PersistentColumnImpl(table, column,
                            id.getName()));
                }
            } else {
                column = StringUtil.isEmpty(jc.getName()) ? accessor.getName()
                        + "_" + referencedColumn : jc.getName();
                for (int i = 0; i < relationshipClassDesc
                        .getPersistentStateDescSize(); i++) {
                    PersistentStateDesc stateDesc = relationshipClassDesc
                            .getPersistentStateDesc(i);
                    if (stateDesc.hasColumn(referencedColumn)) {
                        addForeignKeyColumn(new PersistentColumnImpl(table,
                                column, referencedColumn));
                        break;
                    }
                }
            }
        } else {
            for (PersistentColumn jc : joinColumns) {
                String column = jc.getName();
                String table = StringUtil.isEmpty(jc.getTableName()) ? primaryTableName
                        : jc.getTableName();
                String referencedColumn = jc.getReferencedColumnName();

                for (int i = 0; i < relationshipClassDesc
                        .getPersistentStateDescSize(); i++) {
                    PersistentStateDesc stateDesc = relationshipClassDesc
                            .getPersistentStateDesc(i);
                    if (stateDesc.hasColumn(referencedColumn)) {
                        addForeignKeyColumn(new PersistentColumnImpl(table,
                                column, referencedColumn));
                        break;
                    }
                }
            }
        }
    }

    protected void setupDefaultForeignKeyColumns(
            List<PersistentStateDesc> identifiers) {
        for (PersistentStateDesc idDesc : identifiers) {
            PersistentColumn id = idDesc.getColumn();
            String columnName = accessor.getName() + "_" + id.getName();
            addForeignKeyColumn(new PersistentColumnImpl(primaryTableName,
                    columnName, id.getName()));
        }
    }

    public PersistentClassDesc getRelationshipClassDesc() {
        return relationshipClassDesc;
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        return embeddedClassDesc;
    }

    public void addForeignKeyColumn(PersistentColumn column) {
        fkColumns.add(column);
    }

    public boolean hasColumn(String columnName) {
        if (column.getName() == null) {
            return false;
        }
        return this.column.getName().equalsIgnoreCase(columnName);
    }

    public int getForeignKeyColumnSize() {
        return fkColumns.size();
    }

    public PersistentColumn getForeignKeyColumn(int index) {
        return fkColumns.get(index);
    }

    public String getPathName() {
        Class clazz = persistentClassDesc.getPersistentClass();
        return ClassUtil.getShortClassName(clazz) + "." + accessor.getName();
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("stateName=");
        buf.append(accessor.getName());
        buf.append(",stateType=");
        buf.append(accessor.getPersistentStateType().getName());
        buf.append(",column.getName()=");
        buf.append(column != null ? column.getName() : "null");
        buf.append(",column.getTableName()=");
        buf.append(column != null ? column.getTableName() : "null");
        return buf.toString();
    }

    public Object getValue(Object target) {
        return accessor.getValue(target);
    }

    public void setValue(Object target, Object value) {
        accessor.setValue(target, value);
    }

    public boolean isProperty() {
        return accessor instanceof PropertyAccessor;
    }

    public boolean isDescriminator() {
        return false;
    }
    
    public boolean isOwningSide() {
        return owningSide;
    }
}
