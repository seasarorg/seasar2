/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.sql;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.framework.log.Logger;

/**
 * {@link SqlUnitExecutor}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlUnitExecutorImpl implements SqlUnitExecutor {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(SqlUnitExecutorImpl.class);

    /** データソース */
    protected DataSource dataSource;

    /** エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false} */
    protected boolean haltOnError;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlUnitExecutorImpl(DataSource dataSource, boolean haltOnError) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dataSource = dataSource;
        this.haltOnError = haltOnError;
    }

    public void execute(Callback callback) {
        SqlExecutionContext context = new SqlExecutionContextImpl(dataSource,
                haltOnError);
        try {
            try {
                callback.execute(context);
            } finally {
                for (Exception e : context.getExceptionList()) {
                    logger.error(e.getMessage());
                }
            }
            if (!context.getExceptionList().isEmpty()) {
                throw context.getExceptionList().get(0);
            }
        } finally {
            context.destroy();
        }
    }
}
