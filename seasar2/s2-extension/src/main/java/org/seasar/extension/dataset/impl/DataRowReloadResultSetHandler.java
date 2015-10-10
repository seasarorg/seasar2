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
package org.seasar.extension.dataset.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.PropertyTypeUtil;

/**
 * Reload用の {@link ResultSetHandler}です。
 * 
 * @author higa
 * 
 */
public class DataRowReloadResultSetHandler implements ResultSetHandler {

    private DataRow newRow;

    /**
     * {@link DataRowReloadResultSetHandler}を作成します。
     * 
     * @param newRow
     *            新しい行
     */
    public DataRowReloadResultSetHandler(DataRow newRow) {
        this.newRow = newRow;
    }

    /**
     * 新しい行を返します。
     * 
     * @return 新しい行
     */
    public DataRow getNewRow() {
        return newRow;
    }

    public Object handle(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        PropertyType[] propertyTypes = PropertyTypeUtil
                .createPropertyTypes(rsmd);
        if (rs.next()) {
            reload(rs, propertyTypes);
        }
        return newRow;
    }

    private void reload(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {
        for (int i = 0; i < propertyTypes.length; ++i) {
            Object value = propertyTypes[i].getValueType().getValue(rs, i + 1);
            newRow.setValue(propertyTypes[i].getColumnName(), value);
        }
        newRow.setState(RowStates.UNCHANGED);
    }
}