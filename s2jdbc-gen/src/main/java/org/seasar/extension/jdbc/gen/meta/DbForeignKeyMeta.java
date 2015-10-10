/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
 * データベースの外部キーメタデータです。
 * 
 * @author taedium
 */
public class DbForeignKeyMeta {

    /** 名前 */
    protected String name;

    /** 主キー側のカタログ名 */
    protected String primaryKeyCatalogName;

    /** 主キー側のスキーマ名 */
    protected String primaryKeySchemaName;

    /** 主キー側のテーブル名 */
    protected String primaryKeyTableName;

    /** 主キーのカラム名のリスト */
    protected List<String> primaryKeyColumnNameList = new ArrayList<String>();

    /** 外部キーのカラム名のリスト */
    protected List<String> foreignKeyColumnNameList = new ArrayList<String>();

    /** 一意の場合{@code true} */
    protected boolean unique;

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 主キー側のカタログ名を返します。
     * 
     * @return 主キー側のカタログ名
     */
    public String getPrimaryKeyCatalogName() {
        return primaryKeyCatalogName;
    }

    /**
     * 主キー側のカタログ名を設定します。
     * 
     * @param primaryKeyCatalogName
     *            主キー側のカタログ名
     */
    public void setPrimaryKeyCatalogName(String primaryKeyCatalogName) {
        this.primaryKeyCatalogName = primaryKeyCatalogName;
    }

    /**
     * 主キー側のスキーマ名を返します。
     * 
     * @return 主キー側のスキーマ名
     */
    public String getPrimaryKeySchemaName() {
        return primaryKeySchemaName;
    }

    /**
     * 主キー側のスキーマ名を設定します。
     * 
     * @param primaryKeySchemaName
     *            主キー側のスキーマ名
     */
    public void setPrimaryKeySchemaName(String primaryKeySchemaName) {
        this.primaryKeySchemaName = primaryKeySchemaName;
    }

    /**
     * 主キー側のテーブル名を返します。
     * 
     * @return 主キー側のテーブル名
     */
    public String getPrimaryKeyTableName() {
        return primaryKeyTableName;
    }

    /**
     * 主キー側のテーブル名を設定します。
     * 
     * @param primaryKeyTableName
     *            主キー側のテーブル名
     */
    public void setPrimaryKeyTableName(String primaryKeyTableName) {
        this.primaryKeyTableName = primaryKeyTableName;
    }

    /**
     * 主キーのカラム名のリストを返します。
     * 
     * @return 主キーのカラム名のリスト
     */
    public List<String> getPrimaryKeyColumnNameList() {
        return Collections.unmodifiableList(primaryKeyColumnNameList);
    }

    /**
     * 主キーのカラム名を追加します。
     * 
     * @param primaryKeyColumnName
     *            主キーのカラム名
     */
    public void addPrimaryKeyColumnName(String primaryKeyColumnName) {
        primaryKeyColumnNameList.add(primaryKeyColumnName);
    }

    /**
     * 外部キーのカラム名のリストを返します。
     * 
     * @return 外部キーのカラム名のリスト
     */
    public List<String> getForeignKeyColumnNameList() {
        return Collections.unmodifiableList(foreignKeyColumnNameList);
    }

    /**
     * 外部キーのカラム名を追加します。
     * 
     * @param foreignKeyColumnName
     *            外部キーのカラム名
     */
    public void addForeignKeyColumnName(String foreignKeyColumnName) {
        foreignKeyColumnNameList.add(foreignKeyColumnName);
    }

    /**
     * 一意の場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return 一意の場合{@code true}、そうでない場合{@code false}
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * 一意の場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param unique
     *            一意の場合{@code true}
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * 完全なテーブル名を返します。
     * 
     * @return 完全なテーブル名
     */
    public String getPrimaryKeyFullTableName() {
        StringBuilder buf = new StringBuilder();
        if (primaryKeyCatalogName != null) {
            buf.append(primaryKeyCatalogName).append(".");
        }
        if (primaryKeySchemaName != null) {
            buf.append(primaryKeySchemaName).append(".");
        }
        return buf.append(primaryKeyTableName).toString();
    }

    /**
     * 複合外部キーの場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return 複合外部キーの場合{@code true}、そうでない場合{@code false}
     */
    public boolean isComposite() {
        return foreignKeyColumnNameList.size() > 1;
    }
}
