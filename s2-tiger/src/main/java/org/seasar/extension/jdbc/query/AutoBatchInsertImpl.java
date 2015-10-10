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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.IdGenerator;
import org.seasar.extension.jdbc.IntoClause;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValuesClause;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.PreparedStatementUtil;
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

    /** バージョンプロパティの初期値 */
    protected static final Long INITIAL_VERSION = Long.valueOf(1L);

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

    /** バッチ更新が可能な場合は<code>true</code> */
    protected boolean supportBatch = true;

    /** {@link Statement#getGeneratedKeys()}を使用する場合は<code>true</code> */
    protected boolean useGetGeneratedKeys;

    /**
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entities
     *            エンティティのリスト
     */
    public AutoBatchInsertImpl(final JdbcManagerImplementor jdbcManager,
            final List<T> entities) {
        super(jdbcManager, entities);
    }

    public AutoBatchInsert<T> includes(final CharSequence... propertyNames) {
        includesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
        return this;
    }

    public AutoBatchInsert<T> excludes(final CharSequence... propertyNames) {
        excludesProperties.addAll(Arrays.asList(toStringArray(propertyNames)));
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
            if (propertyMeta.isId()) {
                if (propertyMeta.hasIdGenerator()) {
                    final IdGenerator idGenerator = propertyMeta
                            .getIdGenerator(entityMeta, jdbcManager
                                    .getDialect());
                    supportBatch &= idGenerator.supportBatch(jdbcManager);
                    useGetGeneratedKeys |= idGenerator
                            .useGetGeneratedKeys(jdbcManager);
                    if (idGenerator.isInsertInto(jdbcManager)) {
                        targetProperties.add(propertyMeta);
                    }
                    continue;
                }
                targetProperties.add(propertyMeta);
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
    protected int[] executeBatch(PreparedStatement ps) {
        if (supportBatch) {
            return super.executeBatch(ps);
        }
        final int[] updateRows = new int[entities.size()];
        for (int i = 0; i < updateRows.length; ++i) {
            final T entity = entities.get(i);
            prepareParams(entity);
            logSql();
            prepareInParams(ps);
            updateRows[i] = PreparedStatementUtil.executeUpdate(ps);
            postExecute(ps, entity);
            resetParams();
        }
        return updateRows;
    }

    @Override
    protected void prepareParams(final T entity) {
        for (final PropertyMeta propertyMeta : targetProperties) {
            Object value;
            if (propertyMeta.isId() && propertyMeta.hasIdGenerator()) {
                value = getIdValue(propertyMeta, entity);
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
     * @param entity
     *            エンティティ
     * @return 識別子の値
     */
    protected Object getIdValue(final PropertyMeta propertyMeta, final T entity) {
        final IdGenerator idGenerator = propertyMeta.getIdGenerator(entityMeta,
                jdbcManager.getDialect());
        return idGenerator.preInsert(jdbcManager, entity, this);
    }

    /**
     * INSERT文を実行後処理を行います。
     * <p>
     * {@link GenerationType#IDENTITY}方式で識別子の値を自動生成するIDジェネレータが使われた場合は、
     * 生成された値をエンティティに反映します
     * </p>
     * 
     * @param ps
     *            INSERT文を実行した{@link Statement}
     * @param entity
     *            エンティティ
     */
    protected void postExecute(final PreparedStatement ps, final T entity) {
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
