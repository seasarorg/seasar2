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

import org.seasar.extension.jdbc.gen.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.sqltype.DateType;
import org.seasar.extension.jdbc.gen.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.sqltype.TimestampType;

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
        sqlTypeMap.put(Types.BINARY, new BinaryType("varbinary($l)"));
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType("bit"));
        sqlTypeMap.put(Types.BLOB, new BlobType("image"));
        sqlTypeMap.put(Types.CLOB, new ClobType("text"));
        sqlTypeMap.put(Types.DATE, new DateType("datetime"));
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("decimal($p,$s)"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("double precision"));
        sqlTypeMap.put(Types.INTEGER, new IntegerType("int"));
        sqlTypeMap.put(Types.TIME, new TimeType("datetime"));
        sqlTypeMap.put(Types.TIMESTAMP, new TimestampType("datetime"));

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

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new MssqlSqlBlockContext();
    }

    @Override
    public boolean supportsIdentityInsert() {
        return true;
    }

    @Override
    public String getIdentityInsertOnStatement(String tableName) {
        return "set identity_insert " + tableName + " on";
    }

    @Override
    public String getIdentityInsertOffStatement(String tableName) {
        return "set identity_insert " + tableName + " off";
    }

    /**
     * MS SQL Server用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
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

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         */
        public MssqlColumnType(String columnDefinition, Class<?> attributeClass) {
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
        public MssqlColumnType(String columnDefinition,
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
        public MssqlColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }

    /**
     * MS SQL Server用の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     * 
     */
    public static class MssqlSqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected MssqlSqlBlockContext() {
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
