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

import java.util.Map;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
class AttributeOverridableClassDesc extends AbstractPersistentClassDesc
        implements PersistentClassDesc {

    private final Map<String, PersistentColumn> attribOverrides;

    public AttributeOverridableClassDesc(Class persistentClass,
            String primaryTableName, boolean propertyAccessed,
            Map<String, PersistentColumn> attribOverrides) {

        super(persistentClass);

        this.propertyAccessed = propertyAccessed;
        this.attribOverrides = attribOverrides;
        addTableName(primaryTableName);
        setupPersistentStateDescs();
        overrideAttributes();
    }

    private void overrideAttributes() {
        for (PersistentStateDesc stateDesc : getPersistentStateDescs()) {
            if (attribOverrides.containsKey(stateDesc.getName())) {
                override(stateDesc, attribOverrides.get(stateDesc
                        .getName()));
            }
        }
    }

    private void override(PersistentStateDesc psd, PersistentColumn column) {
        PersistentColumn old = psd.getColumn();

        String columnName = StringUtil.isEmpty(column.getName()) ? old
                .getName() : column.getName();
        String tableName = StringUtil.isEmpty(column.getTable()) ? old
                .getTable() : column.getTable();

        PersistentColumn newColumn = new PersistentColumnImpl(columnName,
                tableName);
        psd.setColumn(newColumn);
    }

    public PersistentClassDesc getRoot() {
        return this;
    }
}