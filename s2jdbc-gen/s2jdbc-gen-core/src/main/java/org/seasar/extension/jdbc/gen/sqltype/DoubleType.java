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
import java.sql.Types;

import org.seasar.framework.util.DoubleConversionUtil;
import org.seasar.framework.util.StringConversionUtil;

/**
 * @author taedium
 * 
 */
public class DoubleType extends AbstractSqlType {

    public DoubleType() {
        this("double");
    }

    public DoubleType(String columnDefinition) {
        super(columnDefinition);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DOUBLE);
        }
        ps.setDouble(index, DoubleConversionUtil.toPrimitiveDouble(value));
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        Double value = resultSet.getDouble(index);
        return value != null ? StringConversionUtil.toString(value) : null;
    }

}
