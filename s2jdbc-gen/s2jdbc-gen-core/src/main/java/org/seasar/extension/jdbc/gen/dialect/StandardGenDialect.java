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
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.DataType;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.JavaType;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、@{JavaType Java型}を値とするマップ */
    protected Map<Integer, JavaType> javaTypeMap = new HashMap<Integer, JavaType>();

    /** SQL型をキー、@{DataType データ型}を値とするマップ */
    protected Map<Integer, DataType> dataTypeMap = new HashMap<Integer, DataType>();

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

        dataTypeMap.put(Types.ARRAY, StandardDataType.ARRAY);
        dataTypeMap.put(Types.BIGINT, StandardDataType.BIGINT);
        dataTypeMap.put(Types.BINARY, StandardDataType.BINARY);
        dataTypeMap.put(Types.BIT, StandardDataType.BIT);
        dataTypeMap.put(Types.BLOB, StandardDataType.BLOB);
        dataTypeMap.put(Types.BOOLEAN, StandardDataType.BOOLEAN);
        dataTypeMap.put(Types.CHAR, StandardDataType.CHAR);
        dataTypeMap.put(Types.CLOB, StandardDataType.CLOB);
        dataTypeMap.put(Types.DATALINK, StandardDataType.DATALINK);
        dataTypeMap.put(Types.DATE, StandardDataType.DATE);
        dataTypeMap.put(Types.DECIMAL, StandardDataType.DECIMAL);
        dataTypeMap.put(Types.DISTINCT, StandardDataType.DISTINCT);
        dataTypeMap.put(Types.DOUBLE, StandardDataType.DOUBLE);
        dataTypeMap.put(Types.FLOAT, StandardDataType.FLOAT);
        dataTypeMap.put(Types.INTEGER, StandardDataType.INTEGER);
        dataTypeMap.put(Types.JAVA_OBJECT, StandardDataType.JAVA_OBJECT);
        dataTypeMap.put(Types.LONGVARBINARY, StandardDataType.LONGVARBYNARY);
        dataTypeMap.put(Types.LONGVARCHAR, StandardDataType.LONGVARCHAR);
        dataTypeMap.put(Types.NULL, StandardDataType.NULL);
        dataTypeMap.put(Types.NUMERIC, StandardDataType.NUMERIC);
        dataTypeMap.put(Types.OTHER, StandardDataType.OTHER);
        dataTypeMap.put(Types.REAL, StandardDataType.REAL);
        dataTypeMap.put(Types.REF, StandardDataType.REF);
        dataTypeMap.put(Types.SMALLINT, StandardDataType.SMALLINT);
        dataTypeMap.put(Types.STRUCT, StandardDataType.STRUCT);
        dataTypeMap.put(Types.TIME, StandardDataType.TIME);
        dataTypeMap.put(Types.TIMESTAMP, StandardDataType.TIMESTAMP);
        dataTypeMap.put(Types.TINYINT, StandardDataType.TINYINT);
        dataTypeMap.put(Types.VARBINARY, StandardDataType.VARBINARY);
        dataTypeMap.put(Types.VARCHAR, StandardDataType.VARCHAR);
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

    public DataType getDataType(int sqlType) {
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

    public String getBlockDelimiter() {
        return null;
    }

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

        protected StandardJavaType() {
            this.clazz = clazz;
        }

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
     * 標準的な{@link DataType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardDataType implements DataType {

        private static DataType ARRAY = new StandardDataType("array");

        private static DataType BIGINT = new StandardDataType("bigint");

        private static DataType BIT = new StandardDataType("bit");

        private static DataType BINARY = new StandardDataType("binary");

        private static DataType BLOB = new StandardDataType("blob");

        private static DataType BOOLEAN = new StandardDataType("boolean");

        private static DataType CHAR = new StandardDataType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("char(%d)", length);
            }
        };

        private static DataType CLOB = new StandardDataType("clob");

        private static DataType DATE = new StandardDataType("date");

        private static DataType DATALINK = new StandardDataType("datalink");

        private static DataType DECIMAL = new StandardDataType("decimal");

        private static DataType DISTINCT = new StandardDataType("distinct");

        private static DataType DOUBLE = new StandardDataType("double");

        private static DataType FLOAT = new StandardDataType("float");

        private static DataType INTEGER = new StandardDataType("integer");

        private static DataType JAVA_OBJECT = new StandardDataType(
                "java_object");

        private static DataType LONGVARBYNARY = new StandardDataType(
                "longvarbynary");

        private static DataType LONGVARCHAR = new StandardDataType(
                "longvarchar");

        private static DataType NULL = new StandardDataType("null");

        private static DataType NUMERIC = new StandardDataType("numeric");

        private static DataType OTHER = new StandardDataType("other");

        private static DataType REAL = new StandardDataType("real");

        private static DataType REF = new StandardDataType("ref");

        private static DataType SMALLINT = new StandardDataType("smallint");

        private static DataType STRUCT = new StandardDataType("struct");

        private static DataType TIME = new StandardDataType("time");

        private static DataType TIMESTAMP = new StandardDataType("timestamp");

        private static DataType TINYINT = new StandardDataType("tinyint");

        private static DataType VARBINARY = new StandardDataType("varbinary") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format("varbinary(%d)", length);
            }
        };

        private static DataType VARCHAR = new StandardDataType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varchar(%d)", length);
            }
        };

        /** 定義 */
        private String definition;

        protected StandardDataType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected StandardDataType(String definition) {
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
