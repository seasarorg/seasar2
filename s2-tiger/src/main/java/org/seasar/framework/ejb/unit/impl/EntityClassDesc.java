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
import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
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

    private static Map<String, Column> EMPTY_OVERRIDES = new HashMap<String, Column>();

    private String name;

    private String discriminatorColumnName;

    private DiscriminatorType discriminatorType;

    private String discriminatorValue;

    private InheritanceType inheritanceType;

    private EntityClassDesc rootEntity;

    private Map<String, Column> attribOverrides = new HashMap<String, Column>();

    protected List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    protected List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();

    private List<Class<?>> hierarchy = new ArrayList<Class<?>>();

    private List<PersistentClassDesc> superClassDescs = new ArrayList<PersistentClassDesc>();

    public EntityClassDesc(Class<?> entityClass) {
        super(entityClass);

        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            throw new AnnotationNotFoundException(entityClass, Entity.class);
        }
        this.name = StringUtil.isEmpty(entity.name()) ? ClassUtil
                .getShortClassName(entityClass) : entity.name();

        setupTableNames();
        setupAttributeOverrides();
        setupSuperclasses();
        setupAccessType();
        setupInheritanceStrategy();
        setupSuperClassPersistentStateDescs();
        setupDiscriminator();
        setupPersistentStateDescs();
        setupJoinColumns();
        setupPrimaryKeyJoinColumns();
    }

    private void setupTableNames() {
        Table primary = persistentClass.getAnnotation(Table.class);
        if (primary == null || StringUtil.isEmpty(primary.name())) {
            tableNames.add(name);
        } else {
            tableNames.add(primary.name());
        }

        SecondaryTable secondary = persistentClass
                .getAnnotation(SecondaryTable.class);
        if (secondary != null) {
            tableNames.add(secondary.name());
        }

        SecondaryTables secondaries = persistentClass
                .getAnnotation(SecondaryTables.class);
        if (secondaries != null) {
            for (SecondaryTable each : secondaries.value()) {
                tableNames.add(each.name());
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
            attribOverrides.put(ao.name(), ao.column());
        } else if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                attribOverrides.put(each.name(), each.column());
            }
        }
    }

    private void setupPrimaryKeyJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = persistentClass
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = persistentClass
                .getAnnotation(PrimaryKeyJoinColumns.class);
        if (pkColumn != null) {
            pkJoinColumns.add(createColumn(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(createColumn(column));
            }
        }

        if (inheritanceType == JOINED) {
            // support only single primary key

            PersistentStateDesc rootId = rootEntity.getIdentifiers().get(0);
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
                id.setColumn(new PersistentColumnImpl(idColumn.getTableName(),
                        name, referenced));
            } else {
            }
        }
    }

    private PersistentColumn createColumn(PrimaryKeyJoinColumn pkColumn) {
        return new PersistentColumnImpl(null, pkColumn.name(), pkColumn
                .referencedColumnName());
    }

    private void setupJoinColumns() {
        JoinColumn jColumn = persistentClass.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = persistentClass.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(createColumn(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(createColumn(column));
            }
        }
    }

    private PersistentColumn createColumn(JoinColumn jColumn) {
        return new PersistentColumnImpl(jColumn.table(), jColumn.name(),
                jColumn.referencedColumnName());
    }

    private void setupSuperclasses() {
        for (Class<?> clazz = persistentClass; clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {
            hierarchy.add(clazz);
        }

        Map<Class<?>, EntityClassDesc> superEntityDescsByClass = new HashMap<Class<?>, EntityClassDesc>();
        List<Class<?>> superClasses = hierarchy.subList(1, hierarchy.size());
        ListIterator<Class<?>> li = superClasses.listIterator(superClasses
                .size());
        while (li.hasPrevious()) {
            Class<?> clazz = li.previous();
            if (clazz.isAnnotationPresent(Entity.class)) {
                EntityClassDesc entityDesc = new EntityClassDesc(clazz);
                superClassDescs.add(entityDesc);
                superEntityDescsByClass.put(clazz, entityDesc);
                if (rootEntity == null) {
                    rootEntity = entityDesc;
                }
            }
        }
        if (rootEntity == null) {
            rootEntity = this;
        }

        Map<String, Column> orverrides = attribOverrides;
        PersistentClassDesc subclass = this;
        while (li.hasNext()) {
            Class<?> clazz = li.next();
            if (superEntityDescsByClass.containsKey(clazz)) {
                orverrides = superEntityDescsByClass.get(clazz).attribOverrides;
                subclass = superEntityDescsByClass.get(clazz);
                continue;
            } else if (clazz.isAnnotationPresent(MappedSuperclass.class)) {
                AttributeOverridableClassDesc ao = new AttributeOverridableClassDesc(
                        clazz, subclass.getTableName(0), subclass
                                .isPropertyAccessed(), Collections
                                .unmodifiableMap(orverrides));
                superClassDescs.add(ao);
            }
            orverrides = EMPTY_OVERRIDES;
        }
    }

    private void setupInheritanceStrategy() {
        if (rootEntity == this) {
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
            inheritanceType = rootEntity.inheritanceType;
            discriminatorColumnName = rootEntity.discriminatorColumnName;
            discriminatorType = rootEntity.discriminatorType;
        }
        DiscriminatorValue dv = persistentClass
                .getAnnotation(DiscriminatorValue.class);
        if (dv != null) {
            discriminatorValue = dv.value();
        } else if (discriminatorType == DiscriminatorType.STRING) {
            discriminatorValue = persistentClass.getName();
        }
    }

    private void setupSuperClassPersistentStateDescs() {
        for (PersistentClassDesc pcd : superClassDescs) {
            for (int i = 0; i < pcd.getPersistentStateDescSize(); i++) {
                PersistentStateDesc stateDesc = pcd.getPersistentStateDesc(i);
                if (inheritanceType == SINGLE_TABLE) {
                    PersistentColumn old = stateDesc.getColumn();
                    if (old != null) {
                        PersistentColumn newColumn = new PersistentColumnImpl(
                                rootEntity.getTableName(0), old.getName());
                        stateDesc.setColumn(newColumn);
                    }
                } else if (inheritanceType == TABLE_PER_CLASS) {
                    PersistentColumn old = stateDesc.getColumn();
                    if (old != null) {
                        PersistentColumn newColumn = new PersistentColumnImpl(
                                tableNames.get(0), old.getName());
                        stateDesc.setColumn(newColumn);
                    }
                } else if (inheritanceType == JOINED) {
                    if (stateDesc.isIdentifier()) {
                        PersistentStateDesc subclassId = null;
                        if (stateDesc instanceof PersistentPropertyDesc) {
                            PersistentPropertyDesc propDesc = (PersistentPropertyDesc) stateDesc;
                            subclassId = new PersistentPropertyDesc(this,
                                    propDesc.getPropertyDesc(), tableNames
                                            .get(0));
                        } else if (stateDesc instanceof PersistentFieldDesc) {
                            PersistentFieldDesc fieldDesc = (PersistentFieldDesc) stateDesc;
                            subclassId = new PersistentFieldDesc(this,
                                    fieldDesc.getField(), tableNames.get(0));
                        }
                        if (subclassId != null) {
                            setupPersistentStateDescs(subclassId);
                        }
                    }
                }
                setupPersistentStateDescs(stateDesc);
            }
        }
    }

    private void setupDiscriminator() {
        if (inheritanceType == SINGLE_TABLE) {
            for (Iterator<PersistentStateDesc> it = stateDescs.iterator(); it
                    .hasNext();) {
                PersistentStateDesc stateDesc = it.next();
                if (stateDesc instanceof DiscriminatorStateDesc) {
                    it.remove();
                    stateDescsByPathName.remove(stateDesc.getPathName());
                }
            }
            DiscriminatorStateDesc d = new DiscriminatorStateDesc(this,
                    rootEntity.getTableName(0), discriminatorColumnName,
                    discriminatorType, discriminatorValue);
            setupPersistentStateDescs(d);
        }
    }

    public String getName() {
        return name;
    }

}
