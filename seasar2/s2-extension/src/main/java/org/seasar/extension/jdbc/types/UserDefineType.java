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

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.util.MethodUtil;

/**
 * ユーザ定義型用の {@link ValueType}です。
 * 
 * @author higa
 * 
 */
public class UserDefineType implements ValueType {

    private ValueType baseValueType;

    private Method valueOfMethod;

    private Method valueMethod;

    /**
     * {@link UserDefineType}を作成します。
     * 
     * @param baseValueType
     * @param valueOfMethod
     * @param valueMethod
     */
    public UserDefineType(ValueType baseValueType, Method valueOfMethod,
            Method valueMethod) {
        this.baseValueType = baseValueType;
        this.valueOfMethod = valueOfMethod;
        this.valueMethod = valueMethod;
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return fromDbToJava(baseValueType.getValue(resultSet, index));
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {

        return fromDbToJava(baseValueType.getValue(resultSet, columnName));
    }

    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {

        baseValueType.bindValue(ps, index, fromJavaToDb(value));
    }

    private Object fromDbToJava(Object value) {
        if (value == null) {
            return null;
        }
        return MethodUtil.invoke(valueOfMethod, null, new Object[] { value });
    }

    private Object fromJavaToDb(Object value) {
        if (value == null) {
            return null;
        }
        return MethodUtil.invoke(valueMethod, value, null);
    }
}