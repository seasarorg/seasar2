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
package org.seasar.extension.jdbc.gen.sql;

import java.sql.SQLException;
import java.sql.Statement;

import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlExecutor;
import org.seasar.extension.jdbc.gen.exception.SqlFailedException;
import org.seasar.framework.log.Logger;

/**
 * @author taedium
 * 
 */
public class SqlExecutorImpl implements SqlExecutor {

    protected Logger logger = Logger.getLogger(SqlExecutorImpl.class);

    protected boolean haltOnError;

    public SqlExecutorImpl(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    public void execute(SqlExecutionContext context) {
        logger.log("DS2JDBCGen0007", new Object[] { context.getSql() });
        Statement statement = context.getStatement();
        try {
            statement.execute(context.getSql());
        } catch (SQLException e) {
            SqlFailedException failedException = new SqlFailedException(e,
                    context.getSqlFile().getName(), context.getSql());
            context.addException(failedException);
            if (haltOnError) {
                throw failedException;
            }
        }
    }
}
