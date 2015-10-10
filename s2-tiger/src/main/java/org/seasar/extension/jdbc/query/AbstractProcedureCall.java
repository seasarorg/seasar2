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

import java.sql.CallableStatement;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ProcedureCall;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;

/**
 * プロシージャを呼び出す抽象クラスです。
 * 
 * @author higa
 * @param <S>
 *            <code>ProcedureCall</code>のサブタイプです。
 */
public abstract class AbstractProcedureCall<S extends ProcedureCall<S>> extends
        AbstractModuleCall<S> implements ProcedureCall<S> {

    /**
     * {@link AbstractProcedureCall}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     */
    public AbstractProcedureCall(JdbcManagerImplementor jdbcManager) {
        super(jdbcManager, false);
    }

    public void execute() {
        prepare("execute");
        logSql();
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            CallableStatement cs = getCallableStatement(jdbcContext);
            boolean resultSetGettable = PreparedStatementUtil.execute(cs);
            handleNonParamResultSets(cs, resultSetGettable);
            handleOutParams(cs);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
            completed();
        }
    }

}
