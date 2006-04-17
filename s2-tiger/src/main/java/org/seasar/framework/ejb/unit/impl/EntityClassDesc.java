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

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.AnnotationNotFoundException;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class EntityClassDesc extends AbstractPersistentClassDesc implements
        PersistentClassDesc {

    private static String DEFAULT_DISCRIMINATOR_COLUMN = "DTYPE";

    private String name;

    private String discriminatorColumnName;

    private DiscriminatorType discriminatorType;

    private String discriminatorValue;

    private InheritanceType inheritanceType;

    private EntityClassDesc rootEntityDesc;

    private EntityClassDesc parentEntityDesc;

    private Map<String, PersistentColumn> attribOverrides = new HashMap<String, PersistentColumn>();

    private List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    private List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();

    private List<Class<?>> hierarchy = new ArrayList<Class<?>>();

    private List<PersistentClassDesc> mappedSuperclassDescs = new ArrayList<PersistentClassDesc>();

    public EntityClassDesc(Class<?> entityClass) {
        super(entityClass);

        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            throw new AnnotationNotFoundException(entityClass, Entity.class);
        }
        this.name = StringUtil.isEmpty(entity.name()) ? ClassUtil
                .getShortClassName(entityClass) : entity.name();

        setupTableNames();
        setupSuperclasses();
        setupAccessType();
        setupInheritanceStrategy();
        setupPersistentStateDescs();
        setupJoinColumns();
        setupPrimaryKeyJoinColumns();
    }

    private void setupTableNames() {
        Table primary = persistentClass.getAnnotation(Table.class);
        if (primary == null || StringUtil.isEmpty(primary.name())) {
            addTableName(name);
        } else {
            addTableName(primary.name());
        }

        SecondaryTable secondary = persistentClass
                .getAnnotation(SecondaryTable.class);
        SecondaryTables secondaries = persistentClass
                .getAnnotation(SecondaryTables.class);

        if (secondary != null) {
            addTableName(secondary.name());
        } else if (secondaries != null) {
            for (SecondaryTable each : secondaries.value()) {
                addTableName(each.name());
            }
        }
    }

    private void setupAccessType() {
        for (Class<?> clazz : hierarchy) {
            BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
            for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
                if (propertyDesc.hasReadMethod()) {
                    Method m = propertyDesc.getReadMethod();
                    if (m.isAnnotationPresent(Id.class)
                            || m.isAnnotationPresent(EmbeddedId.class)) {
                        propertyAccessed = true;
                        return;
                    }
                }
            }
        }
    }

    private void setupAttributeOverrides() {
        AttributeOverride ao = persistentClass
                .getAnnotation(AttributeOverride.class);
        AttributeOverrides aos = persistentClass
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

    private void setupPrimaryKeyJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = persistentClass
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = persistentClass
                .getAnnotation(PrimaryKeyJoinColumns.class);
        if (pkColumn != null) {
            pkJoinColumns.add(new PersistentColumnImpl(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(new PersistentColumnImpl(column));
            }
        }

        if (inheritanceType == JOINED) {
            // support only single primary key

            PersistentStateDesc rootId = rootEntityDesc.getIdentifiers().get(0);
            String rootIdName = rootId.getColumn().getName();
            PersistentStateDesc id = getIdentifiers().get(0);
            PersistentColumn idColumn = id.getColumn();

            if (pkJoinColumns.isEmpty()) {
            } else if (pkJoinColumns.size() == 1) {
                PersistentColumn pk = pkJoinColumns.get(0);
                String name = StringUtil.isEmpty(pk.getName()) ? rootIdName
                        : pk.getName();
                String referenced = StringUtil.isEmpty(pk
                        .getReferencedColumnName()) ? rootIdName : pk
                        .getReferencedColumnName();
                id.setColumn(new PersistentColumnImpl(name,
                        idColumn.getTable(), referenced));
            } else {
            }
        }
    }

    private void setupJoinColumns() {
        JoinColumn jColumn = persistentClass.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = persistentClass.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(new PersistentColumnImpl(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(new PersistentColumnImpl(column));
            }
        }
    }

    private void setupSuperclasses() {
        setupHierarchy();
        setupParentEntity();
        setupRootEntity();
        setupMappedsuperClasses();
    }

    private void setupMappedsuperClasses() {
        setupAttributeOverrides();

        for (Class<?> clazz = persistentClass.getSuperclass(); clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.isAnnotationPresent(MappedSuperclass.class)) {
                AttributeOverridableClassDesc ao = new AttributeOverridableClassDesc(
                        clazz, this.getPrimaryTableName(), this
                                .isPropertyAccessed(), Collections
                                .unmodifiableMap(attribOverrides));
                mappedSuperclassDescs.add(ao);
            } else {
                return;
            }
        }
    }

    private void setupRootEntity() {
        if (parentEntityDesc == null) {
            rootEntityDesc = this;
        } else {
            ListIterator<Class<?>> hierarchyIterator = hierarchy
                    .listIterator(hierarchy.size());
            while (hierarchyIterator.hasPrevious()) {
                Class<?> clazz = hierarchyIterator.previous();
                if (clazz.isAnnotationPresent(Entity.class)) {
                    rootEntityDesc = parentEntityDesc.getPersistentClass() == clazz ? parentEntityDesc
                            : new EntityClassDesc(clazz);
                    return;
                }
            }
        }
    }

    private void setupParentEntity() {
        for (Class<?> clazz = persistentClass.getSuperclass(); clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                parentEntityDesc = new EntityClassDesc(clazz);
                return;
            }
        }
    }

    private void setupHierarchy() {
        for (Class<?> clazz = persistentClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            hierarchy.add(clazz);
        }
    }

    private void setupInheritanceStrategy() {
        if (rootEntityDesc == this) {
            Inheritance inheritance = persistentClass
                    .getAnnotation(Inheritance.class);
            if (inheritance != null) {
                inheritanceType = inheritance.strategy();
            } else {
                inheritanceType = SINGLE_TABLE;
            }
            DiscriminatorColumn dc = persistentClass
                    .getAnnotation(DiscriminatorColumn.class);
            if (dc != null) {
                discriminatorColumnName = StringUtil.isEmpty(dc.name()) ? DEFAULT_DISCRIMINATOR_COLUMN
                        : dc.name();
                discriminatorType = dc.discriminatorType();
            } else {
                discriminatorColumnName = DEFAULT_DISCRIMINATOR_COLUMN;
                discriminatorType = STRING;
            }
        } else {
            inheritanceType = rootEntityDesc.inheritanceType;
            discriminatorColumnName = rootEntityDesc.discriminatorColumnName;
            discriminatorType = rootEntityDesc.discriminatorType;
        }
        DiscriminatorValue dv = persistentClass
                .getAnnotation(DiscriminatorValue.class);
        if (dv != null) {
            discriminatorValue = dv.value();
        } else if (discriminatorType == DiscriminatorType.STRING) {
            discriminatorValue = persistentClass.getName();
        }
    }

    @Override
    protected void setupPersistentStateDescs() {
        setupSuperclassStateDescs();
        setupMappedSuperclassStateDescs();
        setupDiscriminator();
        super.setupPersistentStateDescs();
    }

    public PersistentClassDesc getRoot() {
        return rootEntityDesc;
    }

    private void setupSuperclassStateDescs() {
        if (parentEntityDesc == null) {
            return;
        }
        for (PersistentStateDesc stateDesc : parentEntityDesc
                .getPersistentStateDescs()) {

            switch (inheritanceType) {
            case SINGLE_TABLE:
                if (stateDesc instanceof DiscriminatorStateDesc) {
                    continue;
                }
                replaceTableName(stateDesc, rootEntityDesc
                        .getPrimaryTableName());
                break;
            case TABLE_PER_CLASS:
                replaceTableName(stateDesc, tableNames.get(0));
                break;
            case JOINED:
                if (stateDesc.isIdentifier()
                        && stateDesc.getOwner().equals(rootEntityDesc)) {
                    PersistentStateDescImpl subclassId = new PersistentStateDescImpl(
                            this, getPrimaryTableName(), stateDesc
                                    .getAccessor());
                    addPersistentStateDesc(subclassId);
                }
                break;
            }
            addPersistentStateDesc(stateDesc);
        }
    }

    private void replaceTableName(PersistentStateDesc stateDesc,
            String tableName) {
        PersistentColumn old = stateDesc.getColumn();
        PersistentColumn newColumn = new PersistentColumnImpl(old.getName(),
                tableName);
        stateDesc.setColumn(newColumn);
    }

    private void setupMappedSuperclassStateDescs() {
        for (PersistentClassDesc mappedDesc : mappedSuperclassDescs) {
            for (PersistentStateDesc stateDesc : mappedDesc
                    .getPersistentStateDescs()) {
                addPersistentStateDesc(stateDesc);
            }
        }
    }

    private void setupDiscriminator() {
        if (inheritanceType == SINGLE_TABLE) {
            PersistentColumn column = new PersistentColumnImpl(
                    discriminatorColumnName, rootEntityDesc
                            .getPrimaryTableName());
            DiscriminatorStateDesc d = new DiscriminatorStateDesc(this, column,
                    discriminatorType, discriminatorValue);
            addPersistentStateDesc(d);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EntityClassDesc))
            return false;
        EntityClassDesc castOther = (EntityClassDesc) other;
        return this.getPersistentClass() == castOther.getPersistentClass();
    }

    @Override
    public int hashCode() {
        return getPersistentClass().hashCode();
    }

}
