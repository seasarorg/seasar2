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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.Update;
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

    /**
     * @param jdbcManager
     *            JDBCマネージャ
     * @param entity
     *            エンティティ
     */
    public AbstractAutoUpdate(final JdbcManager jdbcManager, final T entity) {
        super(jdbcManager);
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
        logSql();
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
     * データベースの更新を実行します。
     * 
     * @return 更新した行数
     */
    protected int executeInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        final PreparedStatement ps = getPreparedStatement(jdbcContext);
        prepareBindVariables(ps);
        return PreparedStatementUtil.executeUpdate(ps);
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
        prepareBindVariables(ps);
        return ps;
    }

}
