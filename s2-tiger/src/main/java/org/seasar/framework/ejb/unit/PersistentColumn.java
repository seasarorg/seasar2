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
package org.seasar.framework.ejb.unit;

import javax.persistence.Column;

import org.seasar.framework.util.StringUtil;

public class PersistentColumn {

    private String name;

    private String tableName;

    public PersistentColumn() {
    }
    
    public PersistentColumn(Column column) {
        this(column.name(), column.table());
    }

    public PersistentColumn(String columnName, String tableName) {
        setName(columnName);
        setTable(tableName);
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return tableName;
    }

    public void setName(String name) {
        this.name = StringUtil.isEmpty(name) ? null : name;
    }

    public void setTable(String table) {
        this.tableName = StringUtil.isEmpty(table) ? null : table;
    }
    
    public boolean hasName(String name) {
        if (name == null) {
            return false;
        }
        return name.equalsIgnoreCase(getName());
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("name=");
        buf.append(name);
        buf.append(",tableName=");
        buf.append(tableName);
        return buf.toString();
    }

}
