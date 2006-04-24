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
import org.seasar.framework.util.StringUtil;

public class PersistentColumnImpl implements PersistentColumn {

    private String name;

    private String tableName;

    private String referencedColumnName;

    public PersistentColumnImpl(Column column) {
        this(column.name(), column.table());
    }

    public PersistentColumnImpl(JoinColumn column) {
        this(column.name(), column.table(), column.referencedColumnName());
    }

    public PersistentColumnImpl(PrimaryKeyJoinColumn column) {
        this(column.name(), null, column.referencedColumnName());
    }

    public PersistentColumnImpl(PersistentColumn column) {
        this(column.getName(), column.getTable(), column
                .getReferencedColumnName());
    }

    public PersistentColumnImpl(String columnName, String tableName) {
        this(columnName, tableName, null);
    }

    public PersistentColumnImpl(String name, String tableName,
            String referencedColumnName) {
        setName(name);
        setTable(tableName);
        setReferencedColumnName(referencedColumnName);
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return tableName;
    }

    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    public void setName(String name) {
        this.name = StringUtil.isEmpty(name) ? null : name;
    }

    public void setTable(String table) {
        this.tableName = StringUtil.isEmpty(table) ? null : table;
    }

    public void setReferencedColumnName(String referencedColumnName) {
        this.referencedColumnName = StringUtil.isEmpty(referencedColumnName) ? null
                : referencedColumnName;
    }
    
    public void setNameIfNull(String name) {
        if (getName() == null) {
            setName(name);
        }
    }

    public void setTableIfNull(String table) {
        if (getTable() == null) {
            setTable(table);
        }
    }

    public void setReferencedColumnNameIfNull(String referencedColumnName) {
        if (getReferencedColumnName() == null) {
            setReferencedColumnName(referencedColumnName);
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("name=");
        buf.append(name);
        buf.append(",tableName=");
        buf.append(tableName);
        buf.append(",referencedColumnName=");
        buf.append(referencedColumnName);
        return buf.toString();
    }

}
