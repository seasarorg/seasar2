/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.datasource.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author koichik
 */
public class SingletonDataSourceProxy implements DataSource {

    public static final String actualDataSourceName_BINDING = "bindingType=may";

    protected String actualDataSourceName = "jdbc.dataSource";

    public SingletonDataSourceProxy() {
    }

    public SingletonDataSourceProxy(final String actualDataSourceName) {
        setActualDataSourceName(actualDataSourceName);
    }

    public void setActualDataSourceName(final String actualDataSourceName) {
        this.actualDataSourceName = JndiResourceLocator
                .resolveName(actualDataSourceName);
    }

    public Connection getConnection() throws SQLException {
        return getActualDataSource().getConnection();
    }

    public Connection getConnection(final String username, final String password)
            throws SQLException {
        return getActualDataSource().getConnection(username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return getActualDataSource().getLogWriter();
    }

    public int getLoginTimeout() throws SQLException {
        return getActualDataSource().getLoginTimeout();
    }

    public void setLogWriter(final PrintWriter out) throws SQLException {
        getActualDataSource().setLogWriter(out);
    }

    public void setLoginTimeout(final int seconds) throws SQLException {
        getActualDataSource().setLoginTimeout(seconds);
    }

    protected DataSource getActualDataSource() {
        return (DataSource) SingletonS2ContainerFactory.getContainer()
                .getComponent(actualDataSourceName);
    }

}
