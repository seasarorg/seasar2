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
package org.seasar.extension.dbcp.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.DriverManagerUtil;
import org.seasar.framework.util.StringUtil;

public class XADataSourceImpl implements XADataSource {

    private String driverClassName_;

    private String url_;

    private String user_;

    private String password_;

    private Properties properties_ = new Properties();

    public XADataSourceImpl() {
    }

    public String getDriverClassName() {
        return driverClassName_;
    }

    public void setDriverClassName(String driverClassName) {
        driverClassName_ = driverClassName;
        if (driverClassName != null && driverClassName.length() > 0) {
            DriverManagerUtil.registerDriver(driverClassName);
        }
    }

    public String getURL() {
        return url_;
    }

    public void setURL(String url) {
        url_ = url;
    }

    public String getUser() {
        return user_;
    }

    public void setUser(String user) {
        user_ = user;
    }

    public String getPassword() {
        return password_;
    }

    public void setPassword(String password) {
        password_ = password;
    }

    public void addProperty(String name, String value) {
        properties_.put(name, value);
    }

    public XAConnection getXAConnection() throws SQLException {
        return getXAConnection(user_, password_);
    }

    public XAConnection getXAConnection(String user, String password)
            throws SQLException {

        Connection con = null;
        if (StringUtil.isEmpty(user)) {
            con = DriverManager.getConnection(url_);
        } else if (properties_.isEmpty()) {
            con = DriverManager.getConnection(url_, user, password);
        } else {
            Properties info = new Properties();
            info.putAll(properties_);
            info.put("user", user);
            info.put("password", password);
            con = DriverManager.getConnection(url_, info);
        }
        return new XAConnectionImpl(con);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(final PrintWriter logWriter) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLoginTimeout(final int loginTimeout) throws SQLException {
    }
}
