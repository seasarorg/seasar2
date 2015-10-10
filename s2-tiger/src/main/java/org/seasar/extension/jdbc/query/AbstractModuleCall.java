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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ModuleCall;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.exception.FieldNotGenericsRuntimeException;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.parameter.LobParameter;
import org.seasar.extension.jdbc.parameter.TemporalParameter;
import org.seasar.extension.jdbc.query.AbstractModuleCall.ParamDesc.ParameterType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 永続格納モジュール(ストアドプロシージャまたはストアドファンクション)を呼び出す抽象クラスです。
 * 
 * @author koichik
 * @param <S>
 *            <code>ModuleCall</code>のサブタイプです。
 */
public abstract class AbstractModuleCall<S extends ModuleCall<S>> extends
        AbstractQuery<S> implements ModuleCall<S> {

    /** {@link DisposableUtil} に登録済みなら<code>true</code> */
    protected static boolean initialized;

    /** プロシージャまたはファンクションの引数として使用されるDTOのフィールドを表す{@link ParamDesc}のキャッシュ */
    protected static final ConcurrentMap<Class<?>, ParamDesc[]> paramDescCache = CollectionsUtil
            .newConcurrentHashMap();

    /** 最大行数 */
    protected int maxRows;

    /** フェッチ数 */
    protected int fetchSize;

    /** パラメータ */
    protected Object parameter;

    /** MS SQLServerのような結果セットを<code>OUT</code>パラメータにマッピングしない場合のパラメータのリスト */
    protected List<Param> nonParamList = new ArrayList<Param>();

    /** ファンクション呼び出しの場合は<code>true</code> */
    protected boolean functionCall;

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param functionCall
     *            ファンクション呼び出しの場合は<code>true</code>
     */
    public AbstractModuleCall(final JdbcManagerImplementor jdbcManager,
            final boolean functionCall) {
        super(jdbcManager);
        this.functionCall = functionCall;
    }

    @SuppressWarnings("unchecked")
    public S maxRows(final int maxRows) {
        this.maxRows = maxRows;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S fetchSize(final int fetchSize) {
        this.fetchSize = fetchSize;
        return (S) this;
    }

    /**
     * パラメータの準備をします。
     */
    protected void prepareParameter() {
        if (parameter == null) {
            return;
        }
        final Class<?> paramClass = parameter.getClass();
        if (ValueTypes.isSimpleType(paramClass)
                || TemporalParameter.class == paramClass
                || LobParameter.class == paramClass) {
            addParam(parameter, paramClass);
            return;
        }

        final boolean needsParameterForResultSet = jdbcManager.getDialect()
                .needsParameterForResultSet();
        for (final ParamDesc paramDesc : getParamDescs(paramClass)) {
            final Class<?> clazz = paramDesc.paramClass;
            final ValueType valueType = paramDesc.valueType;
            switch (paramDesc.paramType) {
            case RESULT_SET:
                if (needsParameterForResultSet) {
                    addParam(paramDesc.field, clazz, valueType, ParamType.OUT);
                } else {
                    addNonParam(paramDesc.field);
                }
                break;
            case IN:
                addParam(FieldUtil.get(paramDesc.field, parameter), clazz,
                        valueType);
                break;
            case OUT:
                addParam(paramDesc.field, clazz, valueType, ParamType.OUT);
                break;
            case IN_OUT:
                addParam(paramDesc.field, clazz, valueType, ParamType.IN_OUT);
                break;
            }
        }
    }

    /**
     * パラメータを追加します。
     * 
     * @param field
     *            パラメータとなるフィールド
     * @param paramClass
     *            パラメータのクラス
     * @param valueType
     *            値タイプ
     * @param paramType
     *            パラメータのタイプ
     */
    protected void addParam(final Field field, final Class<?> paramClass,
            final ValueType valueType, final ParamType paramType) {
        final Object value;
        if (field != null && paramType != ParamType.OUT) {
            value = FieldUtil.get(field, parameter);
        } else {
            value = null;
        }
        final Param p = addParam(value, paramClass, valueType);
        p.paramType = paramType;
        p.field = field;
    }

    /**
     * 呼び出し可能なステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 呼び出し可能なステートメント
     */
    protected CallableStatement getCallableStatement(
            final JdbcContext jdbcContext) {
        final CallableStatement ps = jdbcContext
                .getCallableStatement(executedSql);
        setupCallableStatement(ps);
        return ps;
    }

    /**
     * 呼び出し可能なステートメントをセットアップします。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void setupCallableStatement(final CallableStatement cs) {
        if (maxRows > 0) {
            StatementUtil.setMaxRows(cs, maxRows);
        }
        if (fetchSize > 0) {
            StatementUtil.setFetchSize(cs, fetchSize);
        }
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(cs, queryTimeout);
        }
        prepareInParams(cs);
        prepareOutParams(cs);
    }

    /**
     * <code>OUT</code>パラメータを準備します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void prepareOutParams(final CallableStatement cs) {
        final int size = getParamSize();
        try {
            for (int i = 0; i < size; i++) {
                final Param param = getParam(i);
                if (param.paramType == ParamType.IN) {
                    continue;
                }
                param.valueType.registerOutParameter(cs, i + 1);
            }
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * <code>OUT</code>パラメータにマッピングされない結果セットを処理します。
     * <p>
     * このメソッドは{@link #handleOutParams(CallableStatement)}よりも前に呼び出さなくてはなりません。
     * </p>
     * 
     * @param cs
     *            呼び出し可能なステートメント
     * @param resultSetGettable
     *            呼び出し可能なステートメントから結果セットを取得可能な場合{@code true}
     */
    protected void handleNonParamResultSets(final CallableStatement cs,
            final boolean resultSetGettable) {
        try {
            if (!resultSetGettable) {
                cs.getMoreResults();
            }
            for (int i = 0; i < nonParamList.size(); i++) {
                final ResultSet rs = getResultSet(cs);
                if (rs == null) {
                    break;
                }
                final Param param = nonParamList.get(i);
                final Object value = handleResultSet(param.field, cs
                        .getResultSet());
                FieldUtil.set(param.field, parameter, value);
                cs.getMoreResults();
            }
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * <code>OUT/INOUT</code>パラメータとは別に戻される結果セットを返します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     * @return 結果セット
     */
    protected ResultSet getResultSet(final CallableStatement cs) {
        try {
            for (;;) {
                final ResultSet rs = cs.getResultSet();
                if (rs != null) {
                    return rs;
                }
                if (cs.getUpdateCount() == -1) {
                    return null;
                }
                cs.getMoreResults();
            }
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * <code>OUT</code>パラメータを処理します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void handleOutParams(final CallableStatement cs) {
        try {
            final int start = functionCall ? 1 : 0;
            for (int i = start; i < getParamSize(); i++) {
                final Param param = getParam(i);
                if (param.paramType == ParamType.IN) {
                    continue;
                }
                Object value = param.valueType.getValue(cs, i + 1);
                if (value instanceof ResultSet) {
                    value = handleResultSet(param.field, (ResultSet) value);
                }
                FieldUtil.set(param.field, parameter, value);
            }
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * 結果セットを処理します。
     * 
     * @param field
     *            フィールド
     * @param rs
     *            結果セット
     * @return 処理した結果
     */
    protected Object handleResultSet(final Field field, final ResultSet rs) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return handleSingleResult(field.getType(), rs);
        }
        final Class<?> elementClass = ReflectionUtil
                .getElementTypeOfListFromFieldType(field);
        if (elementClass == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new FieldNotGenericsRuntimeException(field);
        }
        return handleResultList(elementClass, rs);
    }

    /**
     * 1行だけの結果セットを処理します。
     * 
     * @param resultClass
     *            結果のクラス
     * @param rs
     *            結果セット
     * @return 処理した結果
     */
    protected Object handleSingleResult(final Class<?> resultClass,
            final ResultSet rs) {
        final ResultSetHandler handler;
        if (ValueTypes.isSimpleType(resultClass)) {
            handler = new ObjectResultSetHandler(ValueTypes
                    .getValueType(resultClass), null);
        } else {
            handler = new BeanResultSetHandler(resultClass, jdbcManager
                    .getDialect(), jdbcManager.getPersistenceConvention(), null);
        }
        return handleResultSet(handler, rs);
    }

    /**
     * 複数の行を持つ結果セットを処理します。
     * 
     * @param elementClass
     *            結果となるリストの要素型
     * @param rs
     *            結果セット
     * @return 処理した結果のリスト
     */
    protected Object handleResultList(final Class<?> elementClass,
            final ResultSet rs) {
        final ResultSetHandler handler;
        if (ValueTypes.isSimpleType(elementClass)) {
            handler = new ObjectListResultSetHandler(ValueTypes
                    .getValueType(elementClass));
        } else {
            handler = new BeanListResultSetHandler(elementClass, jdbcManager
                    .getDialect(), jdbcManager.getPersistenceConvention(), null);
        }
        return handleResultSet(handler, rs);
    }

    /**
     * 直接パラメータでは指定しないパラメータの数を返します。
     * 
     * @return 直接パラメータでは指定しないパラメータの数
     */
    protected int getNonParamSize() {
        return nonParamList.size();
    }

    /**
     * 直接パラメータでは指定しないパラメータを返します。
     * 
     * @param index
     *            インデックス
     * @return 直接パラメータでは指定しないパラメータ
     */
    protected Param getNonParam(final int index) {
        return nonParamList.get(index);
    }

    /**
     * 直接パラメータでは指定しないパラメータを追加します。
     * 
     * @param field
     *            フィールド
     * @return 追加されたパラメータ
     */
    protected Param addNonParam(final Field field) {
        final Param param = new Param();
        param.field = field;
        param.paramType = ParamType.OUT;
        nonParamList.add(param);
        return param;
    }

    /**
     * フェッチ数を返します。
     * 
     * @return フェッチ数
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * 最大行数を返します。
     * 
     * @return 最大行数
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * パラメータを返します。
     * 
     * @return パラメータ
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * 初期化します。
     */
    protected synchronized void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                paramDescCache.clear();
                initialized = false;
            }

        });
        initialized = true;
    }

    /**
     * パラメータとして渡されたDTOのフィールドを表す{@link ParamDesc}の配列を返します。
     * 
     * @param dtoClass
     *            パラメータとして渡されたDTOのクラス
     * @return パラメータとして渡されたDTOのフィールドを表す{@link ParamDesc}の配列
     */
    protected ParamDesc[] getParamDescs(final Class<?> dtoClass) {
        initialize();
        final ParamDesc[] paramDesc = paramDescCache.get(dtoClass);
        if (paramDesc != null) {
            return paramDesc;
        }
        return createParamDesc(dtoClass);
    }

    /**
     * パラメータとして渡されたDTOのフィールドを表す{@link ParamDesc}の配列を作成して返します。
     * 
     * @param dtoClass
     *            パラメータとして渡されたDTOのクラス
     * @return パラメータとして渡されたDTOのフィールドを表す{@link ParamDesc}の配列
     */
    protected ParamDesc[] createParamDesc(final Class<?> dtoClass) {
        final Field[] fields = ClassUtil.getDeclaredFields(dtoClass);
        final List<ParamDesc> list = CollectionsUtil
                .newArrayList(fields.length);
        for (int i = 0; i < fields.length; ++i) {
            final Field field = fields[i];
            if (!ModifierUtil.isInstanceField(field)) {
                continue;
            }
            field.setAccessible(true);
            final ParamDesc paramDesc = new ParamDesc();
            paramDesc.field = field;
            paramDesc.name = field.getName();
            paramDesc.paramClass = field.getType();
            final boolean lob = field.getAnnotation(Lob.class) != null;
            final Temporal temporal = field.getAnnotation(Temporal.class);
            final TemporalType temporalType = temporal != null ? temporal
                    .value() : null;
            paramDesc.valueType = getValueType(paramDesc.paramClass, lob,
                    temporalType);
            if (field
                    .getAnnotation(org.seasar.extension.jdbc.annotation.ResultSet.class) != null) {
                paramDesc.paramType = ParameterType.RESULT_SET;
            } else if (field.getAnnotation(Out.class) != null) {
                paramDesc.paramType = ParameterType.OUT;
            } else if (field.getAnnotation(InOut.class) != null) {
                paramDesc.paramType = ParameterType.IN_OUT;
            } else {
                paramDesc.paramType = ParameterType.IN;
            }
            list.add(paramDesc);
        }
        final ParamDesc[] result = list.toArray(new ParamDesc[list.size()]);
        return CollectionsUtil.putIfAbsent(paramDescCache, dtoClass, result);
    }

    /**
     * パラメータとして渡されたDTOのフィールドを表すクラスです。
     * 
     * @author koichik
     */
    public static class ParamDesc {

        /**
         * パラメータのタイプを表す列挙です。
         */
        public enum ParameterType {
            /** <code>IN</code>パラメータを表します。 */
            IN,

            /** <code>INOUT</code>パラメータを表します。 */
            IN_OUT,

            /** <code>OUT</code>パラメータを表します。 */
            OUT,

            /** ストアドのパラメータとは別に戻される結果セットを表します。 */
            RESULT_SET
        }

        /** パラメータを表すフィールド */
        public Field field;

        /** パラメータ名 */
        public String name;

        /** パラメータのクラス */
        public Class<?> paramClass;

        /** パラメータのタイプ */
        public ParameterType paramType;

        /** パラメータのS2JDBCでの型 */
        public ValueType valueType;

    }

}