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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 外部キーのモデルです。
 * 
 * @author taedium
 */
public class ForeignKeyModel {

    /** 名前 */
    protected String name;

    /** カラム名のリスト */
    protected List<String> columnNameList = new ArrayList<String>();

    /** 参照されるテーブル名 */
    protected String referencedTableName;

    /** 参照されるカラム名のリスト */
    protected List<String> referencedColumnNameList = new ArrayList<String>();

    /** 外部キーを削除する構文 */
    protected String dropForeignKeySyntax;

    /** 削除規則の参照動作、削除規則がない場合{@code null} */
    protected String onDelete;

    /** 更新規則の参照動作、更新規則がない場合{@code null} */
    protected String onUpdate;

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
     * 参照されるカラム名を追加します。
     * 
     * @return 参照されるカラム名
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
     * 外部キーを削除する構文を返します。
     * 
     * @return 外部キーを削除する構文
     */
    public String getDropForeignKeySyntax() {
        return dropForeignKeySyntax;
    }

    /**
     * 外部キーを削除する構文を設定します。
     * 
     * @param dropForeignKeySyntax
     *            外部キーを削除する構文
     */
    public void setDropForeignKeySyntax(String dropForeignKeySyntax) {
        this.dropForeignKeySyntax = dropForeignKeySyntax;
    }

    /**
     * 削除規則の参照動作を返します。
     * 
     * @return 削除規則の参照動作、削除規則がない場合は{@code null}
     */
    public String getOnDelete() {
        return onDelete;
    }

    /**
     * 削除規則の参照動作を設定します。
     * 
     * @param onDelete
     *            削除規則の参照動作、削除規則がない場合は{@code null}
     */
    public void setOnDelete(String onDelete) {
        this.onDelete = onDelete;
    }

    /**
     * 更新規則の参照動作を返します。
     * 
     * @return 更新規則の参照動作、更新規則がない場合は{@code null}
     */
    public String getOnUpdate() {
        return onUpdate;
    }

    /**
     * 更新規則の参照動作を設定します。
     * 
     * @param onUpdate
     *            更新規則の参照動作、更新規則がない場合は{@code null}
     */
    public void setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
    }

}
