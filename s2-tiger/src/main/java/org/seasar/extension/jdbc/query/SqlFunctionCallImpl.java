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

import org.seasar.extension.jdbc.SqlFunctionCall;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;

/**
 * {@link SqlUpdate}の実装クラスです。
 * 
 * @author koichik
 * @param <T>
 *            ファンクションの戻り値の型。戻り値が結果セットの場合は<code>List</code>の要素の型
 */
public class SqlFunctionCallImpl<T> extends
        AbstractFunctionCall<T, SqlFunctionCall<T>> implements
        SqlFunctionCall<T> {

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param resultClass
     *            戻り値のクラス
     * @param sql
     *            SQL
     * @see #SqlFunctionCallImpl(JdbcManagerImplementor, String, Object)
     */
    public SqlFunctionCallImpl(final JdbcManagerImplementor jdbcManager,
            final Class<T> resultClass, final String sql) {
        this(jdbcManager, resultClass, sql, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param resultClass
     *            戻り値のクラス
     * @param sql
     *            SQL
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
    public SqlFunctionCallImpl(final JdbcManagerImplementor jdbcManager,
            final Class<T> resultClass, final String sql, final Object param) {
        super(jdbcManager, resultClass);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.executedSql = sql;
        this.parameter = param;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareReturnParameter();
        prepareParameter();
    }

}