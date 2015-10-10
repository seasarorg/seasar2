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
package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.extension.jdbc.util.BindVariableUtil;

/**
 * Enumの序数をJDBCで扱うためのクラスです。
 * 
 * @author koichik
 */
public class EnumOrdinalType extends AbstractValueType {

    @SuppressWarnings("unchecked")
    private final Class<? extends Enum> enumClass;

    /**
     * インスタンスを構築します。
     * 
     * @param enumClass
     */
    @SuppressWarnings("unchecked")
    public EnumOrdinalType(Class<? extends Enum> enumClass) {
        super(Types.INTEGER);
        this.enumClass = enumClass;
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        final int ordinal = resultSet.getInt(index);
        if (ordinal == 0 && resultSet.wasNull()) {
            return null;
        }
        return toEnum(ordinal);
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        final int ordinal = resultSet.getInt(columnName);
        if (ordinal == 0 && resultSet.wasNull()) {
            return null;
        }
        return toEnum(ordinal);
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        final int ordinal = cs.getInt(index);
        if (ordinal == 0 && cs.wasNull()) {
            return null;
        }
        return toEnum(ordinal);
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        final int ordinal = cs.getInt(parameterName);
        if (ordinal == 0 && cs.wasNull()) {
            return null;
        }
        return toEnum(ordinal);
    }

    @SuppressWarnings("unchecked")
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setInt(index, (Enum.class.cast(value)).ordinal());
        }
    }

    @SuppressWarnings("unchecked")
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setInt(parameterName, (Enum.class.cast(value)).ordinal());
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        return BindVariableUtil.toText((Enum.class.cast(value)).ordinal());
    }

    /**
     * {@link Enum}に変換します。
     * 
     * @param ordinal
     *            序数
     * @return {@link Enum}
     */
    @SuppressWarnings("unchecked")
    protected Enum toEnum(int ordinal) {
        return enumClass.getEnumConstants()[ordinal];
    }

}