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
package org.seasar.extension.jdbc.gen.dialect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

/**
 * MySQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MysqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 1146;

    /**
     * インスタンスを構築します。
     */
    public MysqlGenDialect() {
        sqlTypeMap.put(Types.BINARY, MysqlSqlType.BINARY);
        sqlTypeMap.put(Types.BLOB, MysqlSqlType.BLOB);
        sqlTypeMap.put(Types.CLOB, MysqlSqlType.CLOB);
        sqlTypeMap.put(Types.DECIMAL, MysqlSqlType.DECIMAL);
        sqlTypeMap.put(Types.DOUBLE, MysqlSqlType.DOUBLE);
        sqlTypeMap.put(Types.FLOAT, MysqlSqlType.FLOAT);
        sqlTypeMap.put(Types.INTEGER, MysqlSqlType.INTEGER);

        columnTypeMap.put("bigint unsigned", MysqlColumnType.BIGINT_UNSIGNED);
        columnTypeMap.put("binary", MysqlColumnType.BINARY);
        columnTypeMap.put("bit", MysqlColumnType.BIT);
        columnTypeMap.put("blob", MysqlColumnType.BLOB);
        columnTypeMap.put("datetime", MysqlColumnType.DATETIME);
        columnTypeMap.put("decimal", MysqlColumnType.DECIMAL);
        columnTypeMap.put("double", MysqlColumnType.DOUBLE);
        columnTypeMap.put("int", MysqlColumnType.INT);
        columnTypeMap.put("int unsigned", MysqlColumnType.INT_UNSIGNED);
        columnTypeMap.put("longblob", MysqlColumnType.LONGBLOB);
        columnTypeMap.put("longtext", MysqlColumnType.LONGTEXT);
        columnTypeMap.put("mediumblob", MysqlColumnType.MEDIUMBLOB);
        columnTypeMap.put("mediumint", MysqlColumnType.MEDIUMINT);
        columnTypeMap.put("mediumint unsigned",
                MysqlColumnType.MEDIUMINT_UNSIGNED);
        columnTypeMap.put("mediumtext", MysqlColumnType.MEDIUMTEXT);
        columnTypeMap.put("smallint unsigned",
                MysqlColumnType.SMALLINT_UNSIGNED);
        columnTypeMap.put("tinyblob", MysqlColumnType.TINYBLOB);
        columnTypeMap.put("tinyint", MysqlColumnType.TINYINT);
        columnTypeMap.put("tinyint unsigned", MysqlColumnType.TINYINT_UNSIGNED);
        columnTypeMap.put("tinytext", MysqlColumnType.TINYTEXT);
        columnTypeMap.put("text", MysqlColumnType.TEXT);
        columnTypeMap.put("year", MysqlColumnType.YEAR);

        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "triger"));
        sqlBlockStartWordsList.add(Arrays.asList("alter", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("alter", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("alter", "triger"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "not null auto_increment";
    }

    @Override
    public String getDropForeignKeySyntax() {
        return "drop foreign key";
    }

    @Override
    public String getDropUniqueKeySyntax() {
        return "drop index";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == TABLE_NOT_FOUND_ERROR_CODE;
    }

    /**
     * MySQL用の{@link SqlType}の実装です。
     * 
     * @author taedium
     */
    public static class MysqlSqlType extends StandardSqlType {

        private static MysqlSqlType BINARY = new MysqlSqlType("binary($l)");

        private static MysqlSqlType BLOB = new MysqlSqlType() {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, boolean identity) {
                if (length <= 0xFF) {
                    return "tinyblob";
                } else if (length <= 0xFFFF) {
                    return "blob";
                } else if (length <= 0xFFFFFF) {
                    return "mediumblob";
                }
                return "longblob";
            }
        };

        private static MysqlSqlType CLOB = new MysqlSqlType() {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, boolean identity) {
                if (length <= 0xFF) {
                    return "tinytext";
                } else if (length <= 0xFFFF) {
                    return "text";
                } else if (length <= 0xFFFFFF) {
                    return "mediumtext";
                }
                return "longtext";
            }
        };

        private static MysqlSqlType DECIMAL = new MysqlSqlType("decimal($p,$s)");

        private static MysqlSqlType DOUBLE = new MysqlSqlType("double($p,$s)");

        private static MysqlSqlType FLOAT = new MysqlSqlType("float($p,$s)");

        private static MysqlSqlType INTEGER = new MysqlSqlType("int");

        /**
         * インスタンスを構築します。
         */
        protected MysqlSqlType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         */
        protected MysqlSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    /**
     * MySQL用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class MysqlColumnType extends StandardColumnType {

        private static MysqlColumnType BIGINT_UNSIGNED = new MysqlColumnType(
                "bigint unsigned", BigInteger.class);

        private static MysqlColumnType BINARY = new MysqlColumnType(
                "binary($l)", byte[].class);

        private static MysqlColumnType BIT = new MysqlColumnType("bit($l)",
                byte[].class) {

            @Override
            public Class<?> getAttributeClass(int length, int precision,
                    int scale) {
                return length == 0 ? Boolean.class : super.getAttributeClass(
                        length, precision, scale);
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale) {
                return length == 0 ? "bool" : super.getColumnDefinition(length,
                        precision, scale);
            }

        };

        private static MysqlColumnType BLOB = new MysqlColumnType("blob",
                byte[].class, true);

        private static MysqlColumnType DATETIME = new MysqlColumnType(
                "datetime", Date.class, TemporalType.TIMESTAMP);

        private static MysqlColumnType DECIMAL = new MysqlColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static MysqlColumnType DOUBLE = new MysqlColumnType(
                "double($p,$s)", Double.class);

        private static MysqlColumnType INT = new MysqlColumnType("int",
                Integer.class);

        private static MysqlColumnType INT_UNSIGNED = new MysqlColumnType(
                "int unsigned", Long.class);

        private static MysqlColumnType LONGBLOB = new MysqlColumnType(
                "longblob", byte[].class, true);

        private static MysqlColumnType LONGTEXT = new MysqlColumnType(
                "longtext", String.class, true);

        private static MysqlColumnType MEDIUMBLOB = new MysqlColumnType(
                "mediumblob", byte[].class, true);

        private static MysqlColumnType MEDIUMINT = new MysqlColumnType(
                "mediumint", Integer.class);

        private static MysqlColumnType MEDIUMINT_UNSIGNED = new MysqlColumnType(
                "mediumint unsigned", Integer.class);

        private static MysqlColumnType MEDIUMTEXT = new MysqlColumnType(
                "mediumtext", String.class, true);

        private static MysqlColumnType SMALLINT_UNSIGNED = new MysqlColumnType(
                "smallint unsigned", Integer.class);

        private static MysqlColumnType TINYBLOB = new MysqlColumnType(
                "tinyblob", byte[].class, true);

        private static MysqlColumnType TINYINT = new MysqlColumnType("tinyint",
                Byte.class);

        private static MysqlColumnType TINYINT_UNSIGNED = new MysqlColumnType(
                "tinyint unsigned", Short.class);

        private static MysqlColumnType TINYTEXT = new MysqlColumnType(
                "tinytext", String.class, true);

        private static MysqlColumnType TEXT = new MysqlColumnType("text",
                String.class, true);

        private static MysqlColumnType YEAR = new MysqlColumnType("year",
                Date.class, TemporalType.DATE);

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         */
        public MysqlColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public MysqlColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param temporalType
         *            時制型
         */
        public MysqlColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }
}
