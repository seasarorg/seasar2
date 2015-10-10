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
package org.seasar.extension.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * Map用の {@link ResultSetHandler}の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractMapResultSetHandler implements ResultSetHandler {

    /**
     * {@link AbstractMapResultSetHandler}を作成します。
     */
    public AbstractMapResultSetHandler() {
    }

    /**
     * 行を作成します。
     * 
     * @param rs
     *            結果セット
     * @param propertyTypes
     *            プロパティの型の配列
     * @return 行
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Map createRow(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {

        Map row = new CaseInsensitiveMap();
        for (int i = 0; i < propertyTypes.length; ++i) {
            Object value = propertyTypes[i].getValueType().getValue(rs, i + 1);
            row.put(propertyTypes[i].getPropertyName(), value);
        }
        return row;
    }
}