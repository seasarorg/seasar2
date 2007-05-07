/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.types;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.jdbc.ValueType;

public abstract class ValueTypes {

    public final static ValueType STRING = new StringType();

    public final static ValueType CHARACTER = new CharacterType();

    public final static ValueType SHORT = new ShortType();

    public final static ValueType INTEGER = new IntegerType();

    public final static ValueType LONG = new LongType();

    public final static ValueType FLOAT = new FloatType();

    public final static ValueType DOUBLE = new DoubleType();

    public final static ValueType BIGDECIMAL = new BigDecimalType();

    public final static ValueType TIME = new TimeType();

    public final static ValueType SQLDATE = new SqlDateType();

    public final static ValueType TIMESTAMP = new TimestampType();

    public final static ValueType BINARY = new BinaryType();

    public final static ValueType BINARY_STREAM = new BinaryStreamType();

    public final static ValueType BOOLEAN = new BooleanType();

    public final static ValueType OBJECT = new ObjectType();

    private static final Class BYTE_ARRAY_CLASS = new byte[0].getClass();

    private static Map types_ = new HashMap();

    static {
        registerValueType(String.class, STRING);
        registerValueType(char.class, CHARACTER);
        registerValueType(Character.class, CHARACTER);
        registerValueType(short.class, SHORT);
        registerValueType(Short.class, SHORT);
        registerValueType(int.class, INTEGER);
        registerValueType(Integer.class, INTEGER);
        registerValueType(long.class, LONG);
        registerValueType(Long.class, LONG);
        registerValueType(float.class, FLOAT);
        registerValueType(Float.class, FLOAT);
        registerValueType(double.class, DOUBLE);
        registerValueType(Double.class, DOUBLE);
        registerValueType(BigDecimal.class, BIGDECIMAL);
        registerValueType(java.sql.Date.class, SQLDATE);
        registerValueType(java.sql.Time.class, TIME);
        registerValueType(java.util.Date.class, TIMESTAMP);
        registerValueType(Timestamp.class, TIMESTAMP);
        registerValueType(Calendar.class, TIMESTAMP);
        registerValueType(BYTE_ARRAY_CLASS, BINARY);
        registerValueType(InputStream.class, BINARY_STREAM);
        registerValueType(boolean.class, BOOLEAN);
        registerValueType(Boolean.class, BOOLEAN);
        registerValueType(Object.class, OBJECT);
    }

    protected ValueTypes() {
    }

    public static void registerValueType(Class clazz, ValueType valueType) {
        synchronized (types_) {
            types_.put(clazz, valueType);
        }
    }

    public static ValueType getValueType(Object obj) {
        if (obj == null) {
            return OBJECT;
        }
        return getValueType(obj.getClass());
    }

    public static ValueType getValueType(Class clazz) {
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            ValueType valueType = getValueType0(c);
            if (valueType != null) {
                return valueType;
            }
        }
        return OBJECT;
    }

    protected static ValueType getValueType0(Class clazz) {
        return (ValueType) types_.get(clazz);
    }

    public static Class getType(int sqltype) {
        switch (sqltype) {
        case Types.TINYINT:
        case Types.SMALLINT:
            return Short.class;
        case Types.INTEGER:
            return Integer.class;
        case Types.BIGINT:
            return Long.class;
        case Types.REAL:
        case Types.FLOAT:
            return Float.class;
        case Types.DOUBLE:
            return Double.class;
        case Types.DECIMAL:
        case Types.NUMERIC:
            return BigDecimal.class;
        case Types.DATE:
            return Timestamp.class;
        case Types.TIME:
            return java.sql.Time.class;
        case Types.TIMESTAMP:
            return Timestamp.class;
        case Types.BINARY:
        case Types.BLOB:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
            return BYTE_ARRAY_CLASS;
        case Types.CHAR:
        case Types.LONGVARCHAR:
        case Types.VARCHAR:
            return String.class;
        case Types.BOOLEAN:
            return Boolean.class;
        default:
            return Object.class;
        }
    }

    public static ValueType getValueType(int sqltype) {
        return getValueType(getType(sqltype));
    }
}
