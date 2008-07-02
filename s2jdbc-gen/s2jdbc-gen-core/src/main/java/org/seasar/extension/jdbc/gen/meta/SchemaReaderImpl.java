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
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SchemaReader;
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
public class SchemaReaderImpl implements SchemaReader {

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param dialect
     *            方言
     */
    public SchemaReaderImpl(DataSource dataSource, GenDialect dialect) {
        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    public List<DbTableMeta> read(String schemaName, String tableNamePattern) {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(con);
            schemaName = schemaName != null ? schemaName
                    : getDefaultSchemaName(metaData);
            List<String> tableNames = getTableNameList(metaData, schemaName);

            List<DbTableMeta> result = new ArrayList<DbTableMeta>();
            for (String tableName : filterTableNames(tableNames,
                    tableNamePattern)) {
                if (!dialect.isUserTable(tableName)) {
                    continue;
                }
                DbTableMeta tableMeta = new DbTableMeta();
                tableMeta.setName(tableName);

                Set<String> primaryKeys = getPrimaryKeySet(metaData,
                        schemaName, tableName);
                for (DbColumnMeta cm : getDbColumnMetaList(metaData,
                        schemaName, tableName)) {
                    if (primaryKeys.contains(cm.getName())) {
                        cm.setPrimaryKey(true);
                    }
                    tableMeta.addColumnMeta(cm);
                }
                result.add(tableMeta);
            }
            return result;
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
     * テーブル名のセットを返します。
     * 
     * @param metaData
     *            メタデータ
     * @param schemaName
     *            スキーマ名
     * @return テーブル名のセット
     */
    protected List<String> getTableNameList(DatabaseMetaData metaData,
            String schemaName) {
        List<String> result = new ArrayList<String>();
        try {
            ResultSet rs = metaData.getTables(null, schemaName, null,
                    new String[] { "TABLE" });
            try {
                while (rs.next()) {
                    result.add(rs.getString(3));
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
     * テーブル名をフィルタリングします。
     * 
     * @param tableNames
     *            テーブル名のリスト
     * @param tableNamePattern
     *            テーブル名のパターン
     * @return フィルタリングされたテーブル名の新しいリスト
     */
    protected List<String> filterTableNames(List<String> tableNames,
            String tableNamePattern) {
        List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile(tableNamePattern, Pattern.CASE_INSENSITIVE);
        for (String name : tableNames) {
            if (p.matcher(name).matches()) {
                result.add(name);
            }
        }
        return result;
    }

    /**
     * カラム記述のリストを返します。
     * 
     * @param metaData
     *            メタデータ
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return カラムメタデータのリスト
     */
    protected List<DbColumnMeta> getDbColumnMetaList(DatabaseMetaData metaData,
            String schemaName, String tableName) {
        List<DbColumnMeta> result = new ArrayList<DbColumnMeta>();
        try {
            ResultSet rs = metaData.getColumns(null, schemaName, tableName,
                    null);
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
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 主キーのセット
     */
    protected Set<String> getPrimaryKeySet(DatabaseMetaData metaData,
            String schemaName, String tableName) {
        Set<String> result = new HashSet<String>();
        try {
            ResultSet rs = metaData.getPrimaryKeys(null, schemaName, tableName);
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
