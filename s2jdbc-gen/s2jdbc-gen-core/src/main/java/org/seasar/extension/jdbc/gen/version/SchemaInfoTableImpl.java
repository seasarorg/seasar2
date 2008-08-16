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
package org.seasar.extension.jdbc.gen.version;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SchemaInfoTable;
import org.seasar.extension.jdbc.gen.exception.NoResultRuntimeException;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SchemaInfoTable}の実装クラスです。
 * 
 * @author taedium
 */
public class SchemaInfoTableImpl implements SchemaInfoTable {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(SchemaInfoTableImpl.class);

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** カタログ名やスキーマ名を含む完全なテーブル名 */
    protected String fullTableName;

    /** カラム名 */
    protected String columnName;

    /** スキーマのバージョンを取得するSQL */
    protected String sql;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param dialect
     *            方言
     * @param fullTableName
     *            カタログ名やスキーマ名を含む完全なテーブル名
     * @param columnName
     *            カラム名
     */
    public SchemaInfoTableImpl(DataSource dataSource, GenDialect dialect,
            String fullTableName, String columnName) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (fullTableName == null) {
            throw new NullPointerException("fullTableName");
        }
        if (columnName == null) {
            throw new NullPointerException("columnName");
        }
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.fullTableName = fullTableName;
        this.columnName = columnName;
        sql = "select " + dialect.quote(columnName) + " from "
                + dialect.quote(fullTableName);
    }

    public int getVersionNo() {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            PreparedStatement ps = ConnectionUtil.prepareStatement(conn, sql);
            try {
                ResultSet rs = PreparedStatementUtil.executeQuery(ps);
                try {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    throw new NoResultRuntimeException(fullTableName);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                } finally {
                    ResultSetUtil.close(rs);
                }
            } finally {
                StatementUtil.close(ps);
            }
        } catch (SQLRuntimeException e) {
            if (dialect.isTableNotFound(e)) {
                logger.log("IS2JDBCGen0004", new Object[] { fullTableName });
                return 0;
            }
            throw e;
        } finally {
            ConnectionUtil.close(conn);
        }
    }
}
