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
package org.seasar.extension.jdbc.id;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.exception.IdGenerationFailedRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link GenerationType#IDENTITY}方式で識別子の値を自動生成するIDジェネレータです。
 * 
 * @author koichik
 */
public class IdentityIdGenerator extends AbstractIdGenerator {

    /**
     * インスタンスを構築します。
     * 
     * @param entityMeta
     *            エンティティのメタデータ
     * @param propertyMeta
     *            識別子を表すプロパティのメタデータ
     */
    public IdentityIdGenerator(final EntityMeta entityMeta,
            final PropertyMeta propertyMeta) {
        super(entityMeta, propertyMeta);
    }

    public boolean supportBatch(final JdbcManagerImplementor jdbcManager) {
        return false;
    }

    public boolean useGetGeneratedKeys(final JdbcManagerImplementor jdbcManager) {
        return jdbcManager.getDialect().supportsGetGeneratedKeys();
    }

    public boolean isInsertInto(final JdbcManagerImplementor jdbcManager) {
        return jdbcManager.getDialect().isInsertIdentityColumn();
    }

    public Object preInsert(final JdbcManagerImplementor jdbcManager,
            final Object entity, final SqlLogger sqlLogger) {
        return null;
    }

    public void postInsert(final JdbcManagerImplementor jdbcManager,
            final Object entity, final Statement statement,
            final SqlLogger sqlLogger) {
        final long id;
        if (jdbcManager.getDialect().supportsGetGeneratedKeys()) {
            id = getGeneratedId(statement);
        } else {
            id = getGeneratedId(jdbcManager, sqlLogger);
        }
        setId(entity, id);
    }

    /**
     * {@link Statement#getGeneratedKeys()}を使用して自動生成された識別子の値を取得して返します。
     * 
     * @param statement
     *            INSERT文を実行した{@link Statement}
     * @return 自動生成された識別子の値
     */
    protected long getGeneratedId(final Statement statement) {
        try {
            final ResultSet rs = statement.getGeneratedKeys();
            return getGeneratedId(rs);
        } catch (final SQLException e) {
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName(), e);
        }
    }

    /**
     * DBMS固有のSQLを使用して自動生成された識別子の値を取得して返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param sqlLogger
     *            SQLロガー
     * @return 自動生成された識別子の値
     */
    protected long getGeneratedId(final JdbcManagerImplementor jdbcManager,
            final SqlLogger sqlLogger) {
        final String sql = jdbcManager.getDialect().getIdentitySelectString(
                toQualifiedName(entityMeta.getTableMeta().getSchema(),
                        entityMeta.getTableMeta().getName()),
                propertyMeta.getColumnMeta().getName());
        sqlLogger.logSql(sql);
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final PreparedStatement ps = jdbcContext.getPreparedStatement(sql);
            final ResultSet rs = PreparedStatementUtil.executeQuery(ps);
            return getGeneratedId(rs);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * スキーマ名が指定された場合は修飾された名前を返します。
     * 
     * @param schema
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 修飾された名前
     */
    protected String toQualifiedName(String schema, String tableName) {
        if (StringUtil.isEmpty(schema)) {
            return tableName;
        }
        return schema + "." + tableName;
    }

}
