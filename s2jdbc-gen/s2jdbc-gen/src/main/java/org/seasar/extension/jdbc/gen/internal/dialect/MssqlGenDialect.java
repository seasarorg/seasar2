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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DateType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimestampType;

/**
 * MS SQL Serverの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MssqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static List<Integer> TABLE_NOT_FOUND_ERROR_CODES = Arrays.asList(
            208, 1088);

    /** カラムが見つからないことを示すエラーコード */
    protected static int COLUMN_NOT_FOUND_ERROR_CODE = 207;

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

        columnTypeByNameMap.put("binary", MssqlColumnType.BINARY);
        columnTypeByNameMap.put("bit", MssqlColumnType.BIT);
        columnTypeByNameMap.put("datetime", MssqlColumnType.DATETIME);
        columnTypeByNameMap.put("decimal", MssqlColumnType.DECIMAL);
        columnTypeByNameMap.put("image", MssqlColumnType.IMAGE);
        columnTypeByNameMap.put("int", MssqlColumnType.INT);
        columnTypeByNameMap.put("money", MssqlColumnType.MONEY);
        columnTypeByNameMap.put("nchar", MssqlColumnType.NCHAR);
        columnTypeByNameMap.put("ntext", MssqlColumnType.NTEXT);
        columnTypeByNameMap.put("numeric", MssqlColumnType.NUMERIC);
        columnTypeByNameMap.put("nvarchar", MssqlColumnType.NVARCHAR);
        columnTypeByNameMap.put("smalldatetime", MssqlColumnType.SMALLDATETIME);
        columnTypeByNameMap.put("smallmoney", MssqlColumnType.SMALLMONEY);
        columnTypeByNameMap.put("text", MssqlColumnType.TEXT);
        columnTypeByNameMap.put("varbinary", MssqlColumnType.VARBINARY);
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
        return TABLE_NOT_FOUND_ERROR_CODES.contains(errorCode);
    }

    @Override
    public boolean isColumnNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == COLUMN_NOT_FOUND_ERROR_CODE;
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
    public boolean supportsIdentityInsertControlStatement() {
        return true;
    }

    @Override
    public String getIdentityInsertEnableStatement(String tableName) {
        return "set identity_insert " + tableName + " on";
    }

    @Override
    public String getIdentityInsertDisableStatement(String tableName) {
        return "set identity_insert " + tableName + " off";
    }

    @Override
    public boolean supportsIdentity() {
        return true;
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
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public MssqlColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public MssqlColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param temporalType
         *            時制型
         */
        public MssqlColumnType(String dataType, Class<?> attributeClass,
                TemporalType temporalType) {
            super(dataType, attributeClass, temporalType);
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
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }
}
