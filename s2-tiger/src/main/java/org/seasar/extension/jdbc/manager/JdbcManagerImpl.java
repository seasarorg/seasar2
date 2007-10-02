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
package org.seasar.extension.jdbc.manager;

import java.sql.Connection;

import javax.sql.DataSource;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.util.TransactionManagerUtil;
import org.seasar.framework.util.TransactionUtil;

/**
 * {@link JdbcManager}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class JdbcManagerImpl implements JdbcManager, Synchronization {

    private static final Object[] EMPTY_PARAMETERS = new Object[0];

    private TransactionManager transactionManager;

    private DataSource dataSource;

    private DbmsDialect dialect;

    private EntityMetaFactory entityMetaFactory;

    private ThreadLocal<JdbcContext> jdbcContexts = new ThreadLocal<JdbcContext>();

    private int maxRows = 0;

    private int fetchSize = 100;

    private int queryTimeout = 0;

    public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql) {
        return selectBySql(baseClass, sql, EMPTY_PARAMETERS);
    }

    public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
            Object... parameters) {
        return new SqlSelectImpl<T>(this, baseClass, sql, parameters).maxRows(
                maxRows).fetchSize(fetchSize).queryTimeout(queryTimeout);
    }

    public <T> AutoSelect<T> from(Class<T> baseClass) {
        return new AutoSelectImpl<T>(this, baseClass);
    }

    public void afterCompletion(int status) {
    }

    public void beforeCompletion() {
        JdbcContext ctx = jdbcContexts.get();
        if (ctx == null) {
            throw new NullPointerException("jdbcContext");
        }
        jdbcContexts.set(null);
        ctx.destroy();
    }

    /**
     * JDBCコンテキストを返します。
     * 
     * @return JDBCコンテキスト
     */
    public JdbcContext getJdbcContext() {
        JdbcContext ctx = jdbcContexts.get();
        if (ctx != null) {
            return ctx;
        }
        Transaction tx = TransactionManagerUtil
                .getTransaction(transactionManager);
        Connection con = DataSourceUtil.getConnection(dataSource);
        if (tx != null) {
            ctx = createJdbcContext(con, true);
            jdbcContexts.set(ctx);
            TransactionUtil.registerSynchronization(tx, this);
        } else {
            ctx = createJdbcContext(con, false);
        }
        return ctx;
    }

    /**
     * JDBCコンテキストがnullかどうかを返します。
     * 
     * @return JDBCコンテキストがnullかどうか
     */
    protected boolean isJdbcContextNull() {
        return jdbcContexts.get() == null;
    }

    /**
     * JDBCコンテキストを作成します。
     * 
     * @param connection
     *            コネクション
     * @param transactional
     *            トランザクション中かどうか
     * @return JDBCコンテキスト
     */
    protected JdbcContext createJdbcContext(Connection connection,
            boolean transactional) {
        return new JdbcContextImpl(connection, transactional);
    }

    /**
     * トランザクションマネージャを返します。
     * 
     * @return トランザクションマネージャ
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * トランザクションマネージャを設定します。
     * 
     * @param transactionManager
     *            トランザクションマネージャ
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
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
     * データソースを設定します。
     * 
     * @param dataSource
     *            データソース
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * フェッチ数を返します。
     * 
     * @return フェッチ数
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * 最大行数を返します。
     * 
     * @return 最大行数
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * クエリタイムアウトを返します。
     * 
     * @return クエリタイムアウト
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * クエリタイムアウトを設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウト
     */
    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    /**
     * データベースの方言を返します。
     * 
     * @return データベースの方言
     */
    public DbmsDialect getDialect() {
        return dialect;
    }

    /**
     * データベースの方言を設定します。
     * 
     * @param dialect
     *            データベースの方言
     */
    public void setDialect(DbmsDialect dialect) {
        this.dialect = dialect;
    }

    /**
     * エンティティメタデータファクトリを返します。
     * 
     * @return エンティティメタデータファクトリ
     */
    public EntityMetaFactory getEntityMetaFactory() {
        return entityMetaFactory;
    }

    /**
     * エンティティメタデータファクトリを設定します。
     * 
     * @param entityMetaFactory
     *            エンティティメタデータファクトリ
     */
    public void setEntityMetaFactory(EntityMetaFactory entityMetaFactory) {
        this.entityMetaFactory = entityMetaFactory;
    }
}