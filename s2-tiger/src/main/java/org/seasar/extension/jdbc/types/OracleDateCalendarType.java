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
import java.sql.SQLException;
import java.util.Calendar;

import oracle.sql.DATE;

import org.seasar.extension.jdbc.ValueType;


/**
 * {@link Calendar}型をOracle固有の{@literal DATE}型として扱う{@link ValueType}です。
 * 
 * @author koichik
 */
public class OracleDateCalendarType extends CalendarTimestampType {

    @Override
    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setObject(index, toOracleDate(value));
        }
    }

    @Override
    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setObject(parameterName, toOracleDate(value));
        }
    }

    /**
     * Oracle固有の{@literal DATE}型に変換して返します。
     * 
     * @param value
     *            値
     * @return Oracle固有の{@literal DATE}型
     */
    protected DATE toOracleDate(final Object value) {
        return new DATE(toTimestamp(value));
    }

}
