/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.extension.jdbc.AutoBatchDelete;
import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.AutoDelete;
import org.seasar.extension.jdbc.AutoFunctionCall;
import org.seasar.extension.jdbc.AutoInsert;
import org.seasar.extension.jdbc.AutoProcedureCall;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.SqlFileBatchUpdate;
import org.seasar.extension.jdbc.SqlFileFunctionCall;
import org.seasar.extension.jdbc.SqlFileProcedureCall;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.extension.jdbc.SqlFunctionCall;
import org.seasar.extension.jdbc.SqlProcedureCall;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.extension.jdbc.query.AutoBatchDeleteImpl;
import org.seasar.extension.jdbc.query.AutoBatchInsertImpl;
import org.seasar.extension.jdbc.query.AutoBatchUpdateImpl;
import org.seasar.extension.jdbc.query.AutoDeleteImpl;
import org.seasar.extension.jdbc.query.AutoFunctionCallImpl;
import org.seasar.extension.jdbc.query.AutoInsertImpl;
import org.seasar.extension.jdbc.query.AutoProcedureCallImpl;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.extension.jdbc.query.AutoUpdateImpl;
import org.seasar.extension.jdbc.query.SqlBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlFileProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlFileSelectImpl;
import org.seasar.extension.jdbc.query.SqlFileUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.jdbc.query.SqlUpdateImpl;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * {@link JdbcManager}の実装クラスです。
 * 
 * @author higa
 * 
 * 
 */
public class JdbcManagerImpl implements JdbcManager, JdbcManagerImplementor {

    /**
     * トランザクション同期レジストリです。
     */
    protected TransactionSynchronizationRegistry syncRegistry;

    /**
     * データソースです。
     */
    protected DataSource dataSource;

    /**
     * データベースの方言です。
     */
    protected DbmsDialect dialect;

    /**
     * エンティティメタデータファクトリです。
     */
    protected EntityMetaFactory entityMetaFactory;

    /**
     * 永続化層の規約です。
     */
    protected PersistenceConvention persistenceConvention;

    /**
     * デフォルトの最大行数です。
     */
    protected int maxRows = 0;

    /**
     * デフォルトのフェッチサイズです。
     */
    protected int fetchSize = 0;

    /**
     * デフォルトのクエリタイムアウトです。
     */
    protected int queryTimeout = 0;

    public <T> AutoSelect<T> from(Class<T> baseClass) {
        return new AutoSelectImpl<T>(this, baseClass).maxRows(maxRows)
                .fetchSize(fetchSize).queryTimeout(queryTimeout);
    }

    public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
            Object... params) {
        return new SqlSelectImpl<T>(this, baseClass, sql, params).maxRows(
                maxRows).fetchSize(fetchSize).queryTimeout(queryTimeout);
    }

    public <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path) {
        return selectBySqlFile(baseClass, path, null);
    }

    public <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass,
            String path, Object parameter) {
        return new SqlFileSelectImpl<T>(this, baseClass, path, parameter)
                .maxRows(maxRows).fetchSize(fetchSize).queryTimeout(
                        queryTimeout);
    }

    public <T> AutoInsert<T> insert(final T entity) {
        return new AutoInsertImpl<T>(this, entity).queryTimeout(queryTimeout);
    }

    public <T> AutoBatchInsert<T> insertBatch(final T... entities) {
        return new AutoBatchInsertImpl<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    public <T> AutoBatchInsert<T> insertBatch(final List<T> entities) {
        return new AutoBatchInsertImpl<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    public <T> AutoUpdate<T> update(final T entity) {
        return new AutoUpdateImpl<T>(this, entity).queryTimeout(queryTimeout);
    }

    public <T> AutoBatchUpdate<T> updateBatch(final T... entities) {
        return new AutoBatchUpdateImpl<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    public <T> AutoBatchUpdate<T> updateBatch(final List<T> entities) {
        return new AutoBatchUpdateImpl<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    public SqlUpdate updateBySql(String sql, Class<?>... paramClasses) {
        return new SqlUpdateImpl(this, sql, paramClasses)
                .queryTimeout(queryTimeout);
    }

    public SqlBatchUpdate updateBatchBySql(String sql, Class<?>... paramClasses) {
        return new SqlBatchUpdateImpl(this, sql, paramClasses)
                .queryTimeout(queryTimeout);
    }

    public SqlFileUpdate updateBySqlFile(String path) {
        return new SqlFileUpdateImpl(this, path).queryTimeout(queryTimeout);
    }

    public SqlFileUpdate updateBySqlFile(String path, Object parameter) {
        return new SqlFileUpdateImpl(this, path, parameter)
                .queryTimeout(queryTimeout);
    }

    public <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path,
            List<T> params) {
        return new SqlFileBatchUpdateImpl<T>(this, path, params)
                .queryTimeout(queryTimeout);
    }

    public <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path,
            T... params) {
        return new SqlFileBatchUpdateImpl<T>(this, path, Arrays.asList(params))
                .queryTimeout(queryTimeout);
    }

    public <T> AutoDelete<T> delete(final T entity) {
        return new AutoDeleteImpl<T>(this, entity).queryTimeout(queryTimeout);
    }

    public <T> AutoBatchDelete<T> deleteBatch(final T... entities) {
        return new AutoBatchDeleteImpl<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    public <T> AutoBatchDelete<T> deleteBatch(final List<T> entities) {
        return new AutoBatchDeleteImpl<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    public AutoProcedureCall call(String procedureName) {
        return call(procedureName, null);
    }

    public AutoProcedureCall call(String procedureName, Object parameter) {
        return new AutoProcedureCallImpl(this, procedureName, parameter)
                .maxRows(maxRows).fetchSize(fetchSize).queryTimeout(
                        queryTimeout);
    }

    public SqlProcedureCall callBySql(String sql) {
        return callBySql(sql, null);
    }

    public SqlProcedureCall callBySql(String sql, Object parameter) {
        return new SqlProcedureCallImpl(this, sql, parameter).maxRows(maxRows)
                .fetchSize(fetchSize).queryTimeout(queryTimeout);
    }

    public SqlFileProcedureCall callBySqlFile(String path) {
        return callBySqlFile(path, null);
    }

    public SqlFileProcedureCall callBySqlFile(String path, Object parameter) {
        return new SqlFileProcedureCallImpl(this, path, parameter).maxRows(
                maxRows).fetchSize(fetchSize).queryTimeout(queryTimeout);
    }

    public <T> AutoFunctionCall<T> call(Class<T> resultClass,
            String functionName) {
        return call(resultClass, functionName, null);
    }

    public <T> AutoFunctionCall<T> call(Class<T> resultClass,
            String functionName, Object parameter) {
        return new AutoFunctionCallImpl<T>(this, resultClass, functionName,
                parameter).maxRows(maxRows).fetchSize(fetchSize).queryTimeout(
                queryTimeout);
    }

    public <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql) {
        return callBySql(resultClass, sql, null);
    }

    public <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql,
            Object parameter) {
        return new SqlFunctionCallImpl<T>(this, resultClass, sql, parameter)
                .maxRows(maxRows).fetchSize(fetchSize).queryTimeout(
                        queryTimeout);
    }

    public <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass,
            String path) {
        return callBySqlFile(resultClass, path, null);
    }

    public <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass,
            String path, Object parameter) {
        return new SqlFileFunctionCallImpl<T>(this, resultClass, path,
                parameter).maxRows(maxRows).fetchSize(fetchSize).queryTimeout(
                queryTimeout);
    }

    /**
     * JDBCコンテキストを返します。
     * 
     * @return JDBCコンテキスト
     */
    public JdbcContext getJdbcContext() {
        JdbcContext ctx = getTxBoundJdbcContext();
        if (ctx != null) {
            return ctx;
        }
        Connection con = DataSourceUtil.getConnection(dataSource);
        if (hasTransaction()) {
            ctx = createJdbcContext(con, true);
            setTxBoundJdbcContext(ctx);
        } else {
            ctx = createJdbcContext(con, false);
        }
        return ctx;
    }

    /**
     * 現在のトランザクションに関連づけられたJDBCコンテキストを返します。
     * 
     * @return 現在のトランザクションに関連づけられたJDBCコンテキスト
     */
    protected JdbcContext getTxBoundJdbcContext() {
        if (hasTransaction()) {
            final JdbcContext ctx = JdbcContext.class.cast(syncRegistry
                    .getResource(this));
            if (ctx != null) {
                return ctx;
            }
        }
        return null;
    }

    /**
     * 現在のトランザクションにJDBCコンテキストを関連づけます。
     * 
     * @param ctx
     *            現在のトランザクションに関連づけるJDBCコンテキスト
     */
    protected void setTxBoundJdbcContext(final JdbcContext ctx) {
        syncRegistry.putResource(this, ctx);
        syncRegistry.registerInterposedSynchronization(new SynchronizationImpl(
                ctx));
    }

    /**
     * 現在のスレッドでトランザクションが開始されていれば<code>true</code>を返します。
     * 
     * @return 現在のスレッドでトランザクションが開始されていれば<code>true</code>
     */
    protected boolean hasTransaction() {
        final int status = syncRegistry.getTransactionStatus();
        return status != Status.STATUS_NO_TRANSACTION
                && status != Status.STATUS_UNKNOWN;
    }

    /**
     * JDBCコンテキストがnullかどうかを返します。
     * 
     * @return JDBCコンテキストがnullかどうか
     */
    protected boolean isJdbcContextNull() {
        return getTxBoundJdbcContext() == null;
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
     * トランザクション同期レジストリを返します。
     * 
     * @return トランザクション同期レジストリ
     */
    public TransactionSynchronizationRegistry getSyncRegistry() {
        return syncRegistry;
    }

    /**
     * トランザクション同期レジストリを設定します。
     * 
     * @param syncRegistry
     *            トランザクション同期レジストリ
     */
    public void setSyncRegistry(TransactionSynchronizationRegistry syncRegistry) {
        this.syncRegistry = syncRegistry;
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
     * デフォルトのフェッチ数を返します。
     * 
     * @return デフォルトのフェッチ数
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * デフォルトのフェッチ数を設定します。
     * 
     * @param fetchSize
     *            デフォルトのフェッチ数
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * デフォルトの最大行数を返します。
     * 
     * @return デフォルトの最大行数
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * デフォルトの最大行数を設定します。
     * 
     * @param maxRows
     *            デフォルトの最大行数
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * デフォルトのクエリタイムアウトを返します。
     * 
     * @return デフォルトのクエリタイムアウト
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * デフォルトのクエリタイムアウトを設定します。
     * 
     * @param queryTimeout
     *            デフォルトのクエリタイムアウト
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

    public PersistenceConvention getPersistenceConvention() {
        return persistenceConvention;
    }

    /**
     * 永続化層の規約を設定します。
     * 
     * @param persistenceConvention
     *            永続化層の規約
     */
    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }

    /**
     * {@link Synchronization}の実装です。
     * 
     * @author koichik
     */
    public class SynchronizationImpl implements Synchronization {

        /** JDBCコンテキスト */
        protected final JdbcContext context;

        /**
         * インスタンスを構築します。
         * 
         * @param context
         *            JDBCコンテキスト
         */
        public SynchronizationImpl(final JdbcContext context) {
            this.context = context;
        }

        public final void beforeCompletion() {
        }

        public void afterCompletion(final int status) {
            context.destroy();
        }

    }
}
