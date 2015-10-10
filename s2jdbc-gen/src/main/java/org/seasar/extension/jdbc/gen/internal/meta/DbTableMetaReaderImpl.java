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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.TableNotFoundRuntimeException;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.meta.DbUniqueKeyMeta;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.ResultSetUtil;

/**
 * {@code DbTableMetaReader}の実装クラスです。
 * 
 * @author taedium
 */
public class DbTableMetaReaderImpl implements DbTableMetaReader {

    /** ロガー */
    protected Logger logger = Logger.getLogger(DbTableMetaReaderImpl.class);

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

    /** コメントを読む場合{@code true} */
    protected boolean readComment;

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
     * @param readComment
     *            コメントを読む場合{@code true}
     */
    public DbTableMetaReaderImpl(DataSource dataSource, GenDialect dialect,
            String schemaName, String tableNamePattern,
            String ignoreTableNamePattern, boolean readComment) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (tableNamePattern == null) {
            throw new NullPointerException("tableNamePattern");
        }
        if (ignoreTableNamePattern == null) {
            throw new NullPointerException("ignoreTableNamePattern");
        }
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.schemaName = schemaName;
        this.tableNamePattern = Pattern.compile(tableNamePattern,
                Pattern.CASE_INSENSITIVE);
        this.ignoreTableNamePattern = Pattern.compile(ignoreTableNamePattern,
                Pattern.CASE_INSENSITIVE);
        this.readComment = readComment;
    }

    public List<DbTableMeta> read() {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(con);
            List<DbTableMeta> dbTableMetaList = getDbTableMetaList(metaData,
                    schemaName != null ? schemaName
                            : getDefaultSchemaName(metaData));
            if (dbTableMetaList.isEmpty()) {
                throw new TableNotFoundRuntimeException(dialect.getClass()
                        .getName(), schemaName, tableNamePattern.pattern(),
                        ignoreTableNamePattern.pattern());
            }
            for (DbTableMeta tableMeta : dbTableMetaList) {
                Set<String> primaryKeySet = getPrimaryKeySet(metaData,
                        tableMeta);
                doDbUniqueKeyMeta(metaData, tableMeta, primaryKeySet);
                doDbColumnMeta(metaData, tableMeta, primaryKeySet);
                doDbForeignKeyMeta(metaData, tableMeta);
            }
            if (readComment && !dialect.isJdbcCommentAvailable()) {
                readCommentFromDictinary(con, dbTableMetaList);
            }
            return dbTableMetaList;
        } finally {
            ConnectionUtil.close(con);
        }
    }

    /**
     * 一意キーメタデータを処理します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @param primaryKeySet
     *            主キーのセット
     */
    protected void doDbUniqueKeyMeta(DatabaseMetaData metaData,
            DbTableMeta tableMeta, Set<String> primaryKeySet) {
        for (DbUniqueKeyMeta ukMeta : getDbUniqueKeyMetaList(metaData,
                tableMeta)) {
            if (primaryKeySet.size() == ukMeta.getColumnNameList().size()
                    && primaryKeySet.containsAll(ukMeta.getColumnNameList())) {
                ukMeta.setPrimaryKey(true);
            }
            tableMeta.addUniqueKeyMeta(ukMeta);
        }
    }

    /**
     * カラムメタデータを処理します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @param primaryKeySet
     *            主キーのセット
     */
    protected void doDbColumnMeta(DatabaseMetaData metaData,
            DbTableMeta tableMeta, Set<String> primaryKeySet) {
        for (DbColumnMeta columnMeta : getDbColumnMetaList(metaData, tableMeta)) {
            if (primaryKeySet.contains(columnMeta.getName())) {
                columnMeta.setPrimaryKey(true);
                if (primaryKeySet.size() == 1) {
                    columnMeta.setAutoIncrement(isAutoIncrement(metaData,
                            tableMeta, columnMeta.getName()));
                }
            }
            for (DbUniqueKeyMeta ukMeta : tableMeta.getUniqueKeyMetaList()) {
                if (ukMeta.getColumnNameList().size() == 1) {
                    String ukColumnName = ukMeta.getColumnNameList().get(0);
                    if (columnMeta.getName().equals(ukColumnName)) {
                        columnMeta.setUnique(true);
                    }
                }
            }
            tableMeta.addColumnMeta(columnMeta);
        }
    }

    /**
     * 外部キーメタデータを処理します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     */
    protected void doDbForeignKeyMeta(DatabaseMetaData metaData,
            DbTableMeta tableMeta) {
        for (DbForeignKeyMeta fkMeta : getDbForeignKeyMetaList(metaData,
                tableMeta)) {
            for (DbUniqueKeyMeta ukMeta : tableMeta.getUniqueKeyMetaList()) {
                if (fkMeta.getForeignKeyColumnNameList().equals(
                        ukMeta.getColumnNameList())) {
                    fkMeta.setUnique(true);
                }
            }
            tableMeta.addForeignKeyMeta(fkMeta);
        }
    }

    /**
     * デフォルトのスキーマ名を返します。
     * 
     * @param metaData
     *            データベースメタデータ
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
     *            データベースメタデータ
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
                    dbTableMeta.setCatalogName(rs.getString("TABLE_CAT"));
                    dbTableMeta.setSchemaName(rs.getString("TABLE_SCHEM"));
                    dbTableMeta.setName(rs.getString("TABLE_NAME"));
                    if (readComment) {
                        dbTableMeta.setComment(rs.getString("REMARKS"));
                    }
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
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * 
     * @return カラムメタデータのリスト
     */
    protected List<DbColumnMeta> getDbColumnMetaList(DatabaseMetaData metaData,
            DbTableMeta tableMeta) {
        List<DbColumnMeta> result = new ArrayList<DbColumnMeta>();
        try {
            ResultSet rs = metaData.getColumns(tableMeta.getCatalogName(),
                    tableMeta.getSchemaName(), tableMeta.getName(), null);
            try {
                while (rs.next()) {
                    DbColumnMeta columnDesc = new DbColumnMeta();
                    columnDesc.setName(rs.getString("COLUMN_NAME"));
                    columnDesc.setSqlType(rs.getInt("DATA_TYPE"));
                    columnDesc.setTypeName(rs.getString("TYPE_NAME"));
                    columnDesc.setLength(rs.getInt("COLUMN_SIZE"));
                    columnDesc.setScale(rs.getInt("DECIMAL_DIGITS"));
                    columnDesc.setNullable(rs.getBoolean("NULLABLE"));
                    columnDesc.setDefaultValue(rs.getString("COLUMN_DEF"));
                    if (readComment) {
                        columnDesc.setComment(rs.getString("REMARKS"));
                    }
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
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @return 主キーのセット
     */
    protected Set<String> getPrimaryKeySet(DatabaseMetaData metaData,
            DbTableMeta tableMeta) {
        Set<String> result = new HashSet<String>();
        try {
            ResultSet rs = metaData.getPrimaryKeys(tableMeta.getCatalogName(),
                    tableMeta.getSchemaName(), tableMeta.getName());
            try {
                while (rs.next()) {
                    result.add(rs.getString("COLUMN_NAME"));
                }
            } finally {
                ResultSetUtil.close(rs);
            }
            return result;
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 外部キーメタデータのリストを返します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @return 外部キーメタデータのリスト
     */
    protected List<DbForeignKeyMeta> getDbForeignKeyMetaList(
            DatabaseMetaData metaData, DbTableMeta tableMeta) {
        @SuppressWarnings("unchecked")
        Map<String, DbForeignKeyMeta> map = new ArrayMap();
        try {
            ResultSet rs = metaData.getImportedKeys(tableMeta.getCatalogName(),
                    tableMeta.getSchemaName(), tableMeta.getName());
            try {
                while (rs.next()) {
                    String name = rs.getString("FK_NAME");
                    if (!map.containsKey(name)) {
                        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
                        fkMeta.setName(name);
                        fkMeta.setPrimaryKeyCatalogName(rs
                                .getString("PKTABLE_CAT"));
                        fkMeta.setPrimaryKeySchemaName(rs
                                .getString("PKTABLE_SCHEM"));
                        fkMeta.setPrimaryKeyTableName(rs
                                .getString("PKTABLE_NAME"));
                        map.put(name, fkMeta);
                    }
                    DbForeignKeyMeta fkMeta = map.get(name);
                    fkMeta.addPrimaryKeyColumnName(rs
                            .getString("PKCOLUMN_NAME"));
                    fkMeta.addForeignKeyColumnName(rs
                            .getString("FKCOLUMN_NAME"));
                }
            } finally {
                ResultSetUtil.close(rs);
            }
            DbForeignKeyMeta[] array = map.values().toArray(
                    new DbForeignKeyMeta[map.size()]);
            return Arrays.asList(array);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 一意キーメタデータのリストを返します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @return 一意キーメタデータのリスト
     */
    protected List<DbUniqueKeyMeta> getDbUniqueKeyMetaList(
            DatabaseMetaData metaData, DbTableMeta tableMeta) {

        if (!dialect.supportsGetIndexInfo(tableMeta.getCatalogName(), tableMeta
                .getSchemaName(), tableMeta.getName())) {
            logger.log("WS2JDBCGen0002", new Object[] {
                    tableMeta.getCatalogName(), tableMeta.getSchemaName(),
                    tableMeta.getName() });
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        Map<String, DbUniqueKeyMeta> map = new ArrayMap();
        try {
            ResultSet rs = metaData
                    .getIndexInfo(tableMeta.getCatalogName(), tableMeta
                            .getSchemaName(), tableMeta.getName(), true, false);
            try {
                while (rs.next()) {
                    String name = rs.getString("INDEX_NAME");
                    if (!map.containsKey(name)) {
                        DbUniqueKeyMeta ukMeta = new DbUniqueKeyMeta();
                        ukMeta.setName(name);
                        map.put(name, ukMeta);
                    }
                    DbUniqueKeyMeta ukMeta = map.get(name);
                    ukMeta.addColumnName(rs.getString("COLUMN_NAME"));
                }
            } finally {
                ResultSetUtil.close(rs);
            }
            DbUniqueKeyMeta[] array = map.values().toArray(
                    new DbUniqueKeyMeta[map.size()]);
            return Arrays.asList(array);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 列の値が自動的に増分される場合{@code true}を返します。
     * 
     * @param metaData
     *            データベースメタデータ
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnName
     *            カラム名
     * @return 列が自動的に増分される場合{@code true}、そうでない場合{@code false}
     */
    protected boolean isAutoIncrement(DatabaseMetaData metaData,
            DbTableMeta tableMeta, String columnName) {
        try {
            return dialect.isAutoIncrement(metaData.getConnection(), tableMeta
                    .getCatalogName(), tableMeta.getSchemaName(), tableMeta
                    .getName(), columnName);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * コメントをデータベースのディクショナリから直接取得します。
     * 
     * @param connection
     *            コネクション
     * @param dbTableMetaList
     *            テーブルメタデータのリスト
     */
    protected void readCommentFromDictinary(Connection connection,
            List<DbTableMeta> dbTableMetaList) {
        try {
            for (DbTableMeta tableMeta : dbTableMetaList) {
                String tableComment = dialect.getTableComment(connection,
                        tableMeta.getCatalogName(), tableMeta.getSchemaName(),
                        tableMeta.getName());
                tableMeta.setComment(tableComment);
                Map<String, String> columnCommentMap = dialect
                        .getColumnCommentMap(connection, tableMeta
                                .getCatalogName(), tableMeta.getSchemaName(),
                                tableMeta.getName());
                for (DbColumnMeta columnMeta : tableMeta.getColumnMetaList()) {
                    String columnName = columnMeta.getName();
                    if (columnCommentMap.containsKey(columnName)) {
                        String columnComment = columnCommentMap.get(columnName);
                        columnMeta.setComment(columnComment);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
