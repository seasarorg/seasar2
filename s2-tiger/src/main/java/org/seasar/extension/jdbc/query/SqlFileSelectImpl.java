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

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.parameter.LobParameter;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.extension.jdbc.parameter.TemporalParameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SqlContext;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.extension.sql.context.SqlContextImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.IntegerConversionUtil;

/**
 * {@link SqlFileSelect}の実装クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * 
 */
public class SqlFileSelectImpl<T> extends
        AbstractSqlSelect<T, SqlFileSelect<T>> implements SqlFileSelect<T> {

    /**
     * SQLファイルのパスです。
     */
    protected String path;

    /**
     * パラメータです。
     */
    protected Object parameter;

    /**
     * SQLの解析ノードです。
     */
    protected Node node;

    /**
     * SQLコンテキストです。
     */
    protected SqlContext sqlContext;

    /**
     * {@link SqlFileSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     */
    public SqlFileSelectImpl(JdbcManagerImplementor jdbcManager,
            Class<T> baseClass, String path) {
        this(jdbcManager, baseClass, path, null);
    }

    /**
     * {@link SqlFileSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     * @param parameter
     *            パラメータ
     */
    public SqlFileSelectImpl(JdbcManagerImplementor jdbcManager,
            Class<T> baseClass, String path, Object parameter) {
        super(jdbcManager, baseClass);
        if (path == null) {
            throw new NullPointerException("path");
        }
        this.path = path;
        this.parameter = parameter;
    }

    /**
     * SQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス
     */
    public String getPath() {
        return path;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareNode();
        prepareParameter();
        prepareSql();
    }

    /**
     * SQLの解析ノードを準備します。
     * 
     * @throws ResourceNotFoundRuntimeException
     *             パスに対応するリソースが見つからない場合。
     * 
     */
    protected void prepareNode() throws ResourceNotFoundRuntimeException {
        node = NodeCache.getNode(path, jdbcManager.getDialect().getName());
        if (node == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new ResourceNotFoundRuntimeException(path);
        }
    }

    /**
     * パラメータを準備します。
     */
    protected void prepareParameter() {
        sqlContext = new SqlContextImpl();
        if (parameter != null) {
            Class<?> clazz = parameter.getClass();
            if (ValueTypes.isSimpleType(clazz)
                    || TemporalParameter.class == clazz
                    || LobParameter.class == clazz) {
                sqlContext.addArg("$1", parameter, clazz);
            } else if (parameter instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> paramMap = (Map<Object, Object>) parameter;
                Set<Entry<Object, Object>> entrySet = paramMap.entrySet();
                for (Map.Entry<Object, Object> entry : entrySet) {
                    Object value = entry.getValue();
                    Object key = entry.getKey();
                    if (key == null || !(key instanceof CharSequence)) {
                        continue;
                    }
                    String name = ((CharSequence) key).toString();
                    if (name.equals("limit")) {
                        limit = IntegerConversionUtil.toPrimitiveInt(value);
                    } else if (name.equals("offset")) {
                        offset = IntegerConversionUtil.toPrimitiveInt(value);
                    } else {
                        sqlContext.addArg(name, value,
                                (value == null ? String.class : value
                                        .getClass()));
                    }
                }
            } else {
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
                for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                    PropertyDesc pd = beanDesc.getPropertyDesc(i);
                    if (!pd.isReadable()) {
                        continue;
                    }
                    Object value = pd.getValue(parameter);
                    if (pd.getPropertyName().equals("limit")) {
                        limit = IntegerConversionUtil.toPrimitiveInt(value);
                    } else if (pd.getPropertyName().equals("offset")) {
                        offset = IntegerConversionUtil.toPrimitiveInt(value);
                    } else {
                        value = Parameter.wrapIfNecessary(pd, value);
                        sqlContext.addArg(pd.getPropertyName(), value, pd
                                .getPropertyType());
                    }
                }
            }
        }
        node.accept(sqlContext);
        Object[] vars = sqlContext.getBindVariables();
        Class<?>[] types = sqlContext.getBindVariableTypes();
        int size = vars.length;
        for (int i = 0; i < size; i++) {
            addParam(vars[i], types[i]);
        }
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        final String sql = sqlContext.getSql();
        if (count) {
            executedSql = convertGetCountSql(sql);
        } else {
            executedSql = convertLimitSql(sql);
        }
    }

}
