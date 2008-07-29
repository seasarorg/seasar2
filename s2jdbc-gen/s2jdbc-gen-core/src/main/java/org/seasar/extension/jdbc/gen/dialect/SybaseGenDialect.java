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
 * Sybaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class SybaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public SybaseGenDialect() {
        typeMap.put(Types.BINARY, SybaseSqlType.BINARY);
        typeMap.put(Types.BOOLEAN, SybaseSqlType.BOOLEAN);
        typeMap.put(Types.BLOB, SybaseSqlType.BLOB);
        typeMap.put(Types.CLOB, SybaseSqlType.CLOB);
        typeMap.put(Types.DATE, SybaseSqlType.DATE);
        typeMap.put(Types.DECIMAL, SybaseSqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, SybaseSqlType.DOUBLE);
        typeMap.put(Types.INTEGER, SybaseSqlType.INTEGER);
        typeMap.put(Types.TIME, SybaseSqlType.TIME);
        typeMap.put(Types.TIMESTAMP, SybaseSqlType.TIMESTAMP);

        namedTypeMap.put("binary", SybaseColumnType.BINARY);
        namedTypeMap.put("bit", SybaseColumnType.BIT);
        namedTypeMap.put("datetime", SybaseColumnType.DATETIME);
        namedTypeMap.put("decimal", SybaseColumnType.DECIMAL);
        namedTypeMap.put("image", SybaseColumnType.IMAGE);
        namedTypeMap.put("int", SybaseColumnType.INT);
        namedTypeMap.put("money", SybaseColumnType.MONEY);
        namedTypeMap.put("nchar", SybaseColumnType.NCHAR);
        namedTypeMap.put("ntext", SybaseColumnType.NTEXT);
        namedTypeMap.put("numeric", SybaseColumnType.NUMERIC);
        namedTypeMap.put("nvarchar", SybaseColumnType.NVARCHAR);
        namedTypeMap.put("smalldatetime", SybaseColumnType.SMALLDATETIME);
        namedTypeMap.put("smallmoney", SybaseColumnType.SMALLMONEY);
        namedTypeMap.put("text", SybaseColumnType.TEXT);
        namedTypeMap.put("varbinary", SybaseColumnType.VARBINARY);
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
     * Sybase用の{@link SqlType}の実装です。
     * 
     * @author taedium
     */
    public static class SybaseSqlType extends StandardSqlType {

        private static SybaseSqlType BINARY = new SybaseSqlType("varbinary($l)");

        private static SybaseSqlType BOOLEAN = new SybaseSqlType("bit");

        private static SybaseSqlType BLOB = new SybaseSqlType("image");

        private static SybaseSqlType CLOB = new SybaseSqlType("text");

        private static SybaseSqlType DATE = new SybaseSqlType("datetime");

        private static SybaseSqlType DECIMAL = new SybaseSqlType("decimal($p,$s)");

        private static SybaseSqlType DOUBLE = new SybaseSqlType("double precision");

        private static SybaseSqlType INTEGER = new SybaseSqlType("int");

        private static SybaseSqlType TIME = new SybaseSqlType("datetime");

        private static SybaseSqlType TIMESTAMP = new SybaseSqlType("datetime");

        /**
         * インスタンスを構築します。
         */
        protected SybaseSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    public static class SybaseColumnType extends StandardColumnType {

        private static SybaseColumnType BINARY = new SybaseColumnType(
                "binary($l)", byte[].class);

        private static SybaseColumnType BIT = new SybaseColumnType("bit",
                Boolean.class);

        private static SybaseColumnType DATETIME = new SybaseColumnType(
                "datetime", Date.class, TemporalType.TIMESTAMP);

        private static SybaseColumnType DECIMAL = new SybaseColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static SybaseColumnType IMAGE = new SybaseColumnType("image",
                byte[].class, true);

        private static SybaseColumnType INT = new SybaseColumnType("int",
                Integer.class);

        private static SybaseColumnType MONEY = new SybaseColumnType("money",
                BigDecimal.class);

        private static SybaseColumnType NCHAR = new SybaseColumnType(
                "nchar($l)", BigDecimal.class);

        private static SybaseColumnType NTEXT = new SybaseColumnType("ntext",
                BigDecimal.class);

        private static SybaseColumnType NUMERIC = new SybaseColumnType(
                "numeric($p,$s)", BigDecimal.class);

        private static SybaseColumnType NVARCHAR = new SybaseColumnType(
                "nvarchar($l)", String.class);

        private static SybaseColumnType SMALLDATETIME = new SybaseColumnType(
                "smalldatetime", Date.class, TemporalType.TIMESTAMP);

        private static SybaseColumnType SMALLMONEY = new SybaseColumnType(
                "smallmoney", BigDecimal.class);

        private static SybaseColumnType TEXT = new SybaseColumnType("text",
                String.class);

        private static SybaseColumnType VARBINARY = new SybaseColumnType(
                "varbinary($l)", byte[].class);

        public SybaseColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        public SybaseColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        public SybaseColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }
}
