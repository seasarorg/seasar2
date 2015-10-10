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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlFileBatchUpdate;
import org.seasar.extension.jdbc.exception.SEntityExistsException;
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
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SqlFileBatchUpdate}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            パラメータの型です。
 */
public class SqlFileBatchUpdateImpl<T> extends
        AbstractQuery<SqlFileBatchUpdate<T>> implements SqlFileBatchUpdate<T> {

    /**
     * SQLファイルのパスです。
     */
    protected String path;

    /**
     * パラメータの配列のリストです。
     */
    protected List<T> parameterList;

    /** バッチサイズ */
    protected int batchSize;

    /**
     * SQLの解析ノードです。
     */
    protected Node node;

    /**
     * SQLコンテキストです。
     */
    protected SqlContext sqlContext;

    /**
     * {@link SqlFileBatchUpdate}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param path
     *            SQLファイルのパス
     * @param parameterList
     *            パラメータのリスト
     */
    public SqlFileBatchUpdateImpl(JdbcManagerImplementor jdbcManager,
            String path, List<T> parameterList) {
        super(jdbcManager);
        if (path == null) {
            throw new NullPointerException("path");
        }
        this.path = path;
        this.parameterList = parameterList;
    }

    public SqlFileBatchUpdate<T> batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public int[] execute() {
        prepare("execute");
        try {
            return executeInternal();
        } catch (final RuntimeException e) {
            if (getJdbcManager().getDialect().isUniqueConstraintViolation(e)) {
                throw new SEntityExistsException(executedSql, e);
            }
            throw e;
        } finally {
            completed();
        }
    }

    /**
     * データベースの更新を実行します。
     * 
     * @return 更新した行数
     */
    protected int[] executeInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            PreparedStatement ps = null;
            final int batchSize = this.batchSize > 0 ? this.batchSize
                    : jdbcManager.getDialect().getDefaultBatchSize();
            final int size = parameterList.size();
            final int[] updateRows = new int[size];
            int pos = 0;
            for (int i = 0; i < size; ++i) {
                final T parameter = parameterList.get(i);
                prepareParameter(parameter);
                if (ps == null) {
                    prepareSql();
                    ps = getPreparedStatement(jdbcContext);
                }
                logSql();
                prepareInParams(ps);
                PreparedStatementUtil.addBatch(ps);
                resetParams();
                if (i == size - 1
                        || (batchSize > 0 && (i + 1) % batchSize == 0)) {
                    final int[] rows = PreparedStatementUtil.executeBatch(ps);
                    System.arraycopy(rows, 0, updateRows, pos, rows.length);
                    pos = i + 1;
                }
            }
            return updateRows;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * SQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス
     */
    public String getPath() {
        return path;
    }

    /**
     * 準備されたステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 準備されたステートメント
     */
    protected PreparedStatement getPreparedStatement(JdbcContext jdbcContext) {
        PreparedStatement ps = jdbcContext.getPreparedStatement(executedSql);
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
        return ps;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareNode();
    }

    /**
     * SQLの解析ノードを準備します。
     * 
     * @throws ResourceNotFoundRuntimeException
     *             パスに対応するリソースが見つからない場合。
     * 
     */
    protected void prepareNode() throws ResourceNotFoundRuntimeException {
        node = NodeCache.getNode(path, jdbcManager.getDialect().getName(),
                jdbcManager.isAllowVariableSqlForBatchUpdate());
        if (node == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new ResourceNotFoundRuntimeException(path);
        }
    }

    /**
     * パラメータを準備します。
     * 
     * @param parameter
     *            パラメータ
     */
    protected void prepareParameter(T parameter) {
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
                    sqlContext.addArg(name, value,
                            (value == null ? String.class : value.getClass()));
                }
            } else {
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
                for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                    PropertyDesc pd = beanDesc.getPropertyDesc(i);
                    if (!pd.isReadable()) {
                        continue;
                    }
                    Object value = Parameter.wrapIfNecessary(pd, pd
                            .getValue(parameter));
                    sqlContext.addArg(pd.getPropertyName(), value, pd
                            .getPropertyType());
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
        executedSql = sqlContext.getSql();
    }

}
