/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import java.util.Map;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.MapUtil;

/**
 * カラムの型を管理するクラスです。
 * 
 * @author higa
 * 
 */
public class ColumnTypes {

    /**
     * 文字列用の {@link ColumnType}です。
     * 
     * @see StringType
     */
    public static final ColumnType STRING = new StringType();

    /**
     * トリムをしない文字列用の {@link ColumnType}です。
     * 
     * @see StringType
     */
    public static final ColumnType NOT_TRIM_STRING = new StringType(false);

    /**
     * 数値用の {@link ColumnType}です。
     * 
     * @see BigDecimalType
     */
    public static final ColumnType BIGDECIMAL = new BigDecimalType();

    /**
     * 日付用の {@link ColumnType}です。
     * 
     * @see TimestampType
     */
    public static final ColumnType TIMESTAMP = new TimestampType();

    /**
     * バイナリ用の {@link ColumnType}です。
     * 
     * @see BinaryType
     */
    public static final ColumnType BINARY = new BinaryType();

    /**
     * オブジェクト用の {@link ColumnType}です。
     * 
     * @see ObjectType
     */
    public static final ColumnType OBJECT = new ObjectType();

    /**
     * 論理値用の {@link ColumnType}です。
     * 
     * @see BooleanType
     */
    public static final ColumnType BOOLEAN = new BooleanType();

    private static Map typesByClass = MapUtil.createHashMap(20);

    private static Map typesBySqlType = MapUtil.createHashMap(20);

    static {
        registerColumnType(String.class, STRING);
        registerColumnType(short.class, BIGDECIMAL);
        registerColumnType(Short.class, BIGDECIMAL);
        registerColumnType(int.class, BIGDECIMAL);
        registerColumnType(Integer.class, BIGDECIMAL);
        registerColumnType(long.class, BIGDECIMAL);
        registerColumnType(Long.class, BIGDECIMAL);
        registerColumnType(float.class, BIGDECIMAL);
        registerColumnType(Float.class, BIGDECIMAL);
        registerColumnType(double.class, BIGDECIMAL);
        registerColumnType(Double.class, BIGDECIMAL);
        registerColumnType(boolean.class, BOOLEAN);
        registerColumnType(Boolean.class, BOOLEAN);
        registerColumnType(BigDecimal.class, BIGDECIMAL);
        registerColumnType(Timestamp.class, TIMESTAMP);
        registerColumnType(java.sql.Date.class, TIMESTAMP);
        registerColumnType(java.util.Date.class, TIMESTAMP);
        registerColumnType(Calendar.class, TIMESTAMP);
        registerColumnType(new byte[0].getClass(), BINARY);

        registerColumnType(Types.TINYINT, BIGDECIMAL);
        registerColumnType(Types.SMALLINT, BIGDECIMAL);
        registerColumnType(Types.INTEGER, BIGDECIMAL);
        registerColumnType(Types.BIGINT, BIGDECIMAL);
        registerColumnType(Types.REAL, BIGDECIMAL);
        registerColumnType(Types.FLOAT, BIGDECIMAL);
        registerColumnType(Types.DOUBLE, BIGDECIMAL);
        registerColumnType(Types.DECIMAL, BIGDECIMAL);
        registerColumnType(Types.NUMERIC, BIGDECIMAL);
        registerColumnType(Types.BOOLEAN, BOOLEAN);
        registerColumnType(Types.DATE, TIMESTAMP);
        registerColumnType(Types.TIME, TIMESTAMP);
        registerColumnType(Types.TIMESTAMP, TIMESTAMP);
        registerColumnType(Types.BINARY, BINARY);
        registerColumnType(Types.VARBINARY, BINARY);
        registerColumnType(Types.LONGVARBINARY, BINARY);
        registerColumnType(Types.CHAR, STRING);
        registerColumnType(Types.LONGVARCHAR, STRING);
        registerColumnType(Types.VARCHAR, STRING);
    }

    /**
     * インスタンスを構築します。
     */
    protected ColumnTypes() {
    }

    /**
     * S2JDBC用の型を返します。
     * 
     * @param type
     *            型
     * @return JDBC用の型
     * @see ValueTypes
     */
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

    /**
     * カラムの型を返します。
     * 
     * @param type
     *            型
     * @return カラムの型
     */
    public static ColumnType getColumnType(int type) {
        ColumnType columnType = (ColumnType) typesBySqlType.get(new Integer(
                type));
        if (columnType != null) {
            return columnType;
        }
        return OBJECT;
    }

    /**
     * カラムの型を返します。
     * 
     * @param value
     *            値
     * @return カラムの型
     */
    public static ColumnType getColumnType(Object value) {
        if (value == null) {
            return OBJECT;
        }
        return getColumnType(value.getClass());
    }

    /**
     * カラムの型を返します。
     * 
     * @param clazz
     *            クラス
     * @return カラムの型
     */
    public static ColumnType getColumnType(Class clazz) {
        ColumnType columnType = (ColumnType) typesByClass.get(clazz);
        if (columnType != null) {
            return columnType;
        }
        return OBJECT;
    }

    /**
     * カラムの型を登録します。
     * 
     * @param sqlType
     *            SQL型
     * @param columnType
     *            カラムの型
     * @return 指定されたSQL型に関連した以前のカラムの型。カラムの型にマッピングなかった場合には<code>null</code>
     */
    public static ColumnType registerColumnType(int sqlType,
            ColumnType columnType) {
        return (ColumnType) typesBySqlType
                .put(new Integer(sqlType), columnType);
    }

    /**
     * カラムの型を登録します。
     * 
     * @param clazz
     *            クラス
     * @param columnType
     *            カラムの型
     * @return 指定されたクラスに関連した以前のカラムの型。カラムの型にマッピングなかった場合には<code>null</code>
     */
    public static ColumnType registerColumnType(Class clazz,
            ColumnType columnType) {
        return (ColumnType) typesByClass.put(clazz, columnType);
    }
}
