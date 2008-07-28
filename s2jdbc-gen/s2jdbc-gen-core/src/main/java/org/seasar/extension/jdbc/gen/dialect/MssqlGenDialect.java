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
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.GenerationType;

/**
 * MS SQL Serverの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MssqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 1051;

    /**
     * インスタンスを構築します。
     */
    public MssqlGenDialect() {
        typeMap.put(Types.BINARY, MssqlType.BINARY);
        typeMap.put(Types.BOOLEAN, MssqlType.BOOLEAN);
        typeMap.put(Types.BLOB, MssqlType.BLOB);
        typeMap.put(Types.CHAR, MssqlType.CHAR);
        typeMap.put(Types.CLOB, MssqlType.CLOB);
        typeMap.put(Types.DATE, MssqlType.DATE);
        typeMap.put(Types.DECIMAL, MssqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, MssqlType.DOUBLE);
        typeMap.put(Types.INTEGER, MssqlType.INTEGER);
        typeMap.put(Types.LONGVARBINARY, MssqlType.LONGVARBINARY);
        typeMap.put(Types.LONGVARCHAR, MssqlType.LONGVARCHAR);
        typeMap.put(Types.NUMERIC, MssqlType.NUMERIC);
        typeMap.put(Types.TIME, MssqlType.TIME);
        typeMap.put(Types.TIMESTAMP, MssqlType.TIMESTAMP);
        typeMap.put(Types.VARCHAR, MssqlType.VARCHAR);

        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return "dbo";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getOpenQuote() {
        return "[";
    }

    @Override
    public String getCloseQuote() {
        return "]";
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "go";
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "identity not null";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == TABLE_NOT_FOUND_ERROR_CODE;
    }

    /**
     * MS SQL Server用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class MssqlType extends StandardType {

        private static Type BINARY = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("varbinary(%d)", length);
            }
        };

        private static Type BOOLEAN = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Boolean.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "bit";
            }
        };

        private static Type BLOB = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "image";
            }
        };

        private static Type CHAR = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (length > 1) {
                    return String.class;
                }
                return Character.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("nchar".equalsIgnoreCase(typeName)) {
                    return format("nchar(%d)", length);
                }
                return format("char(%d)", length);
            }
        };

        private static Type CLOB = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "text";
            }
        };

        private static Type DATE = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "datetime";
            }
        };

        private static Type DECIMAL = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return BigDecimal.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("money".equalsIgnoreCase(typeName)) {
                    return "money";
                } else if ("smallmoney".equalsIgnoreCase(typeName)) {
                    return "smallmoney";
                }
                return format("decimal(%d,%d)", precision, scale);
            }
        };

        private static Type DOUBLE = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "double precision";
            }
        };

        private static Type INTEGER = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Integer.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "int";
            }
        };

        private static Type LONGVARBINARY = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "image";
            }
        };

        private static Type LONGVARCHAR = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("ntext".equalsIgnoreCase(typeName)) {
                    return "ntext";
                }
                return "text";
            }
        };

        private static Type NUMERIC = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return BigDecimal.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static Type TIME = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "datetime";
            }
        };

        private static Type TIMESTAMP = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("smalldatetime".equalsIgnoreCase(typeName)) {
                    return "smalldatetime";
                }
                return "datetime";
            }
        };

        private static Type VARCHAR = new MssqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("nvarchar".equalsIgnoreCase(typeName)) {
                    return format("nvarchar(%d)", length);
                }
                return format("varchar(%d)", length);
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected MssqlType() {
        }
    }
}
