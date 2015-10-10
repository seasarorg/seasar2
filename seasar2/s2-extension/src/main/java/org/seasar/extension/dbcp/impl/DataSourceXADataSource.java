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
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.seasar.framework.util.InitialContextUtil;

/**
 * {@link javax.sql.DataSource}から取得したJDBCコネクションを使用する{@link javax.sql.XADataSource}の実装です。
 * <p>
 * このXAデータソースが提供する{@link javax.sql.XAConnection}は非XAコネクションのラッパーであり、
 * 2フェーズ・コミット・プロトコルをシミュレートします。 これは真の2フェーズ・コミット・プロトコルではありません。
 * </p>
 * 
 * @author koichik
 */
public class DataSourceXADataSource implements XADataSource {

    /** JNDIからルックアップするデータソース名 */
    protected String dataSourceName;

    /** JNDIの{@link javax.naming.InitialContext 初期コンテキスト}を作成するための環境 */
    protected Hashtable env = new Hashtable();

    /** JNDIからルックアップしたデータソース */
    protected DataSource dataSource;

    /**
     * JNDIからルックアップするデータソース名を設定します。
     * 
     * @param dataSourceName
     *            JNDIからルックアップするデータソース名
     */
    public void setDataSourceName(final String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * JNDIの{@link javax.naming.InitialContext 初期コンテキスト}を作成するための環境を設定します。
     * 
     * @param env
     *            JNDIの初期コンテキストを作成するための環境
     */
    public void setEnv(final Map env) {
        this.env.putAll(env);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public XAConnection getXAConnection() throws SQLException {
        final Connection con = getDataSource().getConnection();
        return new XAConnectionImpl(con);
    }

    public XAConnection getXAConnection(final String user, final String password)
            throws SQLException {
        final Connection con = getDataSource().getConnection(user, password);
        return new XAConnectionImpl(con);
    }

    public void setLogWriter(final PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(final int seconds) throws SQLException {
    }

    /**
     * JNDIからルックアップしたデータソースを返します。
     * 
     * @return JNDIからルックアップしたデータソース
     */
    protected synchronized DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        final InitialContext ctx = InitialContextUtil.create(env);
        dataSource = (DataSource) InitialContextUtil
                .lookup(ctx, dataSourceName);
        return dataSource;
    }

}
