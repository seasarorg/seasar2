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

import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
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
import org.seasar.framework.ejb.unit.EntityClassDesc;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentDiscriminatorColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class EntityClassDescImpl extends AbstractPersistentClassDesc implements
        EntityClassDesc {

    private String name;

    private InheritanceType inheritanceType;

    private EntityClassDesc rootEntityDesc;

    private EntityClassDescImpl parentEntityDesc;

    private PersistentDiscriminatorColumn discriminatorColumn;

    private List<PersistentJoinColumn> joinColumns = new ArrayList<PersistentJoinColumn>();

    private List<PersistentJoinColumn> pkJoinColumns = new ArrayList<PersistentJoinColumn>();

    private Map<String, List<PersistentJoinColumn>> secondaryTablePkJoinColumns = new HashMap<String, List<PersistentJoinColumn>>();

    private List<Class<?>> hierarchy = new ArrayList<Class<?>>();

    private List<MappedSuperclassDescImpl> mappedSuperclassDescs = new ArrayList<MappedSuperclassDescImpl>();

    public EntityClassDescImpl(Class<?> entityClass) {
        super(entityClass);

        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            throw new AnnotationNotFoundException(entityClass, Entity.class);
        }
        this.name = StringUtil.isEmpty(entity.name()) ? ClassUtil
                .getShortClassName(entityClass) : entity.name();

        detectTable();
        detectSecondaryTables();
        detectSuperclasses();
        detectJoinColumns();
        detectPrimaryKeyJoinColumns();
        detectInheritanceStrategy();

        setupPropertyAccessed();
        setupPersistentStateDescs();
    }

    private void detectTable() {
        Table primary = persistentClass.getAnnotation(Table.class);
        if (primary == null || StringUtil.isEmpty(primary.name())) {
            addTableName(name);
        } else {
            addTableName(primary.name());
        }
    }

    private void detectSecondaryTables() {

        SecondaryTable secondary = persistentClass
                .getAnnotation(SecondaryTable.class);
        SecondaryTables secondaries = persistentClass
                .getAnnotation(SecondaryTables.class);

        if (secondary != null) {
            addTableName(secondary.name());
            addSecondaryTablePkJoinColumns(secondary);
        } else if (secondaries != null) {
            for (SecondaryTable each : secondaries.value()) {
                addTableName(each.name());
                addSecondaryTablePkJoinColumns(each);
            }
        }
    }

    private void addSecondaryTablePkJoinColumns(SecondaryTable secondary) {
        List<PersistentJoinColumn> pkJoinColumns = null;
        if (secondaryTablePkJoinColumns.containsKey(secondary.name())) {
            pkJoinColumns = secondaryTablePkJoinColumns.get(secondary.name());
        } else {
            pkJoinColumns = new ArrayList<PersistentJoinColumn>();
            secondaryTablePkJoinColumns.put(secondary.name(), pkJoinColumns);
        }
        for (PrimaryKeyJoinColumn each : secondary.pkJoinColumns()) {
            pkJoinColumns.add(new PersistentJoinColumn(each));
        }
    }

    private void setupPropertyAccessed() {
        for (Class<?> clazz : hierarchy) {
            BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
            for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
                if (propertyDesc.hasReadMethod()) {
                    Method m = propertyDesc.getReadMethod();
                    if (m.isAnnotationPresent(Id.class)
                            || m.isAnnotationPresent(EmbeddedId.class)) {
                        setPropertyAccessed(true);
                        return;
                    }
                }
            }
        }
    }

    private Map<String, PersistentColumn> detectAttributeOverrides() {
        AttributeOverride ao = persistentClass
                .getAnnotation(AttributeOverride.class);
        AttributeOverrides aos = persistentClass
                .getAnnotation(AttributeOverrides.class);

        Map<String, PersistentColumn> attribOverrides = new HashMap<String, PersistentColumn>();

        if (ao != null) {
            attribOverrides.put(ao.name(), new PersistentColumn(ao.column()));
        } else if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                attribOverrides.put(each.name(), new PersistentColumn(each
                        .column()));
            }
        }
        return attribOverrides;
    }

    private Map<String, List<PersistentJoinColumn>> detectAssociationOverrides() {
        AssociationOverride ao = persistentClass
                .getAnnotation(AssociationOverride.class);
        AssociationOverrides aos = persistentClass
                .getAnnotation(AssociationOverrides.class);

        Map<String, List<PersistentJoinColumn>> associationOverrides = new HashMap<String, List<PersistentJoinColumn>>();

        if (ao != null) {
            associationOverrides.put(ao.name(), createJoinColumns(ao));
        } else if (aos != null) {
            for (AssociationOverride each : aos.value()) {
                associationOverrides.put(each.name(), createJoinColumns(each));
            }
        }
        return associationOverrides;
    }

    private List<PersistentJoinColumn> createJoinColumns(AssociationOverride ao) {
        List<PersistentJoinColumn> joinColumns = new ArrayList<PersistentJoinColumn>(
                ao.joinColumns().length);
        for (JoinColumn jColumn : ao.joinColumns()) {
            joinColumns.add(new PersistentJoinColumn(jColumn));
        }
        return joinColumns;
    }

    private void detectPrimaryKeyJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = persistentClass
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = persistentClass
                .getAnnotation(PrimaryKeyJoinColumns.class);

        if (pkColumn != null) {
            pkJoinColumns.add(new PersistentJoinColumn(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(new PersistentJoinColumn(column));
            }
        }
    }

    private void detectJoinColumns() {
        JoinColumn jColumn = persistentClass.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = persistentClass.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(new PersistentJoinColumn(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(new PersistentJoinColumn(column));
            }
        }
    }

    private void detectSuperclasses() {
        setupHierarchy();
        setupParentEntityDesc();
        setupRootEntityDesc();
        setupMappedsuperclassDescs();
    }

    private void setupMappedsuperclassDescs() {
        Map<String, PersistentColumn> attribOverrides = detectAttributeOverrides();
        Map<String, List<PersistentJoinColumn>> associationOverrides = detectAssociationOverrides();

        for (Class<?> clazz = persistentClass.getSuperclass(); !clazz
                .isAnnotationPresent(Entity.class)
                && clazz != Object.class; clazz = clazz.getSuperclass()) {

            if (clazz.isAnnotationPresent(MappedSuperclass.class)) {
                MappedSuperclassDescImpl mapped = new MappedSuperclassDescImpl(
                        clazz, getPrimaryTableName(), isPropertyAccessed());
                mapped.overrideAttributes(attribOverrides);
                mapped.overrideAssociations(associationOverrides);
                mappedSuperclassDescs.add(mapped);
            }
        }
    }

    private void setupRootEntityDesc() {
        if (parentEntityDesc == null) {
            rootEntityDesc = this;
        } else {
            ListIterator<Class<?>> hierarchyIterator = hierarchy
                    .listIterator(hierarchy.size());
            while (hierarchyIterator.hasPrevious()) {
                Class<?> clazz = hierarchyIterator.previous();
                if (clazz.isAnnotationPresent(Entity.class)) {
                    rootEntityDesc = parentEntityDesc.getPersistentClass() == clazz ? parentEntityDesc
                            : new EntityClassDescImpl(clazz);
                    return;
                }
            }
        }
    }

    private void setupParentEntityDesc() {
        for (Class<?> clazz = persistentClass.getSuperclass(); clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                parentEntityDesc = new EntityClassDescImpl(clazz);
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

    private void detectInheritanceStrategy() {
        if (rootEntityDesc == this) {
            discriminatorColumn = new PersistentDiscriminatorColumn();
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
                discriminatorColumn.setName(dc.name());
                discriminatorColumn.setType(dc.discriminatorType());
            }
            discriminatorColumn.setTable(getPrimaryTableName());

        } else {
            inheritanceType = rootEntityDesc.getInheritanceType();
            if (inheritanceType == SINGLE_TABLE || inheritanceType == JOINED) {
                discriminatorColumn = rootEntityDesc
                        .getRootEntityDiscriminatorColumn();
            }
        }

        DiscriminatorValue dv = persistentClass
                .getAnnotation(DiscriminatorValue.class);
        if (dv != null) {
            discriminatorColumn.setValue(dv.value());
        } else if (discriminatorColumn.getType() == DiscriminatorType.STRING) {
            discriminatorColumn.setValue(name);
        }
    }

    @Override
    protected void setupPersistentStateDescs() {
        addParentStateDescsAndTableNames();
        addMappedSuperclassStateDescs();
        super.setupPersistentStateDescs();
        addSecondaryTableIdStateDescs();
    }

    private void addSecondaryTableIdStateDescs() {
        if (secondaryTablePkJoinColumns.isEmpty()) {
            return;
        }
        for (String tableName : secondaryTablePkJoinColumns.keySet()) {
            for (PersistentStateDesc id : getIdentifiers()) {
                PersistentStateDesc secondaryTableId = PersistentStateDescFactory
                        .getPersistentStateDesc(this, tableName, id
                                .getAccessor());
                List<PersistentJoinColumn> columns = secondaryTablePkJoinColumns
                        .get(tableName);
                secondaryTableId.adjustPrimaryKeyColumns(columns);
                addPersistentStateDesc(secondaryTableId);
            }
        }
    }

    public EntityClassDesc getRoot() {
        return rootEntityDesc;
    }

    private void addParentStateDescsAndTableNames() {
        if (parentEntityDesc == null) {
            return;
        }

        for (String tableName : parentEntityDesc.getTableNames()) {
            addTableName(tableName);
        }

        for (PersistentStateDesc stateDesc : parentEntityDesc
                .getPersistentStateDescs()) {
            switch (rootEntityDesc.getInheritanceType()) {
            case SINGLE_TABLE:
                stateDesc.getColumn().setTable(
                        rootEntityDesc.getPrimaryTableName());
                break;
            case TABLE_PER_CLASS:
                stateDesc.getColumn().setTable(getPrimaryTableName());
                break;
            case JOINED:
                if (stateDesc.isIdentifier()) {
                    PersistentClassDesc classDesc = stateDesc
                            .getPersistentClassDesc();
                    if (classDesc instanceof EntityClassDesc) {
                        EntityClassDesc entityDesc = (EntityClassDesc) classDesc;
                        if (entityDesc.isRoot()) {
                            addPersistentStateDesc(createSubclassId(stateDesc));
                        }
                    } 
                }
                break;
            }
            addPersistentStateDesc(stateDesc);
        }
    }

    private PersistentStateDesc createSubclassId(PersistentStateDesc rootId) {
        PersistentStateAccessor accessor = rootId.getAccessor();
        PersistentStateDesc subclassId = PersistentStateDescFactory
                .getPersistentStateDesc(this, getPrimaryTableName(), accessor);
        subclassId.adjustPrimaryKeyColumns(pkJoinColumns);
        return subclassId;
    }

    private void addMappedSuperclassStateDescs() {
        for (PersistentClassDesc mappedDesc : mappedSuperclassDescs) {
            for (PersistentStateDesc stateDesc : mappedDesc
                    .getPersistentStateDescs()) {
                addPersistentStateDesc(stateDesc);
            }
        }
    }

    public PersistentDiscriminatorColumn getDiscriminatorColumn(String tableName) {

        if (discriminatorColumn != null) {
            if (discriminatorColumn.getTable().equalsIgnoreCase(tableName)) {
                return discriminatorColumn;
            }
        }
        return null;
    }

    public boolean isRoot() {
        return this == rootEntityDesc;
    }

    public InheritanceType getInheritanceType() {
        return inheritanceType;
    }

    public PersistentDiscriminatorColumn getRootEntityDiscriminatorColumn() {
        return isRoot() ? discriminatorColumn : null;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EntityClassDescImpl))
            return false;
        EntityClassDescImpl castOther = (EntityClassDescImpl) other;
        return this.getPersistentClass() == castOther.getPersistentClass();
    }

    @Override
    public int hashCode() {
        return getPersistentClass().hashCode();
    }

}
