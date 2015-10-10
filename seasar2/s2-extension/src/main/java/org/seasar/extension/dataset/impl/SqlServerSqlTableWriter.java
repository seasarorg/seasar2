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
package org.seasar.extension.dataset.impl;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.ObjectResultSetHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StatementUtil;

/**
 * SqlServer用の {@link SqlTableWriter}です。
 * 
 * @author taedium
 * 
 */

public class SqlServerSqlTableWriter extends SqlTableWriter {

    private static final Logger logger = Logger.getLogger(SqlTableWriter.class);

    /**
     * {@link SqlServerSqlTableWriter}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public SqlServerSqlTableWriter(final DataSource dataSource) {
        super(dataSource);
    }

    protected void doWrite(final DataTable dataTable) {
        boolean hasIdentity = hasIdentityColumn(dataTable);
        if (hasIdentity) {
            turnOnIdentityInsert(dataTable);
        }
        super.doWrite(dataTable);
        if (hasIdentity) {
            turnOffIdentityInsert(dataTable);
        }
    }

    private void turnOnIdentityInsert(final DataTable dataTable) {
        setIdentityInsert(dataTable, "ON");
    }

    private void turnOffIdentityInsert(final DataTable dataTable) {
        setIdentityInsert(dataTable, "OFF");
    }

    private void setIdentityInsert(final DataTable dataTable,
            final String command) {
        final String sql = "SET IDENTITY_INSERT " + dataTable.getTableName()
                + " " + command;
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        final Connection connection = DataSourceUtil
                .getConnection(getDataSource());
        try {
            final Statement statement = ConnectionUtil
                    .createStatement(connection);
            try {
                StatementUtil.execute(statement, sql);
            } finally {
                StatementUtil.close(statement);
            }
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    private boolean hasIdentityColumn(final DataTable dataTable) {
        final String sql = "SELECT IDENT_CURRENT ('" + dataTable.getTableName()
                + "') AS IDENT_CURRENT";
        final BasicSelectHandler handler = new BasicSelectHandler(
                getDataSource(), sql, new ObjectResultSetHandler());
        return handler.execute(null) != null;
    }

}