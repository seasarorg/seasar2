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
package org.seasar.extension.jdbc.query;

import java.sql.PreparedStatement;

import javax.persistence.OptimisticLockException;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.Update;
import org.seasar.extension.jdbc.exception.SEntityExistsException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * 更新の抽象クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 * @param <S>
 *            <code>Update</code>のサブタイプです。
 */
public abstract class AbstractAutoUpdate<T, S extends Update<S>> extends
        AbstractQuery<S> implements Update<S> {

    /** エンティティ */
    protected final T entity;

    /** エンティティメタデータ */
    protected final EntityMeta entityMeta;

    /** バージョンチェックを行った場合に、 更新行数が0行でも{@link OptimisticLockException}をスローしないなら<code>true</code> */
    protected boolean suppresOptimisticLockException;

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entity
     *            エンティティ
     */
    public AbstractAutoUpdate(final JdbcManagerImplementor jdbcManager,
            final T entity) {
        super(jdbcManager);
        if (entity == null) {
            throw new NullPointerException("entity");
        }
        this.entity = entity;
        this.entityMeta = jdbcManager.getEntityMetaFactory().getEntityMeta(
                entity.getClass());
    }

    /**
     * エンティティを返します。
     * 
     * @return エンティティ
     */
    public T getEntity() {
        return entity;
    }

    /**
     * エンティティのメタデータを返します。
     * 
     * @return エンティティのメタデータ
     */
    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    public int execute() {
        prepare("execute");
        try {
            return executeInternal();
        } catch (final RuntimeException e) {
            if (getJdbcManager().getDialect().isUniqueConstraintViolation(e)) {
                throw new SEntityExistsException(entity, executedSql, e);
            }
            throw e;
        } finally {
            completed();
        }
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        executedSql = toSql();
    }

    /**
     * SQLに変換します。
     * 
     * @return SQL
     */
    protected abstract String toSql();

    /**
     * データベースの更新を実行します。
     * 
     * @return 更新した行数
     */
    protected int executeInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            logSql();
            final PreparedStatement ps = getPreparedStatement(jdbcContext);
            final int rows = PreparedStatementUtil.executeUpdate(ps);
            postExecute(ps);
            if (isOptimisticLock()) {
                validateRows(rows);
            }
            if (entityMeta.hasVersionPropertyMeta()) {
                incrementVersion();
            }
            return rows;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * 準備されたステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 準備されたステートメント
     */
    protected PreparedStatement getPreparedStatement(
            final JdbcContext jdbcContext) {
        final PreparedStatement ps = createPreparedStatement(jdbcContext);
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
        prepareInParams(ps);
        return ps;
    }

    /**
     * 準備されたステートメントを作成します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 準備されたステートメント
     */
    protected PreparedStatement createPreparedStatement(
            final JdbcContext jdbcContext) {
        return jdbcContext.getPreparedStatement(executedSql);
    }

    /**
     * 準備されたステートメントの後処理を行います。
     * 
     * @param ps
     *            準備されたステートメント
     */
    @SuppressWarnings("unused")
    protected void postExecute(final PreparedStatement ps) {
    }

    /**
     * 楽観的同時実行制御を行っている場合は<code>true</code>を返します。
     * 
     * @return 楽観的同時実行制御を行っている場合は<code>true</code>
     */
    protected abstract boolean isOptimisticLock();

    /**
     * 行を更新または削除できたかどうかチェックします。
     * 
     * @param rows
     *            更新行数
     * @throws OptimisticLockException
     *             行を更新または削除できなかった場合
     */
    protected void validateRows(final int rows) {
        if (!suppresOptimisticLockException && rows == 0) {
            throw new SOptimisticLockException(entity);
        }
    }

    /**
     * バージョンの値を増加させます。
     */
    protected void incrementVersion() {
    }

}
