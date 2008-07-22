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
import java.util.Date;

import javax.persistence.GenerationType;

/**
 * Sybaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class SybaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public SybaseGenDialect() {
        typeMap.put(Types.BINARY, SybaseType.BINARY);
        typeMap.put(Types.BOOLEAN, SybaseType.BOOLEAN);
        typeMap.put(Types.BLOB, SybaseType.BLOB);
        typeMap.put(Types.CLOB, SybaseType.CLOB);
        typeMap.put(Types.DECIMAL, SybaseType.DECIMAL);
        typeMap.put(Types.DOUBLE, SybaseType.DOUBLE);
        typeMap.put(Types.INTEGER, SybaseType.INTEGER);
        typeMap.put(Types.DATE, SybaseType.DATE);
        typeMap.put(Types.TIME, SybaseType.TIME);
        typeMap.put(Types.TIMESTAMP, SybaseType.TIMESTAMP);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "go";
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "identity not null";
    }

    /**
     * Sybase用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class SybaseType extends StandardType {

        private static Type BINARY = new SybaseType() {

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

        private static Type BOOLEAN = new SybaseType() {

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

        private static Type BLOB = new SybaseType() {

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

        private static Type CLOB = new SybaseType() {

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

        private static Type DECIMAL = new SybaseType() {

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

        private static Type DOUBLE = new SybaseType() {

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

        private static Type INTEGER = new SybaseType() {

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

        private static Type DATE = new SybaseType() {

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

        private static Type TIME = new SybaseType() {

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

        private static Type TIMESTAMP = new SybaseType() {

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

        /**
         * インスタンスを構築します。
         */
        protected SybaseType() {
        }

    }

}
