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
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

import static org.seasar.framework.ejb.unit.PersistentStateType.BASIC;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_ONE;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_MANY;
import static org.seasar.framework.ejb.unit.PersistentStateType.EMBEDDED;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescImpl implements PersistentStateDesc {

    protected final PersistentClassDesc persistentClassDesc;

    protected final String primaryTableName;

    protected final PersistentStateAccessor accessor;

    protected final String name;

    protected final Class persistentStateClass;

    protected final AnnotatedElement element;

    protected boolean embedded;

    protected boolean identifier;

    protected boolean toOneRelationship;

    protected boolean toManyRelationship;

    protected boolean owningSide;

    protected Class collectionClass;

    protected PersistentColumn column;

    protected PersistentClassDesc embeddedClassDesc;

    protected PersistentStateType stateType;

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
        this.name = accessor.getName();
        this.persistentStateClass = accessor.getPersistentStateClass();
        this.element = accessor.getAnnotatedElement();
        introspection();
    }

    protected void introspection() {

        identifier = element.isAnnotationPresent(Id.class)
                || element.isAnnotationPresent(EmbeddedId.class);

        embedded = element.isAnnotationPresent(Embedded.class)
                || element.isAnnotationPresent(EmbeddedId.class);

        setupRelationship();

        if (toOneRelationship || toManyRelationship) {
            setupJoinColumns();
            setupPkJoinColumns();
        }
        if (embedded) {
            setupAttributeOverrides();
            setupEmbeddedClassDesc();
        }
        setupColumn();

        if (embedded) {
            stateType = EMBEDDED;
        } else if (toOneRelationship) {
            stateType = TO_ONE;
        } else if (toManyRelationship) {
            stateType = TO_MANY;
        } else {
            stateType = BASIC;
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
                || oneToMany != null
                && StringUtil.isEmpty(oneToMany.mappedBy())
                || manyToOne != null || manyToMany != null
                && StringUtil.isEmpty(manyToMany.mappedBy())) {
            owningSide = true;
        }
    }

    protected void setupColumn() {
        Column columnAnn = element.getAnnotation(Column.class);
        String tableName;
        String columnName;
        if (columnAnn == null) {
            tableName = primaryTableName;
            columnName = name;
        } else {
            tableName = StringUtil.isEmpty(columnAnn.table()) ? primaryTableName
                    : columnAnn.table();
            columnName = StringUtil.isEmpty(columnAnn.name()) ? name
                    : columnAnn.name();
        }
        column = new PersistentColumnImpl(columnName, tableName);
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

    protected void setupJoinColumns() {
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

    protected void setupPkJoinColumns() {
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
        embeddedClassDesc = new AttributeOverridableClassDesc(
                persistentStateClass, primaryTableName, isPropertyAccessed(),
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

    public String getName() {
        return name;
    }

    public Class<?> getPersistentStateClass() {
        return persistentStateClass;
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(persistentStateClass);
    }

    public Class<?> getCollectionClass() {
        return extractCollectionType(accessor.getGenericType());
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public PersistentStateAccessor getAccessor() {
        return accessor;
    }

    public void setupForeignKeyColumns(PersistentClassDesc relationship) {
        List<PersistentStateDesc> identifiers = relationship.getIdentifiers();
        if (identifiers.size() == 1
                && identifiers.get(0).getPersistentStateType() == EMBEDDED) {
            PersistentClassDesc embedded = identifiers.get(0)
                    .getEmbeddedClassDesc();
            identifiers = embedded.getIdentifiers();
        }

        if (!pkJoinColumns.isEmpty()) {
            setupPKJoinForeignKeyColums(identifiers);
        } else if (!joinColumns.isEmpty()) {
            setupJoinForeignKeyColums(identifiers, relationship);
        } else {
            setupDefaultForeignKeyColumns(identifiers);
        }
    }

    protected void setupPKJoinForeignKeyColums(
            List<PersistentStateDesc> identifiers) {
        // not yet supported
    }

    protected void setupJoinForeignKeyColums(
            List<PersistentStateDesc> identifiers,
            PersistentClassDesc relationshipClassDesc) {

        if (joinColumns.size() == 1) {
            PersistentColumn jc = joinColumns.get(0);
            String column = null;
            String table = StringUtil.isEmpty(jc.getTable()) ? primaryTableName
                    : jc.getTable();
            String referencedColumn = jc.getReferencedColumnName();

            if (StringUtil.isEmpty(referencedColumn)) {
                for (PersistentStateDesc idDesc : identifiers) {
                    PersistentColumn id = idDesc.getColumn();
                    column = StringUtil.isEmpty(jc.getName()) ? name + "_"
                            + id.getName() : jc.getName();
                    addForeignKeyColumn(new PersistentColumnImpl(column, table,
                            id.getName()));
                }
            } else {
                column = StringUtil.isEmpty(jc.getName()) ? name + "_"
                        + referencedColumn : jc.getName();
                for (PersistentStateDesc stateDesc : relationshipClassDesc
                        .getPersistentStateDescs()) {
                    if (stateDesc.hasColumn(referencedColumn)) {
                        addForeignKeyColumn(new PersistentColumnImpl(column,
                                table, referencedColumn));
                        break;
                    }
                }
            }
        } else {
            for (PersistentColumn jc : joinColumns) {
                String column = jc.getName();
                String table = StringUtil.isEmpty(jc.getTable()) ? primaryTableName
                        : jc.getTable();
                String referencedColumn = jc.getReferencedColumnName();

                for (PersistentStateDesc stateDesc : relationshipClassDesc
                        .getPersistentStateDescs()) {

                    if (stateDesc.hasColumn(referencedColumn)) {
                        addForeignKeyColumn(new PersistentColumnImpl(column,
                                table, referencedColumn));
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
            String columnName = name + "_" + id.getName();
            addForeignKeyColumn(new PersistentColumnImpl(columnName,
                    primaryTableName, id.getName()));
        }
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        return embeddedClassDesc;
    }

    protected void addForeignKeyColumn(PersistentColumn column) {
        fkColumns.add(column);
    }

    public boolean hasColumn(String columnName) {
        if (column.getName() == null) {
            return false;
        }
        return this.column.getName().equalsIgnoreCase(columnName);
    }

    public List<PersistentColumn> getForeignKeyColumns() {
        return fkColumns;
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("stateName=");
        buf.append(name);
        buf.append(",stateClass=");
        buf.append(persistentStateClass.getName());
        return buf.toString();
    }

    public Object getValue(Object target) {
        return accessor.getValue(target);
    }

    public void setValue(Object target, Object value) {
        accessor.setValue(target, value);
    }

    public boolean isRelationOwningSide() {
        return owningSide;
    }

    protected boolean isPropertyAccessed() {
        return accessor instanceof PropertyAccessor;
    }

    public PersistentStateType getPersistentStateType() {
        return stateType;
    }

}
