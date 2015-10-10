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

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.util.StringConversionUtil;

/**
 * Character用の {@link ValueType}です。
 * 
 * @author manhole
 */
public class CharacterType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public CharacterType() {
        super(Types.CHAR);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return toCharacter(resultSet.getString(index));
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toCharacter(resultSet.getString(columnName));
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return toCharacter(cs.getString(index));
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toCharacter(cs.getString(parameterName));
    }

    private Character toCharacter(final String value) {
        if (value == null) {
            return null;
        }
        final char[] chars = value.toCharArray();
        if (chars.length == 1) {
            return new Character(chars[0]);
        }
        if (chars.length == 0) {
            return null;
        }
        throw new IllegalStateException("length of String should be 1."
                + " actual is [" + value + "]");
    }

    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setString(index, StringConversionUtil.toString(value));
        }
    }

    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setString(parameterName, StringConversionUtil.toString(value));
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        String var = StringConversionUtil.toString(value);
        return BindVariableUtil.toText(var);
    }

}