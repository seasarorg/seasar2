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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class ToOneRelationshipStateDesc extends AbstractPersistentStateDesc {

    private List<PersistentJoinColumn> joinColumns = new ArrayList<PersistentJoinColumn>();

    private List<PersistentJoinColumn> pkJoinColumns = new ArrayList<PersistentJoinColumn>();

    private List<PersistentJoinColumn> fkColumns = new ArrayList<PersistentJoinColumn>();

    private boolean oneToOne;

    private boolean owningSide;

    public ToOneRelationshipStateDesc(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {

        super(persistentClassDesc, primaryTableName, accessor);
        persistentColumn = new PersistentColumn(null, primaryTableName);
        introspect();
    }

    private void introspect() {
        detectJoinColumns();
        detectPkJoinColumns();
        OneToOne oneToOne = annotatedElement.getAnnotation(OneToOne.class);
        ManyToOne manyToOne = annotatedElement.getAnnotation(ManyToOne.class);
        Class<?> entityTarget = null;

        if (oneToOne != null) {
            entityTarget = oneToOne.targetEntity();
            owningSide = StringUtil.isEmpty(oneToOne.mappedBy());
            this.oneToOne = true;
        } else if (manyToOne != null) {
            entityTarget = manyToOne.targetEntity();
            owningSide = true;
        }

        if (entityTarget != null && entityTarget != void.class) {
            setPersistenceTargetClass(entityTarget);
        }
    }

    private void detectJoinColumns() {
        JoinColumn jColumn = annotatedElement.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = annotatedElement
                .getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(new PersistentJoinColumn(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(new PersistentJoinColumn(column));
            }
        }
    }

    private void detectPkJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = annotatedElement
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = annotatedElement
                .getAnnotation(PrimaryKeyJoinColumns.class);

        if (pkColumn != null) {
            pkJoinColumns.add(new PersistentJoinColumn(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(new PersistentJoinColumn(column));
            }
        }
    }

    private void createFkColumsByReferencedColumnName(
            List<PersistentJoinColumn> columns,
            List<PersistentColumn> refColumns) {

        for (PersistentJoinColumn column : columns) {
            PersistentColumn referencedColumn = null;

            for (PersistentColumn refColumn : refColumns) {
                if (refColumn.hasName(column.getReferencedColumnName())) {
                    referencedColumn = refColumn;
                }
            }

            if (referencedColumn != null) {
                String referencedName = referencedColumn.getName();
                PersistentJoinColumn fk = new PersistentJoinColumn(column);
                if (fk.getName() == null) {
                    fk.setName(getFkColumnName(referencedName));
                }
                if (fk.getTable() == null) {
                    fk.setTable(primaryTableName);
                }

                addForeignKeyColumn(fk);
            }
        }
    }

    private void createFkColumsByIndex(List<PersistentJoinColumn> columns,
            List<PersistentColumn> refIdColumns) {

        if (columns.size() != refIdColumns.size()) {
            return;
        }

        for (int i = 0; i < columns.size(); i++) {
            PersistentJoinColumn column = columns.get(i);
            PersistentColumn referencedColumn = refIdColumns.get(i);

            String referencedName = referencedColumn.getName();
            PersistentJoinColumn fk = new PersistentJoinColumn(column);
            if (fk.getName() == null) {
                fk.setName(getFkColumnName(referencedName));
            }
            if (fk.getTable() == null) {
                fk.setTable(primaryTableName);
            }
            if (fk.getReferencedColumnName() == null) {
                fk.setReferencedColumnName(referencedName);
            }

            addForeignKeyColumn(fk);
        }
    }

    private void createDefaultFkColumns(
            List<PersistentColumn> referencedIdColumns) {

        for (PersistentColumn referencedColumn : referencedIdColumns) {
            String referencedName = referencedColumn.getName();
            PersistentJoinColumn fk = new PersistentJoinColumn();
            fk.setName(getFkColumnName(referencedName));
            fk.setTable(primaryTableName);
            fk.setReferencedColumnName(referencedName);

            addForeignKeyColumn(fk);
        }
    }

    private void addForeignKeyColumn(PersistentJoinColumn column) {
        fkColumns.add(column);
    }

    private String getFkColumnName(String referencedColumnName) {
        return persistentStateName + "_" + referencedColumnName;
    }

    @Override
    public List<PersistentJoinColumn> getJoinColumns() {
        return joinColumns;
    }

    @Override
    public void setJoinColumns(List<PersistentJoinColumn> joinColumns) {
        this.joinColumns = joinColumns;
    }

    @Override
    public List<PersistentJoinColumn> getForeignKeyColumns() {
        return fkColumns;
    }

    public PersistentStateType getPersistentStateType() {
        return PersistentStateType.TO_ONE;
    }

    @Override
    public void setupForeignKeyColumns(PersistentClassDesc relationship) {
        if (!owningSide) {
            return;
        }

        List<PersistentColumn> refIdColumns = new ArrayList<PersistentColumn>();
        for (PersistentStateDesc stateDesc : relationship.getIdentifiers()) {
            refIdColumns.add(stateDesc.getColumn());
        }

        if (!pkJoinColumns.isEmpty() && oneToOne) {
            adjustPrimaryKeyColumns(pkJoinColumns);

        } else if (!joinColumns.isEmpty()) {
            if (hasReferencedColumnName(joinColumns)) {
                List<PersistentColumn> refAllColumns = new ArrayList<PersistentColumn>();
                for (PersistentStateDesc stateDesc : relationship
                        .getPersistentStateDescs()) {
                    refAllColumns.add(stateDesc.getColumn());
                }
                createFkColumsByReferencedColumnName(joinColumns, refAllColumns);
            } else {
                createFkColumsByIndex(joinColumns, refIdColumns);
            }

        } else {
            createDefaultFkColumns(refIdColumns);
        }
    }

    @Override
    protected void adjustPkColumnsByReferencedColumnName(
            List<PersistentJoinColumn> pkJoinColumns) {

        for (PersistentStateDesc id : getPersistentClassDesc().getIdentifiers()) {
            id.adjustPrimaryKeyColumns(pkJoinColumns);
        }
    }

    @Override
    protected void adjustPkColumnsByIndex(
            List<PersistentJoinColumn> pkJoinColumns) {

        for (PersistentStateDesc id : getPersistentClassDesc().getIdentifiers()) {
            id.adjustPrimaryKeyColumns(pkJoinColumns);
        }
    }

}
