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
 * DB2の方言を扱うクラスです。
 * 
 * @author taedium
 */
public class Db2GenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public Db2GenDialect() {
        typeMap.put(Types.BINARY, Db2Type.BINARY);
        typeMap.put(Types.BOOLEAN, Db2Type.BOOLEAN);
        typeMap.put(Types.BLOB, Db2Type.BLOB);
        typeMap.put(Types.CLOB, Db2Type.CLOB);
        typeMap.put(Types.DECIMAL, Db2Type.DECIMAL);
        typeMap.put(Types.TINYINT, Db2Type.TINYINT);
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
        return "as " + dataType + " start with " + allocationSize
                + " increment by " + initValue;
    }

    /**
     * DB2用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class Db2Type extends StandardType {

        private static Type BINARY = new Db2Type() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("varchar(%d) for bit data", length);
            }
        };

        private static Type BOOLEAN = new Db2Type() {

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

        private static Type BLOB = new Db2Type() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("blob(%d)", length);
            }
        };

        private static Type CLOB = new Db2Type() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("clob(%d)", length);
            }
        };

        private static Type DECIMAL = new Db2Type() {

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

        private static Type TINYINT = new Db2Type() {

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
        protected Db2Type() {
        }

    }
}
