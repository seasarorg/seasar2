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
import javax.persistence.TemporalType;

/**
 * PostgreSQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class PostgreGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すSQLステート */
    protected static String TABLE_NOT_FOUND_SQL_STATE = "42P01";

    /**
     * インスタンスを構築します。
     */
    public PostgreGenDialect() {
        typeMap.put(Types.BIGINT, PostgreSqlType.BIGINT);
        typeMap.put(Types.BINARY, PostgreSqlType.BINARY);
        typeMap.put(Types.BOOLEAN, PostgreSqlType.BOOLEAN);
        typeMap.put(Types.BLOB, PostgreSqlType.BLOB);
        typeMap.put(Types.CLOB, PostgreSqlType.CLOB);
        typeMap.put(Types.DECIMAL, PostgreSqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, PostgreSqlType.DOUBLE);
        typeMap.put(Types.FLOAT, PostgreSqlType.FLOAT);
        typeMap.put(Types.INTEGER, PostgreSqlType.INTEGER);

        namedTypeMap.put("bigint", PostgreColumnType.BIGINT);
        namedTypeMap.put("bigserial", PostgreColumnType.BIGSERIAL);
        namedTypeMap.put("bit", PostgreColumnType.BIT);
        namedTypeMap.put("bool", PostgreColumnType.BOOL);
        namedTypeMap.put("bpchar", PostgreColumnType.BPCHAR);
        namedTypeMap.put("bytea", PostgreColumnType.BYTEA);
        namedTypeMap.put("float4", PostgreColumnType.FLOAT4);
        namedTypeMap.put("float8", PostgreColumnType.FLOAT8);
        namedTypeMap.put("int2", PostgreColumnType.INT2);
        namedTypeMap.put("int4", PostgreColumnType.INT4);
        namedTypeMap.put("int8", PostgreColumnType.INT8);
        namedTypeMap.put("money", PostgreColumnType.MONEY);
        namedTypeMap.put("numeric", PostgreColumnType.NUMERIC);
        namedTypeMap.put("serial", PostgreColumnType.SERIAL);
        namedTypeMap.put("text", PostgreColumnType.TEXT);
        namedTypeMap.put("timestamptz", PostgreColumnType.TIMESTAMPTZ);
        namedTypeMap.put("timetz", PostgreColumnType.TIMETZ);
        namedTypeMap.put("varbit", PostgreColumnType.VARBIT);
        namedTypeMap.put("varchar", PostgreColumnType.VARCHAR);
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return null;
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

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        String sqlState = getSQLState(throwable);
        return TABLE_NOT_FOUND_SQL_STATE.equals(sqlState);
    }

    /**
     * PostgreSQL用の{@link SqlType}の実装です。
     * 
     * @author taedium
     */
    public static class PostgreSqlType extends StandardSqlType {

        private static PostgreSqlType BIGINT = new PostgreSqlType() {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, boolean identity) {
                return identity ? "bigserial" : "bigint";
            }
        };

        private static PostgreSqlType BINARY = new PostgreSqlType("bytea");

        private static PostgreSqlType BOOLEAN = new PostgreSqlType("bool");

        private static PostgreSqlType BLOB = new PostgreSqlType("oid");

        private static PostgreSqlType CLOB = new PostgreSqlType("text");

        private static PostgreSqlType DECIMAL = new PostgreSqlType(
                "decimal($p,$s)");

        private static PostgreSqlType DOUBLE = new PostgreSqlType("float8");

        private static PostgreSqlType FLOAT = new PostgreSqlType("float4");

        private static PostgreSqlType INTEGER = new PostgreSqlType() {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, boolean identity) {
                return identity ? "serial" : "integer";
            }

        };

        protected PostgreSqlType() {
        }

        /**
         * インスタンスを構築します。
         */
        protected PostgreSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    /**
     * @author taedium
     * 
     */
    public static class PostgreColumnType extends StandardColumnType {

        private static PostgreColumnType BIT = new PostgreColumnType("bit($l)",
                byte[].class);

        private static PostgreColumnType BOOL = new PostgreColumnType("bool",
                Boolean.class);

        private static PostgreColumnType INT8 = new PostgreColumnType("int8",
                Long.class);

        private static PostgreColumnType BIGSERIAL = new PostgreColumnType(
                "bigserial", Long.class);

        private static PostgreColumnType BIGINT = new PostgreColumnType("oid",
                byte[].class, true);

        private static PostgreColumnType BYTEA = new PostgreColumnType("bytea",
                byte[].class);

        private static PostgreColumnType BPCHAR = new PostgreColumnType(
                "char($l)", String.class);

        private static PostgreColumnType NUMERIC = new PostgreColumnType(
                "decimal($p,$s)", BigDecimal.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale) {
                precision = precision > 1000 ? 1000 : precision;
                return super.getColumnDefinition(length, precision, scale);
            }

        };

        private static PostgreColumnType INT4 = new PostgreColumnType("int4",
                Integer.class);

        private static PostgreColumnType SERIAL = new PostgreColumnType(
                "serial", Integer.class);

        private static PostgreColumnType INT2 = new PostgreColumnType("int2",
                Short.class);

        private static PostgreColumnType FLOAT4 = new PostgreColumnType(
                "float4", Float.class);

        private static PostgreColumnType FLOAT8 = new PostgreColumnType(
                "float8", Double.class);

        private static PostgreColumnType MONEY = new PostgreColumnType("money",
                Float.class);

        private static PostgreColumnType TEXT = new PostgreColumnType("text",
                String.class, true);

        private static PostgreColumnType TIMETZ = new PostgreColumnType(
                "timetz", Date.class, TemporalType.TIME);

        private static PostgreColumnType TIMESTAMPTZ = new PostgreColumnType(
                "timestamptz", Date.class, TemporalType.TIMESTAMP);

        private static PostgreColumnType VARBIT = new PostgreColumnType(
                "varbit", byte[].class);

        private static PostgreColumnType VARCHAR = new PostgreColumnType(
                "varchar($l)", String.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale) {
                length = length > 10485760 ? 10485760 : length;
                return super.getColumnDefinition(length, precision, scale);
            }
        };

        public PostgreColumnType(String columnDefinition, Class javaClass) {
            super(columnDefinition, javaClass);
        }

        public PostgreColumnType(String columnDefinition, Class<?> javaClass,
                boolean lob) {
            super(columnDefinition, javaClass, lob, null);
        }

        public PostgreColumnType(String columnDefinition, Class<?> javaClass,
                TemporalType temporalType) {
            super(columnDefinition, javaClass, false, temporalType);
        }
    }
}
