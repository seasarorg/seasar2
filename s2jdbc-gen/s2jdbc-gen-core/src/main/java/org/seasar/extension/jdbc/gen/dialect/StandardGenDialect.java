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

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、Javaクラスを値とするマップ */
    protected Map<Integer, Class<?>> javaTypeMap = new HashMap<Integer, Class<?>>();

    /** SQL型をキー、データ型を値とするマップ */
    protected Map<Integer, DataType> dataTypeMap = new HashMap<Integer, DataType>();

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect() {
        javaTypeMap.put(Types.ARRAY, Object.class);
        javaTypeMap.put(Types.BIGINT, Long.class);
        javaTypeMap.put(Types.BINARY, byte[].class);
        javaTypeMap.put(Types.BIT, Object.class);
        javaTypeMap.put(Types.BLOB, byte[].class);
        javaTypeMap.put(Types.BOOLEAN, Boolean.class);
        javaTypeMap.put(Types.CHAR, String.class);
        javaTypeMap.put(Types.CLOB, String.class);
        javaTypeMap.put(Types.DATALINK, Object.class);
        javaTypeMap.put(Types.DATE, Date.class);
        javaTypeMap.put(Types.DECIMAL, BigDecimal.class);
        javaTypeMap.put(Types.DISTINCT, Object.class);
        javaTypeMap.put(Types.DOUBLE, Double.class);
        javaTypeMap.put(Types.FLOAT, Float.class);
        javaTypeMap.put(Types.INTEGER, Integer.class);
        javaTypeMap.put(Types.JAVA_OBJECT, Object.class);
        javaTypeMap.put(Types.LONGVARBINARY, byte[].class);
        javaTypeMap.put(Types.LONGVARCHAR, String.class);
        javaTypeMap.put(Types.NULL, Object.class);
        javaTypeMap.put(Types.NUMERIC, BigDecimal.class);
        javaTypeMap.put(Types.OTHER, Object.class);
        javaTypeMap.put(Types.REAL, Float.class);
        javaTypeMap.put(Types.REF, Object.class);
        javaTypeMap.put(Types.SMALLINT, Short.class);
        javaTypeMap.put(Types.STRUCT, Object.class);
        javaTypeMap.put(Types.TIME, Date.class);
        javaTypeMap.put(Types.TIMESTAMP, Date.class);
        javaTypeMap.put(Types.TINYINT, Short.class);
        javaTypeMap.put(Types.VARBINARY, byte[].class);
        javaTypeMap.put(Types.VARCHAR, String.class);

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

    public Class<?> getJavaType(int sqlType, String typeName, boolean nullable) {
        return javaTypeMap.get(sqlType);
    }

    public DataType getDataType(int sqlType) {
        return dataTypeMap.get(sqlType);
    }

    public GenerationType getDefaultGenerationType() {
        return GenerationType.TABLE;
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

        private static DataType CHAR = new StandardDataType("char") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
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

        private static DataType VARCHAR = new StandardDataType("varchar") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format("varchar(%d)", length);
            }
        };

        /** 定義 */
        protected String definition;

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
