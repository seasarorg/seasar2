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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.exception.SqlFailedException;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.log.Logger;

/**
 * @author taedium
 * 
 */
public class SqlExecutionContextImpl implements SqlExecutionContext {

    private static final Logger logger = Logger
            .getLogger(SqlExecutionContextImpl.class);

    protected List<SqlFailedException> exceptionList = new ArrayList<SqlFailedException>();

    protected Connection connection;

    protected Statement statement;

    protected String sql;

    protected File sqlFile;

    /**
     * @param connection
     * @param sqlFailList
     */
    public SqlExecutionContextImpl(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        this.connection = connection;
    }

    /**
     * @return Returns the sql.
     */
    public String getSql() {
        return sql;
    }

    /**
     * @param sql
     *            The sql to set.
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * @return Returns the sqlFile.
     */
    public File getSqlFile() {
        return sqlFile;
    }

    /**
     * @param sqlFile
     *            The sqlFile to set.
     */
    public void setSqlFile(File sqlFile) {
        this.sqlFile = sqlFile;
    }

    /**
     * @return Returns the statement.
     */
    public Statement getStatement() {
        if (statement != null) {
            return statement;
        }
        statement = ConnectionUtil.createStatement(connection);
        return statement;
    }

    /**
     * @return Returns the exceptionList.
     */
    public List<SqlFailedException> getExceptionList() {
        return Collections.unmodifiableList(exceptionList);
    }

    public void addException(SqlFailedException exception) {
        exceptionList.add(exception);
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (SQLException ignore) {
        }
        statement = null;
    }

    public void destroy() {
        if (connection == null) {
            return;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(e);
            }
            statement = null;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(e);
        }
        connection = null;
        exceptionList.clear();
    }

}
