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

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * SQLをあつかう検索用の抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * 
 */
public abstract class AbstractSqlSelect<T> extends AbstractSelect<T> {

    /**
     * {@link AbstractSqlSelect}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AbstractSqlSelect(JdbcManager jdbcManager, Class<T> baseClass) {
        super(jdbcManager, baseClass);
    }

    @Override
    protected ResultSetHandler createResultListResultSetHandler() {
        DbmsDialect dialect = jdbcManager.getDialect();
        boolean simple = ValueTypes.isSimpleType(baseClass);
        ValueType valueType = simple ? dialect.getValueType(baseClass) : null;
        if (limit > 0 && !dialect.supportsLimit()) {
            if (simple) {
                return new ObjectListSupportLimitResultSetHandler(valueType,
                        limit);
            }
            return new BeanListSupportLimitResultSetHandler(baseClass, dialect,
                    executedSql, limit);

        }
        if (simple) {
            return new ObjectListResultSetHandler(valueType);
        }
        return new BeanListResultSetHandler(baseClass, dialect, executedSql);

    }

    @Override
    protected ResultSetHandler createSingleResultResultSetHandler() {
        DbmsDialect dialect = jdbcManager.getDialect();
        if (ValueTypes.isSimpleType(baseClass)) {
            ValueType valueType = dialect.getValueType(baseClass);
            return new ObjectResultSetHandler(valueType, executedSql);
        }
        return new BeanResultSetHandler(baseClass, dialect, executedSql);
    }
}