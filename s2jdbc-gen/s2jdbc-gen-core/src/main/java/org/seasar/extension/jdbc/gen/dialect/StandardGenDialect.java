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
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.GenDialect;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、{@link Type}を値とするマップ */
    protected Map<Integer, Type> typeMap = new HashMap<Integer, Type>();

    /** SQLブロックの開始を表す単語の連なりのリスト */
    protected List<List<String>> sqlBlockStartWordsList = new ArrayList<List<String>>();

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect() {
        typeMap.put(Types.BIGINT, StandardType.BIGINT);
        typeMap.put(Types.BINARY, StandardType.BINARY);
        typeMap.put(Types.BIT, StandardType.BIT);
        typeMap.put(Types.BLOB, StandardType.BLOB);
        typeMap.put(Types.BOOLEAN, StandardType.BOOLEAN);
        typeMap.put(Types.CHAR, StandardType.CHAR);
        typeMap.put(Types.CLOB, StandardType.CLOB);
        typeMap.put(Types.DATE, StandardType.DATE);
        typeMap.put(Types.DECIMAL, StandardType.DECIMAL);
        typeMap.put(Types.DOUBLE, StandardType.DOUBLE);
        typeMap.put(Types.FLOAT, StandardType.FLOAT);
        typeMap.put(Types.INTEGER, StandardType.INTEGER);
        typeMap.put(Types.LONGVARBINARY, StandardType.LONGVARBYNARY);
        typeMap.put(Types.LONGVARCHAR, StandardType.LONGVARCHAR);
        typeMap.put(Types.NUMERIC, StandardType.NUMERIC);
        typeMap.put(Types.REAL, StandardType.REAL);
        typeMap.put(Types.SMALLINT, StandardType.SMALLINT);
        typeMap.put(Types.TIME, StandardType.TIME);
        typeMap.put(Types.TIMESTAMP, StandardType.TIMESTAMP);
        typeMap.put(Types.TINYINT, StandardType.TINYINT);
        typeMap.put(Types.VARBINARY, StandardType.VARBINARY);
        typeMap.put(Types.VARCHAR, StandardType.VARCHAR);
    }

    public boolean isUserTable(String tableName) {
        return true;
    }

    public boolean isLobType(int sqlType, String typeName) {
        switch (sqlType) {
        case Types.BLOB:
        case Types.CLOB:
        case Types.LONGVARBINARY:
        case Types.LONGVARCHAR:
            return true;
        }
        return false;
    }

    public TemporalType getTemporalType(int sqlType, String typeName) {
        switch (sqlType) {
        case Types.DATE:
            return TemporalType.DATE;
        case Types.TIME:
            return TemporalType.TIME;
        case Types.TIMESTAMP:
            return TemporalType.TIMESTAMP;
        }
        return null;
    }

    public String getDefaultSchemaName(String userName) {
        return userName;
    }

    public Type getType(int sqlType) {
        Type type = typeMap.get(sqlType);
        return type != null ? type : StandardType.UNKOWN;
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
     * 標準の{@link Type}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardType implements Type {

        private static Type BIGINT = new StandardType(Long.class, "bigint");

        private static Type BIT = new StandardType(Boolean.class, "bit");

        private static Type BINARY = new StandardType(byte[].class, "binary");

        private static Type BLOB = new StandardType(byte[].class, "blob");

        private static Type BOOLEAN = new StandardType(Boolean.class, "boolean");

        private static Type CHAR = new StandardType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                if (length > 1) {
                    return String.class;
                }
                return Character.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("char(%d)", length);
            }
        };

        private static Type CLOB = new StandardType(String.class, "clob");

        private static Type DATE = new StandardType(Date.class, "date");

        private static Type DECIMAL = new StandardType(BigDecimal.class,
                "decimal");

        private static Type DOUBLE = new StandardType(Double.class, "double");

        private static Type FLOAT = new StandardType(Float.class, "float");

        private static Type INTEGER = new StandardType(Integer.class, "integer");

        private static Type LONGVARBYNARY = new StandardType(byte[].class,
                "longvarbinary");

        private static Type LONGVARCHAR = new StandardType(String.class,
                "longvarchar");

        private static Type NUMERIC = new StandardType(BigDecimal.class,
                "numeric");

        private static Type REAL = new StandardType(Float.class, "real");

        private static Type SMALLINT = new StandardType(Short.class, "smallint");

        private static Type TIME = new StandardType(Date.class, "time");

        private static Type TIMESTAMP = new StandardType(Date.class,
                "timestamp");

        private static Type TINYINT = new StandardType(Short.class, "tinyint");

        private static Type UNKOWN = new StandardType(Object.class, null);

        private static Type VARBINARY = new StandardType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("varbinary(%d)", length);
            }
        };

        private static Type VARCHAR = new StandardType() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return format("varchar(%d)", length);
            }
        };

        private Class<?> javaClass;

        /** 定義 */
        private String columnDefinition;

        /**
         * インスタンスを構築します。
         */
        protected StandardType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param javaClass
         *            Javaのクラス
         * @param columnDefinition
         *            カラム定義
         */
        protected StandardType(Class<?> javaClass, String columnDefinition) {
            this.javaClass = javaClass;
            this.columnDefinition = columnDefinition;
        }

        public Class<?> getJavaClass(int length, int precision, int scale,
                String typeName) {
            if (javaClass == null) {
                throw new IllegalStateException("clazz");
            }
            return javaClass;
        }

        public String getColumnDefinition(int length, int precision, int scale,
                String typeName) {
            return columnDefinition;
        }

        /**
         * フォーマットします。
         * 
         * @param format
         *            フォーマット
         * @param args
         *            引数
         * @return フォーマットされた文字列
         */
        protected String format(String format, Object... args) {
            return new Formatter().format(format, args).toString();
        }

    }

}
