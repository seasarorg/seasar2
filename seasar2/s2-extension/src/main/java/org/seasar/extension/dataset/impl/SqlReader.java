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
package org.seasar.extension.dataset.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableReader;

/**
 * SQLを扱うための {@link DataReader}です。
 * 
 * @author higa
 * 
 */
public class SqlReader implements DataReader {

    private DataSource dataSource;

    private List tableReaders = new ArrayList();

    /**
     * {@link SqlReader}を作成します。
     * 
     * @param dataSource
     */
    public SqlReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * データセットを追加します。
     * 
     * @param dataSet
     *            データセット
     */
    public void addDataSet(DataSet dataSet) {
        for (int i = 0; i < dataSet.getTableSize(); i++) {
            DataTable table = dataSet.getTable(i);
            addTable(table.getTableName());
        }
    }

    /**
     * テーブルを追加します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void addTable(String tableName) {
        addTable(tableName, null);
    }

    /**
     * テーブルを追加します。
     * 
     * @param tableName
     *            テーブル名
     * @param condition
     *            条件
     */
    public void addTable(String tableName, String condition) {
        SqlTableReader reader = new SqlTableReader(dataSource);
        reader.setTable(tableName, condition);
        tableReaders.add(reader);
    }

    /**
     * テーブルを追加します。
     * 
     * @param tableName
     *            テーブル名
     * @param condition
     *            条件
     * @param sort
     *            ソート条件
     */
    public void addTable(String tableName, String condition, String sort) {
        SqlTableReader reader = new SqlTableReader(dataSource);
        reader.setTable(tableName, condition, sort);
        tableReaders.add(reader);
    }

    /**
     * SQLを追加します。
     * 
     * @param sql
     *            SQL
     * @param tableName
     *            テーブル名
     */
    public void addSql(String sql, String tableName) {
        SqlTableReader reader = new SqlTableReader(dataSource);
        reader.setSql(sql, tableName);
        tableReaders.add(reader);
    }

    public DataSet read() {
        DataSet dataSet = new DataSetImpl();
        for (int i = 0; i < tableReaders.size(); ++i) {
            TableReader reader = (TableReader) tableReaders.get(i);
            dataSet.addTable(reader.read());
        }
        return dataSet;
    }

}
