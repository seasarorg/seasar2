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
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.seasar.extension.jdbc.BatchUpdate;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.exception.SEntityExistsException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * バッチ更新の抽象クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 * @param <S>
 *            <code>BatchUpdate</code>のサブタイプです。
 */
public abstract class AbstractAutoBatchUpdate<T, S extends BatchUpdate<S>>
        extends AbstractQuery<S> implements BatchUpdate<S> {

    /** エンティティのリスト */
    protected final List<T> entities;

    /** エンティティメタデータ */
    protected final EntityMeta entityMeta;

    /** バッチサイズ */
    protected int batchSize;

    /**
     * バージョンチェックを行った場合に、 更新行数が0行でも{@link OptimisticLockException}をスローしないなら
     * <code>true</code>
     */
    protected boolean suppresOptimisticLockException;

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entities
     *            エンティティのリスト
     * @throws EmptyRuntimeException
     *             エンティティのリストが空の場合
     */
    public AbstractAutoBatchUpdate(final JdbcManagerImplementor jdbcManager,
            final List<T> entities) {
        super(jdbcManager);
        if (entities == null) {
            throw new NullPointerException("entities");
        }
        if (entities.isEmpty()) {
            throw new EmptyRuntimeException("entities");
        }
        this.entities = entities;
        entityMeta = jdbcManager.getEntityMetaFactory().getEntityMeta(
                entities.get(0).getClass());
    }

    @SuppressWarnings("unchecked")
    public S batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return (S) this;
    }

    public int[] execute() {
        prepare("executeBatch");
        try {
            return executeInternal();
        } catch (final RuntimeException e) {
            if (getJdbcManager().getDialect().isUniqueConstraintViolation(e)) {
                throw new SEntityExistsException(executedSql, e);
            }
            throw e;
        } finally {
            completed();
        }
    }

    /**
     * エンティティのリストを返します。
     * 
     * @return エンティティのリスト
     */
    public List<T> getEntities() {
        return entities;
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
     * データベースのバッチ更新を実行します。
     * 
     * @return 更新した行数の配列
     */
    protected int[] executeInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final PreparedStatement ps = getPreparedStatement(jdbcContext);
            final int[] rows = executeBatch(ps);
            if (entityMeta.hasVersionPropertyMeta()) {
                incrementVersions();
            }
            return rows;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * バッチ更新を実行します。
     * 
     * @param ps
     *            準備されたステートメント
     * @return 更新された行数の配列
     */
    protected int[] executeBatch(final PreparedStatement ps) {
        final DbmsDialect dialect = jdbcManager.getDialect();
        final int batchSize = this.batchSize > 0 ? this.batchSize : dialect
                .getDefaultBatchSize();
        final int size = entities.size();
        final int[] updateRows = new int[size];
        int pos = 0;
        for (int i = 0; i < size; ++i) {
            final T entity = entities.get(i);
            prepareParams(entity);
            logSql();
            prepareInParams(ps);
            PreparedStatementUtil.addBatch(ps);
            resetParams();
            if (i == size - 1 || (batchSize > 0 && (i + 1) % batchSize == 0)) {
                final int[] rows = PreparedStatementUtil.executeBatch(ps);
                if (isOptimisticLock()) {
                    validateRows(ps, rows);
                }
                System.arraycopy(rows, 0, updateRows, pos, rows.length);
                pos = i + 1;
            }
        }
        return updateRows;
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
     * バインド変数を準備します．
     * 
     * @param entity
     *            エンティティ
     */
    protected abstract void prepareParams(T entity);

    /**
     * 楽観的同時実行制御を行っている場合は<code>true</code>を返します。
     * 
     * @return 楽観的同時実行制御を行っている場合は<code>true</code>
     */
    protected abstract boolean isOptimisticLock();

    /**
     * 行を更新または削除できたかどうかチェックします。
     * 
     * @param ps
     *            準備されたステートメント
     * @param rows
     *            更新行数の配列
     * @throws OptimisticLockException
     *             行を更新または削除できなかった場合
     */
    protected void validateRows(final PreparedStatement ps, final int[] rows) {
        if (suppresOptimisticLockException) {
            return;
        }
        final DbmsDialect dialect = jdbcManager.getDialect();
        if (dialect.supportsBatchUpdateResults()) {
            for (int i = 0; i < rows.length; ++i) {
                if (rows[i] != 1) {
                    throw new SOptimisticLockException(entities.get(i));
                }
            }
        } else if (StatementUtil.getUpdateCount(ps) == rows.length) {
            for (int i = 0; i < rows.length; ++i) {
                rows[i] = 1;
            }
        } else {
            throw new SOptimisticLockException();
        }
    }

    /**
     * バージョンの値を増加させます。
     */
    protected void incrementVersions() {
    }

}
