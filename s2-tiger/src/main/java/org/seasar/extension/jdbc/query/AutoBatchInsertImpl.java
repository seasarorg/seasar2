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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.IntoClause;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValuesClause;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link AutoBatchUpdate}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoBatchInsertImpl<T> extends
        AbstractAutoBatchUpdate<T, AutoBatchInsert<T>> implements
        AutoBatchInsert<T> {

    /** INSERT文 */
    protected static final String INSERT_STATEMENT = "insert into ";

    /** 挿入対象とするプロパティ */
    protected final Set<String> includesProperties = CollectionsUtil
            .newHashSet();

    /** 挿入から除外するプロパティ */
    protected final Set<String> excludesProperties = CollectionsUtil
            .newHashSet();

    /** 更新対象となるプロパティメタデータの{@link List} */
    protected final List<PropertyMeta> targetProperties = CollectionsUtil
            .newArrayList();

    /** into句 */
    protected final IntoClause intoClause = new IntoClause();

    /** values句 */
    protected final ValuesClause valuesClause = new ValuesClause();

    /**
     * @param jdbcManager
     *            JDBCマネージャ
     * @param entities
     *            エンティティのリスト
     */
    public AutoBatchInsertImpl(final JdbcManager jdbcManager,
            final List<T> entities) {
        super(jdbcManager, entities);
    }

    public AutoBatchInsert<T> includes(final String... propertyNames) {
        includesProperties.addAll(Arrays.asList(propertyNames));
        return this;
    }

    public AutoBatchInsert<T> excludes(final String... propertyNames) {
        excludesProperties.addAll(Arrays.asList(propertyNames));
        return this;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTargetProperties();
        prepareIntoClause();
        prepareValuesClause();
        prepareSql();
    }

    /**
     * into句に設定されるプロパティの準備をします。
     */
    protected void prepareTargetProperties() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getAllColumnPropertyMeta()) {
            final String propertyName = propertyMeta.getName();
            if (!propertyMeta.getColumnMeta().isInsertable()) {
                continue;
            }
            if (!includesProperties.isEmpty()
                    && !includesProperties.contains(propertyName)) {
                continue;
            }
            if (excludesProperties.contains(propertyName)) {
                continue;
            }
            targetProperties.add(propertyMeta);
        }
    }

    /**
     * into句の準備をします。
     */
    protected void prepareIntoClause() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            intoClause.addSql(propertyMeta.getColumnMeta().getName());
        }
    }

    /**
     * where句の準備をします。
     */
    @SuppressWarnings("unused")
    protected void prepareValuesClause() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            valuesClause.addSql();
        }
    }

    @Override
    protected void prepareParams(final T entity) {
        for (final PropertyMeta propertyMeta : targetProperties) {
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
        final StringBuilder buf = new StringBuilder(INSERT_STATEMENT.length()
                + tableName.length() + intoClause.getLength()
                + valuesClause.getLength());
        return new String(buf.append(INSERT_STATEMENT).append(tableName)
                .append(intoClause.toSql()).append(valuesClause.toSql()));
    }

    @Override
    protected boolean isOptimisticLock() {
        return false;
    }

}
