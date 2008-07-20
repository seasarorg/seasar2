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
 * Interbaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class InterbaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public InterbaseGenDialect() {
        typeMap.put(Types.BIGINT, InterbaseType.BIGINT);
        typeMap.put(Types.BINARY, InterbaseType.BINARY);
        typeMap.put(Types.BOOLEAN, InterbaseType.BOOLEAN);
        typeMap.put(Types.CLOB, InterbaseType.CLOB);
        typeMap.put(Types.DECIMAL, InterbaseType.DECIMAL);
        typeMap.put(Types.DOUBLE, InterbaseType.DOUBLE);
        typeMap.put(Types.TINYINT, InterbaseType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    /**
     * Interbase用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class InterbaseType extends StandardType {

        private static Type BIGINT = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Long.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("numeric(%d,0)", precision);
            }
        };

        private static Type BINARY = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "blob";
            }
        };

        private static Type BOOLEAN = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Boolean.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "smallint";
            }
        };

        private static Type CLOB = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "blob sub_type 1";
            }
        };

        private static Type DECIMAL = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return BigDecimal.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("number(%d,%d)", precision, scale);
            }
        };

        private static Type DOUBLE = new InterbaseType() {

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

        private static Type TINYINT = new InterbaseType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return Short.class;
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
        protected InterbaseType() {
        }

    }
}
