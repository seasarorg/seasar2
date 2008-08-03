/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.sqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * @author taedium
 * 
 */
public class TimestampType extends AbstractSqlType {

    public TimestampType() {
        this("timestamp");
    }

    public TimestampType(String columnDefinition) {
        super(columnDefinition);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIMESTAMP);
        }
        Timestamp timestamp = TimestampConversionUtil.toTimestamp(value,
                "yyyy-MM-dd hh:mm:ss");
        int pos = value.indexOf('.');
        if (pos > -1) {
            int nanos = IntegerConversionUtil.toPrimitiveInt(value
                    .substring(pos));
            timestamp.setNanos(nanos);
        }
        ps.setTimestamp(index, timestamp);
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        Timestamp value = resultSet.getTimestamp(index);
        return value != null ? value.toString() : null;
    }

}