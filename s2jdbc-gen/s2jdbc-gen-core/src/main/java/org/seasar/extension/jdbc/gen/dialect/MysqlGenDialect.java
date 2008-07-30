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
        typeMap.put(Types.BINARY, MysqlSqlType.BINARY);
        typeMap.put(Types.BLOB, MysqlSqlType.BLOB);
        typeMap.put(Types.CLOB, MysqlSqlType.CLOB);
        typeMap.put(Types.DECIMAL, MysqlSqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, MysqlSqlType.DOUBLE);
        typeMap.put(Types.FLOAT, MysqlSqlType.FLOAT);
        typeMap.put(Types.INTEGER, MysqlSqlType.INTEGER);

        namedTypeMap.put("bigint unsigned", MysqlColumnType.BIGINT_UNSIGNED);
        namedTypeMap.put("binary", MysqlColumnType.BINARY);
        namedTypeMap.put("bit", MysqlColumnType.BIT);
        namedTypeMap.put("blob", MysqlColumnType.BLOB);
        namedTypeMap.put("datetime", MysqlColumnType.DATETIME);
        namedTypeMap.put("decimal", MysqlColumnType.DECIMAL);
        namedTypeMap.put("double", MysqlColumnType.DOUBLE);
        namedTypeMap.put("int", MysqlColumnType.INT);
        namedTypeMap.put("int unsigned", MysqlColumnType.INT_UNSIGNED);
        namedTypeMap.put("longblob", MysqlColumnType.LONGBLOB);
        namedTypeMap.put("longtext", MysqlColumnType.LONGTEXT);
        namedTypeMap.put("mediumblob", MysqlColumnType.MEDIUMBLOB);
        namedTypeMap.put("mediumint", MysqlColumnType.MEDIUMINT);
        namedTypeMap.put("mediumint unsigned",
                MysqlColumnType.MEDIUMINT_UNSIGNED);
        namedTypeMap.put("mediumtext", MysqlColumnType.MEDIUMTEXT);
        namedTypeMap
                .put("smallint unsigned", MysqlColumnType.SMALLINT_UNSIGNED);
        namedTypeMap.put("tinyblob", MysqlColumnType.TINYBLOB);
        namedTypeMap.put("tinyint", MysqlColumnType.TINYINT);
        namedTypeMap.put("tinyint unsigned", MysqlColumnType.TINYINT_UNSIGNED);
        namedTypeMap.put("tinytext", MysqlColumnType.TINYTEXT);
        namedTypeMap.put("text", MysqlColumnType.TEXT);
        namedTypeMap.put("year", MysqlColumnType.YEAR);
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

        protected MysqlSqlType() {
        }

        /**
         * インスタンスを構築します。
         */
        protected MysqlSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

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

        public MysqlColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        public MysqlColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        public MysqlColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }
}
