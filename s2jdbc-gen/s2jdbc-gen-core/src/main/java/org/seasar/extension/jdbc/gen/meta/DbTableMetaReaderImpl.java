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
package org.seasar.extension.jdbc.gen.meta;

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

import org.seasar.extension.jdbc.gen.DbColumnMeta;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.ResultSetUtil;

/**
 * {@code SchemaReader}の実装クラスです。
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

    /** 正規表現で表されたテーブル名のパターン */
    protected String tableNamePattern;

    protected boolean schemaSpecified;

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
     *            正規表現で表されたテーブル名のパターン
     */
    public DbTableMetaReaderImpl(DataSource dataSource, GenDialect dialect,
            String schemaName, String tableNamePattern) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (tableNamePattern == null) {
            throw new NullPointerException(tableNamePattern);
        }
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.schemaName = schemaName;
        this.tableNamePattern = tableNamePattern;
        schemaSpecified = schemaName != null;
    }

    public List<DbTableMeta> read() {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(con);
            String schemaName = schemaSpecified ? this.schemaName
                    : getDefaultSchemaName(metaData);
            List<DbTableMeta> dbTableMetaList = getDbTableMetaList(metaData,
                    schemaName);
            List<DbTableMeta> filteredDbTableMetaList = filterDbTableMetaList(
                    dbTableMetaList, tableNamePattern);
            for (DbTableMeta tm : filteredDbTableMetaList) {
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
            return filteredDbTableMetaList;
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
                    if (schemaSpecified) {
                        dbTableMeta.setSchemaName(rs.getString(2));
                    }
                    dbTableMeta.setName(rs.getString(3));
                    result.add(dbTableMeta);
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
     * テーブルメタデータをフィルタリングします。
     * 
     * @param dbTableMetaList
     *            テーブルメタデータのリスト
     * @param tableNamePattern
     *            テーブル名のパターン
     * @return フィルタリングされたテーブルメタデータのリスト
     */
    protected List<DbTableMeta> filterDbTableMetaList(
            List<DbTableMeta> dbTableMetaList, String tableNamePattern) {
        List<DbTableMeta> result = new ArrayList<DbTableMeta>();
        Pattern p = Pattern.compile(tableNamePattern, Pattern.CASE_INSENSITIVE);
        for (DbTableMeta dbTableMeta : dbTableMetaList) {
            if (p.matcher(dbTableMeta.getName()).matches()) {
                if (dialect.isUserTable(dbTableMeta.getName())) {
                    result.add(dbTableMeta);
                }
            }
        }
        return result;
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
