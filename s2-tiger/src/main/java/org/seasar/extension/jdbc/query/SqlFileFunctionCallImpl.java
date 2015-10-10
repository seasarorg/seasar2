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

import org.seasar.extension.jdbc.SqlFileFunctionCall;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SqlContext;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.extension.sql.context.SqlContextImpl;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;

/**
 * {@link SqlFileFunctionCall}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            ファンクションの戻り値の型。戻り値が結果セットの場合は<code>List</code>の要素の型
 */
public class SqlFileFunctionCallImpl<T> extends
        AbstractFunctionCall<T, SqlFileFunctionCall<T>> implements
        SqlFileFunctionCall<T> {

    /**
     * SQLファイルのパスです。
     */
    protected String path;

    /**
     * SQLの解析ノードです。
     */
    protected Node node;

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param resultClass
     *            戻り値のクラス
     * @param path
     *            SQLファイルのパス
     * @see #SqlFileProcedureCallImpl(JdbcManagerImplementor, String, Object)
     */
    public SqlFileFunctionCallImpl(final JdbcManagerImplementor jdbcManager,
            final Class<T> resultClass, final String path) {
        this(jdbcManager, resultClass, path, null);
    }

    /**
     * {@link SqlFileFunctionCallImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param resultClass
     *            戻り値のクラス
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
     *            プロシージャを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            継承もとのクラスのフィールドは認識しません。
     *            </p>
     */
    public SqlFileFunctionCallImpl(final JdbcManagerImplementor jdbcManager,
            final Class<T> resultClass, final String path, final Object param) {
        super(jdbcManager, resultClass);
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
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareNode();
        prepareSql();
        prepareReturnParameter();
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
        final SqlContext sqlContext = new SqlContextImpl();
        node.accept(sqlContext);
        executedSql = sqlContext.getSql();
    }

}
