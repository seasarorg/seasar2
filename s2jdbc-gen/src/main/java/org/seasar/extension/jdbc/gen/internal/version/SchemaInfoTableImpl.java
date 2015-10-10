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
package org.seasar.extension.jdbc.gen.internal.version;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.NoResultRuntimeException;
import org.seasar.extension.jdbc.gen.version.SchemaInfoTable;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.exception.SRuntimeException;
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

    /** バージョンを取得するSQL */
    protected String selectSql;

    /** テーブルを作成するSQL */
    protected String createSql;

    /** 全データを削除するSQL */
    protected String deleteSql;

    /** バージョンを追加するSQL */
    protected String insertSql;

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
        selectSql = createSelectSql();
        insertSql = createInsertSql();
        deleteSql = createDeleteSql();
        createSql = createCreateSql();
    }

    /**
     * バージョンを取得するSQLを作成します。
     * 
     * @return バージョンを取得するSQL
     */
    protected String createSelectSql() {
        return "select " + columnName + " from " + fullTableName;
    }

    /**
     * バージョンを追加するSQLを作成します。
     * 
     * @return バージョンを追加するSQL
     */
    protected String createInsertSql() {
        StringBuilder buf = new StringBuilder();
        buf.append("insert into ");
        buf.append(fullTableName);
        buf.append(" (");
        buf.append(columnName);
        buf.append(") values (?)");
        return buf.toString();
    }

    /**
     * 全データを削除するSQL
     * 
     * @return 全データを削除するSQLを作成します。
     */
    protected String createDeleteSql() {
        return "delete from " + fullTableName;
    }

    /**
     * テーブルを作成するSQLを作成します。
     * 
     * @return テーブルを作成するSQL
     */
    protected String createCreateSql() {
        StringBuilder buf = new StringBuilder();
        buf.append("create table ");
        buf.append(fullTableName);
        buf.append(" (");
        buf.append(columnName);
        buf.append(" ");
        buf.append(dialect.getSqlType(Types.INTEGER).getDataType(0, 10, 0,
                false));
        buf.append(")");
        return buf.toString();
    }

    public int getVersionNo() {
        if (!exists()) {
            return 0;
        }
        return getVersionNoInternal();
    }

    /**
     * 内部的にバージョン番号を返します。
     * 
     * @return バージョン番号
     */
    protected int getVersionNoInternal() {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            logger.debug(selectSql);
            PreparedStatement ps = ConnectionUtil.prepareStatement(conn,
                    selectSql);
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
        } finally {
            ConnectionUtil.close(conn);
        }
    }

    public void setVersionNo(int versionNo) {
        if (!exists()) {
            create();
        }
        setVersionNoInternal(versionNo);
    }

    /**
     * 内部的にバージョン番号を設定します。
     * 
     * @param versionNo
     *            バージョン番号
     */
    protected void setVersionNoInternal(int versionNo) {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            logger.debug(deleteSql);
            PreparedStatement delete = ConnectionUtil.prepareStatement(conn,
                    deleteSql);
            try {
                PreparedStatementUtil.executeUpdate(delete);
            } finally {
                StatementUtil.close(delete);
            }
            logger.debug(insertSql.replace("?", Integer.toString(versionNo)));
            PreparedStatement insert = ConnectionUtil.prepareStatement(conn,
                    insertSql);
            try {
                insert.setInt(1, versionNo);
                PreparedStatementUtil.executeUpdate(insert);
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            } finally {
                StatementUtil.close(insert);
            }
        } finally {
            ConnectionUtil.close(conn);
        }
    }

    /**
     * テーブルが存在する場合{@code true}を返します。
     * 
     * @return テーブルが存在する場合{@code true}
     */
    protected boolean exists() {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            logger.debug(selectSql);
            PreparedStatement ps = conn.prepareStatement(selectSql);
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    return true;
                } finally {
                    ResultSetUtil.close(rs);
                }
            } finally {
                StatementUtil.close(ps);
            }
        } catch (Exception e) {
            if (dialect.isTableNotFound(e)) {
                logger.log("IS2JDBCGen0004", new Object[] { fullTableName });
                return false;
            }
            throw new SRuntimeException("ES2JDBCGen0027", new Object[] { e }, e);
        } finally {
            ConnectionUtil.close(conn);
        }
    }

    /**
     * テーブルを作成します。
     */
    protected void create() {
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            logger.debug(createSql);
            PreparedStatement ps = ConnectionUtil.prepareStatement(conn,
                    createSql);
            try {
                PreparedStatementUtil.execute(ps);
            } finally {
                StatementUtil.close(ps);
            }
        } finally {
            ConnectionUtil.close(conn);
        }
    }

}
