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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;

/**
 * MySQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MysqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 1146;

    /** カラムが見つからないことを示すエラーコード */
    protected static int COLUMN_NOT_FOUND_ERROR_CODE = 1054;

    /**
     * インスタンスを構築します。
     */
    public MysqlGenDialect() {
        sqlTypeMap.put(Types.BINARY, new BinaryType("binary($l)"));
        sqlTypeMap.put(Types.BLOB, new BlobType() {

            @Override
            public String getDataType(int length, int precision, int scale,
                    boolean identity) {
                if (length <= 0xFF) {
                    return "tinyblob";
                } else if (length <= 0xFFFF) {
                    return "blob";
                } else if (length <= 0xFFFFFF) {
                    return "mediumblob";
                }
                return "longblob";
            }
        });
        sqlTypeMap.put(Types.CLOB, new ClobType() {

            @Override
            public String getDataType(int length, int precision, int scale,
                    boolean identity) {
                if (length <= 0xFF) {
                    return "tinytext";
                } else if (length <= 0xFFFF) {
                    return "text";
                } else if (length <= 0xFFFFFF) {
                    return "mediumtext";
                }
                return "longtext";
            }
        });
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("decimal($p,$s)"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("double($p,$s)"));
        sqlTypeMap.put(Types.FLOAT, new FloatType("float($p,$s)"));
        sqlTypeMap.put(Types.INTEGER, new IntegerType("int"));

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
    }

    @Override
    public String getName() {
        return "mysql";
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

    @Override
    public boolean isColumnNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == COLUMN_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new MysqlSqlBlockContext();
    }

    @Override
    public boolean supportsIdentityInsert() {
        return true;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsCommentInCreateTable() {
        return true;
    }

    @Override
    public boolean supportsCommentOn() {
        return false;
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
                    int scale, String defaultValue) {
                if (length == 0) {
                    return getColumnDefinitionInternal("bool", defaultValue);
                }
                return super.getColumnDefinition(length, precision, scale,
                        defaultValue);
            }

        };

        private static MysqlColumnType BLOB = new MysqlColumnType("blob",
                byte[].class, true);

        private static MysqlColumnType DATETIME = new MysqlColumnType(
                "datetime", Timestamp.class);

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
                java.sql.Date.class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public MysqlColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public MysqlColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

    }

    /**
     * MySQL用の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class MysqlSqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected MysqlSqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }
}
