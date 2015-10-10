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

import org.seasar.extension.jdbc.AutoDelete;
import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.FieldUtil;

/**
 * {@link AutoDelete}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoDeleteImpl<T> extends AbstractAutoUpdate<T, AutoDelete<T>>
        implements AutoDelete<T> {

    /** DELETE文 */
    protected static final String DELETE_STATEMENT = "delete from ";

    /** バージョンプロパティを無視して削除する場合<code>true</code> */
    protected boolean ignoreVersion;

    /** where句 */
    protected final WhereClause whereClause = new WhereClause();

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entity
     *            エンティティ
     */
    public AutoDeleteImpl(final JdbcManagerImplementor jdbcManager,
            final T entity) {
        super(jdbcManager, entity);
    }

    public AutoDelete<T> ignoreVersion() {
        ignoreVersion = true;
        return this;
    }

    public AutoDelete<T> suppresOptimisticLockException() {
        suppresOptimisticLockException = true;
        return this;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareWhereClause();
        prepareParams();
        prepareSql();
    }

    /**
     * where句の準備をします。
     */
    protected void prepareWhereClause() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getIdPropertyMetaList()) {
            whereClause.addAndSql(ConditionType.EQ.getCondition(propertyMeta
                    .getColumnMeta().getName(), null));
        }
        if (!ignoreVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            whereClause.addAndSql(ConditionType.EQ.getCondition(propertyMeta
                    .getColumnMeta().getName(), null));
        }
    }

    /**
     * バインド変数を準備します．
     */
    protected void prepareParams() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getIdPropertyMetaList()) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            addParam(value, propertyMeta);
        }
        if (!ignoreVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            addParam(value, propertyMeta);
        }
    }

    /**
     * SQLに変換します。
     * 
     * @return SQL
     */
    @Override
    protected String toSql() {
        final String tableName = entityMeta.getTableMeta().getFullName();
        final StringBuilder buf = new StringBuilder(DELETE_STATEMENT.length()
                + tableName.length() + whereClause.getLength());
        return new String(buf.append(DELETE_STATEMENT).append(tableName)
                .append(whereClause.toSql()));
    }

    @Override
    protected boolean isOptimisticLock() {
        return !ignoreVersion && entityMeta.hasVersionPropertyMeta();
    }

}
