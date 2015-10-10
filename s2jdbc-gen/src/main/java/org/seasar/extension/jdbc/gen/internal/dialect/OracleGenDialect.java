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
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BigIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.SmallIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimestampType;
import org.seasar.extension.jdbc.gen.internal.sqltype.VarcharType;
import org.seasar.extension.jdbc.gen.internal.util.CharUtil;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * Oracleの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class OracleGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 942;

    /** カラムが見つからないことを示すエラーコード */
    protected static int COLUMN_NOT_FOUND_ERROR_CODE = 904;

    /** シーケンスが見つからないことを示すエラーコード */
    protected static int SEQUENCE_NOT_FOUND_ERROR_CODE = 2289;

    /** Oracle固有の{@literal DATE}型を使用する場合は{@literal true} */
    protected boolean useOracleDate = true;

    /**
     * インスタンスを構築します。
     */
    public OracleGenDialect() {
        sqlTypeMap.put(Types.BINARY, new BinaryType("raw($l)"));
        sqlTypeMap.put(Types.BIGINT, new BigIntType("number($p,0)"));
        sqlTypeMap.put(Types.BLOB, new BlobType("blob"));
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType("number(1,0)"));
        sqlTypeMap.put(Types.CLOB, new ClobType("clob"));
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("number($p,$s)"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("double precision"));
        sqlTypeMap.put(Types.INTEGER, new IntegerType("number($p,0)"));
        sqlTypeMap.put(Types.SMALLINT, new SmallIntType("number($p,0)"));
        sqlTypeMap.put(Types.TIME, new TimeType("date"));
        sqlTypeMap.put(Types.VARCHAR, new VarcharType("varchar2($l)"));

        columnTypeMap.put("binary_double", OracleColumnType.BINARY_DOUBLE);
        columnTypeMap.put("binary_float", OracleColumnType.BINARY_FLOAT);
        columnTypeMap.put("blob", OracleColumnType.BLOB);
        columnTypeMap.put("clob", OracleColumnType.CLOB);
        columnTypeMap.put("long", OracleColumnType.LONG);
        columnTypeMap.put("long raw", OracleColumnType.LONG_RAW);
        columnTypeMap.put("nchar", OracleColumnType.NCHAR);
        columnTypeMap.put("nclob", OracleColumnType.NCLOB);
        columnTypeMap.put("number", OracleColumnType.NUMBER);
        columnTypeMap.put("nvarchar2", OracleColumnType.NVARCHAR2);
        columnTypeMap.put("raw", OracleColumnType.RAW);
        columnTypeMap.put("timestamp", OracleColumnType.TIMESTAMP);
        columnTypeMap.put("varchar2", OracleColumnType.VARCHAR2);
    }

    /**
     * Oracle固有の{@literal DATE}型を使用する場合は{@literal true}を返します。
     * 
     * @return Oracle固有の{@literal DATE}型を使用する場合は{@literal true}
     */
    public boolean isUseOracleDate() {
        return useOracleDate;
    }

    /**
     * Oracle固有の{@literal DATE}型を使用する場合は{@literal true}を設定します。
     * 
     * @param useOracleDate
     *            Oracle固有の{@literal DATE}型を使用する場合は{@literal true}
     */
    public void setUseOracleDate(boolean useOracleDate) {
        this.useOracleDate = useOracleDate;
    }

    @Override
    public String getName() {
        return "oracle";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public boolean supportsGetIndexInfo(String catalogName, String schemaName,
            String tableName) {
        if (tableName == null) {
            throw new NullPointerException("tableName");
        }
        for (int i = 0; i < tableName.length(); i++) {
            if (!CharUtil.isAscii(tableName.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType,
            long initialValue, int allocationSize) {
        return "increment by " + allocationSize + " start with " + initialValue;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == TABLE_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public boolean isColumnNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == COLUMN_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public boolean isSequenceNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == SEQUENCE_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public ColumnType getColumnType(String typeName, int sqlType) {
        if (useOracleDate && StringUtil.equalsIgnoreCase(typeName, "date")) {
            return OracleColumnType.DATE;
        }
        ColumnType columnType = columnTypeMap.get(typeName);
        if (columnType != null) {
            return columnType;
        }

        if (StringUtil.startsWithIgnoreCase(typeName, "timestamp")) {
            typeName = "timestamp";
        }
        return super.getColumnType(typeName, sqlType);
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new OracleSqlBlockContext();
    }

    @Override
    public String getSequenceNextValString(String sequenceName,
            int allocationSize) {
        return "select " + sequenceName + ".nextval from dual";
    }

    @Override
    public boolean supportsCommentInCreateTable() {
        return false;
    }

    @Override
    public boolean supportsCommentOn() {
        return true;
    }

    @Override
    public boolean isJdbcCommentAvailable() {
        return false;
    }

    @Override
    public String getTableComment(Connection connection, String catalogName,
            String schemaName, String tableName) throws SQLException {
        String sql = "select comments from all_tab_comments where owner = ? and table_name = ? and table_type = 'TABLE'";
        logger.debug(String.format(sql.replace("?", "'%s'"), schemaName,
                tableName));
        PreparedStatement ps = ConnectionUtil.prepareStatement(connection, sql);
        try {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ResultSet rs = PreparedStatementUtil.executeQuery(ps);
            try {
                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            StatementUtil.close(ps);
        }
    }

    @Override
    public Map<String, String> getColumnCommentMap(Connection connection,
            String catalogName, String schemaName, String tableName)
            throws SQLException {
        String sql = "select column_name, comments from all_col_comments where owner = ? and table_name = ?";
        logger.debug(String.format(sql.replace("?", "'%s'"), schemaName,
                tableName));
        PreparedStatement ps = ConnectionUtil.prepareStatement(connection, sql);
        try {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ResultSet rs = PreparedStatementUtil.executeQuery(ps);
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> commentMap = new CaseInsensitiveMap();
                while (rs.next()) {
                    commentMap.put(rs.getString(1), rs.getString(2));
                }
                return commentMap;
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            StatementUtil.close(ps);
        }
    }

    @Override
    public boolean supportsReferentialUpdateRule() {
        return false;
    }

    @Override
    public SqlType getSqlType(ValueTypeProvider valueTypeProvider,
            PropertyMeta propertyMeta) {
        if (useOracleDate) {
            ValueType valueType = valueTypeProvider.provide(propertyMeta);
            if (valueType instanceof org.seasar.extension.jdbc.types.OracleDateType
                    || valueType instanceof org.seasar.extension.jdbc.types.OracleDateCalendarType) {
                return new TimestampType("date");
            }
        }
        return super.getSqlType(valueTypeProvider, propertyMeta);
    }

    /**
     * Oracle用の{@link ColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class OracleColumnType extends StandardColumnType {

        private static OracleColumnType BINARY_DOUBLE = new OracleColumnType(
                "binary_double", Double.class);

        private static OracleColumnType BINARY_FLOAT = new OracleColumnType(
                "binary_float", Float.class);

        private static OracleColumnType BLOB = new OracleColumnType("blob",
                byte[].class, true);

        private static OracleColumnType CLOB = new OracleColumnType("clob",
                String.class, true);

        private static OracleColumnType DATE = new OracleColumnType("date",
                Timestamp.class, false, TemporalType.TIMESTAMP);

        private static OracleColumnType LONG_RAW = new OracleColumnType(
                "long raw", byte[].class);

        private static OracleColumnType LONG = new OracleColumnType("long",
                String.class);

        private static OracleColumnType NCHAR = new OracleColumnType(
                "nchar($l)", String.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String defaultValue) {
                return super.getColumnDefinition(length / 2, precision, scale,
                        defaultValue);
            }
        };

        private static OracleColumnType NCLOB = new OracleColumnType("nclob",
                String.class, true);

        private static OracleColumnType NUMBER = new OracleColumnType(
                "number($p,$s)", BigDecimal.class) {

            @Override
            public Class<?> getAttributeClass(int length, int precision,
                    int scale) {
                if (scale != 0) {
                    return BigDecimal.class;
                }
                if (precision < 5) {
                    return Short.class;
                }
                if (precision < 10) {
                    return Integer.class;
                }
                if (precision < 19) {
                    return Long.class;
                }
                return BigInteger.class;
            }
        };

        private static OracleColumnType NVARCHAR2 = new OracleColumnType(
                "nvarchar2($l)", String.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String defaultValue) {
                return super.getColumnDefinition(length / 2, precision, scale,
                        defaultValue);
            }
        };

        private static OracleColumnType RAW = new OracleColumnType("raw($l)",
                byte[].class);

        private static OracleColumnType TIMESTAMP = new OracleColumnType(
                "timestamp($s)", Timestamp.class);

        private static OracleColumnType VARCHAR2 = new OracleColumnType(
                "varchar2($l)", String.class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public OracleColumnType(String dataType, Class<?> attributeClass) {
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
        public OracleColumnType(String dataType, Class<?> attributeClass,
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
         * @param lob
         *            LOBの場合{@code true}
         * @param temporalType
         *            時制の種別
         */
        public OracleColumnType(String dataType, Class<?> attributeClass,
                boolean lob, TemporalType temporalType) {
            super(dataType, attributeClass, lob, temporalType);
        }
    }

    /**
     * Oracle用の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class OracleSqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected OracleSqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "triger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }

}
