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
import java.sql.Types;

import org.seasar.extension.jdbc.gen.sqltype.SqlType;

/**
 * {@link Types#CHAR}に対応する{@link SqlType}です。
 * 
 * @author taedium
 */
public class CharType extends AbstractSqlType {

    /**
     * インスタンスを構築します。
     */
    public CharType() {
        this("char(1)");
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dataType
     *            データ型
     */
    public CharType(String dataType) {
        super(dataType);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.CHAR);
        } else {
            ps.setString(index, value);
        }
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        String value = resultSet.getString(index);
        if (value == null) {
            return null;
        }
        final char[] chars = value.toCharArray();
        if (chars.length == 1) {
            return new String(chars);
        }
        if (chars.length == 0) {
            return null;
        }
        throw new IllegalStateException("length of String should be 1."
                + " actual is [" + value + "]");
    }

}