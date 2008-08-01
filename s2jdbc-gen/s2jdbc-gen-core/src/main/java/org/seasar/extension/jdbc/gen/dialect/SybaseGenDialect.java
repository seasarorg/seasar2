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
        sqlTypeMap.put(Types.BINARY, SybaseSqlType.BINARY);
        sqlTypeMap.put(Types.BOOLEAN, SybaseSqlType.BOOLEAN);
        sqlTypeMap.put(Types.BLOB, SybaseSqlType.BLOB);
        sqlTypeMap.put(Types.CLOB, SybaseSqlType.CLOB);
        sqlTypeMap.put(Types.DATE, SybaseSqlType.DATE);
        sqlTypeMap.put(Types.DECIMAL, SybaseSqlType.DECIMAL);
        sqlTypeMap.put(Types.DOUBLE, SybaseSqlType.DOUBLE);
        sqlTypeMap.put(Types.INTEGER, SybaseSqlType.INTEGER);
        sqlTypeMap.put(Types.TIME, SybaseSqlType.TIME);
        sqlTypeMap.put(Types.TIMESTAMP, SybaseSqlType.TIMESTAMP);

        columnTypeMap.put("binary", SybaseColumnType.BINARY);
        columnTypeMap.put("bit", SybaseColumnType.BIT);
        columnTypeMap.put("datetime", SybaseColumnType.DATETIME);
        columnTypeMap.put("decimal", SybaseColumnType.DECIMAL);
        columnTypeMap.put("image", SybaseColumnType.IMAGE);
        columnTypeMap.put("int", SybaseColumnType.INT);
        columnTypeMap.put("money", SybaseColumnType.MONEY);
        columnTypeMap.put("nchar", SybaseColumnType.NCHAR);
        columnTypeMap.put("ntext", SybaseColumnType.NTEXT);
        columnTypeMap.put("numeric", SybaseColumnType.NUMERIC);
        columnTypeMap.put("nvarchar", SybaseColumnType.NVARCHAR);
        columnTypeMap.put("smalldatetime", SybaseColumnType.SMALLDATETIME);
        columnTypeMap.put("smallmoney", SybaseColumnType.SMALLMONEY);
        columnTypeMap.put("text", SybaseColumnType.TEXT);
        columnTypeMap.put("varbinary", SybaseColumnType.VARBINARY);
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

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new SybaseSqlBlockContext();
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

        private static SybaseSqlType DECIMAL = new SybaseSqlType(
                "decimal($p,$s)");

        private static SybaseSqlType DOUBLE = new SybaseSqlType(
                "double precision");

        private static SybaseSqlType INTEGER = new SybaseSqlType("int");

        private static SybaseSqlType TIME = new SybaseSqlType("datetime");

        private static SybaseSqlType TIMESTAMP = new SybaseSqlType("datetime");

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         */
        protected SybaseSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    /**
     * Sybase用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
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

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性クラス
         */
        public SybaseColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public SybaseColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param temporalType
         *            時制型
         */
        public SybaseColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }

    /**
     * Sybase用の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class SybaseSqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected SybaseSqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "triger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "triger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }
}
