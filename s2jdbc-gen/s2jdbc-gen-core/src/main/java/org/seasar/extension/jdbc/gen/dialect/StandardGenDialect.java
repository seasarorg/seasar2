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
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、{@link SqlType}を値とするマップ */
    protected Map<Integer, SqlType> typeMap = new HashMap<Integer, SqlType>();

    protected Map<String, ColumnType> namedTypeMap = new CaseInsensitiveMap();

    /** SQLブロックの開始を表す単語の連なりのリスト */
    protected List<List<String>> sqlBlockStartWordsList = new ArrayList<List<String>>();

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect() {
        typeMap.put(Types.BIGINT, StandardSqlType.BIGINT);
        typeMap.put(Types.BINARY, StandardSqlType.BINARY);
        typeMap.put(Types.BLOB, StandardSqlType.BLOB);
        typeMap.put(Types.BOOLEAN, StandardSqlType.BOOLEAN);
        typeMap.put(Types.CHAR, StandardSqlType.CHAR);
        typeMap.put(Types.CLOB, StandardSqlType.CLOB);
        typeMap.put(Types.DATE, StandardSqlType.DATE);
        typeMap.put(Types.DECIMAL, StandardSqlType.DECIMAL);
        typeMap.put(Types.DOUBLE, StandardSqlType.DOUBLE);
        typeMap.put(Types.FLOAT, StandardSqlType.FLOAT);
        typeMap.put(Types.INTEGER, StandardSqlType.INTEGER);
        typeMap.put(Types.SMALLINT, StandardSqlType.SMALLINT);
        typeMap.put(Types.TIME, StandardSqlType.TIME);
        typeMap.put(Types.TIMESTAMP, StandardSqlType.TIMESTAMP);
        typeMap.put(Types.VARCHAR, StandardSqlType.VARCHAR);

        namedTypeMap.put("bigint", StandardColumnType.BIGINT);
        namedTypeMap.put("bit", StandardColumnType.BIT);
        namedTypeMap.put("binary", StandardColumnType.BINARY);
        namedTypeMap.put("blob", StandardColumnType.BLOB);
        namedTypeMap.put("boolean", StandardColumnType.BOOLEAN);
        namedTypeMap.put("char", StandardColumnType.CHAR);
        namedTypeMap.put("clob", StandardColumnType.CLOB);
        namedTypeMap.put("date", StandardColumnType.DATE);
        namedTypeMap.put("decimal", StandardColumnType.DECIMAL);
        namedTypeMap.put("double", StandardColumnType.DOUBLE);
        namedTypeMap.put("float", StandardColumnType.FLOAT);
        namedTypeMap.put("integer", StandardColumnType.INTEGER);
        namedTypeMap.put("longvarbinary", StandardColumnType.LONGVARBINARY);
        namedTypeMap.put("longvarchar", StandardColumnType.LONGVARCHAR);
        namedTypeMap.put("numeric", StandardColumnType.NUMERIC);
        namedTypeMap.put("real", StandardColumnType.REAL);
        namedTypeMap.put("time", StandardColumnType.TIME);
        namedTypeMap.put("timestamp", StandardColumnType.TIMESTAMP);
        namedTypeMap.put("tinyint", StandardColumnType.TINYINT);
        namedTypeMap.put("varbinary", StandardColumnType.VARBINARY);
        namedTypeMap.put("varchar", StandardColumnType.VARCHAR);
    }

    public boolean isUserTable(String tableName) {
        return true;
    }

    public String getDefaultSchemaName(String userName) {
        return userName;
    }

    public SqlType getSqlType(int sqlType) {
        return typeMap.get(sqlType);
    }

    public ColumnType getColumnType(String typeName) {
        return namedTypeMap.get(typeName);
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

    public boolean isSqlBlockStartWords(List<String> words) {
        boolean equals = false;
        for (List<String> startWords : sqlBlockStartWordsList) {
            if (startWords.size() > words.size()) {
                continue;
            }
            for (int i = 0; i < startWords.size(); i++) {
                String word1 = startWords.get(i);
                String word2 = words.get(i);
                equals = word1.equalsIgnoreCase(word2);
                if (!equals) {
                    break;
                }
            }
            if (equals) {
                return true;
            }
        }
        return equals;
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
     * 標準の{@link SqlType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardSqlType implements SqlType {

        private static StandardSqlType BIGINT = new StandardSqlType("bigint");

        private static StandardSqlType BINARY = new StandardSqlType("binary");

        private static StandardSqlType BLOB = new StandardSqlType("blob");

        private static StandardSqlType BOOLEAN = new StandardSqlType("boolean");

        private static StandardSqlType CHAR = new StandardSqlType("char(1)");

        private static StandardSqlType CLOB = new StandardSqlType("clob");

        private static StandardSqlType DATE = new StandardSqlType("date");

        private static StandardSqlType DECIMAL = new StandardSqlType("decimal");

        private static StandardSqlType DOUBLE = new StandardSqlType("double");

        private static StandardSqlType FLOAT = new StandardSqlType("float");

        private static StandardSqlType INTEGER = new StandardSqlType("integer");

        private static StandardSqlType SMALLINT = new StandardSqlType(
                "smallint");

        private static StandardSqlType TIME = new StandardSqlType("time");

        private static StandardSqlType TIMESTAMP = new StandardSqlType(
                "timestamp");

        private static StandardSqlType VARCHAR = new StandardSqlType(
                "varchar($l)");

        /** 定義 */
        protected String columnDefinition;

        protected StandardSqlType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param columnDefinition
         *            カラム定義
         */
        protected StandardSqlType(String columnDefinition) {
            this.columnDefinition = columnDefinition;
        }

        public String getColumnDefinition(int length, int precision, int scale, boolean identity) {
            return format(columnDefinition, length, precision, scale);
        }
    }

    /**
     * @author taedium
     * 
     */
    public static class StandardColumnType implements ColumnType {

        private static StandardColumnType BIGINT = new StandardColumnType(
                "bigint", Long.class);

        private static StandardColumnType BIT = new StandardColumnType("bit",
                Boolean.class);

        private static StandardColumnType BINARY = new StandardColumnType(
                "binary", byte[].class);

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

        protected String columnDefinition;

        protected Class attributeClass;

        protected boolean lob;

        protected TemporalType temporalType;

        public StandardColumnType(String columnDefinition,
                Class<?> attributeClass) {
            this(columnDefinition, attributeClass, false, null);
        }

        public StandardColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            this(columnDefinition, attributeClass, lob, null);
        }

        public StandardColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            this(columnDefinition, attributeClass, false, temporalType);
        }

        public StandardColumnType(String columnDefinition,
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

}
