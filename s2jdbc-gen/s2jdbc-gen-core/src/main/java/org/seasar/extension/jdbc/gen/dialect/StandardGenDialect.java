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

    /** SQL型をキー、@{JavaType Java型}を値とするマップ */
    protected Map<Integer, JavaType> javaTypeMap = new HashMap<Integer, JavaType>();

    /** SQL型をキー、@{DbType データ型}を値とするマップ */
    protected Map<Integer, DbType> dataTypeMap = new HashMap<Integer, DbType>();

    /** SQLブロックの開始を表す単語の連なりのリスト */
    protected List<List<String>> sqlBlockStartWordsList = new ArrayList<List<String>>();

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect() {
        javaTypeMap.put(Types.ARRAY, StandardJavaType.ARRAY);
        javaTypeMap.put(Types.BIGINT, StandardJavaType.BIGINT);
        javaTypeMap.put(Types.BINARY, StandardJavaType.BINARY);
        javaTypeMap.put(Types.BIT, StandardJavaType.BIT);
        javaTypeMap.put(Types.BLOB, StandardJavaType.BLOB);
        javaTypeMap.put(Types.BOOLEAN, StandardJavaType.BOOLEAN);
        javaTypeMap.put(Types.CHAR, StandardJavaType.CHAR);
        javaTypeMap.put(Types.CLOB, StandardJavaType.CLOB);
        javaTypeMap.put(Types.DATALINK, StandardJavaType.DATALINK);
        javaTypeMap.put(Types.DATE, StandardJavaType.DATE);
        javaTypeMap.put(Types.DECIMAL, StandardJavaType.DECIMAL);
        javaTypeMap.put(Types.DISTINCT, StandardJavaType.DISTINCT);
        javaTypeMap.put(Types.DOUBLE, StandardJavaType.DOUBLE);
        javaTypeMap.put(Types.FLOAT, StandardJavaType.FLOAT);
        javaTypeMap.put(Types.INTEGER, StandardJavaType.INTEGER);
        javaTypeMap.put(Types.JAVA_OBJECT, StandardJavaType.JAVA_OBJECT);
        javaTypeMap.put(Types.LONGVARBINARY, StandardJavaType.LONGVARBINARY);
        javaTypeMap.put(Types.LONGVARCHAR, StandardJavaType.LONGVARCHAR);
        javaTypeMap.put(Types.NULL, StandardJavaType.NULL);
        javaTypeMap.put(Types.NUMERIC, StandardJavaType.NUMERIC);
        javaTypeMap.put(Types.OTHER, StandardJavaType.OTHER);
        javaTypeMap.put(Types.REAL, StandardJavaType.REAL);
        javaTypeMap.put(Types.REF, StandardJavaType.REF);
        javaTypeMap.put(Types.SMALLINT, StandardJavaType.SMALLINT);
        javaTypeMap.put(Types.STRUCT, StandardJavaType.STRUCT);
        javaTypeMap.put(Types.TIME, StandardJavaType.TIME);
        javaTypeMap.put(Types.TIMESTAMP, StandardJavaType.TIMESTAMP);
        javaTypeMap.put(Types.TINYINT, StandardJavaType.TINYINT);
        javaTypeMap.put(Types.VARBINARY, StandardJavaType.VARBINARY);
        javaTypeMap.put(Types.VARCHAR, StandardJavaType.VARCHAR);

        dataTypeMap.put(Types.ARRAY, StandardDbType.ARRAY);
        dataTypeMap.put(Types.BIGINT, StandardDbType.BIGINT);
        dataTypeMap.put(Types.BINARY, StandardDbType.BINARY);
        dataTypeMap.put(Types.BIT, StandardDbType.BIT);
        dataTypeMap.put(Types.BLOB, StandardDbType.BLOB);
        dataTypeMap.put(Types.BOOLEAN, StandardDbType.BOOLEAN);
        dataTypeMap.put(Types.CHAR, StandardDbType.CHAR);
        dataTypeMap.put(Types.CLOB, StandardDbType.CLOB);
        dataTypeMap.put(Types.DATALINK, StandardDbType.DATALINK);
        dataTypeMap.put(Types.DATE, StandardDbType.DATE);
        dataTypeMap.put(Types.DECIMAL, StandardDbType.DECIMAL);
        dataTypeMap.put(Types.DISTINCT, StandardDbType.DISTINCT);
        dataTypeMap.put(Types.DOUBLE, StandardDbType.DOUBLE);
        dataTypeMap.put(Types.FLOAT, StandardDbType.FLOAT);
        dataTypeMap.put(Types.INTEGER, StandardDbType.INTEGER);
        dataTypeMap.put(Types.JAVA_OBJECT, StandardDbType.JAVA_OBJECT);
        dataTypeMap.put(Types.LONGVARBINARY, StandardDbType.LONGVARBYNARY);
        dataTypeMap.put(Types.LONGVARCHAR, StandardDbType.LONGVARCHAR);
        dataTypeMap.put(Types.NULL, StandardDbType.NULL);
        dataTypeMap.put(Types.NUMERIC, StandardDbType.NUMERIC);
        dataTypeMap.put(Types.OTHER, StandardDbType.OTHER);
        dataTypeMap.put(Types.REAL, StandardDbType.REAL);
        dataTypeMap.put(Types.REF, StandardDbType.REF);
        dataTypeMap.put(Types.SMALLINT, StandardDbType.SMALLINT);
        dataTypeMap.put(Types.STRUCT, StandardDbType.STRUCT);
        dataTypeMap.put(Types.TIME, StandardDbType.TIME);
        dataTypeMap.put(Types.TIMESTAMP, StandardDbType.TIMESTAMP);
        dataTypeMap.put(Types.TINYINT, StandardDbType.TINYINT);
        dataTypeMap.put(Types.VARBINARY, StandardDbType.VARBINARY);
        dataTypeMap.put(Types.VARCHAR, StandardDbType.VARCHAR);
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

    public JavaType getJavaType(int sqlType) {
        return javaTypeMap.get(sqlType);
    }

    public DbType getDbType(int sqlType) {
        return dataTypeMap.get(sqlType);
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

    /**
     * 標準的な{@link JavaType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardJavaType implements JavaType {

        private static JavaType ARRAY = new StandardJavaType(Object.class);

        private static JavaType BIGINT = new StandardJavaType(Long.class);

        private static JavaType BINARY = new StandardJavaType(byte[].class);

        private static JavaType BIT = new StandardJavaType(Object.class);

        private static JavaType BLOB = new StandardJavaType(byte[].class);

        private static JavaType BOOLEAN = new StandardJavaType(Boolean.class);

        private static JavaType CHAR = new StandardJavaType(String.class);

        private static JavaType CLOB = new StandardJavaType(String.class);

        private static JavaType DATALINK = new StandardJavaType(Object.class);

        private static JavaType DATE = new StandardJavaType(Date.class);

        private static JavaType DECIMAL = new StandardJavaType(BigDecimal.class);

        private static JavaType DISTINCT = new StandardJavaType(Object.class);

        private static JavaType DOUBLE = new StandardJavaType(Double.class);

        private static JavaType FLOAT = new StandardJavaType(Float.class);

        private static JavaType INTEGER = new StandardJavaType(Integer.class);

        private static JavaType JAVA_OBJECT = new StandardJavaType(Object.class);

        private static JavaType LONGVARBINARY = new StandardJavaType(
                byte[].class);

        private static JavaType LONGVARCHAR = new StandardJavaType(String.class);

        private static JavaType NULL = new StandardJavaType(Object.class);

        private static JavaType NUMERIC = new StandardJavaType(BigDecimal.class);

        private static JavaType OTHER = new StandardJavaType(Object.class);

        private static JavaType REAL = new StandardJavaType(Float.class);

        private static JavaType REF = new StandardJavaType(Object.class);

        private static JavaType SMALLINT = new StandardJavaType(Short.class);

        private static JavaType STRUCT = new StandardJavaType(Object.class);

        private static JavaType TIME = new StandardJavaType(Date.class);

        private static JavaType TIMESTAMP = new StandardJavaType(Date.class);

        private static JavaType TINYINT = new StandardJavaType(Short.class);

        private static JavaType VARBINARY = new StandardJavaType(byte[].class);

        private static JavaType VARCHAR = new StandardJavaType(String.class);

        private Class<?> clazz;

        /**
         * インスタンスを構築します。
         */
        protected StandardJavaType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param clazz
         *            クラス
         */
        protected StandardJavaType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getJavaClass(int length, int scale, String typeName,
                boolean nullable) {
            if (clazz == null) {
                throw new IllegalStateException("clazz");
            }
            return clazz;
        }
    }

    /**
     * 標準的な{@link DbType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardDbType implements DbType {

        private static DbType ARRAY = new StandardDbType("array");

        private static DbType BIGINT = new StandardDbType("bigint");

        private static DbType BIT = new StandardDbType("bit");

        private static DbType BINARY = new StandardDbType("binary");

        private static DbType BLOB = new StandardDbType("blob");

        private static DbType BOOLEAN = new StandardDbType("boolean");

        private static DbType CHAR = new StandardDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("char(%d)", length);
            }
        };

        private static DbType CLOB = new StandardDbType("clob");

        private static DbType DATE = new StandardDbType("date");

        private static DbType DATALINK = new StandardDbType("datalink");

        private static DbType DECIMAL = new StandardDbType("decimal");

        private static DbType DISTINCT = new StandardDbType("distinct");

        private static DbType DOUBLE = new StandardDbType("double");

        private static DbType FLOAT = new StandardDbType("float");

        private static DbType INTEGER = new StandardDbType("integer");

        private static DbType JAVA_OBJECT = new StandardDbType("java_object");

        private static DbType LONGVARBYNARY = new StandardDbType(
                "longvarbynary");

        private static DbType LONGVARCHAR = new StandardDbType("longvarchar");

        private static DbType NULL = new StandardDbType("null");

        private static DbType NUMERIC = new StandardDbType("numeric");

        private static DbType OTHER = new StandardDbType("other");

        private static DbType REAL = new StandardDbType("real");

        private static DbType REF = new StandardDbType("ref");

        private static DbType SMALLINT = new StandardDbType("smallint");

        private static DbType STRUCT = new StandardDbType("struct");

        private static DbType TIME = new StandardDbType("time");

        private static DbType TIMESTAMP = new StandardDbType("timestamp");

        private static DbType TINYINT = new StandardDbType("tinyint");

        private static DbType VARBINARY = new StandardDbType("varbinary") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format("varbinary(%d)", length);
            }
        };

        private static DbType VARCHAR = new StandardDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varchar(%d)", length);
            }
        };

        /** 定義 */
        private String definition;

        /**
         * インスタンスを構築します。
         */
        protected StandardDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected StandardDbType(String definition) {
            this.definition = definition;
        }

        public String getDefinition(int length, int presision, int scale) {
            if (definition == null) {
                throw new IllegalStateException("definition");
            }
            return definition;
        }

        /**
         * 定義の文字列をフォーマットします。
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
