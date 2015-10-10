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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * {@link Time}と互換性をもつ{@link Date}用の{@link ValueType}です。
 * 
 * @author taedium
 * 
 */
public class DateTimeType extends TimeType {

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return toDate(super.getValue(resultSet, index));
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toDate(super.getValue(resultSet, columnName));
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return toDate(super.getValue(cs, index));
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toDate(super.getValue(cs, parameterName));
    }

    /**
     * {@link Date}に変換します。
     * 
     * @param value
     *            値
     * @return {@link Date}
     */
    protected Date toDate(Object value) {
        if (value instanceof Time || value instanceof Calendar) {
            return TimeConversionUtil.toTime(value);
        }
        try {
            return DateConversionUtil.toDate(value, TimestampConversionUtil
                    .getPattern(Locale.getDefault()));
        } catch (ParseRuntimeException e) {
            return TimeConversionUtil.toTime(value);
        }
    }

    protected Time toTime(Object value) {
        Calendar base = Calendar.getInstance();
        base.setTime(toDate(value));
        base.set(Calendar.YEAR, 1970);
        base.set(Calendar.MONTH, Calendar.JANUARY);
        base.set(Calendar.DATE, 1);
        return new Time(base.getTimeInMillis());
    }
}
