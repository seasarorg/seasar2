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
 * Derbyの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class DerbyGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すSQLステート */
    protected static String TABLE_NOT_FOUND_SQL_STATE = "42X05";

    /**
     * インスタンスを構築します。
     */
    public DerbyGenDialect() {
        typeMap.put(Types.BINARY, DerbyType.BINARY);
        typeMap.put(Types.BOOLEAN, DerbyType.BOOLEAN);
        typeMap.put(Types.BLOB, DerbyType.BLOB);
        typeMap.put(Types.CLOB, DerbyType.CLOB);
        typeMap.put(Types.DECIMAL, DerbyType.DECIMAL);
        typeMap.put(Types.TINYINT, DerbyType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "not null generated always as identity";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        String sqlState = getSQLState(throwable);
        return TABLE_NOT_FOUND_SQL_STATE.equals(sqlState);
    }

    /**
     * Derby用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class DerbyType extends StandardType {

        private static Type BINARY = new DerbyType() {

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

        private static Type BOOLEAN = new DerbyType() {

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

        private static Type BLOB = new DerbyType() {

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

        private static Type CLOB = new DerbyType() {

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

        private static Type DECIMAL = new DerbyType() {

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

        private static Type TINYINT = new DerbyType() {

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
        protected DerbyType() {
        }

    }
}
