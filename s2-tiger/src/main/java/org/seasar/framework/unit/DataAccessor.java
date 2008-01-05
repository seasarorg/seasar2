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
package org.seasar.framework.unit;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * Excelファイルやデータベースにアクセスします。
 * <p>
 * 通常、このインターフェースの実装クラスは<code>s2junit4.dicon</code>に定義されます。
 * </p>
 * 
 * @author taedium
 * 
 */
public interface DataAccessor {

    /**
     * Excelファイルから読み込みんだデータを返します。
     * 
     * @param path
     *            Excelファイルのパス
     * @return データセット
     */
    DataSet readXls(String path);

    /**
     * Excelファイルから読み込みんだデータを返します。
     * 
     * @param path
     *            Excelファイルのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     * @return データセット
     */
    DataSet readXls(String path, boolean trimString);

    /**
     * データセットのデータをExcelに書き込みます。
     * 
     * @param path
     *            Excelファイルのパス
     * @param dataSet
     *            データセット
     */
    void writeXls(String path, DataSet dataSet);

    /**
     * データセットのデータをデータベースに書き込みます。
     * 
     * @param dataSet
     *            データセット
     */
    void writeDb(DataSet dataSet);

    /**
     * データベースから読み込んだデータを返します。
     * <p>
     * 読み込む対象とするテーブルはデータセットの情報から特定します。
     * </p>
     * 
     * @param dataSet
     *            データセット
     * @return 新しいデータセット
     */
    DataSet readDb(DataSet dataSet);

    /**
     * テーブル名を指定してデータベースから読み込んだデータを返します。
     * 
     * @param table
     *            テーブル名
     * @return データテーブル
     */
    DataTable readDbByTable(String table);

    /**
     * テーブル名と条件を指定してデータベースから読み込んだデータを返します。
     * 
     * @param table
     *            テーブル名
     * @param condition
     *            条件
     * @return データテーブル
     */
    DataTable readDbByTable(String table, String condition);

    /**
     * SQLを指定してデータベースから読み込んだデータを返します。
     * 
     * @param sql
     *            SQL
     * @param tableName
     *            テーブル名
     * @return データテーブル
     */
    DataTable readDbBySql(String sql, String tableName);

    /**
     * 指定されたExcelファイルのデータをデータベースに書き込みます。
     * 
     * @param path
     *            Excelファイルのパス
     */
    void readXlsWriteDb(String path);

    /**
     * 指定されたExcelファイルのデータをデータベースに書き込みます。
     * 
     * @param path
     *            Excelファイルのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     */
    void readXlsWriteDb(String path, boolean trimString);

    /**
     * 指定されたExcelファイルのデータでデータベースのテーブルの特定行を置換します。
     * 
     * @param path
     *            Excelファイルのパス
     */
    void readXlsReplaceDb(String path);

    /**
     * 指定されたExcelファイルのデータでデータベースのテーブルの特定行を置換します。
     * 
     * @param path
     *            Excelファイルのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     */
    void readXlsReplaceDb(String path, boolean trimString);

    /**
     * 指定されたExcelファイルのデータでデータベースのテーブルの全行を置換します。
     * 
     * @param path
     *            Excelファイルのパス
     */
    void readXlsAllReplaceDb(String path);

    /**
     * 指定されたExcelファイルのデータでデータベースのテーブルの全行を置換します。
     * 
     * @param path
     *            Excelファイルのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     */
    void readXlsAllReplaceDb(String path, boolean trimString);

    /**
     * 主キーを条件としてデータベースのデータを再読み込みし、新しいデータを返します。
     * 
     * @param dataSet
     *            データセット
     * @return 新しいデータセット
     */
    DataSet reload(DataSet dataSet);

    /**
     * 主キーを条件としてデータベースのテーブルのデータを再読み込みし、新しいデータを返します。
     * 
     * @param table
     *            データテーブル
     * @return 新しいデータテーブル
     */
    DataTable reload(DataTable table);

    /**
     * データセットに主キーが存在する場合は主キーを条件に再読み込みし、含まれていない場合は全件を読み込みます。
     * 再読み込み、または読み込みはデータテーブルごとに行い新しいデータを返します。
     * 
     * @param dataSet
     *            データセット
     * @return 新しいデータセット
     */
    DataSet reloadOrReadDb(DataSet dataSet);

    /**
     * 主キーを条件にしてデータベースのデータを削除します。
     * 
     * @param dataSet
     *            データセット
     */
    void deleteDb(DataSet dataSet);

    /**
     * 指定されたテーブルのデータをデータベースから削除します。
     * 
     * @param tableName
     *            テーブル名
     */
    void deleteTable(String tableName);

}
