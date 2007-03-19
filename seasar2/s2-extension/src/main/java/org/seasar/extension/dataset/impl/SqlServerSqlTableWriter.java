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
package org.seasar.extension.dataset.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * @author taedium
 * 
 */

public class SqlServerSqlTableWriter extends SqlTableWriter {

    private static final Logger LOGGER = Logger.getLogger(SqlTableWriter.class);

    private final Map hasIdentityColumn = new HashMap();

    public SqlServerSqlTableWriter(final DataSource dataSource) {
        super(dataSource);
    }

    protected void doWrite(final DataTable dataTable) {
        turnIdentityInsert(dataTable, true);
        super.doWrite(dataTable);
        turnIdentityInsert(dataTable, false);
    }

    private void turnIdentityInsert(final DataTable dataTable,
            final boolean identityInsert) {
        final Connection connection = DataSourceUtil
                .getConnection(getDataSource());
        try {
            if (hasIdentityColumn(connection, dataTable)) {
                final String sql = "SET IDENTITY_INSERT "
                        + dataTable.getTableName() + " "
                        + (identityInsert ? "ON" : "OFF");
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(sql);
                }
                execute(connection, sql);
            }
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    private boolean hasIdentityColumn(final Connection connection,
            final DataTable dataTable) {
        final String tableName = dataTable.getTableName();
        if (!hasIdentityColumn.containsKey(tableName)) {
            final String sql = "SELECT IDENT_CURRENT ('"
                    + dataTable.getTableName() + "') AS IDENT_CURRENT";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sql);
            }
            Object identCurrent = queryToOneColumn(connection, sql);
            hasIdentityColumn.put(tableName, Boolean
                    .valueOf(identCurrent != null));
        }
        return ((Boolean) hasIdentityColumn.get(tableName)).booleanValue();
    }

    private boolean execute(final Connection connection, final String sql) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            return statement.execute(sql);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        } finally {
            StatementUtil.close(statement);
        }
    }

    private Object queryToOneColumn(final Connection connection,
            final String sql) {
        final PreparedStatement statement = ConnectionUtil.prepareStatement(
                connection, sql);
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
            Object column = null;
            while (resultSet.next()) {
                column = resultSet.getObject(1);
            }
            return column;
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        } finally {
            ResultSetUtil.close(resultSet);
            StatementUtil.close(statement);
        }
    }
}