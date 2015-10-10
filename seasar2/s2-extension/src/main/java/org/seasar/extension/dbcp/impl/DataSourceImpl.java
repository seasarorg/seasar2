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
package org.seasar.extension.dbcp.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.dbcp.ConnectionPool;

/**
 * {@link DataSource}の実装です。
 * 
 * @author higa
 * 
 */
public class DataSourceImpl implements DataSource, Serializable {

    static final long serialVersionUID = 1L;

    private ConnectionPool connectionPool;

    /**
     * {@link DataSourceImpl}を作成します。
     * 
     * @param connectionPool
     *            コネクションプール
     */
    public DataSourceImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * コネクションプールを返します。
     * 
     * @return コネクションプール
     */
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public Connection getConnection() throws SQLException {
        Connection con = connectionPool.checkOut();
        return con;
    }

    public Connection getConnection(String user, String password)
            throws SQLException {

        return getConnection();
    }

    public void setLoginTimeout(int loginTimeout) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLogWriter(PrintWriter logWriter) throws SQLException {
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }
}