/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.sqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;

/**
 * {@link Types#TIME}に対応する{@link SqlType}です。
 * 
 * @author taedium
 */
public class TimeType extends AbstractSqlType {

    /**
     * インスタンスを構築します。
     */
    public TimeType() {
        this("time");
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dataType
     *            データ型
     */
    public TimeType(String dataType) {
        super(dataType);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIME);
        } else {
            ps.setTime(index, TimeConversionUtil.toTime(value, "hh:mm:ss"));
        }
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        Time value = resultSet.getTime(index);
        return value != null ? StringConversionUtil.toString(value, "hh:mm:ss")
                : null;
    }

}