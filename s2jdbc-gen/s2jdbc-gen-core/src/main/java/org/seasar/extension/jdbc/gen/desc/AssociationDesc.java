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
package org.seasar.extension.jdbc.gen.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class AssociationDesc {

    protected String name;

    protected EntityDesc referencedEntityDesc;

    protected AssociationType associationType;

    /** カラム名のリスト */
    protected List<String> columnNameList = new ArrayList<String>();

    /** 参照されるテーブルのカタログ名 */
    protected String referencedCatalogName;

    /** 参照されるテーブルのスキーマ名 */
    protected String referencedSchemaName;

    /** 参照されるテーブル名 */
    protected String referencedTableName;

    /** 参照されるカタログ名、参照されるスキーマ名、参照される名前をピリオドで連結した完全な名前 */
    protected String referencedFullTableName;

    /** 参照されるカラム名のリスト */
    protected List<String> referencedColumnNameList = new ArrayList<String>();

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the relationshipEntityDesc.
     */
    public EntityDesc getReferencedEntityDesc() {
        return referencedEntityDesc;
    }

    /**
     * @param relationshipEntityDesc
     *            The relationshipEntityDesc to set.
     */
    public void setReferencedEntityDesc(EntityDesc relationshipEntityDesc) {
        this.referencedEntityDesc = relationshipEntityDesc;
    }

    /**
     * @return Returns the associationType.
     */
    public AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * @param associationType
     *            The associationType to set.
     */
    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    /**
     * カラム名のリストを返します。
     * 
     * @return カラム名のリスト
     */
    public List<String> getColumnNameList() {
        return Collections.unmodifiableList(columnNameList);
    }

    /**
     * カラム名を追加します。
     * 
     * @param columnName
     *            カラム名
     */
    public void addColumnName(String columnName) {
        columnNameList.add(columnName);
    }

    /**
     * 参照されるテーブルのカタログ名を返します。
     * 
     * @return 参照されるテーブルのカタログ名
     */
    public String getReferencedCatalogName() {
        return referencedCatalogName;
    }

    /**
     * 参照されるテーブルのカタログ名を設定します。
     * 
     * @param referencedCatalogName
     *            参照されるテーブルのカタログ名
     */
    public void setReferencedCatalogName(String referencedCatalogName) {
        this.referencedCatalogName = referencedCatalogName;
    }

    /**
     * 参照されるテーブルのスキーマ名を返します。
     * 
     * @return 参照されるテーブルのスキーマ名
     */
    public String getReferencedSchemaName() {
        return referencedSchemaName;
    }

    /**
     * 参照されるテーブルのスキーマ名を設定します。
     * 
     * @param referencedSchemaName
     *            参照されるテーブルのスキーマ名
     */
    public void setReferencedSchemaName(String referencedSchemaName) {
        this.referencedSchemaName = referencedSchemaName;
    }

    /**
     * 参照されるテーブル名を返します。
     * 
     * @return 参照されるテーブル名
     */
    public String getReferencedTableName() {
        return referencedTableName;
    }

    /**
     * 参照されるテーブル名を設定します。
     * 
     * @param referencedTableName
     *            参照されるテーブル名
     */
    public void setReferencedTableName(String referencedTableName) {
        this.referencedTableName = referencedTableName;
    }

    /**
     * 参照されるカラム名のリストを返します。
     * 
     * @return 参照されるカラム名のリスト
     */
    public List<String> getReferencedColumnNameList() {
        return Collections.unmodifiableList(referencedColumnNameList);
    }

    /**
     * 参照されるカラム名を追加します。
     * 
     * @param referencedColumnName
     *            参照されるカラム名
     */
    public void addReferencedColumnName(String referencedColumnName) {
        referencedColumnNameList.add(referencedColumnName);
    }

    /**
     * 参照されるテーブルの完全な名前を設定します。
     * 
     * @param referencedFullTableName
     *            参照されるテーブルの完全な名前
     */
    public void setReferencedFullTableName(String referencedFullTableName) {
        this.referencedFullTableName = referencedFullTableName;
    }

}
