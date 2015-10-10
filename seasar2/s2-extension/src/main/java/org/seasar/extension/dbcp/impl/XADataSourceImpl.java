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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.seasar.framework.log.Logger;
import org.seasar.framework.util.DriverManagerUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link XADataSource}の実装です。
 * 
 * @author higa
 * 
 */
public class XADataSourceImpl implements XADataSource {

    private Logger logger = Logger.getLogger(XADataSourceImpl.class);

    private String driverClassName;

    private String url;

    private String user;

    private String password;

    private Properties properties = new Properties();

    private int loginTimeout;

    /**
     * {@link XADataSourceImpl}を作成します。
     */
    public XADataSourceImpl() {
    }

    /**
     * ドライバクラス名を返します。
     * 
     * @return ドライバクラス名
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * ドライバクラス名を設定します。
     * 
     * @param driverClassName
     *            ドライバクラス名
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        if (driverClassName != null && driverClassName.length() > 0) {
            DriverManagerUtil.registerDriver(driverClassName);
        }
    }

    /**
     * URLを返します。
     * 
     * @return URL
     */
    public String getURL() {
        return url;
    }

    /**
     * URLを設定します。
     * 
     * @param url
     *            URL
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * ユーザを返します。
     * 
     * @return ユーザ
     */
    public String getUser() {
        return user;
    }

    /**
     * ユーザを設定します。
     * 
     * @param user
     *            ユーザ
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * パスワードを返します。
     * 
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定します。
     * 
     * @param password
     *            パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * プロパティを追加します。
     * 
     * @param name
     *            プロパティ名
     * @param value
     *            値
     */
    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public XAConnection getXAConnection() throws SQLException {
        return getXAConnection(user, password);
    }

    public XAConnection getXAConnection(String user, String password)
            throws SQLException {

        Properties info = new Properties();
        info.putAll(properties);
        if (StringUtil.isNotEmpty(user)) {
            info.put("user", user);
        }
        if (StringUtil.isNotEmpty(password)) {
            info.put("password", password);
        }
        int currentLoginTimeout = DriverManager.getLoginTimeout();
        try {
            DriverManager.setLoginTimeout(loginTimeout);
            Connection con = DriverManager.getConnection(url, info);
            return new XAConnectionImpl(con);
        } finally {
            try {
                DriverManager.setLoginTimeout(currentLoginTimeout);
            } catch (Exception e) {
                logger.log("ESSR0017", new Object[] { e.toString() }, e);
            }
        }
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(final PrintWriter logWriter) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    public void setLoginTimeout(final int loginTimeout) throws SQLException {
        this.loginTimeout = loginTimeout;
    }
}
