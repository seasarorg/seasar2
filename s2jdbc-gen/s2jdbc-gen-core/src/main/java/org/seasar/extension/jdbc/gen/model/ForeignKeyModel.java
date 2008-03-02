/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class ForeignKeyModel {

    protected String name;

    protected List<String> columnNameList = new ArrayList<String>();

    protected List<String> referencedColumnNameList = new ArrayList<String>();

    protected String referencedSchemaName;

    protected String referencedTableName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addColumnName(String columnName) {
        columnNameList.add(columnName);
    }

    public List<String> getColumnNameList() {
        return Collections.unmodifiableList(columnNameList);
    }

    public void addReferencedColumnName(String referencedColumnName) {
        referencedColumnNameList.add(referencedColumnName);
    }

    public List getReferencedColumnNameList() {
        return Collections.unmodifiableList(referencedColumnNameList);
    }

    public String getReferencedSchemaName() {
        return referencedSchemaName;
    }

    public void setReferencedSchemaName(String referencedSchemaName) {
        this.referencedSchemaName = referencedSchemaName;
    }

    public String getReferencedTableName() {
        return referencedTableName;
    }

    public void setReferencedTableName(String referencedTableName) {
        this.referencedTableName = referencedTableName;
    }

    public Key getKey() {
        Object[] keyValues = new Object[columnNameList.size()];
        for (int i = 0; i < columnNameList.size(); i++) {
            keyValues[i] = columnNameList.get(i).toLowerCase();
        }
        return new Key(keyValues);
    }
}
