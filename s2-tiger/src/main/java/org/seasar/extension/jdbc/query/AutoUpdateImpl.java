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
import java.util.Map;
import java.util.Set;

import org.seasar.extension.jdbc.AutoUpdate;
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
 * {@link AutoUpdate}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoUpdateImpl<T> extends AbstractAutoUpdate<T, AutoUpdate<T>>
        implements AutoUpdate<T> {

    /** UDPATE文 */
    protected static final String UPDATE_STATEMENT = "update ";

    /** バージョンプロパティを更新対象に含める場合<code>true</code> */
    protected boolean includeVersion;

    /** <code>null</code>値のプロパティを更新から除外する場合<code>true</code> */
    protected boolean excludesNull;

    /** 更新対象とするプロパティ */
    protected final Set<String> includesProperties = CollectionsUtil
            .newHashSet();

    /** 更新から除外するプロパティ */
    protected final Set<String> excludesProperties = CollectionsUtil
            .newHashSet();

    /** 更新前のプロパティの状態を持つ{@link Map} */
    protected Map<String, Object> beforeStates;

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
     * @param entity
     *            エンティティ
     */
    public AutoUpdateImpl(final JdbcManagerImplementor jdbcManager,
            final T entity) {
        super(jdbcManager, entity);
    }

    public AutoUpdate<T> includesVersion() {
        includeVersion = true;
        return this;
    }

    public AutoUpdate<T> excludesNull() {
        excludesNull = true;
        return this;
    }

    public AutoUpdate<T> includes(final CharSequence... propertyNames) {
        includesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoUpdate<T> excludes(final CharSequence... propertyNames) {
        excludesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoUpdate<T> changedFrom(final T beforeEntity) {
        this.beforeStates = CollectionsUtil.newHashMap(entityMeta
                .getPropertyMetaSize());
        for (final PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            final String propertyName = propertyMeta.getName();
            final Object value = FieldUtil.get(propertyMeta.getField(),
                    beforeEntity);
            this.beforeStates.put(propertyName, value);
        }
        return this;
    }

    public AutoUpdate<T> changedFrom(
            final Map<String, ? extends Object> beforeStates) {
        this.beforeStates = CollectionsUtil.newHashMap(beforeStates.size());
        this.beforeStates.putAll(beforeStates);
        return this;
    }

    public AutoUpdate<T> suppresOptimisticLockException() {
        suppresOptimisticLockException = true;
        return this;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTargetProperties();
        prepareSetClause();
        prepareWhereClause();
        prepareParams();
        prepareSql();
    }

    @Override
    protected int executeInternal() {
        if (targetProperties.isEmpty()) {
            return 0;
        }
        return super.executeInternal();
    }

    /**
     * set句に設定されるプロパティの準備をします。
     */
    protected void prepareTargetProperties() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getAllColumnPropertyMeta()) {
            final String propertyName = propertyMeta.getName();
            final Field field = propertyMeta.getField();
            final Object value = FieldUtil.get(field, entity);
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
            if (excludesNull && value == null) {
                continue;
            }
            if (beforeStates != null) {
                final Object oldValue = beforeStates.get(propertyName);
                if (value == oldValue) {
                    continue;
                }
                if (value != null && value.equals(oldValue)) {
                    continue;
                }
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

    /**
     * バインド変数を準備します．
     */
    protected void prepareParams() {
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
    protected void incrementVersion() {
        if (includeVersion) {
            return;
        }
        final Field field = entityMeta.getVersionPropertyMeta().getField();
        if (field.getType() == int.class || field.getType() == Integer.class) {
            final int version = IntegerConversionUtil.toPrimitiveInt(FieldUtil
                    .get(field, entity)) + 1;
            FieldUtil.set(field, entity, Integer.valueOf(version));
        } else if (field.getType() == long.class
                || field.getType() == Long.class) {
            final long version = LongConversionUtil.toPrimitiveLong(FieldUtil
                    .get(field, entity)) + 1;
            FieldUtil.set(field, entity, Long.valueOf(version));
        }
    }

}
