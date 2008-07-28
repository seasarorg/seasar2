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

import org.seasar.extension.jdbc.gen.SchemaVersion;
import org.seasar.extension.jdbc.gen.exception.NoResultRuntimeException;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SchemaVersion}の実装クラスです。
 * 
 * @author taedium
 */
public class SchemaVersionImpl implements SchemaVersion {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(SchemaVersionImpl.class);

    /** データソース */
    protected DataSource dataSource;

    /** カタログ名やスキーマ名を含む完全なテーブル名 */
    protected String fullTableName;

    /** カラム名 */
    protected String columnName;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     * @param fullTableName
     * @param columnName
     */
    public SchemaVersionImpl(DataSource dataSource, String fullTableName,
            String columnName) {
        this.dataSource = dataSource;
        this.fullTableName = fullTableName;
        this.columnName = columnName;
    }

    public int getVersionNo() {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            String sql = "select " + columnName + " from " + fullTableName;
            PreparedStatement ps = null;
            try {
                ResultSet rs = null;
                try {
                    try {
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();
                    } catch (SQLException e) {
                        logger.log("IS2JDBCGen0004", new Object[] { sql });
                        return 0;
                    }
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
        } finally {
            ConnectionUtil.close(conn);
        }
    }
}
