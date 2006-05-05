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
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class ToOneRelationshipStateDesc extends AbstractPersistentStateDesc {

    private List<PersistentColumn> joinColumns = new ArrayList<PersistentColumn>();

    private List<PersistentColumn> pkJoinColumns = new ArrayList<PersistentColumn>();
    
    private List<PersistentColumn> fkColumns = new ArrayList<PersistentColumn>();
    
    private boolean oneToOne;
    
    private boolean owningSide;
    
    public ToOneRelationshipStateDesc(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {
       
        super (persistentClassDesc, primaryTableName, accessor);
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
            persistenceTargetClass = entityTarget;
        }
    }

    private void detectJoinColumns() {
        JoinColumn jColumn = annotatedElement.getAnnotation(JoinColumn.class);
        JoinColumns jColumns = annotatedElement.getAnnotation(JoinColumns.class);

        if (jColumn != null) {
            joinColumns.add(new PersistentColumn(jColumn));
        } else if (jColumns != null) {
            for (JoinColumn column : jColumns.value()) {
                joinColumns.add(new PersistentColumn(column));
            }
        }
    }

    private void detectPkJoinColumns() {
        PrimaryKeyJoinColumn pkColumn = annotatedElement
                .getAnnotation(PrimaryKeyJoinColumn.class);
        PrimaryKeyJoinColumns pkColumns = annotatedElement
                .getAnnotation(PrimaryKeyJoinColumns.class);

        if (pkColumn != null) {
            pkJoinColumns.add(new PersistentColumn(pkColumn));
        } else if (pkColumns != null) {
            for (PrimaryKeyJoinColumn column : pkColumns.value()) {
                pkJoinColumns.add(new PersistentColumn(column));
            }
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

        PersistentColumn newColumn = new PersistentColumn(column);
        String referencedName = referencedColumn.getName();
        newColumn.setNameIfNull(getFkColumnName(referencedName));
        newColumn.setTableIfNull(primaryTableName);
        newColumn.setReferencedColumnNameIfNull(referencedName);
        return newColumn;
    }

    private void setupDefaultFkColumn(List<PersistentColumn> referencedIdColumns) {

        for (PersistentColumn referencedColumn : referencedIdColumns) {
            String referencedName = referencedColumn.getName();
            PersistentColumn fk = new PersistentColumn(
                    getFkColumnName(referencedName), primaryTableName,
                    referencedName);
            addForeignKeyColumn(fk);
        }
    }
    
    private void addForeignKeyColumn(PersistentColumn column) {
        fkColumns.add(column);
    }
    
    private String getFkColumnName(String referencedColumnName) {
        return persistentStateName + "_" + referencedColumnName;
    }
    
    @Override
    public List<PersistentColumn> getForeignKeyColumns() {
        return fkColumns;
    }

    @Override
    public PersistentStateType getPersistentStateType() {
        return PersistentStateType.TO_ONE;
    }
    
    @Override
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
}
