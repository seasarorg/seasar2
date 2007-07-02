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
package org.seasar.extension.dataset.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.PropertyTypeUtil;

public class DataRowReloadResultSetHandler implements ResultSetHandler {

    private DataRow row_;

    private DataRow newRow_;

    public DataRowReloadResultSetHandler(DataRow row, DataRow newRow) {
        row_ = row;
        newRow_ = newRow;
    }

    public DataRow getRow() {
        return row_;
    }

    public DataRow getNewRow() {
        return newRow_;
    }

    public Object handle(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        PropertyType[] propertyTypes = PropertyTypeUtil
                .createPropertyTypes(rsmd);
        if (rs.next()) {
            reload(rs, propertyTypes);
        }
        return newRow_;
    }

    private void reload(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {
        for (int i = 0; i < propertyTypes.length; ++i) {
            Object value = propertyTypes[i].getValueType().getValue(rs, i + 1);
            newRow_.setValue(propertyTypes[i].getColumnName(), value);
        }
        newRow_.setState(RowStates.UNCHANGED);
    }
}