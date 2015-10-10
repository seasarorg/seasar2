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

import org.seasar.extension.jdbc.AutoProcedureCall;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;

/**
 * {@link AutoProcedureCall}の実装クラスです。
 * 
 * @author koichik
 */
public class AutoProcedureCallImpl extends
        AbstractProcedureCall<AutoProcedureCall> implements AutoProcedureCall {

    /** 呼び出すストアドプロシージャの名前 */
    protected String procedureName;

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param procedureName
     *            呼び出すストアドプロシージャの名前
     * @see #AutoProcedureCallImpl(JdbcManagerImplementor, Object)
     */
    public AutoProcedureCallImpl(final JdbcManagerImplementor jdbcManager,
            final String procedureName) {
        this(jdbcManager, procedureName, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param procedureName
     *            呼び出すストアドプロシージャの名前
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
    public AutoProcedureCallImpl(final JdbcManagerImplementor jdbcManager,
            final String procedureName, final Object param) {
        super(jdbcManager);
        if (procedureName == null) {
            throw new NullPointerException("procedureName");
        }
        this.procedureName = procedureName;
        this.parameter = param;
    }

    @Override
    protected void prepare(final String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareParameter();
        prepareSql();
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        final StringBuilder buf = new StringBuilder(100).append("{call ")
                .append(procedureName);
        final int paramSize = getParamSize();
        buf.append("(");
        if (paramSize > 0) {
            for (int i = 0; i < paramSize; ++i) {
                buf.append("?, ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        executedSql = new String(buf.append("}"));
    }

}
