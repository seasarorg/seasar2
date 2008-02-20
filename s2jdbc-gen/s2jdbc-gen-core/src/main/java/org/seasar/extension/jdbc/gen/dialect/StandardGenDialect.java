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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.GenDialect;

/**
 * @author taedium
 * 
 */
public class StandardGenDialect implements GenDialect {

    protected Map<Integer, Class<?>> javaTypeMap = new HashMap<Integer, Class<?>>();

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

    public String getDefaultSchema(String userName) {
        return userName;
    }

    public Class<?> getJavaType(int sqlType, String typeName, boolean nullable) {
        return javaTypeMap.get(sqlType);
    }

}
