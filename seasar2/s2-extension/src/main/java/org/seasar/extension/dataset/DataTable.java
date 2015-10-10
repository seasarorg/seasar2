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
package org.seasar.extension.dataset;

import java.sql.DatabaseMetaData;

import org.seasar.extension.jdbc.ColumnNotFoundRuntimeException;

/**
 * DataSetのテーブルをあらわすインターフェースです。
 * 
 * @author higa
 * 
 */
public interface DataTable {

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    String getTableName();

    /**
     * テーブル名を設定します。
     * 
     * @param tableName
     *            テーブル名
     */
    void setTableName(String tableName);

    /**
     * 行数を返します。
     * 
     * @return 行数
     */
    int getRowSize();

    /**
     * 行を返します。
     * 
     * @param index
     *            位置
     * @return 行
     */
    DataRow getRow(int index);

    /**
     * 行を追加します。
     * 
     * @return 行
     */
    DataRow addRow();

    /**
     * 削除された行数を返します。
     * 
     * @return 削除された行数
     */
    int getRemovedRowSize();

    /**
     * 削除された行を返します。
     * 
     * @param index
     *            位置
     * @return 削除された行
     */
    DataRow getRemovedRow(int index);

    /**
     * 削除されたすべての行を返します。
     * 
     * @return 削除されたすべての行
     */
    DataRow[] removeRows();

    /**
     * カラム数を返します。
     * 
     * @return カラム数
     */
    int getColumnSize();

    /**
     * カラムを返します。
     * 
     * @param index
     *            位置
     * @return カラム
     */
    DataColumn getColumn(int index);

    /**
     * カラムを返します。
     * 
     * @param columnName
     *            カラム名
     * @return カラム
     * @throws ColumnNotFoundRuntimeException
     *             カラムが見つからなかった場合
     */
    DataColumn getColumn(String columnName)
            throws ColumnNotFoundRuntimeException;

    /**
     * カラムを持っているかどうかを返します。
     * 
     * @param columnName
     *            カラム名
     * @return カラムを持っているかどうか
     */
    boolean hasColumn(String columnName);

    /**
     * カラム名を返します。
     * 
     * @param index
     *            位置
     * @return カラム名
     */
    String getColumnName(int index);

    /**
     * カラムの型を返します。
     * 
     * @param index
     *            位置
     * @return カラムの型
     */
    ColumnType getColumnType(int index);

    /**
     * カラムの型を返します。
     * 
     * @param columnName
     *            カラム名
     * @return カラムの型
     */
    ColumnType getColumnType(String columnName);

    /**
     * カラムを追加します。
     * 
     * @param columnName
     *            カラム名
     * @return カラム
     */
    DataColumn addColumn(String columnName);

    /**
     * カラムを追加します。
     * 
     * @param columnName
     *            カラム名
     * @param columnType
     *            カラムの型
     * @return カラム
     */
    DataColumn addColumn(String columnName, ColumnType columnType);

    /**
     * メタデータを持っているかどうかを返します。
     * 
     * @return メタデータを持っているかどうか
     */
    boolean hasMetaData();

    /**
     * メタデータのセットアップを行ないます。
     * 
     * @param dbMetaData
     *            データベースのメタデータ
     */
    void setupMetaData(DatabaseMetaData dbMetaData);

    /**
     * カラムのセットアップを行ないます。
     * 
     * @param beanClass
     *            JavaBeansのクラス
     */
    void setupColumns(Class beanClass);

    /**
     * 他のオブジェクトから値をコピーします。
     * 
     * @param source
     *            他のオブジェクト
     */
    void copyFrom(Object source);
}
