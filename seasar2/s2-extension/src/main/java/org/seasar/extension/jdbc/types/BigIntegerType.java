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

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.util.BigDecimalConversionUtil;
import org.seasar.framework.util.BigIntegerConversionUtil;

/**
 * BigInteger用の {@link ValueType}です。
 * 
 * @author higa
 */
public class BigIntegerType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public BigIntegerType() {
        super(Types.BIGINT);
    }

    public Object getValue(final ResultSet resultSet, final int index)
            throws SQLException {
        return BigIntegerConversionUtil.toBigInteger(resultSet
                .getBigDecimal(index));
    }

    public Object getValue(final ResultSet resultSet, final String columnName)
            throws SQLException {
        return BigIntegerConversionUtil.toBigInteger(resultSet
                .getBigDecimal(columnName));
    }

    public Object getValue(final CallableStatement cs, final int index)
            throws SQLException {
        return BigIntegerConversionUtil.toBigInteger(cs.getBigDecimal(index));
    }

    public Object getValue(final CallableStatement cs,
            final String parameterName) throws SQLException {
        return BigIntegerConversionUtil.toBigInteger(cs
                .getBigDecimal(parameterName));
    }

    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setBigDecimal(index, BigDecimalConversionUtil
                    .toBigDecimal(value));
        }
    }

    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setBigDecimal(parameterName, BigDecimalConversionUtil
                    .toBigDecimal(value));
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        BigDecimal var = BigDecimalConversionUtil.toBigDecimal(value);
        return BindVariableUtil.toText(var);
    }

}
