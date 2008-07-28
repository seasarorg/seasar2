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

import org.seasar.framework.util.StringUtil;

/**
 * MySQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MysqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 208;

    /**
     * インスタンスを構築します。
     */
    public MysqlGenDialect() {
        typeMap.put(Types.BIGINT, MysqlType.BIGINT);
        typeMap.put(Types.BINARY, MysqlType.BINARY);
        typeMap.put(Types.BIT, MysqlType.BIT);
        typeMap.put(Types.BLOB, MysqlType.BLOB);
        typeMap.put(Types.CLOB, MysqlType.CLOB);
        typeMap.put(Types.DATE, MysqlType.DATE);
        typeMap.put(Types.DECIMAL, MysqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, MysqlType.DOUBLE);
        typeMap.put(Types.REAL, MysqlType.REAL);
        typeMap.put(Types.INTEGER, MysqlType.INTEGER);
        typeMap.put(Types.LONGVARBINARY, MysqlType.LONGVARBINARY);
        typeMap.put(Types.LONGVARCHAR, MysqlType.LONGVARCHAR);
        typeMap.put(Types.SMALLINT, MysqlType.SMALLINT);
        typeMap.put(Types.TINYINT, MysqlType.TINYINT);
        typeMap.put(Types.TIMESTAMP, MysqlType.TIMESTAMP);
        typeMap.put(Types.VARCHAR, MysqlType.VARCHAR);
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
     * MySQL用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class MysqlType extends StandardType {

        private static Type BINARY = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("binary".equalsIgnoreCase(typeName)) {
                    return format("binary(%d)", length);
                }
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

        private static Type BIGINT = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return BigInteger.class;
                }
                return Long.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return "bigint unsigned";
                }
                return "bigint";
            }
        };

        private static Type BIT = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (length > 1) {
                    return byte[].class;
                }
                return Boolean.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (length > 1) {
                    return format("bit(%d)", length);
                }
                return "bit";
            }
        };

        private static Type BLOB = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
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

        private static Type CLOB = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
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

        private static Type DATE = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("year".equalsIgnoreCase(typeName)) {
                    return "year";
                }
                return "date";
            }
        };

        private static Type DECIMAL = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return BigDecimal.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("decimal(%d,%d)", precision, scale);
            }
        };

        private static Type DOUBLE = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("double(%d,%d)", precision, scale);
            }
        };

        private static Type REAL = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Float.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("float(%d,%d)", precision, scale);
            }
        };

        private static Type INTEGER = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return Long.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return "int unsigned";
                }
                return "int";
            }
        };

        private static Type LONGVARBINARY = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
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

        private static Type LONGVARCHAR = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
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

        private static Type SMALLINT = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return Integer.class;
                }
                return Short.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if (StringUtil.endsWithIgnoreCase(typeName, "unsigned")) {
                    return "smallint unsigned";
                }
                return "smallint";
            }
        };

        private static Type TINYINT = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Short.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "tinyint";
            }
        };

        private static Type TIMESTAMP = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Date.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("datetime".equalsIgnoreCase(typeName)) {
                    return "datetime";
                }
                return "timestamp";
            }
        };

        private static Type VARCHAR = new MysqlType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                if ("tinytext".equalsIgnoreCase(typeName)) {
                    return "tinytext";
                }
                return format("varchar(%d)", length);
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected MysqlType() {
        }
    }
}
