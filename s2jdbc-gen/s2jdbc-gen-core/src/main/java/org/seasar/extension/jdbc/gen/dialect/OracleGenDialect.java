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
 * Oracleの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class OracleGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public OracleGenDialect() {
        typeMap.put(101, OracleType.BINARY_DOUBLE);
        typeMap.put(100, OracleType.BINARY_FLOAT);
        typeMap.put(Types.BINARY, OracleType.BINARY);
        typeMap.put(Types.BOOLEAN, OracleType.BOOLEAN);
        typeMap.put(Types.BIGINT, OracleType.BIGINT);
        typeMap.put(Types.CHAR, OracleType.CHAR);
        typeMap.put(Types.DECIMAL, OracleType.DECIMAL);
        typeMap.put(Types.DOUBLE, OracleType.DOUBLE);
        typeMap.put(Types.INTEGER, OracleType.INTEGER);
        typeMap.put(Types.LONGVARBINARY, OracleType.LONGVARBINARY);
        typeMap.put(Types.LONGVARCHAR, OracleType.LONGVARCHAR);
        typeMap.put(Types.OTHER, OracleType.OTHER);
        typeMap.put(Types.SMALLINT, OracleType.SMALLINT);
        typeMap.put(Types.TIME, OracleType.DATE);
        typeMap.put(Types.TIME, OracleType.TIME);
        typeMap.put(Types.TIME, OracleType.TIMESTAMP);
        typeMap.put(Types.TINYINT, OracleType.TINYINT);
        typeMap.put(Types.VARBINARY, OracleType.VARBINARY);
        typeMap.put(Types.VARCHAR, OracleType.VARCHAR);

        sqlBlockStartWordsList.add(Arrays.asList("create", "or", "replace",
                "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "or", "replace",
                "function"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public boolean isUserTable(String tableName) {
        return !tableName.contains("$");
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return "increment by " + allocationSize + " start with " + initValue;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    /**
     * Oracle用の{@link Type}の実装クラスです。
     * 
     * @author taedium
     */
    public static class OracleType extends StandardType {

        private static Type BINARY_DOUBLE = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "binary_double";
            }
        };

        private static Type BINARY_FLOAT = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "binary_float";
            }
        };

        private static Type BIGINT = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return BigDecimal.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("numeric(%d,0)", precision);
            }
        };

        private static Type BINARY = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (length > 2000) {
                    return "long raw";
                }
                return format("raw(%d)", length);
            }
        };

        private static Type BOOLEAN = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Boolean.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "number(1,0)";
            }
        };

        private static Type CHAR = new OracleType() {

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
                return format("char(%d)", length);
            }
        };

        private static Type DECIMAL = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (precision > 10 || scale > 0) {
                    return BigDecimal.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("number(%d,%d)", precision, scale);
            }
        };

        private static Type DOUBLE = new OracleType() {

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

        private static Type INTEGER = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Integer.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "number(10,0)";
            }
        };

        private static Type LONGVARCHAR = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "long";
            }
        };

        private static Type LONGVARBINARY = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "long raw";
            }
        };

        private static Type OTHER = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("nchar".equalsIgnoreCase(typeName)) {
                    return format("nchar(%d)", length / 2);
                } else if ("nvarchar2".equalsIgnoreCase(typeName)) {
                    return format("nvarchar2(%d)", length / 2);
                } else if ("nclob".equalsIgnoreCase(typeName)) {
                    return "nclob";
                } else if ("rowid".equalsIgnoreCase(typeName)) {
                    return "rowid";
                } else if ("urowid".equalsIgnoreCase(typeName)) {
                    return format("urowid(%d)", length);
                }

                return null;
            }
        };

        private static Type SMALLINT = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Short.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "number(5,0)";
            }
        };

        private static Type DATE = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "date";
            }
        };

        private static Type TIME = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "date";
            }
        };

        private static Type TIMESTAMP = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("date".equalsIgnoreCase(typeName)) {
                    return "date";
                }
                return "timestamp";
            }
        };

        private static Type TINYINT = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Short.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "number(3,0)";
            }
        };

        private static Type VARBINARY = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (length > 2000) {
                    return "long raw";
                }
                return format("raw(%d)", length);
            }
        };

        private static Type VARCHAR = new OracleType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (length > 4000) {
                    return "long";
                }
                return format("varchar2(%d)", length);
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected OracleType() {
        }

    }
}
