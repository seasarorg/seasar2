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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * Timestamp用の {@link ValueType}です。
 * 
 * @author higa
 * 
 */
public class TimestampType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public TimestampType() {
        super(Types.TIMESTAMP);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getTimestamp(index);
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return resultSet.getTimestamp(columnName);
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getTimestamp(index);
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getTimestamp(parameterName);
    }

    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setTimestamp(index, toTimestamp(value));
        }
    }

    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setTimestamp(parameterName, toTimestamp(value));
        }
    }

    /**
     * {@link Timestamp}に変換します。
     * 
     * @param value
     *            値
     * @return {@link Timestamp}
     */
    protected Timestamp toTimestamp(Object value) {
        if (value instanceof Date || value instanceof Calendar) {
            return TimestampConversionUtil.toTimestamp(value);
        }
        try {
        return TimestampConversionUtil.toTimestamp(value,
                TimestampConversionUtil.getPattern(Locale.getDefault()));
        } catch (ParseRuntimeException e) {
            return TimestampConversionUtil.toTimestamp(value,
                    DateConversionUtil.getPattern(Locale.getDefault()));
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        return BindVariableUtil.toText(toTimestamp(value));
    }
}