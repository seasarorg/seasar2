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
package org.seasar.extension.jdbc.gen.internal.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.CaseInsensitiveSet;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StringUtil;

/**
 * テーブルに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class TableUtil {

    /**
     * 
     */
    protected TableUtil() {
    }

    /**
     * テーブルの集合を返します。
     * 
     * @param dialect
     *            方言
     * @param dataSource
     *            データソース
     * @return テーブルの集合
     */
    public static TableSet getTableSet(GenDialect dialect, DataSource dataSource) {
        return new TableSet(dialect, dataSource);
    }

    /**
     * 標準のテーブル名を組み立てます。
     * 
     * @param dialect
     *            方言
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 標準のテーブル名
     */
    public static String buildCanonicalTableName(GenDialect dialect,
            String catalogName, String schemaName, String tableName) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (tableName == null) {
            throw new NullPointerException("tableName");
        }
        StringBuilder buf = new StringBuilder();
        if (catalogName != null) {
            buf.append(dialect.unquote(catalogName.toLowerCase()));
            buf.append(".");
            if (schemaName != null) {
                buf.append(dialect.unquote(schemaName.toLowerCase()));
            }
            buf.append(".");
        } else if (schemaName != null) {
            buf.append(dialect.unquote(schemaName.toLowerCase()));
            buf.append(".");
        }
        buf.append(dialect.unquote(tableName.toLowerCase()));
        return buf.toString();
    }

    /**
     * 完全なテーブル名を組み立てます。
     * 
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 完全なテーブル名
     */
    public static String buildFullTableName(String catalogName,
            String schemaName, String tableName) {
        StringBuilder buf = new StringBuilder();
        if (!StringUtil.isEmpty(catalogName)) {
            buf.append(catalogName).append(".");
        }
        if (!StringUtil.isEmpty(schemaName)) {
            buf.append(schemaName).append(".");
        }
        return buf.append(tableName).toString();
    }

    /**
     * 標準のテーブル名を、カタログ名、スキーマ名、テーブル名の3つを要素とする文字列配列に分解します。
     * 
     * @param canonicalTableName
     *            標準のテーブル名
     * @return 要素数が3の文字列配列
     */
    public static String[] splitCanonicalTableName(String canonicalTableName) {
        List<String> list = new ArrayList<String>();
        int pos = 0;
        for (int i = 0; i < canonicalTableName.length(); i++) {
            char c = canonicalTableName.charAt(i);
            if (c == '.') {
                list.add(canonicalTableName.substring(pos, i));
                pos = i + 1;
            }
        }
        list.add(canonicalTableName.substring(pos));

        String[] elements = new String[3];
        if (list.size() == 3) {
            for (int i = 0; i < 3; i++) {
                if (!StringUtil.isEmpty(list.get(i))) {
                    elements[i] = list.get(i);
                }
            }
        } else if (list.size() == 2) {
            for (int i = 0; i < 2; i++) {
                if (!StringUtil.isEmpty(list.get(i))) {
                    elements[i + 1] = list.get(i);
                }
            }
        } else if (list.size() == 1) {
            elements[2] = list.get(0);
        } else {
            throw new IllegalArgumentException(canonicalTableName);
        }
        return elements;
    }

    /**
     * テーブルの集合です。
     * 
     * @author taedium
     */
    public static class TableSet {

        /** 方言 */
        protected GenDialect dialect;

        /** データソース */
        protected DataSource dataSource;

        /** デフォルトのスキーマ名 */
        protected String defaultSchemaName;

        /** 修飾子をキー、テーブル名の配列を値とするマップ */
        protected Map<Qualifier, CaseInsensitiveSet> tableNamesMap = new HashMap<Qualifier, CaseInsensitiveSet>();

        /**
         * インスタンスを構築します。
         * 
         * @param dialect
         *            方言
         * @param dataSource
         *            データソース
         */
        protected TableSet(GenDialect dialect, DataSource dataSource) {
            if (dialect == null) {
                throw new NullPointerException("dialect");
            }
            if (dataSource == null) {
                throw new NullPointerException("dataSource");
            }
            this.dialect = dialect;
            this.dataSource = dataSource;
            this.defaultSchemaName = getDefaultSchemaName();
        }

        /**
         * デフォルトのスキーマ名を返します。
         * 
         * @return デフォルトのスキーマ名
         */
        protected String getDefaultSchemaName() {
            Connection connection = DataSourceUtil.getConnection(dataSource);
            try {
                DatabaseMetaData metaData = ConnectionUtil
                        .getMetaData(connection);
                String userName = DatabaseMetaDataUtil.getUserName(metaData);
                return dialect.getDefaultSchemaName(userName);
            } finally {
                ConnectionUtil.close(connection);
            }
        }

        /**
         * テーブルがデータベースに存在する場合{@code true}を返します。
         * 
         * @param catalogName
         *            カタログ名
         * @param schemaName
         *            スキーマ名
         * @param tableName
         *            テーブル名
         * @return テーブルがデータベースに存在する場合{@code true}
         */
        public boolean exists(String catalogName, String schemaName,
                String tableName) {
            schemaName = schemaName != null ? schemaName : defaultSchemaName;
            CaseInsensitiveSet tableNames = getTableNames(catalogName,
                    schemaName);
            return tableNames.contains(tableName);
        }

        /**
         * テーブル名のセットを返します。
         * 
         * @param catalogName
         *            カタログ名
         * @param schemaName
         *            スキーマ名
         * @return テーブル名のセット
         */
        protected CaseInsensitiveSet getTableNames(String catalogName,
                String schemaName) {
            Qualifier qualifier = new Qualifier(catalogName, schemaName);
            if (tableNamesMap.containsKey(qualifier)) {
                return tableNamesMap.get(qualifier);
            }

            Connection connection = DataSourceUtil.getConnection(dataSource);
            try {
                DatabaseMetaData metaData = ConnectionUtil
                        .getMetaData(connection);
                ResultSet rs = metaData.getTables(catalogName, schemaName,
                        null, new String[] { "TABLE" });
                try {
                    CaseInsensitiveSet tableNames = new CaseInsensitiveSet();
                    while (rs.next()) {
                        tableNames.add(rs.getString("TABLE_NAME"));
                    }
                    tableNamesMap.put(qualifier, tableNames);
                    return tableNames;
                } finally {
                    ResultSetUtil.close(rs);
                }
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            } finally {
                ConnectionUtil.close(connection);
            }
        }
    }

    /**
     * テーブルの修飾子です。
     * 
     * @author taedium
     */
    protected static class Qualifier {

        /** カタログ名 */
        protected String catalogName;

        /** スキーマ名 */
        protected String schemaName;

        /**
         * インスタンスを構築します。
         * 
         * @param catalogName
         *            カタログ名
         * @param schemaName
         *            スキーマ名
         */
        public Qualifier(String catalogName, String schemaName) {
            if (catalogName != null) {
                this.catalogName = catalogName.toLowerCase();
            }
            if (schemaName != null) {
                this.schemaName = schemaName.toLowerCase();
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((catalogName == null) ? 0 : catalogName.hashCode());
            result = prime * result
                    + ((schemaName == null) ? 0 : schemaName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Qualifier other = (Qualifier) obj;
            if (catalogName == null) {
                if (other.catalogName != null) {
                    return false;
                }
            } else if (!catalogName.equals(other.catalogName)) {
                return false;
            }
            if (schemaName == null) {
                if (other.schemaName != null) {
                    return false;
                }
            } else if (!schemaName.equals(other.schemaName)) {
                return false;
            }
            return true;
        }
    }
}
