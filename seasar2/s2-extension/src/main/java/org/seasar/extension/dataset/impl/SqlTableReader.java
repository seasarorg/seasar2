/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
 * @author higa
 * 
 */
public class SqlTableReader implements TableReader {

    private DataSource dataSource_;

    private String tableName_;

    private String sql_;

    public SqlTableReader(DataSource dataSource) {
        dataSource_ = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource_;
    }

    public String getTableName() {
        return tableName_;
    }

    public String getSql() {
        return sql_;
    }

    public void setTable(String tableName) {
        setTable(tableName, null);
    }

    public void setTable(String tableName, String condition) {
        StringBuffer sortBuf = new StringBuffer(100);
        String[] primaryKeys = null;
        Connection con = DataSourceUtil.getConnection(dataSource_);
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

    public void setTable(String tableName, String condition, String sort) {
        tableName_ = tableName;
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
        sql_ = sqlBuf.toString();
    }

    public void setSql(String sql, String tableName) {
        sql_ = sql;
        tableName_ = tableName;

    }

    /**
     * @see org.seasar.extension.dataset.TableReader#read()
     */
    public DataTable read() {
        SelectHandler selectHandler = new BasicSelectHandler(dataSource_, sql_,
                new DataTableResultSetHandler(tableName_));
        return (DataTable) selectHandler.execute(null);
    }
}
