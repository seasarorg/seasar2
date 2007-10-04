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
import java.util.Collection;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlFileSelect;
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
public class SqlFileSelectImpl<T> extends AbstractSqlSelect<T> implements
        SqlFileSelect<T> {

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
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     */
    public SqlFileSelectImpl(JdbcManager jdbcManager, Class<T> baseClass,
            String path) {
        this(jdbcManager, baseClass, path, null);
    }

    /**
     * {@link SqlFileSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     * @param parameter
     *            パラメータ
     */
    public SqlFileSelectImpl(JdbcManager jdbcManager, Class<T> baseClass,
            String path, Object parameter) {
        super(jdbcManager, baseClass);
        if (path == null) {
            throw new NullPointerException("path");
        }
        this.path = path;
        this.parameter = parameter;
    }

    public SqlFileSelect<T> callerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return this;
    }

    public SqlFileSelect<T> callerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return this;
    }

    public SqlFileSelect<T> maxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public SqlFileSelect<T> fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public SqlFileSelect<T> queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    public SqlFileSelect<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SqlFileSelect<T> offset(int offset) {
        this.offset = offset;
        return this;
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
    @SuppressWarnings("unchecked")
    protected void prepareParameter() {
        sqlContext = new SqlContextImpl();
        if (parameter != null) {
            Class<?> clazz = parameter.getClass();
            if (ValueTypes.isSimpleType(clazz)) {
                sqlContext.addArg("$1", parameter, clazz);
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
                        sqlContext.addArg(pd.getPropertyName(), value, pd
                                .getPropertyType());
                    }
                }
            }
        }
        node.accept(sqlContext);
        bindVariableList.addAll(Arrays.asList(sqlContext.getBindVariables()));
        bindVariableClassList.addAll((Collection<? extends Class<?>>) Arrays
                .asList(sqlContext.getBindVariableTypes()));
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        executedSql = convertLimitSql(sqlContext.getSql());
    }
}