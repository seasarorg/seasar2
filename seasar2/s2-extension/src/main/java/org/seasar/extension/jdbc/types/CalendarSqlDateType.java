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
import java.util.Calendar;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.util.CalendarConversionUtil;

/**
 * {@link java.sql.Date}と互換性をもつ{@link Calendar}用の{@link ValueType}です。
 * 
 * @author taedium
 * 
 */
public class CalendarSqlDateType extends SqlDateType {

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return toCalendar(super.getValue(resultSet, index));
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toCalendar(super.getValue(resultSet, columnName));
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return toCalendar(super.getValue(cs, index));
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toCalendar(super.getValue(cs, parameterName));
    }

    /**
     * {@link Calendar}に変換します。
     * 
     * @param value
     *            値
     * @return {@link Calendar}
     */
    protected Calendar toCalendar(Object value) {
        return CalendarConversionUtil.toCalendar(value);
    }

    protected java.sql.Date toSqlDate(Object value) {
        Calendar calendar = CalendarConversionUtil.localize(toCalendar(value));
        Calendar base = Calendar.getInstance();
        base.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        base.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        base.set(Calendar.DATE, calendar.get(Calendar.DATE));
        base.set(Calendar.HOUR_OF_DAY, 0);
        base.set(Calendar.MINUTE, 0);
        base.set(Calendar.SECOND, 0);
        base.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(base.getTimeInMillis());
    }
}
