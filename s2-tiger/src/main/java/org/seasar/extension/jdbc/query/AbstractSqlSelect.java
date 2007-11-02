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

import java.util.Map;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.Select;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.handler.MapListResultSetHandler;
import org.seasar.extension.jdbc.handler.MapListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.MapResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * SQLをあつかう検索用の抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * @param <S>
 *            <code>Select</code>のサブタイプです。
 */
public abstract class AbstractSqlSelect<T, S extends Select<T, S>> extends
        AbstractSelect<T, S> implements Select<T, S> {

    /**
     * {@link AbstractSqlSelect}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AbstractSqlSelect(JdbcManagerImplementor jdbcManager,
            Class<T> baseClass) {
        super(jdbcManager, baseClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ResultSetHandler createResultListResultSetHandler() {
        DbmsDialect dialect = jdbcManager.getDialect();
        PersistenceConvention persistenceConvention = jdbcManager
                .getPersistenceConvention();
        boolean simple = ValueTypes.isSimpleType(baseClass);
        ValueType valueType = simple ? dialect.getValueType(baseClass) : null;
        if (limit > 0 && !dialect.supportsLimit()) {
            if (simple) {
                return new ObjectListSupportLimitResultSetHandler(valueType,
                        limit);
            }
            if (Map.class.isAssignableFrom(baseClass)) {
                return new MapListSupportLimitResultSetHandler(
                        (Class<? extends Map>) baseClass, dialect,
                        persistenceConvention, executedSql, limit);
            }
            return new BeanListSupportLimitResultSetHandler(baseClass, dialect,
                    executedSql, limit);

        }
        if (simple) {
            return new ObjectListResultSetHandler(valueType);
        }
        if (Map.class.isAssignableFrom(baseClass)) {
            return new MapListResultSetHandler(
                    (Class<? extends Map>) baseClass, dialect,
                    persistenceConvention, executedSql);
        }
        return new BeanListResultSetHandler(baseClass, dialect, executedSql);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected ResultSetHandler createSingleResultResultSetHandler() {
        DbmsDialect dialect = jdbcManager.getDialect();
        PersistenceConvention persistenceConvention = jdbcManager
                .getPersistenceConvention();
        if (ValueTypes.isSimpleType(baseClass)) {
            ValueType valueType = dialect.getValueType(baseClass);
            return new ObjectResultSetHandler(valueType, executedSql);
        }
        if (Map.class.isAssignableFrom(baseClass)) {
            return new MapResultSetHandler((Class<? extends Map>) baseClass,
                    dialect, persistenceConvention, executedSql);
        }
        return new BeanResultSetHandler(baseClass, dialect, executedSql);
    }
}