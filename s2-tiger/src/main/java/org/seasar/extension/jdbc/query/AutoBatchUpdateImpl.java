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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SetClause;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.LongConversionUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link AutoBatchUpdate}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoBatchUpdateImpl<T> extends
        AbstractAutoBatchUpdate<T, AutoBatchUpdate<T>> implements
        AutoBatchUpdate<T> {

    /** UDPATE文 */
    protected static final String UPDATE_STATEMENT = "update ";

    /** バージョンプロパティを更新対象に含める場合<code>true</code> */
    protected boolean includeVersion;

    /** 更新対象とするプロパティ */
    protected final Set<String> includesProperties = CollectionsUtil
            .newHashSet();

    /** 更新から除外するプロパティ */
    protected final Set<String> excludesProperties = CollectionsUtil
            .newHashSet();

    /** 更新対象となるプロパティメタデータの{@link List} */
    protected final List<PropertyMeta> targetProperties = CollectionsUtil
            .newArrayList();

    /** set句 */
    protected final SetClause setClause = new SetClause();

    /** where句 */
    protected final WhereClause whereClause = new WhereClause();

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entities
     *            エンティティのリスト
     */
    public AutoBatchUpdateImpl(final JdbcManagerImplementor jdbcManager,
            final List<T> entities) {
        super(jdbcManager, entities);
    }

    public AutoBatchUpdate<T> includesVersion() {
        includeVersion = true;
        return this;
    }

    public AutoBatchUpdate<T> includes(final CharSequence... propertyNames) {
        includesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoBatchUpdate<T> excludes(final CharSequence... propertyNames) {
        excludesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoBatchUpdate<T> suppresOptimisticLockException() {
        suppresOptimisticLockException = true;
        return this;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTargetProperties();
        prepareSetClause();
        prepareWhereClause();
        prepareSql();
    }

    /**
     * set句に設定されるプロパティの準備をします。
     */
    protected void prepareTargetProperties() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getAllColumnPropertyMeta()) {
            final String propertyName = propertyMeta.getName();
            if (propertyMeta.isId()
                    || !propertyMeta.getColumnMeta().isUpdatable()) {
                continue;
            }
            if (propertyMeta.isVersion() && !includeVersion) {
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
     * set句の準備をします。
     */
    protected void prepareSetClause() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            setClause.addSql(propertyMeta.getColumnMeta().getName());
        }
        if (!includeVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            final String columnName = propertyMeta.getColumnMeta().getName();
            setClause.addSql(columnName, columnName + " + 1");
        }
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
        if (!includeVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            whereClause.addAndSql(ConditionType.EQ.getCondition(propertyMeta
                    .getColumnMeta().getName(), null));
        }
    }

    @Override
    protected void prepareParams(final T entity) {
        for (final PropertyMeta propertyMeta : targetProperties) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            addParam(value, propertyMeta);
        }
        for (final PropertyMeta propertyMeta : entityMeta
                .getIdPropertyMetaList()) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            addParam(value, propertyMeta);
        }
        if (!includeVersion && entityMeta.hasVersionPropertyMeta()) {
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
        final StringBuilder buf = new StringBuilder(UPDATE_STATEMENT.length()
                + tableName.length() + setClause.getLength()
                + whereClause.getLength());
        return new String(buf.append(UPDATE_STATEMENT).append(tableName)
                .append(setClause.toSql()).append(whereClause.toSql()));
    }

    @Override
    protected boolean isOptimisticLock() {
        return !includeVersion && entityMeta.hasVersionPropertyMeta();
    }

    @Override
    protected void incrementVersions() {
        if (includeVersion) {
            return;
        }
        final Field field = entityMeta.getVersionPropertyMeta().getField();
        for (final T entity : entities) {
            if (field.getType() == int.class
                    || field.getType() == Integer.class) {
                final int version = IntegerConversionUtil
                        .toPrimitiveInt(FieldUtil.get(field, entity)) + 1;
                FieldUtil.set(field, entity, Integer.valueOf(version));
            } else if (field.getType() == long.class
                    || field.getType() == Long.class) {
                final long version = LongConversionUtil
                        .toPrimitiveLong(FieldUtil.get(field, entity)) + 1;
                FieldUtil.set(field, entity, Long.valueOf(version));
            }
        }
    }

}
