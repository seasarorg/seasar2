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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMapper;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.FromClause;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.JoinMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.OrderByClause;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SelectClause;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.JoinDuplicatedRuntimeException;
import org.seasar.extension.jdbc.handler.BeanAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListSupportLimitAutoResultSetHandler;
import org.seasar.extension.jdbc.mapper.AbstractEntityMapper;
import org.seasar.extension.jdbc.mapper.AbstractRelationshipEntityMapper;
import org.seasar.extension.jdbc.mapper.EntityMapperImpl;
import org.seasar.extension.jdbc.mapper.ManyToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToManyEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;
import org.seasar.extension.jdbc.util.QueryTokenizer;
import org.seasar.framework.util.StringUtil;

/**
 * {@link AutoSelect}の実装クラスです。
 * 
 * @author higa
 * @param <T>
 *            エンティティの型です。
 * 
 */
public class AutoSelectImpl<T> extends AbstractSelect<T, AutoSelect<T>>
        implements AutoSelect<T> {

    /**
     * 結合メタデータのリストです。
     */
    protected List<JoinMeta> joinMetaList = new ArrayList<JoinMeta>();

    /**
     * テーブルのインデックスです。
     */
    protected int tableIndex;

    /**
     * テーブル別名のマップです。
     */
    protected Map<String, String> tableAliasMap = new HashMap<String, String>();

    /**
     * エンティティメタデータのマップです。
     */
    protected Map<String, EntityMeta> entityMetaMap = new HashMap<String, EntityMeta>();

    /**
     * エンティティ名です。
     */
    protected String entityName;

    /**
     * select句です。
     */
    protected SelectClause selectClause = new SelectClause();

    /**
     * from句です。
     */
    protected FromClause fromClause = new FromClause();

    /**
     * where句です。
     */
    protected WhereClause whereClause = new WhereClause();

    /**
     * order by句です。
     */
    protected OrderByClause orderByClause = new OrderByClause();

    /**
     * ソート順です。
     */
    protected String orderBy = "";

    /**
     * 値タイプのリストです。
     */
    protected List<ValueType> valueTypeList = new ArrayList<ValueType>(50);

    /**
     * selectリストのインデックスです。
     */
    protected int selectListIndex;

    /**
     * エンティティマッパーのマップです。
     */
    protected Map<String, AbstractEntityMapper> entityMapperMap = new HashMap<String, AbstractEntityMapper>();

    /**
     * Mapによるwhere句の条件指定です。
     */
    protected Map<String, Object> conditions;

    /**
     * クライテリアです。
     */
    protected String criteria;

    /**
     * {@link AutoSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AutoSelectImpl(JdbcManager jdbcManager, Class<T> baseClass) {
        super(jdbcManager, baseClass);
    }

    public AutoSelect<T> join(String name) {
        return join(name, JoinType.LEFT_OUTER);
    }

    public AutoSelect<T> join(String name, JoinType joinType) {
        return join(name, joinType, true);
    }

    public AutoSelect<T> join(String name, boolean fetch) {
        return join(name, JoinType.LEFT_OUTER, fetch);
    }

    public AutoSelect<T> join(String name, JoinType joinType, boolean fetch) {
        joinMetaList.add(new JoinMeta(name, joinType, fetch));
        return this;
    }

    /**
     * 結合メタデータの数を返します。
     * 
     * @return 結合メタデータの数
     */
    protected int getJoinMetaSize() {
        return joinMetaList.size();
    }

    /**
     * 結合メタデータを返します。
     * 
     * @param index
     *            位置
     * @return 結合メタデータ
     */
    protected JoinMeta getJoinMeta(int index) {
        return joinMetaList.get(index);
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareTarget();
        prepareJoins();
        prepareConditions();
        prepareCriteria();
        prepareOrderBy();
        prepareSql();
    }

    /**
     * 対象エンティティの準備をします。
     */
    protected void prepareTarget() {
        String tableAlias = prepareTableAlias(null);
        EntityMeta entityMeta = prepareEntityMeta(baseClass, null);
        entityName = entityMeta.getName();
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        prepareEntity(entityMeta, tableAlias, propertyMapperList, idIndexList);
        PropertyMapper[] propertyMappers = toPropertyMapperArray(propertyMapperList);
        int[] idIndices = toIdIndexArray(idIndexList);
        entityMapperMap.put(null, new EntityMapperImpl(baseClass,
                propertyMappers, idIndices));
        fromClause.addSql(entityMeta.getTableMeta().getFullName(), tableAlias);
    }

    /**
     * エンティティの準備をします。
     * 
     * @param em
     *            エンティティメタデータ
     * @param tableAlias
     *            テーブル別名
     * @param propertyMapperList
     *            プロパティマッパーのリスト
     * @param idIndexList
     *            識別子のインデックスのリスト
     */
    protected void prepareEntity(EntityMeta em, String tableAlias,
            List<PropertyMapper> propertyMapperList, List<Integer> idIndexList) {
        for (int i = 0; i < em.getPropertyMetaSize(); i++) {
            PropertyMeta pm = em.getPropertyMeta(i);
            if (pm.isTransient() || pm.isRelationship()) {
                continue;
            }
            selectClause.addSql(tableAlias, pm.getColumnMeta().getName());
            valueTypeList.add(getValueType(pm.getPropertyClass(), pm.isLob()));
            propertyMapperList.add(new PropertyMapperImpl(pm.getField(),
                    selectListIndex));
            if (pm.isId()) {
                idIndexList.add(new Integer(selectListIndex));
            }
            ++selectListIndex;
        }
    }

    /**
     * テーブル別名を返します。
     * 
     * @param join
     *            結合名
     * @return テーブル別名
     */
    protected String getTableAlias(String join) {
        return tableAliasMap.get(join);
    }

    /**
     * テーブル別名を作成します。
     * 
     * @return テーブル別名
     */
    protected String createTableAlias() {
        return "T" + ++tableIndex + "_";
    }

    /**
     * テーブル別名を準備します。
     * 
     * @param join
     *            結合名
     * @return テーブル別名
     */
    protected String prepareTableAlias(String join) {
        String tableAlias = createTableAlias();
        tableAliasMap.put(join, tableAlias);
        return tableAlias;
    }

    /**
     * エンティティメタデータを返します。
     * 
     * @param join
     *            結合名
     * @return エンティティメタデータ
     */
    protected EntityMeta getEntityMeta(String join) {
        return entityMetaMap.get(join);
    }

    /**
     * エンティティメタデータを準備します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param join
     *            結合名
     * @return エンティティメタデータ
     */
    protected EntityMeta prepareEntityMeta(Class<?> entityClass, String join) {
        EntityMeta entityMeta = null;
        try {
            entityMeta = jdbcManager.getEntityMetaFactory().getEntityMeta(
                    entityClass);
        } catch (RuntimeException e) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            logger.log("ESSR0711", new Object[] { baseClass.getName() });
            throw e;
        }
        entityMetaMap.put(join, entityMeta);
        return entityMeta;
    }

    /**
     * 値タイプの配列を返します。
     * 
     * @return 値タイプの配列
     */
    protected ValueType[] getValueTypes() {
        return valueTypeList.toArray(new ValueType[valueTypeList.size()]);
    }

    /**
     * プロパティマッパーの配列に変換します。
     * 
     * @param propertyMapperList
     *            プロパティマッパーのリスト
     * @return プロパティマッパーの配列
     */
    protected PropertyMapperImpl[] toPropertyMapperArray(
            List<PropertyMapper> propertyMapperList) {
        return propertyMapperList
                .toArray(new PropertyMapperImpl[propertyMapperList.size()]);
    }

    /**
     * 識別子のインデックスのリストをintの配列に変換します。
     * 
     * @param idIndexList
     * @return intの配列
     */
    protected int[] toIdIndexArray(List<Integer> idIndexList) {
        int[] idIndices = new int[idIndexList.size()];
        for (int i = 0; i < idIndices.length; i++) {
            idIndices[i] = idIndexList.get(i).intValue();
        }
        return idIndices;
    }

    /**
     * エンティティマッパーを返します。
     * 
     * @param join
     *            結合名
     * @return エンティティマッパー
     */
    protected AbstractEntityMapper getEntityMapper(String join) {
        return entityMapperMap.get(join);
    }

    /**
     * 結合の準備をします。
     * 
     * @see #prepareJoin(JoinMeta)
     */
    protected void prepareJoins() {
        for (JoinMeta joinMeta : joinMetaList) {
            prepareJoin(joinMeta);
        }
    }

    /**
     * 結合の準備をします。
     * 
     * @param joinMeta
     *            結合メタデータ
     */
    protected void prepareJoin(JoinMeta joinMeta) {
        String tableAlias = prepareTableAlias(joinMeta.getName());
        String[] names = splitBaseAndProperty(joinMeta.getName());
        EntityMeta baseEntityMeta = getBaseEntityMeta(joinMeta.getName(),
                names[0]);
        AbstractEntityMapper baseEntityMapper = getBaseEntityMapper(joinMeta
                .getName(), names[0]);
        PropertyMeta propertyMeta = getPropertyMeta(baseEntityMeta, joinMeta
                .getName(), names[1]);
        Class<?> relationshipClass = propertyMeta.getRelationshipClass();
        EntityMeta inverseEntityMeta = getInverseEntityMeta(relationshipClass,
                joinMeta.getName());
        PropertyMeta inversePropertyMeta = getInversePropertyMeta(
                inverseEntityMeta, propertyMeta);
        String baseTableAlias = getTableAlias(names[0]);
        String fkTableAlias = baseTableAlias;
        String pkTableAlias = tableAlias;
        List<JoinColumnMeta> joinColumnMetaList = propertyMeta
                .getJoinColumnMetaList();
        if (propertyMeta.getMappedBy() != null) {
            fkTableAlias = tableAlias;
            pkTableAlias = baseTableAlias;
            joinColumnMetaList = inversePropertyMeta.getJoinColumnMetaList();
        }
        if (joinMeta.isFetch()) {
            List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                    50);
            List<Integer> idIndexList = new ArrayList<Integer>();
            prepareEntity(inverseEntityMeta, tableAlias, propertyMapperList,
                    idIndexList);
            PropertyMapper[] propertyMappers = toPropertyMapperArray(propertyMapperList);
            int[] idIndices = toIdIndexArray(idIndexList);
            AbstractRelationshipEntityMapper remapper = createRelationshipEntityMapper(
                    relationshipClass, propertyMappers, idIndices,
                    propertyMeta, inversePropertyMeta);
            entityMapperMap.put(joinMeta.getName(), remapper);
            baseEntityMapper.addRelationshipEntityMapper(remapper);
        }
        jdbcManager.getDialect().setupJoin(fromClause, whereClause,
                joinMeta.getJoinType(),
                inverseEntityMeta.getTableMeta().getFullName(), tableAlias,
                fkTableAlias, pkTableAlias, joinColumnMetaList);
    }

    /**
     * <p>
     * 関連名をベースとプロパティに分離します。
     * </p>
     * <p>
     * <code>aaa.bbb.ccc</code>ならベースが<code>aaa.bbb</code>、プロパティが<code>ccc</code>になります。
     * </p>
     * 
     * @param name
     *            関連名
     * @return ベースとプロパティの配列
     */
    protected String[] splitBaseAndProperty(String name) {
        String[] ret = new String[2];
        int index = name.lastIndexOf('.');
        if (index < 0) {
            ret[1] = name;
        } else {
            ret[0] = name.substring(0, index);
            ret[1] = name.substring(index + 1);
        }
        return ret;
    }

    /**
     * ベースのエンティティメタデータを返します。
     * 
     * @param join
     *            結合名
     * @param base
     *            ベースの結合名
     * @return ベースのエンティティメタデータ
     * @throws BaseJoinNotFoundRuntimeException
     *             ベースの結合が見つからない場合。
     */
    protected EntityMeta getBaseEntityMeta(String join, String base)
            throws BaseJoinNotFoundRuntimeException {
        EntityMeta baseEntityMeta = getEntityMeta(base);
        if (baseEntityMeta == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new BaseJoinNotFoundRuntimeException(entityName, join, base);
        }
        return baseEntityMeta;
    }

    /**
     * ベースのエンティティメタマッパーを返します。
     * 
     * @param join
     *            結合名
     * @param baseJoin
     *            ベースの結合名
     * @return ベースのエンティティマッパー
     * @throws BaseJoinNotFoundRuntimeException
     *             ベースの結合が見つからない場合。
     */
    protected AbstractEntityMapper getBaseEntityMapper(String join,
            String baseJoin) throws BaseJoinNotFoundRuntimeException {
        AbstractEntityMapper baseEntityMapper = getEntityMapper(baseJoin);
        if (baseEntityMapper == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new BaseJoinNotFoundRuntimeException(entityName, join,
                    baseJoin);
        }
        return baseEntityMapper;
    }

    /**
     * プロパティメタデータを返します。
     * 
     * @param baseEntityMeta
     *            ベースのエンティティメタデータ
     * @param fullPropertyName
     *            全体のプロパティ名
     * @param propertyName
     *            プロパティ名
     * @return プロパティメタデータ
     * @throws RuntimeException
     *             実行時例外が発生した場合。
     */
    protected PropertyMeta getPropertyMeta(EntityMeta baseEntityMeta,
            String fullPropertyName, String propertyName)
            throws RuntimeException {
        PropertyMeta pm = null;
        try {
            pm = baseEntityMeta.getPropertyMeta(propertyName);
        } catch (RuntimeException e) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            logger.log("ESSR0708",
                    new Object[] { entityName, fullPropertyName });
            throw e;
        }
        return pm;
    }

    /**
     * 関連の逆側のエンティティメタデータを返します。
     * 
     * @param relationshipClass
     *            関連クラス
     * @param join
     *            結合名
     * @return 関連の逆側のエンティティメタデータ
     * @throws RuntimeException
     *             実行時例外が発生した場合。
     * @throws JoinDuplicatedRuntimeException
     *             結合が重複している場合。
     * 
     */
    protected EntityMeta getInverseEntityMeta(Class<?> relationshipClass,
            String join) throws RuntimeException,
            JoinDuplicatedRuntimeException {
        EntityMeta inverseEntityMeta = null;
        try {
            inverseEntityMeta = jdbcManager.getEntityMetaFactory()
                    .getEntityMeta(relationshipClass);
        } catch (RuntimeException e) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            logger.log("ESSR0710", new Object[] { entityName, join });
            throw e;
        }
        if (entityMetaMap.put(join, inverseEntityMeta) != null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new JoinDuplicatedRuntimeException(entityName, join);
        }
        return inverseEntityMeta;
    }

    /**
     * 逆側のプロパティメタデータを返します。
     * 
     * @param inverseEntityMeta
     *            逆側のエンティティメタデータ
     * @param relationshipPropertyMeta
     *            関連のプロパティメタデータ
     * @return 逆側のプロパティメタデータ
     * 
     */
    protected PropertyMeta getInversePropertyMeta(EntityMeta inverseEntityMeta,
            PropertyMeta relationshipPropertyMeta) {
        if (relationshipPropertyMeta.getMappedBy() != null) {
            return inverseEntityMeta.getPropertyMeta(relationshipPropertyMeta
                    .getMappedBy());
        }
        return inverseEntityMeta.getMappedByPropertyMeta(
                relationshipPropertyMeta.getName(), baseClass);
    }

    /**
     * 関連エンティティマッパーを作成します。
     * 
     * @param relationshipClass
     *            関連クラス
     * @param propertyMappers
     *            プロパティマッパーの配列
     * @param idIndices
     *            識別子のインデックスの配列
     * @param propertyMeta
     *            関連のプロパティメタデータ
     * @param inversePropertyMeta
     *            逆側の関連のプロパティメタデータ
     * @return 関連エンティティマッパー
     */
    protected AbstractRelationshipEntityMapper createRelationshipEntityMapper(
            Class<?> relationshipClass, PropertyMapper[] propertyMappers,
            int[] idIndices, PropertyMeta propertyMeta,
            PropertyMeta inversePropertyMeta) {
        Field inverseField = inversePropertyMeta != null ? inversePropertyMeta
                .getField() : null;
        switch (propertyMeta.getRelationshipType()) {
        case ONE_TO_ONE:
            return new OneToOneEntityMapperImpl(relationshipClass,
                    propertyMappers, idIndices, propertyMeta.getField(),
                    inverseField);
        case ONE_TO_MANY:
            return new OneToManyEntityMapperImpl(relationshipClass,
                    propertyMappers, idIndices, propertyMeta.getField(),
                    inverseField);
        case MANY_TO_ONE:
            return new ManyToOneEntityMapperImpl(relationshipClass,
                    propertyMappers, idIndices, propertyMeta.getField(),
                    inverseField);
        }
        throw new IllegalStateException(propertyMeta.getRelationshipType()
                .toString());
    }

    /**
     * SQLに変換します。
     * 
     * @return SQL
     */
    protected String toSql() {
        StringBuilder sb = new StringBuilder(selectClause.getLength()
                + fromClause.getLength() + whereClause.getLength()
                + orderByClause.getLength());
        return sb.append(selectClause.toSql()).append(fromClause.toSql())
                .append(whereClause.toSql()).append(orderByClause.toSql())
                .toString();
    }

    public AutoSelect<T> where(Map<String, Object> conditions) {
        this.conditions = conditions;
        return this;
    }

    public AutoSelect<T> where(String criteria, Object... params) {
        if (criteria == null) {
            throw new NullPointerException("criteria");
        }
        this.criteria = criteria;
        if (params == null) {
            throw new NullPointerException("params");
        }
        for (Object o : params) {
            addParam(o);
        }
        return this;
    }

    public AutoSelect<T> where(Where where) {
        if (where == null) {
            throw new NullPointerException("where");
        }
        return where(where.getCriteria(), where.getParams());
    }

    /**
     * where句の条件を準備します。
     * 
     */
    protected void prepareConditions() {
        if (conditions == null || conditions.size() == 0) {
            return;
        }
        List<Object> valueList = new ArrayList<Object>();
        List<Class<?>> valueClassList = new ArrayList<Class<?>>();
        for (Map.Entry<String, Object> e : conditions.entrySet()) {
            prepareCondition(e.getKey(), e.getValue(), valueList,
                    valueClassList);
        }
        for (int i = 0; i < valueList.size(); i++) {
            addParam(valueList.get(i), valueClassList.get(i));
        }
    }

    /**
     * 条件を準備します。
     * 
     * @param name
     *            プロパティ名
     * @param value
     *            プロパティの値
     * @param valueList
     *            値のリスト
     * @param valueClassList
     *            値のクラスのリスト
     */
    protected void prepareCondition(String name, Object value,
            List<Object> valueList, List<Class<?>> valueClassList) {
        ConditionType conditionType = ConditionType.getConditionType(name);
        String pname = conditionType.removeSuffix(name);
        String[] names = splitBaseAndProperty(pname);
        String tableAlias = getTableAlias(names[0]);
        if (tableAlias == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            logger.log("ESSR0716", new Object[] { name });
            throw new BaseJoinNotFoundRuntimeException(entityName, pname,
                    names[0]);
        }
        EntityMeta baseEntityMeta = getBaseEntityMeta(pname, names[0]);
        PropertyMeta propertyMeta = getPropertyMeta(baseEntityMeta, pname,
                names[1]);
        ColumnMeta columnMeta = propertyMeta.getColumnMeta();
        if (columnMeta == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            logger.log("ESSR0716", new Object[] { name });
            throw new EntityColumnNotFoundRuntimeException(entityName,
                    propertyMeta.getName());
        }
        String columnName = columnMeta.getName();
        int size = conditionType.addCondition(tableAlias, columnName, value,
                whereClause, valueList);
        for (int i = 0; i < size; i++) {
            valueClassList.add(propertyMeta.getPropertyClass());
        }
    }

    /**
     * 条件を追加します。
     * 
     * @param condition
     *            条件
     * @param value
     *            パラメータの値
     * @param paramClass
     *            パラメータのクラス
     */
    protected void addCondition(String condition, Object value,
            Class<?> paramClass) {
        whereClause.addAndSql(condition);
        addParam(value, paramClass);
    }

    /**
     * in、not in用の条件を追加します。
     * 
     * @param condition
     *            条件
     * @param values
     *            パラメータの値の配列
     * @param paramClass
     *            パラメータのクラス
     * @param size
     *            配列のサイズ
     */
    protected void addInCondition(String condition, Object values,
            Class<?> paramClass, int size) {
        whereClause.addAndSql(condition);
        for (int i = 0; i < size; i++) {
            addParam(Array.get(values, i), paramClass);
        }
    }

    /**
     * クライテリアの準備をします。
     */
    protected void prepareCriteria() {
        if (criteria == null) {
            return;
        }
        whereClause.addAndSql("(");
        whereClause.addSql(convertCriteria(criteria));
        whereClause.addSql(convertCriteria(")"));
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        executedSql = convertLimitSql(toSql());
    }

    @Override
    protected ResultSetHandler createResultListResultSetHandler() {
        DbmsDialect dialect = jdbcManager.getDialect();
        if (limit > 0 && !dialect.supportsLimit()) {
            return new BeanListSupportLimitAutoResultSetHandler(
                    getValueTypes(), getEntityMapper(), executedSql, limit);
        }
        return new BeanListAutoResultSetHandler(getValueTypes(),
                getEntityMapper(), executedSql);
    }

    @Override
    protected ResultSetHandler createSingleResultResultSetHandler() {
        return new BeanAutoResultSetHandler(getValueTypes(), getEntityMapper(),
                executedSql);
    }

    /**
     * エンティティマッパーを返します。
     * 
     * @return エンティティマッパー
     */
    protected EntityMapper getEntityMapper() {
        return (EntityMapper) getEntityMapper(null);
    }

    public AutoSelect<T> orderBy(String orderBy) {
        if (orderBy == null) {
            throw new NullPointerException("orderBy");
        }
        this.orderBy = orderBy;
        return this;
    }

    /**
     * order by句の準備をします。
     */
    protected void prepareOrderBy() {
        if (StringUtil.isEmpty(orderBy)) {
            return;
        }
        orderByClause.addSql(convertCriteria(orderBy));
    }

    /**
     * プロパティ名で記述されたクライテリアをカラム名に変換します。
     * 
     * @param str
     *            クライテリア
     * @return カラム名で記述されたクライテリア
     */
    protected String convertCriteria(String str) {
        StringBuilder sb = new StringBuilder(20 + str.length());
        QueryTokenizer tokenizer = new QueryTokenizer(str);
        for (int type = tokenizer.nextToken(); type != QueryTokenizer.TT_EOF; type = tokenizer
                .nextToken()) {
            String token = tokenizer.getToken();
            if (type == QueryTokenizer.TT_WORD) {
                String[] names = splitBaseAndProperty(token);
                String tableAlias = getTableAlias(names[0]);
                EntityMeta entityMeta = getEntityMeta(names[0]);
                if (entityMeta == null || !entityMeta.hasPropertyMeta(names[1])) {
                    sb.append(token);
                } else {
                    PropertyMeta pm = entityMeta.getPropertyMeta(names[1]);
                    sb.append(tableAlias + "." + pm.getColumnMeta().getName());
                }
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }
}