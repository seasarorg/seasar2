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

import org.seasar.extension.jdbc.gen.internal.util.TableUtil;

/**
 * データベースのテーブルメタデータです。
 * 
 * @author taedium
 */
public class DbTableMeta {

    /** カタログ名 */
    protected String catalogName;

    /** スキーマ名 */
    protected String schemaName;

    /** 名前 */
    protected String name;

    /** コメント */
    protected String comment;

    /** カラムメタデータのリスト */
    protected List<DbColumnMeta> columnMetaList = new ArrayList<DbColumnMeta>();

    /** 主キーのカラムメタデータのリスト */
    protected List<DbColumnMeta> primaryKeyColumnMetaList = new ArrayList<DbColumnMeta>();

    /** 外部キーメタデータのリスト */
    protected List<DbForeignKeyMeta> foreignKeyMetaList = new ArrayList<DbForeignKeyMeta>();

    /** 一意キーメタデータのリスト */
    protected List<DbUniqueKeyMeta> uniqueKeyMetaList = new ArrayList<DbUniqueKeyMeta>();

    /**
     * カタログ名を返します。
     * 
     * @return カタログ名
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * カタログ名を設定します。
     * 
     * @param catalogName
     *            カタログ名
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * スキーマ名を返します
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

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
     * コメントを返します。
     * 
     * @return コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * コメントを設定します。
     * 
     * @param comment
     *            コメント
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * カラムのメタデータのリストを返します。
     * 
     * @return カラムのメタデータのリスト
     */
    public List<DbColumnMeta> getColumnMetaList() {
        return Collections.unmodifiableList(columnMetaList);
    }

    /**
     * カラムのメタデータを追加します。
     * 
     * @param columnDesc
     *            カラム記述
     */
    public void addColumnMeta(DbColumnMeta columnDesc) {
        columnMetaList.add(columnDesc);
        if (columnDesc.isPrimaryKey()) {
            primaryKeyColumnMetaList.add(columnDesc);
        }
    }

    /**
     * 主キーのカラムメタデータのリストを返します。
     * 
     * @return 主キーのカラムメタデータのリスト
     */
    public List<DbColumnMeta> getPrimaryKeyColumnMetaList() {
        return Collections.unmodifiableList(primaryKeyColumnMetaList);
    }

    /**
     * 外部キーメタデータのリストを返します。
     * 
     * @return 外部キーメタデータのリスト
     */
    public List<DbForeignKeyMeta> getForeignKeyMetaList() {
        return Collections.unmodifiableList(foreignKeyMetaList);
    }

    /**
     * 外部キーメタデータを追加します。
     * 
     * @param foreignKeyMeta
     *            外部キーメタデータ
     */
    public void addForeignKeyMeta(DbForeignKeyMeta foreignKeyMeta) {
        foreignKeyMetaList.add(foreignKeyMeta);
    }

    /**
     * 一意キーメタデータのリスト
     * 
     * @return 一意キーメタデータのリスト
     */
    public List<DbUniqueKeyMeta> getUniqueKeyMetaList() {
        return Collections.unmodifiableList(uniqueKeyMetaList);
    }

    /**
     * 一意キーメタデータを追加します。
     * 
     * @param uniqueKeyMeta
     *            一意キーメタデータ
     */
    public void addUniqueKeyMeta(DbUniqueKeyMeta uniqueKeyMeta) {
        uniqueKeyMetaList.add(uniqueKeyMeta);
    }

    /**
     * 完全なテーブル名を返します。
     * 
     * @return 完全なテーブル名
     */
    public String getFullTableName() {
        return TableUtil.buildFullTableName(catalogName, schemaName, name);
    }

    /**
     * 複合主キーを持つ場合{@code true}を返します。
     * 
     * @return 複合主キーを持つ場合{@code true}、そうでない場合{@code false}
     */
    public boolean hasCompositePrimaryKey() {
        return primaryKeyColumnMetaList.size() > 1;
    }
}
