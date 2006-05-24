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
package org.seasar.extension.component.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * 他のデータソースに委譲するデータソースのプロキシです。
 * <p>
 * このプロキシはWEBアプリケーション等でユーザごとに異なったデータソースを切り替えたい場合に使われることを想定しています。
 * データソースを利用するDAOはこのプロキシをデータソースとして使用します。
 * 委譲先のデータソースはスレッドコンテキスに設定された名前を使ってS2コンテナから取得されます。
 * </p>
 * <p>
 * S2AOPのインターセプタを作成してスレッドコンテキストにユーザが使用する実際のデータソース名を設定します。
 * </p>
 * <pre>
 *     public Object invoke(MethodInvocation invocation) throws Throwable {
 *         String currentName = proxy.getDataSourceName();
 *         try {
 *             String dataSourceName = ...
 *             proxy.setDataSourceName(dataSourceName); //スレッドコンテキストにデータソース名を設定
 *             return invocation.proceed();
 *         } finally {
 *             proxy.setDataSourceName(currentName);
 *         }
 *     }
 * </pre>
 * <p>
 * DAOはスレッドコンテキストに設定された名前を持つデータソースを使ってデータベースにアクセスできます。
 * </p>
 * 
 * @author koichik
 * 
 */
public class SelectableDataSourceProxy implements DataSource {
    private final S2Container container;

    private final ThreadLocal context = new ThreadLocal();

    /**
     * インスタンスを構築します。
     */
    public SelectableDataSourceProxy(final S2Container container) {
        this.container = container;
    }

    /**
     * データソース名を返します。
     * 
     * @return データソース名。設定されていない場合は<code>null</code>
     */
    public String getDataSourceName() {
        return (String) context.get();
    }

    /**
     * データソース名をスレッドコンテキストに設定します。
     * 
     * @param dataSourceName
     *            データソース名
     */
    public void setDataSourceName(final String dataSourceName) {
        context.set(dataSourceName);
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースをS2コンテナから取得して返します。
     * 
     * @return スレッドコンテキストに設定された名前を持つデータソース
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     */
    public DataSource getDataSource() {
        final String dataSourceName = getDataSourceName();
        if (dataSourceName == null) {
            throw new EmptyRuntimeException("dataSourceName");
        }
        return (DataSource) container.getComponent(dataSourceName);
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースからコネクションを取得して返します。
     * 
     * @return スレッドコンテキストに設定された名前を持つデータソースから取得したコネクション
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースからコネクションを取得して返します。
     * 
     * @param username
     *            ユーザ名
     * @param password
     *            パスワード
     * @return スレッドコンテキストに設定された名前を持つデータソースから取得したコネクション
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public Connection getConnection(final String username, final String password)
            throws SQLException {
        return getDataSource().getConnection(username, password);
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースからログライターを取得して返します。
     * 
     * @return スレッドコンテキストに設定された名前を持つデータソースから取得したログライター
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public PrintWriter getLogWriter() throws SQLException {
        return getDataSource().getLogWriter();
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースにログライターを設定します。
     * 
     * @param out
     *            スレッドコンテキストに設定された名前を持つデータソースに設定するログライター
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public void setLogWriter(final PrintWriter out) throws SQLException {
        getDataSource().setLogWriter(out);
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースからログインタイムアウト時間(秒）を取得して返します。
     * 
     * @return スレッドコンテキストに設定された名前を持つデータソースから取得したログインタイムアウト時間(秒）
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public int getLoginTimeout() throws SQLException {
        return getDataSource().getLoginTimeout();
    }

    /**
     * スレッドコンテキストに設定された名前を持つデータソースにログインタイムアウト時間(秒）を設定します。
     * 
     * @param out
     *            スレッドコンテキストに設定された名前を持つデータソースに設定するログインタイムアウト時間(秒）
     * @throws EmptyRuntimeException
     *             スレッドコンテキストにデータソース名が設定されていない場合にスローされます
     * @throws org.seasar.framework.container.ComponentNotFoundException
     *             スレッドコンテキストに設定されたデータソース名を持つコンポーネントがS2コンテナに登録されていない場合にスローされます
     * @throws SQLException
     *             データソースで例外が発生した場合にスローされます
     */
    public void setLoginTimeout(int seconds) throws SQLException {
        getDataSource().setLoginTimeout(seconds);
    }
}
