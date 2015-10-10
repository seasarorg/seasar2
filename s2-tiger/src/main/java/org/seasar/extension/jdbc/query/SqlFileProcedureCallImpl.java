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

import org.seasar.extension.jdbc.SqlFileProcedureCall;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SqlContext;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.extension.sql.context.SqlContextImpl;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;

/**
 * {@link SqlFileProcedureCall}の実装クラスです。
 * 
 * @author taedium
 * 
 */
public class SqlFileProcedureCallImpl extends
        AbstractProcedureCall<SqlFileProcedureCall> implements
        SqlFileProcedureCall {

    /**
     * SQLファイルのパスです。
     */
    protected String path;

    /**
     * SQLの解析ノードです。
     */
    protected Node node;

    /**
     * {@link SqlFileProcedureCallImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param path
     *            SQLファイルのパス
     * @see #SqlFileProcedureCallImpl(JdbcManagerImplementor, String, Object)
     */
    public SqlFileProcedureCallImpl(JdbcManagerImplementor jdbcManager,
            String path) {
        this(jdbcManager, path, null);
    }

    /**
     * {@link SqlFileProcedureCallImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param path
     *            SQLファイルのパス
     * @param param
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つしかない場合は、数値や文字列などを直接指定します。 それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            プロシージャを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。
     *            フィールド名が_OUTで終わっている場合<code>OUT</code>パラメータになります。
     *            フィールド名が_IN_OUTで終わっている場合<code>IN OUT</code>パラメータになります。
     *            フィールド名が_INで終わっている場合<code>IN</code>パラメータになります。
     *            フィールド名が_OUT、_IN_OUT、_INで終わっていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List<レコードの型></code>にします。
     *            </p>
     *            <p>
     *            継承もとのクラスのフィールドは認識しません。
     *            </p>
     */
    public SqlFileProcedureCallImpl(JdbcManagerImplementor jdbcManager,
            String path, Object param) {
        super(jdbcManager);
        if (path == null) {
            throw new NullPointerException("path");
        }
        this.path = path;
        this.parameter = param;
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
        prepareSql();
        prepareParameter();
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
     * SQLを準備します。
     */
    protected void prepareSql() {
        SqlContext sqlContext = new SqlContextImpl();
        node.accept(sqlContext);
        executedSql = sqlContext.getSql();
    }
}
