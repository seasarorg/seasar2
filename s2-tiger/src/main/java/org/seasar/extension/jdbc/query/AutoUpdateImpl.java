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

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SetClause;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.util.ConditionUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link AutoUpdate}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public class AutoUpdateImpl<T> extends AbstractQuery<AutoUpdate<T>> implements
        AutoUpdate<T> {

    /** UDPATE文のキーワード */
    protected static final String UPDATE_STATEMENT = "update ";

    /** エンティティ */
    protected final T entity;

    /** エンティティメタデータ */
    protected final EntityMeta entityMeta;

    /** バージョンプロパティを更新対象に含める場合<code>true</code> */
    protected boolean includeVersion;

    /** <code>null</code>値のプロパティを更新から除外する場合<code>true</code> */
    protected boolean excludeNull;

    /** 更新対象とするプロパティ */
    protected final Set<String> includeProperties = CollectionsUtil
            .newHashSet();

    /** 更新から除外するプロパティ */
    protected final Set<String> excludeProperties = CollectionsUtil
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
     *            JDBCマネージャ
     * @param entity
     *            エンティティ
     */
    public AutoUpdateImpl(final JdbcManager jdbcManager, final T entity) {
        super(jdbcManager);
        this.entity = entity;
        this.entityMeta = jdbcManager.getEntityMetaFactory().getEntityMeta(
                entity.getClass());
    }

    public AutoUpdate<T> includeVersion() {
        includeVersion = true;
        return this;
    }

    public AutoUpdate<T> excludeNull() {
        excludeNull = true;
        return this;
    }

    public AutoUpdate<T> include(final String... propertyNames) {
        includeProperties.addAll(Arrays.asList(propertyNames));
        return this;
    }

    public AutoUpdate<T> exclude(final String... propertyNames) {
        excludeProperties.addAll(Arrays.asList(propertyNames));
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

    public AutoUpdate<T> changedFrom(final Map<String, Object> beforeStates) {
        this.beforeStates = CollectionsUtil.newHashMap(beforeStates.size());
        this.beforeStates.putAll(beforeStates);
        return this;
    }

    public int execute() {
        prepare("execute");
        logSql();
        return executeInternal();
    }

    /**
     * クエリの準備をします。
     * 
     * @param methodName
     *            メソッド名
     */
    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTargetProperties();
        prepareSetClause();
        prepareWhereClause();
        prepareParams();
        prepareSql();
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
            if (propertyMeta.isId()) {
                continue;
            }
            if (propertyMeta.isRelationship()) {
                continue;
            }
            if (propertyMeta.isVersion() && !includeVersion) {
                continue;
            }
            if (!includeProperties.isEmpty()
                    && !includeProperties.contains(propertyName)) {
                continue;
            }
            if (excludeProperties.contains(propertyName)) {
                continue;
            }
            if (excludeNull && value == null) {
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
            whereClause.addAndSql(ConditionUtil.getEqCondition(propertyMeta
                    .getColumnMeta().getName()));
        }
        if (!includeVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            whereClause.addAndSql(ConditionUtil.getEqCondition(propertyMeta
                    .getColumnMeta().getName()));
        }
    }

    /**
     * バインド変数を準備します．
     */
    protected void prepareParams() {
        for (final PropertyMeta propertyMeta : targetProperties) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            bindVariableList.add(value);
            bindVariableClassList.add(propertyMeta.getPropertyClass());
        }
        for (final PropertyMeta propertyMeta : entityMeta
                .getIdPropertyMetaList()) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            bindVariableList.add(value);
            bindVariableClassList.add(propertyMeta.getPropertyClass());
        }
        if (!includeVersion && entityMeta.hasVersionPropertyMeta()) {
            final PropertyMeta propertyMeta = entityMeta
                    .getVersionPropertyMeta();
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            bindVariableList.add(value);
            bindVariableClassList.add(propertyMeta.getPropertyClass());
        }
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
    protected String toSql() {
        final String tableName = entityMeta.getTableMeta().getName();
        final StringBuilder buf = new StringBuilder(UPDATE_STATEMENT.length()
                + tableName.length() + setClause.getLength()
                + whereClause.getLength());
        return new String(buf.append(UPDATE_STATEMENT).append(tableName)
                .append(setClause.toSql()).append(whereClause.toSql()));
    }

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
