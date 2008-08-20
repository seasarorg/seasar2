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
package org.seasar.extension.jdbc.gen.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class DbForeignKeyMeta {

    protected String foreignKeyName;

    protected String primaryKeyCatalogName;

    protected String primaryKeySchemaName;

    protected String primaryKeyTableName;

    protected String foreignKeyCatalogName;

    protected String foreignKeySchemaName;

    protected String foreignKeyTableName;

    protected List<String> primaryKeyColumnNameList = new ArrayList<String>();

    protected List<String> foreignKeyColumnNameList = new ArrayList<String>();

    /**
     * @return Returns the foreignKeyName.
     */
    public String getForeignKeyName() {
        return foreignKeyName;
    }

    /**
     * @param foreignKeyName
     *            The foreignKeyName to set.
     */
    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    /**
     * @return Returns the primaryKeyCatalogName.
     */
    public String getPrimaryKeyCatalogName() {
        return primaryKeyCatalogName;
    }

    /**
     * @param primaryKeyCatalogName
     *            The primaryKeyCatalogName to set.
     */
    public void setPrimaryKeyCatalogName(String primaryKeyCatalogName) {
        this.primaryKeyCatalogName = primaryKeyCatalogName;
    }

    /**
     * @return Returns the primaryKeySchemaName.
     */
    public String getPrimaryKeySchemaName() {
        return primaryKeySchemaName;
    }

    /**
     * @param primaryKeySchemaName
     *            The primaryKeySchemaName to set.
     */
    public void setPrimaryKeySchemaName(String primaryKeySchemaName) {
        this.primaryKeySchemaName = primaryKeySchemaName;
    }

    /**
     * @return Returns the primaryKeyTableName.
     */
    public String getPrimaryKeyTableName() {
        return primaryKeyTableName;
    }

    /**
     * @param primaryKeyTableName
     *            The primaryKeyTableName to set.
     */
    public void setPrimaryKeyTableName(String primaryKeyTableName) {
        this.primaryKeyTableName = primaryKeyTableName;
    }

    /**
     * @return Returns the foreignKeyCatalogName.
     */
    public String getForeignKeyCatalogName() {
        return foreignKeyCatalogName;
    }

    /**
     * @param foreignKeyCatalogName
     *            The foreignKeyCatalogName to set.
     */
    public void setForeignKeyCatalogName(String foreignKeyCatalogName) {
        this.foreignKeyCatalogName = foreignKeyCatalogName;
    }

    /**
     * @return Returns the foreignKeySchemaName.
     */
    public String getForeignKeySchemaName() {
        return foreignKeySchemaName;
    }

    /**
     * @param foreignKeySchemaName
     *            The foreignKeySchemaName to set.
     */
    public void setForeignKeySchemaName(String foreignKeySchemaName) {
        this.foreignKeySchemaName = foreignKeySchemaName;
    }

    /**
     * @return Returns the foreignKeyTableName.
     */
    public String getForeignKeyTableName() {
        return foreignKeyTableName;
    }

    /**
     * @param foreignKeyTableName
     *            The foreignKeyTableName to set.
     */
    public void setForeignKeyTableName(String foreignKeyTableName) {
        this.foreignKeyTableName = foreignKeyTableName;
    }

    /**
     * @return Returns the primaryKeyColumnNameList.
     */
    public List<String> getPrimaryKeyColumnNameList() {
        return Collections.unmodifiableList(primaryKeyColumnNameList);
    }

    /**
     * @param primaryKeyColumnNameList
     *            The primaryKeyColumnNameList to set.
     */
    public void addPrimaryKeyColumnName(String primaryKeyColumnName) {
        primaryKeyColumnNameList.add(primaryKeyColumnName);
    }

    /**
     * @return Returns the foreignKeycolumnNameList.
     */
    public List<String> getForeignKeyColumnNameList() {
        return Collections.unmodifiableList(foreignKeyColumnNameList);
    }

    /**
     * @param foreignKeyColumnNameList
     *            The foreignKeycolumnNameList to set.
     */
    public void addForeignKeyColumnName(String foreignKeyColumnName) {
        foreignKeyColumnNameList.add(foreignKeyColumnName);
    }

}
