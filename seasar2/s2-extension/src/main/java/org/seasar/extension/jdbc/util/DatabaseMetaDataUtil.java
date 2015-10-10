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
package org.seasar.extension.jdbc.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.CaseInsensitiveSet;

/**
 * データベースメタデータ用のユーティリティです。
 * 
 * @author higa
 * @author manhole
 */
public class DatabaseMetaDataUtil {

    /**
     * インスタンスを構築します。
     */
    protected DatabaseMetaDataUtil() {
    }

    /**
     * プライマリーキーの配列を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param tableName
     *            テーブル名
     * @return プライマリーキーの配列
     */
    public static String[] getPrimaryKeys(DatabaseMetaData dbMetaData,
            String tableName) {

        Set set = getPrimaryKeySet(dbMetaData, tableName);
        return (String[]) set.toArray(new String[set.size()]);
    }

    /**
     * プライマリーキーの {@link Set}を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param tableName
     *            テーブル名
     * @return プライマリーキーの {@link Set}
     */
    public static Set getPrimaryKeySet(DatabaseMetaData dbMetaData,
            String tableName) {
        final String schema;
        int index = tableName.indexOf('.');
        if (index >= 0) {
            schema = tableName.substring(0, index);
            tableName = tableName.substring(index + 1);
        } else {
            schema = getUserName(dbMetaData);
        }
        final String convertedTableName = convertIdentifier(dbMetaData,
                tableName);
        Set set = new CaseInsensitiveSet();
        addPrimaryKeys(dbMetaData, convertIdentifier(dbMetaData, schema),
                convertedTableName, set);
        if (set.size() == 0) {
            addPrimaryKeys(dbMetaData, schema, tableName, set);
        }
        if (set.size() == 0 && schema != null) {
            addPrimaryKeys(dbMetaData, null, convertedTableName, set);
            if (set.size() == 0) {
                addPrimaryKeys(dbMetaData, null, tableName, set);
            }
        }
        return set;
    }

    /**
     * プライマリーキーの {@link Set}を返します。
     * 
     * @param dbMetaData
     * @param schema
     * @param tableName
     * @return
     */
    public static Set getPrimaryKeySet(DatabaseMetaData dbMetaData,
            String schema, String tableName) {
        Set set = new CaseInsensitiveSet();
        addPrimaryKeys(dbMetaData, schema, tableName, set);
        return set;
    }

    /**
     * ユーザ名を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return ユーザ名
     */
    public static String getUserName(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.getUserName();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    private static void addPrimaryKeys(DatabaseMetaData dbMetaData,
            String schema, String tableName, Set set) {
        try {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, schema, tableName);
            while (rs.next()) {
                set.add(rs.getString(4));
            }
            rs.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * カラム名の配列を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param tableName
     *            テーブル名
     * @return カラム名の配列
     */
    public static String[] getColumns(DatabaseMetaData dbMetaData,
            String tableName) {

        Map map = getColumnMap(dbMetaData, tableName);
        String[] ret = new String[map.size()];
        int index = 0;
        for (Iterator i = map.values().iterator(); i.hasNext();) {
            ColumnDesc cd = (ColumnDesc) i.next();
            ret[index++] = cd.getName();
        }
        return ret;
    }

    /**
     * カラムデータの {@link Map}を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param tableName
     *            テーブル名
     * @return カラムデータの {@link Map}
     */
    public static Map getColumnMap(DatabaseMetaData dbMetaData, String tableName) {
        return getColumnCaseInsensitiveMap(dbMetaData, tableName);
    }

    /**
     * カラムデータの {@link CaseInsensitiveMap}を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param tableName
     *            テーブル名
     * @return カラムデータの {@link CaseInsensitiveMap}
     */
    public static CaseInsensitiveMap getColumnCaseInsensitiveMap(
            DatabaseMetaData dbMetaData, String tableName) {
        final String schema;
        int index = tableName.indexOf('.');
        if (index >= 0) {
            schema = tableName.substring(0, index);
            tableName = tableName.substring(index + 1);
        } else {
            schema = getUserName(dbMetaData);
        }
        final String convertedTableName = convertIdentifier(dbMetaData,
                tableName);
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        addColumns(dbMetaData, convertIdentifier(dbMetaData, schema),
                convertedTableName, map);
        if (map.size() == 0) {
            addColumns(dbMetaData, schema, tableName, map);
        }
        if (map.size() == 0 && schema != null) {
            addColumns(dbMetaData, null, convertedTableName, map);
            if (map.size() == 0) {
                addColumns(dbMetaData, null, tableName, map);
            }
        }
        return map;
    }

    /**
     * カラムデータの {@link CaseInsensitiveMap}を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param schema
     *            スキーマ
     * @param tableName
     *            テーブル名
     * @return カラムデータの {@link CaseInsensitiveMap}
     */
    public static CaseInsensitiveMap getColumnMap(DatabaseMetaData dbMetaData,
            String schema, String tableName) {
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        addColumns(dbMetaData, schema, tableName, map);
        return map;
    }

    private static void addColumns(DatabaseMetaData dbMetaData, String schema,
            String tableName, Map map) {
        try {
            ResultSet rs = dbMetaData.getColumns(null, schema, tableName, null);
            while (rs.next()) {
                ColumnDesc cd = new ColumnDesc();
                cd.setName(rs.getString(4));
                cd.setSqlType(rs.getInt(5));
                map.put(cd.getName(), cd);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 識別子を変換します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @param identifier
     *            識別子
     * @return 変換後の識別子
     */
    public static String convertIdentifier(DatabaseMetaData dbMetaData,
            String identifier) {

        if (identifier == null) {
            return null;
        }
        if (!supportsMixedCaseIdentifiers(dbMetaData)) {
            if (storesUpperCaseIdentifiers(dbMetaData)) {
                return identifier.toUpperCase();
            }
            return identifier.toLowerCase();
        }
        return identifier;
    }

    /**
     * 大文字小文字のミックスをサポートしているかどうか返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return 大文字小文字のミックスをサポートしているかどうか
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    public static boolean supportsMixedCaseIdentifiers(
            DatabaseMetaData dbMetaData) throws SQLRuntimeException {

        try {
            return dbMetaData.supportsMixedCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 識別子を大文字で保存するかどうかを返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return 識別子を大文字で保存するかどうか
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    public static boolean storesUpperCaseIdentifiers(DatabaseMetaData dbMetaData)
            throws SQLRuntimeException {
        try {
            return dbMetaData.storesUpperCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 識別子を小文字で保存するかどうかを返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return 識別子を小文字で保存するかどうか
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    public static boolean storesLowerCaseIdentifiers(DatabaseMetaData dbMetaData)
            throws SQLRuntimeException {
        try {
            return dbMetaData.storesLowerCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * スキーマをサポートしているかどうかを返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return スキーマをサポートしているかどうか
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    public static boolean supportsSchemasInTableDefinitions(
            DatabaseMetaData dbMetaData) throws SQLRuntimeException {
        try {
            return dbMetaData.supportsSchemasInTableDefinitions();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 識別子の自動作成をサポートしているかどうかを返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return 識別子の自動作成をサポートしているかどうか
     */
    public static boolean supportsGetGeneratedKeys(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.supportsGetGeneratedKeys();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * データベースのプロダクト名を返します。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     * @return データベースのプロダクト名
     */
    public static String getDatabaseProductName(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.getDatabaseProductName();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}