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

    public PersistentColumnImpl(String columnName, String tableName) {
        this(columnName, tableName, null);
    }

    public PersistentColumnImpl(String name, String tableName,
            String referencedColumnName) {
        this.name = name != null ? name : null;
        this.tableName = tableName != null ? tableName : null;
        this.referencedColumnName = referencedColumnName != null ? referencedColumnName
                : null;
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
