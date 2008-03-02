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

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlType;

/**
 * 標準的な方言をあつかうクラスです。
 * 
 * @author taedium
 */
public class StandardGenDialect implements GenDialect {

    /** SQL型をキー、Javaクラスを値とするマップ */
    protected Map<Integer, Class<?>> javaTypeMap = new HashMap<Integer, Class<?>>();

    protected Map<Integer, SqlType> sqlTypeMap = new HashMap<Integer, SqlType>();

    protected DbmsDialect dbmsDialect;

    /**
     * インスタンスを構築します。
     */
    public StandardGenDialect(DbmsDialect dbmsDialect) {
        this.dbmsDialect = dbmsDialect;
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

        sqlTypeMap.put(Types.ARRAY, StandardSqlType.ARRAY);
        sqlTypeMap.put(Types.BIGINT, StandardSqlType.BIGINT);
        sqlTypeMap.put(Types.BINARY, StandardSqlType.BYNARY);
        sqlTypeMap.put(Types.BIT, StandardSqlType.BIT);
        sqlTypeMap.put(Types.BLOB, StandardSqlType.BLOB);
        sqlTypeMap.put(Types.BOOLEAN, StandardSqlType.BLOOEAN);
        sqlTypeMap.put(Types.CHAR, StandardSqlType.CHAR);
        sqlTypeMap.put(Types.CLOB, StandardSqlType.CLOB);
        sqlTypeMap.put(Types.DATALINK, StandardSqlType.DATALINK);
        sqlTypeMap.put(Types.DATE, StandardSqlType.DATE);
        sqlTypeMap.put(Types.DECIMAL, StandardSqlType.DECIMAL);
        sqlTypeMap.put(Types.DISTINCT, StandardSqlType.DISTINCT);
        sqlTypeMap.put(Types.DOUBLE, StandardSqlType.DOUBLE);
        sqlTypeMap.put(Types.FLOAT, StandardSqlType.FLOAT);
        sqlTypeMap.put(Types.INTEGER, StandardSqlType.INTEGER);
        sqlTypeMap.put(Types.JAVA_OBJECT, StandardSqlType.JAVA_OBJECT);
        sqlTypeMap.put(Types.LONGVARBINARY, StandardSqlType.LONGVARBINARY);
        sqlTypeMap.put(Types.LONGVARCHAR, StandardSqlType.LONGVARBINARY);
        sqlTypeMap.put(Types.NULL, StandardSqlType.NULL);
        sqlTypeMap.put(Types.NUMERIC, StandardSqlType.NUMERIC);
        sqlTypeMap.put(Types.OTHER, StandardSqlType.OTHER);
        sqlTypeMap.put(Types.REAL, StandardSqlType.REAL);
        sqlTypeMap.put(Types.REF, StandardSqlType.REF);
        sqlTypeMap.put(Types.SMALLINT, StandardSqlType.SMALLINT);
        sqlTypeMap.put(Types.STRUCT, StandardSqlType.STRUCT);
        sqlTypeMap.put(Types.TIME, StandardSqlType.TIME);
        sqlTypeMap.put(Types.TIMESTAMP, StandardSqlType.TIMESTAMP);
        sqlTypeMap.put(Types.TINYINT, StandardSqlType.TINYINT);
        sqlTypeMap.put(Types.VARBINARY, StandardSqlType.VARBINARY);
        sqlTypeMap.put(Types.VARCHAR, StandardSqlType.VARCHAR);

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

    public SqlType getSqlType(int sqlType) {
        return sqlTypeMap.get(sqlType);
    }

    public DbmsDialect getDbmsDialect() {
        return dbmsDialect;
    }

    public static enum StandardSqlType implements SqlType {

        ARRAY,

        BIGINT,

        BYNARY,

        BIT,

        BLOB,

        BLOOEAN,

        CHAR {

            @Override
            public String toText(int length, int presision, int scale) {
                return format("char(%d)", length);
            }

        },

        CLOB,

        DATE,

        DATALINK,

        DECIMAL,

        DISTINCT,

        DOUBLE,

        FLOAT,

        INTEGER,

        JAVA_OBJECT,

        LONGVARBINARY,

        LONGVARCHAR,

        NULL,

        NUMERIC,

        OTHER,

        REAL,

        REF,

        SMALLINT,

        STRUCT,

        TIME,

        TIMESTAMP,

        TINYINT,

        VARCHAR {

            @Override
            public String toText(int length, int presision, int scale) {
                return format("varchar(%d)", length);
            }
        },

        VARBINARY {

            @Override
            public String toText(int length, int presision, int scale) {
                return format("varbinary(%d)", length);
            }
        },
        ;

        public String toText(int length, int presision, int scale) {
            return name().toLowerCase();
        }

        protected String format(String format, Object... args) {
            return new Formatter().format(format, args).toString();
        }

    }

}
