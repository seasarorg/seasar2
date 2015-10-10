/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import javax.persistence.GenerationType;

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
import org.seasar.framework.util.StringUtil;

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
    public String getName() {
        return "mssql";
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
    public ColumnType getColumnType(String typeName, int sqlType) {
        ColumnType columnType = columnTypeMap.get(typeName);
        if (columnType != null) {
            return columnType;
        }

        if (StringUtil.startsWithIgnoreCase(typeName, "int")) {
            typeName = "int";
        } else if (StringUtil.startsWithIgnoreCase(typeName, "bigint")) {
            typeName = "bigint";
        } else if (StringUtil.startsWithIgnoreCase(typeName, "smallint")) {
            typeName = "smallint";
        } else if (StringUtil.startsWithIgnoreCase(typeName, "tinyint")) {
            typeName = "tinyint";
        } else if (StringUtil.startsWithIgnoreCase(typeName, "decimal")) {
            typeName = "decimal";
        } else if (StringUtil.startsWithIgnoreCase(typeName, "numeric")) {
            typeName = "numeric";
        }
        return super.getColumnType(typeName, sqlType);
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
                "datetime", Timestamp.class);

        private static MssqlColumnType DECIMAL = new MssqlColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static MssqlColumnType IMAGE = new MssqlColumnType("image",
                byte[].class, true);

        private static MssqlColumnType INT = new MssqlColumnType("int",
                Integer.class);

        private static MssqlColumnType MONEY = new MssqlColumnType("money",
                BigDecimal.class);

        private static MssqlColumnType NCHAR = new MssqlColumnType("nchar($l)",
                String.class);

        private static MssqlColumnType NTEXT = new MssqlColumnType("ntext",
                String.class);

        private static MssqlColumnType NUMERIC = new MssqlColumnType(
                "numeric($p,$s)", BigDecimal.class);

        private static MssqlColumnType NVARCHAR = new MssqlColumnType(
                "nvarchar($l)", String.class);

        private static MssqlColumnType SMALLDATETIME = new MssqlColumnType(
                "smalldatetime", Timestamp.class);

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
