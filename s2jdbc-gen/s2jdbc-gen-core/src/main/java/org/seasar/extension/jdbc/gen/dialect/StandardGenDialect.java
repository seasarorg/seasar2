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
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.exception.UnsupportedSqlTypeRuntimeException;
import org.seasar.extension.jdbc.gen.sqltype.BigIntType;
import org.seasar.extension.jdbc.gen.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.sqltype.CharType;
import org.seasar.extension.jdbc.gen.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.sqltype.DateType;
import org.seasar.extension.jdbc.gen.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.sqltype.SmallIntType;
import org.seasar.extension.jdbc.gen.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.sqltype.TimestampType;
import org.seasar.extension.jdbc.gen.sqltype.VarcharType;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、{@link SqlType}を値とするマップ */
    protected Map<Integer, SqlType> sqlTypeMap = new HashMap<Integer, SqlType>();

    /** カラムの型名をキー、{@link ColumnType}を値とするマップ */
    @SuppressWarnings("unchecked")
    protected Map<String, ColumnType> columnTypeMap = new CaseInsensitiveMap();

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
    }

    public boolean isUserTable(String tableName) {
        return true;
    }

    public String getDefaultSchemaName(String userName) {
        return userName;
    }

    public SqlType getSqlType(int sqlType) {
        SqlType type = sqlTypeMap.get(sqlType);
        if (type != null) {
            return type;
        }
        throw new UnsupportedSqlTypeRuntimeException(sqlType);
    }

    public ColumnType getColumnType(String typeName) {
        return columnTypeMap.get(typeName);
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

    public boolean supportsSequence() {
        return false;
    }

    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
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

    public SqlBlockContext createSqlBlockContext() {
        return new StandardSqlBlockContext();
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
                Date.class, TemporalType.DATE);

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
                Date.class, TemporalType.DATE);

        private static StandardColumnType TIMESTAMP = new StandardColumnType(
                "timestamp", Date.class, TemporalType.DATE);

        private static StandardColumnType TINYINT = new StandardColumnType(
                "tinyint", Short.class);

        private static StandardColumnType VARBINARY = new StandardColumnType(
                "varbinary($l)", byte[].class);

        private static StandardColumnType VARCHAR = new StandardColumnType(
                "varchar($l)", String.class);

        /** カラム定義 */
        protected String columnDefinition;

        /** 属性のクラス */
        protected Class<?> attributeClass;

        /** LOBの場合{@code true} */
        protected boolean lob;

        /** 時制型 */
        protected TemporalType temporalType;

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         */
        protected StandardColumnType(String columnDefinition,
                Class<?> attributeClass) {
            this(columnDefinition, attributeClass, false, null);
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
        protected StandardColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            this(columnDefinition, attributeClass, lob, null);
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
        protected StandardColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            this(columnDefinition, attributeClass, false, temporalType);
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
         * @param temporalType
         *            時制型
         */
        protected StandardColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob, TemporalType temporalType) {
            this.columnDefinition = columnDefinition;
            this.attributeClass = attributeClass;
            this.lob = lob;
            this.temporalType = temporalType;
        }

        public String getColumnDefinition(int length, int precision, int scale) {
            return format(columnDefinition, length, precision, scale);
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
     * フォーマットします。
     * 
     * @param format
     *            フォーマット文字列
     * @param length
     *            長さ
     * @param precision
     *            精度
     * @param scale
     *            スケール
     * @return フォーマットされた文字列
     */
    protected static String format(String format, int length, int precision,
            int scale) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == '$') {
                i++;
                if (i < format.length()) {
                    c = format.charAt(i);
                    switch (c) {
                    case 'l':
                        buf.append(length);
                        break;
                    case 'p':
                        buf.append(precision);
                        break;
                    case 's':
                        buf.append(scale);
                        break;
                    default:
                        buf.append('$');
                        buf.append(c);
                        break;
                    }
                } else {
                    buf.append(c);
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
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
