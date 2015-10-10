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

/**
 * データの抽象的な集まりをあらわします。
 * 
 * @author higa
 * 
 */
public interface DataSet {

    /**
     * テーブル数を返します。
     * 
     * @return テーブル数
     */
    int getTableSize();

    /**
     * テーブル名を返します。
     * 
     * @param index
     *            位置
     * @return テーブル名
     */
    String getTableName(int index);

    /**
     * テーブルを持っているかどうか返します。
     * 
     * @param tableName
     *            テーブル名
     * @return テーブルを持っているかどうか
     */
    boolean hasTable(String tableName);

    /**
     * テーブルを返します。
     * 
     * @param tableName
     *            テーブル名
     * @return テーブル
     * @throws TableNotFoundRuntimeException
     *             テーブルが存在しない場合
     */
    DataTable getTable(String tableName) throws TableNotFoundRuntimeException;

    /**
     * テーブルを返します。
     * 
     * @param index
     *            位置
     * @return テーブル
     */
    DataTable getTable(int index);

    /**
     * テーブルを追加します。
     * 
     * @param tableName
     *            テーブル名
     * @return テーブル
     */
    DataTable addTable(String tableName);

    /**
     * テーブルを追加します。
     * 
     * @param table
     *            テーブル
     * @return テーブル
     */
    DataTable addTable(DataTable table);

    /**
     * テーブルを削除します。
     * 
     * @param tableName
     *            テーブル名
     * @return 削除したテーブル
     * @throws TableNotFoundRuntimeException
     *             テーブルが見つからない場合
     */
    DataTable removeTable(String tableName)
            throws TableNotFoundRuntimeException;

    /**
     * テーブルを削除します。
     * 
     * @param index
     *            位置
     * @return 削除したテーブル
     */
    DataTable removeTable(int index);

    /**
     * テーブルを削除します。
     * 
     * @param table
     *            テーブル
     * @return 削除したテーブル
     * @throws TableNotFoundRuntimeException
     *             テーブルが見つからない場合
     */
    DataTable removeTable(DataTable table) throws TableNotFoundRuntimeException;
}
