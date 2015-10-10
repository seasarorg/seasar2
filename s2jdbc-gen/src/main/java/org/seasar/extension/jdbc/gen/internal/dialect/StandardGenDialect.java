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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.exception.UnsupportedSqlTypeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.sqltype.BigIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.CharType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DateType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.SmallIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimestampType;
import org.seasar.extension.jdbc.gen.internal.sqltype.VarcharType;
import org.seasar.extension.jdbc.gen.internal.util.ColumnUtil;
import org.seasar.extension.jdbc.gen.internal.util.TableUtil;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(StandardGenDialect.class);

    /** SQL型をキー、{@link SqlType}を値とするマップ */
    protected Map<Integer, SqlType> sqlTypeMap = new HashMap<Integer, SqlType>();

    /** カラムの型名をキー、{@link ColumnType}を値とするマップ */
    @SuppressWarnings("unchecked")
    protected Map<Object, ColumnType> columnTypeMap = new CaseInsensitiveMap();

    /** カラムのSQL型をキー、{@link ColumnType}を値とするマップ。 */
    protected Map<Integer, ColumnType> fallbackColumnTypeMap = new HashMap<Integer, ColumnType>();

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect() {
        sqlTypeMap.put(Types.BIGINT, new BigIntType());
        sqlTypeMap.put(Types.BINARY, new BinaryType());
        sqlTypeMap.put(Types.BLOB, new BlobType());
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType());
        sqlTypeMap.put(Types.CHAR, new CharType());
        sqlTypeMap.put(Types.CLOB, new ClobType());
        sqlTypeMap.put(Types.DATE, new DateType());
        sqlTypeMap.put(Types.DECIMAL, new DecimalType());
        sqlTypeMap.put(Types.DOUBLE, new DoubleType());
        sqlTypeMap.put(Types.FLOAT, new FloatType());
        sqlTypeMap.put(Types.INTEGER, new IntegerType());
        sqlTypeMap.put(Types.SMALLINT, new SmallIntType());
        sqlTypeMap.put(Types.TIME, new TimeType());
        sqlTypeMap.put(Types.TIMESTAMP, new TimestampType());
        sqlTypeMap.put(Types.VARCHAR, new VarcharType());

        columnTypeMap.put("bigint", StandardColumnType.BIGINT);
        columnTypeMap.put("binary", StandardColumnType.BINARY);
        columnTypeMap.put("bit", StandardColumnType.BIT);
        columnTypeMap.put("blob", StandardColumnType.BLOB);
        columnTypeMap.put("boolean", StandardColumnType.BOOLEAN);
        columnTypeMap.put("char", StandardColumnType.CHAR);
        columnTypeMap.put("clob", StandardColumnType.CLOB);
        columnTypeMap.put("date", StandardColumnType.DATE);
        columnTypeMap.put("decimal", StandardColumnType.DECIMAL);
        columnTypeMap.put("double", StandardColumnType.DOUBLE);
        columnTypeMap.put("float", StandardColumnType.FLOAT);
        columnTypeMap.put("integer", StandardColumnType.INTEGER);
        columnTypeMap.put("longvarbinary", StandardColumnType.LONGVARBINARY);
        columnTypeMap.put("longvarchar", StandardColumnType.LONGVARCHAR);
        columnTypeMap.put("numeric", StandardColumnType.NUMERIC);
        columnTypeMap.put("real", StandardColumnType.REAL);
        columnTypeMap.put("smallint", StandardColumnType.SMALLINT);
        columnTypeMap.put("time", StandardColumnType.TIME);
        columnTypeMap.put("timestamp", StandardColumnType.TIMESTAMP);
        columnTypeMap.put("tinyint", StandardColumnType.TINYINT);
        columnTypeMap.put("varbinary", StandardColumnType.VARBINARY);
        columnTypeMap.put("varchar", StandardColumnType.VARCHAR);

        fallbackColumnTypeMap.put(Types.BIGINT, StandardColumnType.BIGINT);
        fallbackColumnTypeMap.put(Types.BINARY, StandardColumnType.BINARY);
        fallbackColumnTypeMap.put(Types.BIT, StandardColumnType.BIT);
        fallbackColumnTypeMap.put(Types.BLOB, StandardColumnType.BLOB);
        fallbackColumnTypeMap.put(Types.BOOLEAN, StandardColumnType.BOOLEAN);
        fallbackColumnTypeMap.put(Types.CHAR, StandardColumnType.CHAR);
        fallbackColumnTypeMap.put(Types.CLOB, StandardColumnType.CLOB);
        fallbackColumnTypeMap.put(Types.DATE, StandardColumnType.DATE);
        fallbackColumnTypeMap.put(Types.DECIMAL, StandardColumnType.DECIMAL);
        fallbackColumnTypeMap.put(Types.DOUBLE, StandardColumnType.DOUBLE);
        fallbackColumnTypeMap.put(Types.FLOAT, StandardColumnType.FLOAT);
        fallbackColumnTypeMap.put(Types.INTEGER, StandardColumnType.INTEGER);
        fallbackColumnTypeMap.put(Types.LONGVARBINARY,
                StandardColumnType.LONGVARBINARY);
        fallbackColumnTypeMap.put(Types.LONGVARCHAR,
                StandardColumnType.LONGVARCHAR);
        fallbackColumnTypeMap.put(Types.NUMERIC, StandardColumnType.NUMERIC);
        fallbackColumnTypeMap.put(Types.REAL, StandardColumnType.REAL);
        fallbackColumnTypeMap.put(Types.SMALLINT, StandardColumnType.SMALLINT);
        fallbackColumnTypeMap.put(Types.TIME, StandardColumnType.TIME);
        fallbackColumnTypeMap
                .put(Types.TIMESTAMP, StandardColumnType.TIMESTAMP);
        fallbackColumnTypeMap.put(Types.TINYINT, StandardColumnType.TINYINT);
        fallbackColumnTypeMap
                .put(Types.VARBINARY, StandardColumnType.VARBINARY);
        fallbackColumnTypeMap.put(Types.VARCHAR, StandardColumnType.VARCHAR);
    }

    public String getName() {
        return null;
    }

    public String getDefaultSchemaName(String userName) {
        return userName;
    }

    public SqlType getSqlType(int sqlType) {
        return getSqlTypeInternal(sqlType);
    }

    public SqlType getSqlType(ValueTypeProvider valueTypeProvider,
            PropertyMeta propertyMeta) {
        ValueType valueType = valueTypeProvider.provide(propertyMeta);
        return getSqlTypeInternal(valueType.getSqlType());
    }

    /**
     * 内部的にSQL型を返します。
     * 
     * @param sqlType
     *            JDBCのSQL型
     * @return SQL型
     */
    protected SqlType getSqlTypeInternal(int sqlType) {
        SqlType type = sqlTypeMap.get(sqlType);
        if (type != null) {
            return type;
        }
        throw new UnsupportedSqlTypeRuntimeException(sqlType);
    }

    public ColumnType getColumnType(String typeName, int sqlType) {
        ColumnType columnType = columnTypeMap.get(typeName);
        return columnType != null ? columnType : fallbackColumnTypeMap
                .get(sqlType);
    }

    public GenerationType getDefaultGenerationType() {
        return GenerationType.TABLE;
    }

    public String getOpenQuote() {
        return "\"";
    }

    public String getCloseQuote() {
        return "\"";
    }

    public String quote(String value) {
        if (value == null) {
            return null;
        }
        return getOpenQuote() + value + getCloseQuote();
    }

    public String unquote(String value) {
        String s = StringUtil.ltrim(value, getOpenQuote());
        return StringUtil.rtrim(s, getCloseQuote());
    }

    public boolean supportsSequence() {
        return false;
    }

    public boolean supportsGetIndexInfo(String catalogName, String schemaName,
            String tableName) {
        return true;
    }

    public String getSequenceDefinitionFragment(String dataType,
            long initialValue, int allocationSize) {
        throw new UnsupportedOperationException("getSequenceDefinitionFragment");
    }

    public String getSqlBlockDelimiter() {
        return null;
    }

    public String getIdentityColumnDefinition() {
        throw new UnsupportedOperationException("getIdentityDefinition");
    }

    public String getDropForeignKeySyntax() {
        return "drop constraint";
    }

    public String getDropUniqueKeySyntax() {
        return "drop constraint";
    }

    public boolean isTableNotFound(Throwable throwable) {
        return false;
    }

    public boolean isColumnNotFound(Throwable throwable) {
        return false;
    }

    public boolean isSequenceNotFound(Throwable throwable) {
        return false;
    }

    public SqlBlockContext createSqlBlockContext() {
        return new StandardSqlBlockContext();
    }

    public boolean supportsIdentityInsert() {
        return false;
    }

    public boolean supportsIdentityInsertControlStatement() {
        return false;
    }

    public String getIdentityInsertEnableStatement(String tableName) {
        throw new UnsupportedOperationException("getIdentityInsertOnStatement");
    }

    public String getIdentityInsertDisableStatement(String tableName) {
        throw new UnsupportedOperationException("getIdentityInsertOffStatement");
    }

    public boolean supportsNullableUnique() {
        return true;
    }

    public boolean supportsIdentity() {
        return false;
    }

    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        throw new UnsupportedOperationException("getSequenceNextValString");
    }

    public boolean supportsCommentInCreateTable() {
        return false;
    }

    public boolean supportsCommentOn() {
        return false;
    }

    public boolean isJdbcCommentAvailable() {
        return true;
    }

    public String getTableComment(Connection connection, String catalogName,
            String schemaName, String tableName) throws SQLException {
        throw new UnsupportedOperationException("getTableComment");
    }

    public Map<String, String> getColumnCommentMap(Connection connection,
            String catalogName, String schemaName, String tableName)
            throws SQLException {
        throw new UnsupportedOperationException("getColumnCommentMap");
    }

    public boolean supportsReferentialDeleteRule() {
        return true;
    }

    public boolean supportsReferentialUpdateRule() {
        return true;
    }

    public boolean isAutoIncrement(Connection connection, String catalogName,
            String schemaName, String tableName, String columnName)
            throws SQLException {
        String fullTableName = TableUtil.buildFullTableName(catalogName,
                schemaName, tableName);
        String sql = "select " + columnName + " from " + fullTableName
                + " where 1 = 0";
        logger.debug(sql);

        PreparedStatement ps = ConnectionUtil.prepareStatement(connection, sql);
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                return rsMetaData.isAutoIncrement(1);
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            StatementUtil.close(ps);
        }
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException#getSQLState() SQLステート}を返します。
     * <p>
     * 例外チェーンに{@link SQLException SQL例外}が存在しない場合や、SQLステートが設定されていない場合は
     * <code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException#getSQLState() SQLステート}
     */
    protected String getSQLState(Throwable t) {
        SQLException cause = getCauseSQLException(t);
        if (cause != null && !StringUtil.isEmpty(cause.getSQLState())) {
            return cause.getSQLState();
        }
        return null;
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException#getErrorCode() ベンダー固有の例外コード}を返します。
     * <p>
     * 例外チェーンに{@link SQLException SQL例外}が存在しない場合や、例外コードが設定されていない場合は
     * <code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException#getErrorCode() ベンダー固有の例外コード}
     */
    protected Integer getErrorCode(Throwable t) {
        SQLException cause = getCauseSQLException(t);
        if (cause != null) {
            return cause.getErrorCode();
        }
        return null;
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException SQL例外}を返します。
     * <p>
     * 例外チェーンにSQL例外が存在しない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException SQL例外}
     */
    protected SQLException getCauseSQLException(Throwable t) {
        SQLException cause = null;
        while (t != null) {
            if (t instanceof SQLException) {
                cause = SQLException.class.cast(t);
                if (cause.getNextException() != null) {
                    cause = cause.getNextException();
                    t = cause;
                    continue;
                }
            }
            t = t.getCause();
        }
        return cause;
    }

    /**
     * 標準の{@link ColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardColumnType implements ColumnType {

        private static StandardColumnType BIGINT = new StandardColumnType(
                "bigint", Long.class);

        private static StandardColumnType BINARY = new StandardColumnType(
                "binary", byte[].class);

        private static StandardColumnType BIT = new StandardColumnType("bit",
                Boolean.class);

        private static StandardColumnType BLOB = new StandardColumnType("blob",
                byte[].class, true);

        private static StandardColumnType BOOLEAN = new StandardColumnType(
                "boolean", Boolean.class);

        private static StandardColumnType CHAR = new StandardColumnType(
                "char($l)", String.class);

        private static StandardColumnType CLOB = new StandardColumnType("clob",
                String.class, true);

        private static StandardColumnType DATE = new StandardColumnType("date",
                java.sql.Date.class);

        private static StandardColumnType DECIMAL = new StandardColumnType(
                "decimal", BigDecimal.class);

        private static StandardColumnType DOUBLE = new StandardColumnType(
                "double", Double.class);

        private static StandardColumnType FLOAT = new StandardColumnType(
                "float", Float.class);

        private static StandardColumnType INTEGER = new StandardColumnType(
                "integer", Integer.class);

        private static StandardColumnType LONGVARBINARY = new StandardColumnType(
                "longvarbinary", byte[].class);

        private static StandardColumnType LONGVARCHAR = new StandardColumnType(
                "longvarchar", String.class);

        private static StandardColumnType NUMERIC = new StandardColumnType(
                "numeric", BigDecimal.class);

        private static StandardColumnType REAL = new StandardColumnType("real",
                Float.class);

        private static StandardColumnType SMALLINT = new StandardColumnType(
                "smallint", Short.class);

        private static StandardColumnType TIME = new StandardColumnType("time",
                java.sql.Time.class);

        private static StandardColumnType TIMESTAMP = new StandardColumnType(
                "timestamp", Timestamp.class);

        private static StandardColumnType TINYINT = new StandardColumnType(
                "tinyint", Short.class);

        private static StandardColumnType VARBINARY = new StandardColumnType(
                "varbinary($l)", byte[].class);

        private static StandardColumnType VARCHAR = new StandardColumnType(
                "varchar($l)", String.class);

        /** カラム定義 */
        protected String dataType;

        /** 属性のクラス */
        protected Class<?> attributeClass;

        /** LOBの場合{@code true} */
        protected boolean lob;

        /** 時制の種別 */
        protected TemporalType temporalType;

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        protected StandardColumnType(String dataType, Class<?> attributeClass) {
            this(dataType, attributeClass, false);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        protected StandardColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            this(dataType, attributeClass, lob, null);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         * @param temporalType
         *            時制の種別
         */
        protected StandardColumnType(String dataType, Class<?> attributeClass,
                boolean lob, TemporalType temporalType) {
            this.dataType = dataType;
            this.attributeClass = attributeClass;
            this.lob = lob;
            this.temporalType = temporalType;
        }

        public String getColumnDefinition(int length, int precision, int scale,
                String defaultValue) {
            String completeDataType = ColumnUtil.formatDataType(dataType,
                    length, precision, scale);
            return getColumnDefinitionInternal(completeDataType, defaultValue);
        }

        /**
         * カラム定義を返します。
         * 
         * @param completeDataType
         *            完全なデータ型
         * @param defaultValue
         *            デフォルト値、存在しない場合は{@code null}
         * @return カラム定義
         */
        protected String getColumnDefinitionInternal(String completeDataType,
                String defaultValue) {
            if (defaultValue == null) {
                return completeDataType;
            }
            return completeDataType + " default " + defaultValue;
        }

        public Class<?> getAttributeClass(int length, int precision, int scale) {
            return attributeClass;
        }

        public boolean isLob() {
            return lob;
        }

        public TemporalType getTemporalType() {
            return temporalType;
        }

    }

    /**
     * 標準の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardSqlBlockContext implements SqlBlockContext {

        /** SQLブロックの開始を表すキーワードの連なりのリスト */
        protected List<List<String>> sqlBlockStartKeywordsList = new ArrayList<List<String>>();

        /** 追加されたキーワードの連なり */
        protected List<String> keywords = new ArrayList<String>();

        /** SQLブロックの内側の場合{@code true} */
        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if (!inSqlBlock) {
                keywords.add(keyword);
                check();
            }
        }

        /**
         * ブロックの内側かどうかチェックします。
         */
        protected void check() {
            for (List<String> startKeywords : sqlBlockStartKeywordsList) {
                if (startKeywords.size() > keywords.size()) {
                    continue;
                }
                for (int i = 0; i < startKeywords.size(); i++) {
                    String word1 = startKeywords.get(i);
                    String word2 = keywords.get(i);
                    inSqlBlock = word1.equalsIgnoreCase(word2);
                    if (!inSqlBlock) {
                        break;
                    }
                }
                if (inSqlBlock) {
                    break;
                }
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }

}
