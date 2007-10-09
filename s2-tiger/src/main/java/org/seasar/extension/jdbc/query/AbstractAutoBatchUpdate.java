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
package org.seasar.extension.jdbc.query;

import java.sql.PreparedStatement;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.seasar.extension.jdbc.BatchUpdate;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
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

    /**
     * @param jdbcManager
     *            JDBCマネージャ
     * @param entities
     *            エンティティのリスト
     * @throws EmptyRuntimeException
     *             エンティティのリストが空の場合
     */
    public AbstractAutoBatchUpdate(final JdbcManager jdbcManager,
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

    /**
     * エンティティのリストを返します。
     * 
     * @return エンティティのリスト
     */
    public List<T> getEntities() {
        return entities;
    }

    public int[] executeBatch() {
        prepare("executeBatch");
        return executeInternal();
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
            for (final T entity : entities) {
                prepareParams(entity);
                logSql();
                prepareInParams(ps);
                PreparedStatementUtil.addBatch(ps);
                resetParams();
            }
            final int[] rows = PreparedStatementUtil.executeBatch(ps);
            if (isOptimisticLock()) {
                validateRows(rows);
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
        final PreparedStatement ps = jdbcContext
                .getPreparedStatement(executedSql);
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
        prepareInParams(ps);
        return ps;
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
     * @param rows
     *            更新行数の配列
     * @throws OptimisticLockException
     *             行を更新または削除できなかった場合
     */
    protected void validateRows(final int[] rows) {
        for (int i = 0; i < rows.length; ++i) {
            if (rows[i] == 0) {
                throw new SOptimisticLockException(entities.get(i));
            }
        }
    }

}
