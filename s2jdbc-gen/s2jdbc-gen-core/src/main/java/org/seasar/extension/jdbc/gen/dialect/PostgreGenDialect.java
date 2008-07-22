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

import javax.persistence.GenerationType;

/**
 * PostgreSQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class PostgreGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public PostgreGenDialect() {
        typeMap.put(Types.BINARY, PostgreType.BINARY);
        typeMap.put(Types.BLOB, PostgreType.BLOB);
        typeMap.put(Types.CLOB, PostgreType.CLOB);
        typeMap.put(Types.DECIMAL, PostgreType.DECIMAL);
        typeMap.put(Types.DOUBLE, PostgreType.DOUBLE);
        typeMap.put(Types.FLOAT, PostgreType.FLOAT);
        typeMap.put(Types.TINYINT, PostgreType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return dataType + " start with " + allocationSize + " increment by "
                + initValue;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "not null";
    }

    /**
     * PostgreSQL用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class PostgreType extends StandardType {

        private static Type BINARY = new PostgreType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "bytea";
            }
        };

        private static Type BLOB = new PostgreType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "oid";
            }
        };

        private static Type CLOB = new PostgreType() {

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

        private static Type DECIMAL = new PostgreType() {

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

        private static Type DOUBLE = new PostgreType() {

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

        private static Type FLOAT = new PostgreType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "real";
            }
        };

        private static Type TINYINT = new PostgreType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Double.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "smallint";
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected PostgreType() {
        }
    }
}
