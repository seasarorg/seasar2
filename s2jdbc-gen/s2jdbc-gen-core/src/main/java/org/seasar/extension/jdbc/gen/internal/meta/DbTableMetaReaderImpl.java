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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.ResultSetUtil;

/**
 * {@code DbTableMetaReader}の実装クラスです。
 * 
 * @author taedium
 */
public class DbTableMetaReaderImpl implements DbTableMetaReader {

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** スキーマ名 */
    protected String schemaName;

    /** 読み取り対象とするテーブル名のパターン */
    protected Pattern tableNamePattern;

    /** 読み取り非対象とするテーブル名のパターン */
    protected Pattern ignoreTableNamePattern;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param dialect
     *            方言
     * @param schemaName
     *            スキーマ名、デフォルトのスキーマ名を表す場合は{@code null}
     * @param tableNamePattern
     *            対象とするテーブル名の正規表現
     * @param ignoreTableNamePattern
     *            対象としないテーブル名の正規表現
     */
    public DbTableMetaReaderImpl(DataSource dataSource, GenDialect dialect,
            String schemaName, String tableNamePattern,
            String ignoreTableNamePattern) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (tableNamePattern == null) {
            throw new NullPointerException(tableNamePattern);
        }
        if (ignoreTableNamePattern == null) {
            throw new NullPointerException(ignoreTableNamePattern);
        }
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.schemaName = schemaName;
        this.tableNamePattern = Pattern.compile(tableNamePattern,
                Pattern.CASE_INSENSITIVE);
        this.ignoreTableNamePattern = Pattern.compile(ignoreTableNamePattern,
                Pattern.CASE_INSENSITIVE);
    }

    public List<DbTableMeta> read() {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(con);
            String schemaName = this.schemaName != null ? this.schemaName
                    : getDefaultSchemaName(metaData);
            List<DbTableMeta> dbTableMetaList = getDbTableMetaList(metaData,
                    schemaName);
            for (DbTableMeta tm : dbTableMetaList) {
                Set<String> primaryKeys = getPrimaryKeySet(metaData, tm
                        .getCatalogName(), tm.getSchemaName(), tm.getName());
                for (DbColumnMeta cm : getDbColumnMetaList(metaData, tm
                        .getCatalogName(), tm.getSchemaName(), tm.getName())) {
                    if (primaryKeys.contains(cm.getName())) {
                        cm.setPrimaryKey(true);
                    }
                    tm.addColumnMeta(cm);
                }
            }
            return dbTableMetaList;
        } finally {
            ConnectionUtil.close(con);
        }
    }

    /**
     * デフォルトのスキーマ名を返します。
     * 
     * @param metaData
     *            メタデータ
     * @return デフォルトのスキーマ名
     */
    protected String getDefaultSchemaName(DatabaseMetaData metaData) {
        String userName = DatabaseMetaDataUtil.getUserName(metaData);
        return dialect.getDefaultSchemaName(userName);
    }

    /**
     * テーブルメタデータのリストを返します。
     * 
     * @param metaData
     *            メタデータ
     * @param schemaName
     *            スキーマ名
     * @return テーブルメタデータのリスト
     */
    protected List<DbTableMeta> getDbTableMetaList(DatabaseMetaData metaData,
            String schemaName) {
        List<DbTableMeta> result = new ArrayList<DbTableMeta>();
        try {
            ResultSet rs = metaData.getTables(null, schemaName, null,
                    new String[] { "TABLE" });
            try {
                while (rs.next()) {
                    DbTableMeta dbTableMeta = new DbTableMeta();
                    dbTableMeta.setCatalogName(rs.getString(1));
                    dbTableMeta.setSchemaName(rs.getString(2));
                    dbTableMeta.setName(rs.getString(3));
                    if (isTargetTable(dbTableMeta)) {
                        result.add(dbTableMeta);
                    }
                }
                return result;
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * 読み取り対象のテーブルの場合{@code true}を返します。
     * 
     * @param dbTableMeta
     *            テーブルメタデータ
     * @return 読み取り対象のテーブルの場合{@code true}
     */
    protected boolean isTargetTable(DbTableMeta dbTableMeta) {
        String name = dbTableMeta.getName();
        if (!dialect.isUserTable(name)) {
            return false;
        }
        if (!tableNamePattern.matcher(name).matches()) {
            return false;
        }
        if (ignoreTableNamePattern.matcher(name).matches()) {
            return false;
        }
        return true;
    }

    /**
     * カラムメタデータのリストを返します。
     * 
     * @param metaData
     *            メタデータ
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return カラムメタデータのリスト
     */
    protected List<DbColumnMeta> getDbColumnMetaList(DatabaseMetaData metaData,
            String catalogName, String schemaName, String tableName) {
        List<DbColumnMeta> result = new ArrayList<DbColumnMeta>();
        try {
            ResultSet rs = metaData.getColumns(catalogName, schemaName,
                    tableName, null);
            try {
                while (rs.next()) {
                    DbColumnMeta columnDesc = new DbColumnMeta();
                    columnDesc.setName(rs.getString(4));
                    columnDesc.setSqlType(rs.getInt(5));
                    columnDesc.setTypeName(rs.getString(6));
                    columnDesc.setLength(rs.getInt(7));
                    columnDesc.setScale(rs.getInt(9));
                    columnDesc.setNullable(rs.getBoolean(11));
                    result.add(columnDesc);
                }
                return result;
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 主キーのセットを返します。
     * 
     * @param metaData
     *            メタデータ
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 主キーのセット
     */
    protected Set<String> getPrimaryKeySet(DatabaseMetaData metaData,
            String catalogName, String schemaName, String tableName) {
        Set<String> result = new HashSet<String>();
        try {
            ResultSet rs = metaData.getPrimaryKeys(catalogName, schemaName,
                    tableName);
            try {
                while (rs.next()) {
                    result.add(rs.getString(4));
                }
            } finally {
                ResultSetUtil.close(rs);
            }
            return result;
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}
