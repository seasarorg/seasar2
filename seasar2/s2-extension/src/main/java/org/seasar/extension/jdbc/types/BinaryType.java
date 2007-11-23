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

import org.seasar.extension.jdbc.ValueType;

/**
 * Binary用の {@link ValueType}です。
 * 
 * @author higa
 */
public class BinaryType extends AbstractBinaryType {

    /**
     * インスタンスを構築します。
     */
    public BinaryType() {
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        try {
            return toByteArray(resultSet.getBlob(index));
        } catch (SQLException e) {
            return resultSet.getBytes(index);
        }
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        try {
            return toByteArray(resultSet.getBlob(columnName));
        } catch (SQLException e) {
            return resultSet.getBytes(columnName);
        }
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        try {
            return toByteArray(cs.getBlob(index));
        } catch (SQLException e) {
            return cs.getBytes(index);
        }
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        try {
            return toByteArray(cs.getBlob(parameterName));
        } catch (SQLException e) {
            return cs.getBytes(parameterName);
        }
    }

}
