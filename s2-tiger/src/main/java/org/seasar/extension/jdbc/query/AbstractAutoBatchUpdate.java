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

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.seasar.extension.jdbc.BatchUpdate;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.LongConversionUtil;
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

    /**
     * エンティティのリストを返します。
     * 
     * @return エンティティのリスト
     */
    public List<T> getEntities() {
        return entities;
    }

    public int[] execute() {
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
            final int[] rows = executeBatch(ps);
            if (isOptimisticLock()) {
                validateRows(rows);
            }
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
        for (final T entity : entities) {
            prepareParams(entity);
            logSql();
            prepareInParams(ps);
            PreparedStatementUtil.addBatch(ps);
            resetParams();
        }
        return PreparedStatementUtil.executeBatch(ps);
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

    /**
     * バージョンの値を増加させます。
     */
    protected void incrementVersions() {
        Field field = entityMeta.getVersionPropertyMeta().getField();
        for (final T entity : entities) {
            if (field.getType() == int.class
                    || field.getType() == Integer.class) {
                int version = IntegerConversionUtil.toPrimitiveInt(FieldUtil
                        .get(field, entity)) + 1;
                FieldUtil.set(field, entity, Integer.valueOf(version));
            } else if (field.getType() == long.class
                    || field.getType() == Long.class) {
                long version = LongConversionUtil.toPrimitiveLong(FieldUtil
                        .get(field, entity)) + 1;
                FieldUtil.set(field, entity, Long.valueOf(version));
            }
        }
    }

}
