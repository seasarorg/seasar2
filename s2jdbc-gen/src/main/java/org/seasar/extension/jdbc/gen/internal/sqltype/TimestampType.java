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
package org.seasar.extension.jdbc.gen.internal.sqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * {@link Types#TIMESTAMP}に対応する{@link SqlType}です。
 * 
 * @author taedium
 */
public class TimestampType extends AbstractSqlType {

    /**
     * インスタンスを構築します。
     */
    public TimestampType() {
        this("timestamp");
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dataType
     *            データ型
     */
    public TimestampType(String dataType) {
        super(dataType);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(index, toTimestamp(value));
        }
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        Timestamp value = resultSet.getTimestamp(index);
        if (value == null) {
            return null;
        }
        return value.toString().replace('-', '/');
    }

    /**
     * {@link Timestamp}へ変換します。
     * <p>
     * nano秒を考慮した変換を行います。
     * </p>
     * 
     * @param value
     *            yyyy-MM-dd HH:mm:ss.fffffffff 形式の文字列
     * @return {@link Timestamp}の値
     */
    protected Timestamp toTimestamp(String value) {
        String pattern = null;
        if (value.contains("-")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        } else {
            pattern = "yyyy/MM/dd HH:mm:ss";
        }
        Timestamp timestamp = TimestampConversionUtil.toTimestamp(value,
                pattern);
        int pos = value.indexOf('.');
        if (pos > -1) {
            int nanos = IntegerConversionUtil.toPrimitiveInt(value
                    .substring(pos + 1));
            if (nanos > 0) {
                int n = (int) Math.log10(nanos);
                if (n < 8) {
                    nanos *= (int) Math.pow(10, 8 - n);
                }
            }
            timestamp.setNanos(nanos);
        }
        return timestamp;
    }
}