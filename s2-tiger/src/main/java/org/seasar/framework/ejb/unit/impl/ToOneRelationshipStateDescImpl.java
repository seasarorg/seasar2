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

import org.seasar.framework.ejb.unit.EmbeddedStateDesc;
import org.seasar.framework.ejb.unit.ForeignKey;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.ToOneRelationshipStateDesc;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class ToOneRelationshipStateDescImpl extends AbstractPersistentStateDesc
        implements ToOneRelationshipStateDesc {

    private List<PersistentJoinColumn> joinColumns = new ArrayList<PersistentJoinColumn>();

    private List<PersistentJoinColumn> pkJoinColumns = new ArrayList<PersistentJoinColumn>();

    private List<ForeignKey> ForeignKeys = new ArrayList<ForeignKey>();

    private boolean oneToOne;

    private boolean owningSide;

    public ToOneRelationshipStateDescImpl(
            PersistentClassDesc persistentClassDesc, String primaryTableName,
            PersistentStateAccessor accessor) {

        super(persistentClassDesc, primaryTableName, accessor);
        setColumn(new PersistentColumn(null, primaryTableName));
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

    private void createForeignKeysByReferencedColumnName(
            List<PersistentJoinColumn> columns, PersistentClassDesc relationship) {

        List<EmbeddedStateDesc> embeddedStateDescs = new ArrayList<EmbeddedStateDesc>();
        List<PersistentStateDesc> allReferencedStateDescs = new ArrayList<PersistentStateDesc>();

        for (PersistentStateDesc stateDesc : relationship
                .getPersistentStateDescs()) {

            if (stateDesc instanceof EmbeddedStateDesc) {
                EmbeddedStateDesc embedded = (EmbeddedStateDesc) stateDesc;
                embeddedStateDescs.add(embedded);
                for (PersistentStateDesc e : embedded.getPersistentStateDesc()) {
                    allReferencedStateDescs.add(e);
                }
            } else {
                allReferencedStateDescs.add(stateDesc);
            }
        }

        for (PersistentJoinColumn column : columns) {

            for (PersistentStateDesc referencedId : allReferencedStateDescs) {
                PersistentColumn referencedColumn = referencedId.getColumn();

                if (referencedColumn.hasName(column.getReferencedColumnName())) {

                    PersistentColumn fkColumn = new PersistentColumn(column);
                    if (fkColumn.getName() == null) {
                        String referencedName = referencedColumn.getName();
                        fkColumn.setName(getFkColumnName(referencedName));
                    }
                    if (fkColumn.getTable() == null) {
                        fkColumn.setTable(primaryTableName);
                    }

                    ForeignKey foreignKey = new ForeignKey(referencedId,
                            fkColumn);
                    for (EmbeddedStateDesc each : embeddedStateDescs) {
                        if (each.contains(referencedId)) {
                            foreignKey.setEmbeddedStateDesc(each);
                            break;
                        }
                    }
                    addForeignKey(foreignKey);
                }
            }
        }
    }

    private void createForeignKeysByIndex(List<PersistentJoinColumn> columns,
            PersistentClassDesc relationship) {

        List<PersistentStateDesc> referencedIds = getReferencedIdentifiers(relationship);

        if (columns.size() != referencedIds.size()) {
            return;
        }

        for (int i = 0; i < columns.size(); i++) {
            PersistentJoinColumn column = columns.get(i);
            PersistentStateDesc referencedId = referencedIds.get(i);

            PersistentColumn fkColumn = new PersistentColumn(column);
            if (fkColumn.getName() == null) {
                String referencedName = referencedId.getColumn().getName();
                fkColumn.setName(getFkColumnName(referencedName));
            }
            if (fkColumn.getTable() == null) {
                fkColumn.setTable(primaryTableName);
            }

            ForeignKey foreignKey = new ForeignKey(referencedId, fkColumn);
            if (relationship.hasEmbeddedId()) {
                foreignKey.setEmbeddedStateDesc(relationship.getEmbeddedId());
            }
            addForeignKey(foreignKey);
        }
    }

    private void createDefaultForeignKeys(PersistentClassDesc relationship) {

        List<PersistentStateDesc> referencedIds = getReferencedIdentifiers(relationship);

        for (PersistentStateDesc referencedId : referencedIds) {
            PersistentColumn referencedColumn = referencedId.getColumn();
            String referencedName = referencedColumn.getName();
            PersistentColumn fkColumn = new PersistentColumn();
            fkColumn.setName(getFkColumnName(referencedName));
            fkColumn.setTable(primaryTableName);

            ForeignKey foreignKey = new ForeignKey(referencedId, fkColumn);
            if (relationship.hasEmbeddedId()) {
                foreignKey.setEmbeddedStateDesc(relationship.getEmbeddedId());
            }
            addForeignKey(foreignKey);
        }
    }

    private List<PersistentStateDesc> getReferencedIdentifiers(
            PersistentClassDesc relationship) {

        if (relationship.hasEmbeddedId()) {
            return relationship.getEmbeddedId().getPersistentStateDesc();
        } else {
            return relationship.getIdentifiers();
        }
    }

    private void addForeignKey(ForeignKey fk) {
        ForeignKeys.add(fk);
    }

    private String getFkColumnName(String referencedColumnName) {
        return persistentStateName + "_" + referencedColumnName;
    }

    public List<PersistentJoinColumn> getJoinColumns() {
        return joinColumns;
    }

    public void setJoinColumns(List<PersistentJoinColumn> joinColumns) {
        this.joinColumns = joinColumns;
    }

    public List<ForeignKey> getForeignKeys() {
        return ForeignKeys;
    }

    public void setupForeignKeys(PersistentClassDesc relationship) {
        if (!owningSide) {
            return;
        }

        if (!pkJoinColumns.isEmpty() && oneToOne) {
            return;

        } else if (!joinColumns.isEmpty()) {
            if (hasReferencedColumnName(joinColumns)) {
                createForeignKeysByReferencedColumnName(joinColumns,
                        relationship);
            } else {
                createForeignKeysByIndex(joinColumns, relationship);
            }

        } else {
            createDefaultForeignKeys(relationship);
        }
    }

    public void adjustPrimaryKeyColumns(List<PersistentJoinColumn> pkJoinColumns) {
        throw new UnsupportedOperationException("adjustPrimaryKeyColumns");
    }

}
