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
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPersistentStateDesc implements
        PersistentStateDesc {

    protected final PersistentClassDesc persistentClassDesc;

    protected final String primaryTableName;

    protected final String stateName;

    protected final Class<?> persistentStateType;

    protected String tableName;

    protected String columnName;

    protected boolean embedded;

    protected boolean identifier;

    protected boolean toOneRelationship;

    protected boolean toManyRelationship;

    protected Class collectionType;

    protected PersistentColumn column;

    protected PersistentClassDesc embeddedClassDesc;

    protected PersistentClassDesc relClassDesc;

    protected Map<String, Column> attribOverrides = new HashMap<String, Column>();

    protected List<PersistentColumn> fkColumns = new ArrayList<PersistentColumn>();

    protected List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    protected List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();

    protected AbstractPersistentStateDesc(
            PersistentClassDesc persistentClassDesc, String stateName,
            Class<?> stateType, String primaryTableName) {
        if (persistentClassDesc == null) {
            throw new EmptyRuntimeException("persistentClassDesc");
        }
        if (stateName == null) {
            throw new EmptyRuntimeException("stateName");
        }
        if (stateType == null) {
            throw new EmptyRuntimeException("stateType");
        }
        if (primaryTableName == null) {
            throw new EmptyRuntimeException("primaryTableName");
        }
        this.persistentClassDesc = persistentClassDesc;
        this.stateName = stateName;
        this.persistentStateType = stateType;
        this.primaryTableName = primaryTableName;
        this.tableName = primaryTableName;
    }

    protected void introspection(AnnotatedElement target) {

        identifier = target.isAnnotationPresent(Id.class)
                || target.isAnnotationPresent(EmbeddedId.class);

        if (isRelationshipAnnotationPresent(target)) {
            setupRelationship(target);
        } else if (target.isAnnotationPresent(Embedded.class)
                || target.isAnnotationPresent(EmbeddedId.class)) {
            setupEmbedded(target);
            setupEmbeddedClassDesc();
        } else {
            setupBasic(target);
        }
        column = new PersistentColumnImpl(tableName, columnName);
    }

    protected boolean isToOneRelationship(AnnotatedElement target) {
        return target.isAnnotationPresent(OneToOne.class)
                || target.isAnnotationPresent(ManyToOne.class);
    }

    protected boolean isToManyRelationship(AnnotatedElement target) {
        return target.isAnnotationPresent(OneToMany.class)
                || target.isAnnotationPresent(ManyToMany.class);
    }

    protected boolean isRelationshipAnnotationPresent(AnnotatedElement target) {
        return isToOneRelationship(target) || isToManyRelationship(target);
    }

    protected void setupRelationship(AnnotatedElement target) {
        if (isToManyRelationship(target)) {
            toManyRelationship = true;
        } else {
            toOneRelationship = true;
        }

        PrimaryKeyJoinColumn pkColumn = target
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = target
                .getAnnotation(PrimaryKeyJoinColumns.class);
        if (pkColumn != null) {
            pkJoinColumns.add(createColumn(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(createColumn(column));
            }
        }

        JoinColumn jColumn = target.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = target.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(createColumn(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(createColumn(column));
            }
        }

    }

    protected PersistentColumn createColumn(JoinColumn jColumn) {
        return new PersistentColumnImpl(jColumn.table(), jColumn.name(),
                jColumn.referencedColumnName());
    }

    protected PersistentColumn createColumn(PrimaryKeyJoinColumn pkColumn) {
        return new PersistentColumnImpl(null, pkColumn.name(), pkColumn
                .referencedColumnName());
    }

    protected void setupEmbedded(AnnotatedElement target) {
        embedded = true;
        AttributeOverride ao = target.getAnnotation(AttributeOverride.class);
        AttributeOverrides aos = target.getAnnotation(AttributeOverrides.class);
        if (ao != null) {
            attribOverrides.put(ao.name(), ao.column());
        } else if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                attribOverrides.put(each.name(), each.column());
            }
        }
    }

    protected void setupBasic(AnnotatedElement target) {
        Column c = target.getAnnotation(Column.class);
        if (c == null) {
            tableName = primaryTableName;
            columnName = stateName;
        } else {
            tableName = StringUtil.isEmpty(c.table()) ? primaryTableName : c
                    .table();
            columnName = StringUtil.isEmpty(c.name()) ? stateName : c.name();
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
        if (embedded) {
            embeddedClassDesc = new AttributeOverridableClassDesc(
                    persistentStateType, tableName, isProperty(), Collections
                            .unmodifiableMap(attribOverrides));
        }
    }

    public String getTableName() {
        return tableName.toUpperCase();
    }

    public PersistentColumn getColumn() {
        return column;
    }

    public void setColumn(PersistentColumn column) {
        this.column = column;
    }

    public String getStateName() {
        return stateName;
    }

    public Class<?> getPersistentStateType() {
        return persistentStateType;
    }

    public boolean isCollection() {
        return collectionType != null;
    }

    public Class<?> getCollectionType() {
        return collectionType;
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
        this.relClassDesc = persistentClassDesc;
    }

    public void setupForeignKeyColumns() {
        List<PersistentStateDesc> identifiers = relClassDesc.getIdentifiers();
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
                    column = StringUtil.isEmpty(jc.getName()) ? stateName + "_"
                            + id.getName() : jc.getName();
                    addForeignKeyColumn(new PersistentColumnImpl(table, column,
                            id.getName()));
                }
            } else {
                column = StringUtil.isEmpty(jc.getName()) ? stateName + "_"
                        + referencedColumn : jc.getName();
                if (relClassDesc.hasReferencedStateDesc(referencedColumn)) {
                    addForeignKeyColumn(new PersistentColumnImpl(table, column,
                            referencedColumn));
                }
            }
        } else {
            for (PersistentColumn jc : joinColumns) {
                String column = jc.getName();
                String table = StringUtil.isEmpty(jc.getTableName()) ? primaryTableName
                        : jc.getTableName();
                String referencedColumn = jc.getReferencedColumnName();

                if (relClassDesc.hasReferencedStateDesc(referencedColumn)) {
                    addForeignKeyColumn(new PersistentColumnImpl(table, column,
                            referencedColumn));
                }
            }
        }
    }

    protected void setupDefaultForeignKeyColumns(
            List<PersistentStateDesc> identifiers) {
        for (PersistentStateDesc idDesc : identifiers) {
            PersistentColumn id = idDesc.getColumn();
            String columnName = stateName + "_" + id.getName();
            addForeignKeyColumn(new PersistentColumnImpl(primaryTableName,
                    columnName, id.getName()));
        }
    }

    public PersistentClassDesc getRelationshipClassDesc() {
        return relClassDesc;
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        return embeddedClassDesc;
    }

    public void addForeignKeyColumn(PersistentColumn column) {
        fkColumns.add(column);
    }

    public boolean hasReferencedColumn(String columnName) {
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
        return ClassUtil.getShortClassName(clazz) + "." + stateName;
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("stateName=");
        buf.append(stateName);
        buf.append(",stateType=");
        buf.append(persistentStateType.getName());
        buf.append(",tableName=");
        buf.append(tableName);
        buf.append(",perperty=");
        buf.append(isProperty());
        buf.append(",column.getName()=");
        buf.append(column != null ? column.getName() : "null");
        buf.append(",column.getTableName()=");
        buf.append(column != null ? column.getTableName() : "null");
        return buf.toString();
    }

    public abstract Object getValue(Object target);

    public abstract void setValue(Object target, Object value);

    public abstract boolean isProperty();

}
