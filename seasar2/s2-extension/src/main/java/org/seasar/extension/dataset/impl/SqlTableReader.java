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

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableReader;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.util.StringUtil;

/**
 * SQL用の {@link TableReader}です。
 * 
 * @author higa
 * 
 */
public class SqlTableReader implements TableReader {

    private DataSource dataSource;

    private String tableName;

    private String sql;

    /**
     * {@link SqlTableReader}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public SqlTableReader(DataSource dataSource) {
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
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

    /**
     * テーブルを設定します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void setTable(String tableName) {
        setTable(tableName, null);
    }

    /**
     * テーブルを設定します。
     * 
     * @param tableName
     *            テーブル名
     * @param condition
     *            条件
     */
    public void setTable(String tableName, String condition) {
        StringBuffer sortBuf = new StringBuffer(100);
        String[] primaryKeys = null;
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData dbMetaData = ConnectionUtil.getMetaData(con);
            primaryKeys = DatabaseMetaDataUtil.getPrimaryKeys(dbMetaData,
                    tableName);
        } finally {
            ConnectionUtil.close(con);
        }
        if (primaryKeys.length > 0) {
            for (int i = 0; i < primaryKeys.length; i++) {
                sortBuf.append(primaryKeys[i]);
                sortBuf.append(", ");
            }
            sortBuf.setLength(sortBuf.length() - 2);
        }
        setTable(tableName, condition, sortBuf.toString());
    }

    /**
     * テーブルを設定します。
     * 
     * @param tableName
     *            テーブル名
     * @param condition
     *            条件
     * @param sort
     *            ソート条件
     */
    public void setTable(String tableName, String condition, String sort) {
        this.tableName = tableName;
        StringBuffer sqlBuf = new StringBuffer(100);
        sqlBuf.append("SELECT * FROM ");
        sqlBuf.append(tableName);
        if (!StringUtil.isEmpty(condition)) {
            sqlBuf.append(" WHERE ");
            sqlBuf.append(condition);
        }
        if (!StringUtil.isEmpty(sort)) {
            sqlBuf.append(" ORDER BY ");
            sqlBuf.append(sort);
        }
        sql = sqlBuf.toString();
    }

    /**
     * SQLを設定します。
     * 
     * @param sql
     *            SQL
     * @param tableName
     *            テーブル名
     */
    public void setSql(String sql, String tableName) {
        this.sql = sql;
        this.tableName = tableName;

    }

    /**
     * @see org.seasar.extension.dataset.TableReader#read()
     */
    public DataTable read() {
        SelectHandler selectHandler = new BasicSelectHandler(dataSource, sql,
                new DataTableResultSetHandler(tableName));
        return (DataTable) selectHandler.execute(null);
    }
}
