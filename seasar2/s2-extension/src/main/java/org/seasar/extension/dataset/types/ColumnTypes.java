/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.extension.dataset.types;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class ColumnTypes {

    public static final ColumnType STRING = new StringType();

    public static final ColumnType BIGDECIMAL = new BigDecimalType();

    public static final ColumnType TIMESTAMP = new TimestampType();

    public static final ColumnType BINARY = new BinaryType();

    public static final ColumnType OBJECT = new ObjectType();

    public static final ColumnType BOOLEAN = new BooleanType();

    private static Map types_ = new HashMap();

    static {
        types_.put(String.class, STRING);
        types_.put(short.class, BIGDECIMAL);
        types_.put(Short.class, BIGDECIMAL);
        types_.put(int.class, BIGDECIMAL);
        types_.put(Integer.class, BIGDECIMAL);
        types_.put(long.class, BIGDECIMAL);
        types_.put(Long.class, BIGDECIMAL);
        types_.put(float.class, BIGDECIMAL);
        types_.put(Float.class, BIGDECIMAL);
        types_.put(double.class, BIGDECIMAL);
        types_.put(Double.class, BIGDECIMAL);
        types_.put(boolean.class, BOOLEAN);
        types_.put(Boolean.class, BOOLEAN);
        types_.put(BigDecimal.class, BIGDECIMAL);
        types_.put(Timestamp.class, TIMESTAMP);
        types_.put(java.sql.Date.class, TIMESTAMP);
        types_.put(java.util.Date.class, TIMESTAMP);
        types_.put(Calendar.class, TIMESTAMP);
        types_.put(new byte[0].getClass(), BINARY);
    }

    public static ValueType getValueType(int type) {
        switch (type) {
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
        case Types.REAL:
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.DECIMAL:
        case Types.NUMERIC:
            return ValueTypes.BIGDECIMAL;
        case Types.BOOLEAN:
            return ValueTypes.BOOLEAN;
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            return ValueTypes.TIMESTAMP;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
            return ValueTypes.BINARY;
        case Types.CHAR:
        case Types.LONGVARCHAR:
        case Types.VARCHAR:
            return ValueTypes.STRING;
        default:
            return ValueTypes.OBJECT;
        }
    }

    public static ColumnType getColumnType(int type) {
        switch (type) {
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
        case Types.REAL:
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.DECIMAL:
        case Types.NUMERIC:
            return BIGDECIMAL;
        case Types.BOOLEAN:
            return BOOLEAN;
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            return TIMESTAMP;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
            return BINARY;
        case Types.CHAR:
        case Types.LONGVARCHAR:
        case Types.VARCHAR:
            return STRING;
        default:
            return OBJECT;
        }
    }

    public static ColumnType getColumnType(Object value) {
        if (value == null) {
            return OBJECT;
        }
        return getColumnType(value.getClass());
    }

    public static ColumnType getColumnType(Class clazz) {
        ColumnType columnType = getColumnType0(clazz);
        if (columnType != null) {
            return columnType;
        }
        return OBJECT;
    }

    private static ColumnType getColumnType0(Class clazz) {
        synchronized (types_) {
            return (ColumnType) types_.get(clazz);
        }
    }
}
