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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.AutoInsert;
import org.seasar.extension.jdbc.IdGenerator;
import org.seasar.extension.jdbc.IntoClause;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValuesClause;
import org.seasar.extension.jdbc.exception.IdPropertyNotAssignedRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link AutoInsert}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoInsertImpl<T> extends AbstractAutoUpdate<T, AutoInsert<T>>
        implements AutoInsert<T> {

    /** INSERT文 */
    protected static final String INSERT_STATEMENT = "insert into ";

    /** バージョンプロパティの初期値 */
    protected static final Long INITIAL_VERSION = Long.valueOf(1L);

    /** <code>null</code>値のプロパティを挿入から除外する場合<code>true</code> */
    protected boolean excludesNull;

    /** 挿入対象とするプロパティ */
    protected final Set<String> includesProperties = CollectionsUtil
            .newHashSet();

    /** 挿入から除外するプロパティ */
    protected final Set<String> excludesProperties = CollectionsUtil
            .newHashSet();

    /** 挿入対象となるプロパティメタデータの{@link List} */
    protected final List<PropertyMeta> targetProperties = CollectionsUtil
            .newArrayList();

    /** into句 */
    protected final IntoClause intoClause = new IntoClause();

    /** values句 */
    protected final ValuesClause valuesClause = new ValuesClause();

    /** {@link Statement#getGeneratedKeys()}を使用する場合は<code>true</code> */
    protected boolean useGetGeneratedKeys;

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entity
     *            エンティティ
     */
    public AutoInsertImpl(final JdbcManagerImplementor jdbcManager,
            final T entity) {
        super(jdbcManager, entity);
    }

    public AutoInsert<T> excludesNull() {
        excludesNull = true;
        return this;
    }

    public AutoInsert<T> includes(final CharSequence... propertyNames) {
        includesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoInsert<T> excludes(final CharSequence... propertyNames) {
        excludesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTargetProperties();
        prepareIntoClause();
        prepareValuesClause();
        prepareParams();
        prepareSql();
    }

    /**
     * into句およびvalues句に設定されるプロパティの準備をします。
     */
    protected void prepareTargetProperties() {
        for (final PropertyMeta propertyMeta : entityMeta
                .getAllColumnPropertyMeta()) {
            final String propertyName = propertyMeta.getName();
            final Field field = propertyMeta.getField();
            final Object value = FieldUtil.get(field, entity);
            if (!propertyMeta.getColumnMeta().isInsertable()) {
                continue;
            }
            if (propertyMeta.isId()) {
                if (propertyMeta.hasIdGenerator()) {
                    final IdGenerator idGenerator = propertyMeta
                            .getIdGenerator(entityMeta, jdbcManager
                                    .getDialect());
                    useGetGeneratedKeys |= idGenerator
                            .useGetGeneratedKeys(jdbcManager);
                    if (idGenerator.isInsertInto(jdbcManager)) {
                        targetProperties.add(propertyMeta);
                    }
                    continue;
                }
                if (value != null) {
                    targetProperties.add(propertyMeta);
                    continue;
                }
                throw new IdPropertyNotAssignedRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
            if (excludesNull && value == null) {
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
     * value句の準備をします。
     */
    @SuppressWarnings("unused")
    protected void prepareValuesClause() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            valuesClause.addSql();
        }
    }

    /**
     * バインド変数を準備します．
     */
    protected void prepareParams() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            Object value;
            if (propertyMeta.isId() && propertyMeta.hasIdGenerator()) {
                value = getIdValue(propertyMeta);
            } else {
                value = FieldUtil.get(propertyMeta.getField(), entity);
                if (propertyMeta.isVersion()) {
                    if (value == null
                            || Number.class.cast(value).longValue() <= 0L) {
                        value = INITIAL_VERSION;
                        final Class<?> fieldClass = ClassUtil
                                .getWrapperClassIfPrimitive(propertyMeta
                                        .getPropertyClass());
                        FieldUtil.set(propertyMeta.getField(), entity,
                                NumberConversionUtil.convertNumber(fieldClass,
                                        value));
                    }
                }
            }
            addParam(value, propertyMeta);
        }
    }

    /**
     * バインドする識別子の値を返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return 識別子の値
     */
    protected Object getIdValue(final PropertyMeta propertyMeta) {
        final IdGenerator idGenerator = propertyMeta.getIdGenerator(entityMeta,
                jdbcManager.getDialect());
        return idGenerator.preInsert(jdbcManager, entity, this);
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
    protected PreparedStatement createPreparedStatement(
            final JdbcContext jdbcContext) {
        if (useGetGeneratedKeys) {
            return jdbcContext.getPreparedStatement(executedSql,
                    Statement.RETURN_GENERATED_KEYS);
        }
        return super.createPreparedStatement(jdbcContext);
    }

    @Override
    protected void postExecute(final PreparedStatement ps) {
        for (final PropertyMeta propertyMeta : entityMeta
                .getIdPropertyMetaList()) {
            if (propertyMeta.hasIdGenerator()) {
                final IdGenerator idGenerator = propertyMeta.getIdGenerator(
                        entityMeta, jdbcManager.getDialect());
                idGenerator.postInsert(jdbcManager, entity, ps, this);
            }
        }
    }

    @Override
    protected boolean isOptimisticLock() {
        return false;
    }

}
