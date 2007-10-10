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
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.SqlProcedureCall;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.ModifierUtil;

/**
 * {@link SqlUpdate}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlProcedureCallImpl extends
        AbstractProcedureCall<SqlProcedureCall> implements SqlProcedureCall {

    /**
     * {@link SqlProcedureCallImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param sql
     *            SQL
     * @see #SqlProcedureCallImpl(JdbcManager, String, Object)
     */
    public SqlProcedureCallImpl(JdbcManager jdbcManager, String sql) {
        this(jdbcManager, sql, null);
    }

    /**
     * {@link SqlProcedureCallImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
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
    public SqlProcedureCallImpl(JdbcManager jdbcManager, String sql,
            Object param) {
        super(jdbcManager);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.executedSql = sql;
        this.parameter = param;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareParameter();
    }

    /**
     * パラメータの準備をします。
     */
    protected void prepareParameter() {
        if (parameter == null) {
            return;
        }
        boolean needsParameterForResultSet = jdbcManager.getDialect()
                .needsParameterForResultSet();
        Class<?> paramClass = parameter.getClass();
        if (ValueTypes.isSimpleType(paramClass)) {
            addParam(parameter, paramClass);
        } else {
            Field[] fields = paramClass.getDeclaredFields();
            for (Field f : fields) {
                if (!ModifierUtil.isInstanceField(f)) {
                    continue;
                }
                f.setAccessible(true);
                String name = f.getName();
                Class<?> clazz = f.getType();
                if (name.endsWith(ParamType.IN_OUT.getSuffix())) {
                    Param p = addParam(FieldUtil.get(f, parameter), clazz);
                    p.paramType = ParamType.IN_OUT;
                    p.field = f;
                } else if (name.endsWith(ParamType.OUT.getSuffix())) {
                    if (List.class.isAssignableFrom(clazz)
                            && !needsParameterForResultSet) {
                        addNonParam(f);
                    } else {
                        Param p = addParam(null, clazz);
                        p.paramType = ParamType.OUT;
                        p.field = f;
                    }
                } else {
                    addParam(FieldUtil.get(f, parameter), clazz);
                }
            }
        }
    }
}