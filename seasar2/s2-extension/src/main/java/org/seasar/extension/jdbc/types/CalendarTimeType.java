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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.util.CalendarConversionUtil;

/**
 * {@link Time}と互換性をもつ{@link Calendar}用の{@link ValueType}です。
 * 
 * @author taedium
 * 
 */
public class CalendarTimeType extends TimeType {

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

    protected Time toTime(Object value) {
        if (value == null) {
            return null;
        }
        if (!Calendar.class.isInstance(value)) {
            throw new IllegalArgumentException("value");
        }
        Calendar cal = (Calendar) value;
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        Calendar newCal = Calendar.getInstance();
        newCal.set(1970, Calendar.JANUARY, 1, hourOfDay, minute, second);
        return new Time(newCal.getTimeInMillis());
    }
}
