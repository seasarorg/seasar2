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

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

import org.seasar.framework.ejb.unit.PersistentColumn;

public class PersistentColumnImpl implements PersistentColumn {

    private String tableName;

    private String name;

    private String referencedColumnName;

    public PersistentColumnImpl(Column column) {
        this(column.table(), column.name());
    }

    public PersistentColumnImpl(JoinColumn column) {
        this(column.table(), column.name(), column.referencedColumnName());
    }

    public PersistentColumnImpl(PrimaryKeyJoinColumn column) {
        this(null, column.name(), column.referencedColumnName());
    }

    public PersistentColumnImpl(String tableName, String columnName) {
        this(tableName, columnName, null);
    }

    public PersistentColumnImpl(String tableName, String name,
            String referencedColumnName) {
        this.name = name != null ? name.toUpperCase() : null;
        this.tableName = tableName != null ? tableName.toUpperCase() : null;
        this.referencedColumnName = referencedColumnName != null ? referencedColumnName
                .toUpperCase()
                : null;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("tableName=");
        buf.append(tableName);
        buf.append(",name=");
        buf.append(name);
        buf.append(",referencedColumnName=");
        buf.append(referencedColumnName);
        return buf.toString();
    }

}
