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
 * MS SQL Serverの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MssqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 208;

    /**
     * インスタンスを構築します。
     */
    public MssqlGenDialect() {
        typeMap.put(Types.BINARY, MssqlSqlType.BINARY);
        typeMap.put(Types.BOOLEAN, MssqlSqlType.BOOLEAN);
        typeMap.put(Types.BLOB, MssqlSqlType.BLOB);
        typeMap.put(Types.CLOB, MssqlSqlType.CLOB);
        typeMap.put(Types.DATE, MssqlSqlType.DATE);
        typeMap.put(Types.DECIMAL, MssqlSqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, MssqlSqlType.DOUBLE);
        typeMap.put(Types.INTEGER, MssqlSqlType.INTEGER);
        typeMap.put(Types.TIME, MssqlSqlType.TIME);
        typeMap.put(Types.TIMESTAMP, MssqlSqlType.TIMESTAMP);

        columnTypeMap.put("binary", MssqlColumnType.BINARY);
        columnTypeMap.put("bit", MssqlColumnType.BIT);
        columnTypeMap.put("datetime", MssqlColumnType.DATETIME);
        columnTypeMap.put("decimal", MssqlColumnType.DECIMAL);
        columnTypeMap.put("image", MssqlColumnType.IMAGE);
        columnTypeMap.put("int", MssqlColumnType.INT);
        columnTypeMap.put("money", MssqlColumnType.MONEY);
        columnTypeMap.put("nchar", MssqlColumnType.NCHAR);
        columnTypeMap.put("ntext", MssqlColumnType.NTEXT);
        columnTypeMap.put("numeric", MssqlColumnType.NUMERIC);
        columnTypeMap.put("nvarchar", MssqlColumnType.NVARCHAR);
        columnTypeMap.put("smalldatetime", MssqlColumnType.SMALLDATETIME);
        columnTypeMap.put("smallmoney", MssqlColumnType.SMALLMONEY);
        columnTypeMap.put("text", MssqlColumnType.TEXT);
        columnTypeMap.put("varbinary", MssqlColumnType.VARBINARY);

        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return "dbo";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getOpenQuote() {
        return "[";
    }

    @Override
    public String getCloseQuote() {
        return "]";
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
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == TABLE_NOT_FOUND_ERROR_CODE;
    }

    /**
     * MS SQL Server用の{@link SqlType}の実装です。
     * 
     * @author taedium
     */
    public static class MssqlSqlType extends StandardSqlType {

        private static MssqlSqlType BINARY = new MssqlSqlType("varbinary($l)");

        private static MssqlSqlType BOOLEAN = new MssqlSqlType("bit");

        private static MssqlSqlType BLOB = new MssqlSqlType("image");

        private static MssqlSqlType CLOB = new MssqlSqlType("text");

        private static MssqlSqlType DATE = new MssqlSqlType("datetime");

        private static MssqlSqlType DECIMAL = new MssqlSqlType("decimal($p,$s)");

        private static MssqlSqlType DOUBLE = new MssqlSqlType(
                "double precision");

        private static MssqlSqlType INTEGER = new MssqlSqlType("int");

        private static MssqlSqlType TIME = new MssqlSqlType("datetime");

        private static MssqlSqlType TIMESTAMP = new MssqlSqlType("datetime");

        /**
         * インスタンスを構築します。
         */
        protected MssqlSqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    public static class MssqlColumnType extends StandardColumnType {

        private static MssqlColumnType BINARY = new MssqlColumnType(
                "binary($l)", byte[].class);

        private static MssqlColumnType BIT = new MssqlColumnType("bit",
                Boolean.class);

        private static MssqlColumnType DATETIME = new MssqlColumnType(
                "datetime", Date.class, TemporalType.TIMESTAMP);

        private static MssqlColumnType DECIMAL = new MssqlColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static MssqlColumnType IMAGE = new MssqlColumnType("image",
                byte[].class, true);

        private static MssqlColumnType INT = new MssqlColumnType("int",
                Integer.class);

        private static MssqlColumnType MONEY = new MssqlColumnType("money",
                BigDecimal.class);

        private static MssqlColumnType NCHAR = new MssqlColumnType("nchar($l)",
                BigDecimal.class);

        private static MssqlColumnType NTEXT = new MssqlColumnType("ntext",
                BigDecimal.class);

        private static MssqlColumnType NUMERIC = new MssqlColumnType(
                "numeric($p,$s)", BigDecimal.class);

        private static MssqlColumnType NVARCHAR = new MssqlColumnType(
                "nvarchar($l)", String.class);

        private static MssqlColumnType SMALLDATETIME = new MssqlColumnType(
                "smalldatetime", Date.class, TemporalType.TIMESTAMP);

        private static MssqlColumnType SMALLMONEY = new MssqlColumnType(
                "smallmoney", BigDecimal.class);

        private static MssqlColumnType TEXT = new MssqlColumnType("text",
                String.class);

        private static MssqlColumnType VARBINARY = new MssqlColumnType(
                "varbinary($l)", byte[].class);

        public MssqlColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        public MssqlColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        public MssqlColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }
}
