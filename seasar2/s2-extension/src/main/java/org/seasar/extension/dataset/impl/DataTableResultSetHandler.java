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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.PropertyTypeUtil;

/**
 * {@link DataTable}用の {@link ResultSetHandler}です。
 * 
 * @author higa
 * 
 */
public class DataTableResultSetHandler implements ResultSetHandler {

    private String tableName;

    /**
     * {@link DataTableResultSetHandler}を作成します。
     * 
     * @param tableName
     */
    public DataTableResultSetHandler(String tableName) {
        this.tableName = tableName;
    }

    public Object handle(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        PropertyType[] propertyTypes = PropertyTypeUtil
                .createPropertyTypes(rsmd);
        DataTable table = new DataTableImpl(tableName);
        for (int i = 0; i < propertyTypes.length; ++i) {
            String columnName = propertyTypes[i].getColumnName();
            table.addColumn(columnName, ColumnTypes.getColumnType(rsmd
                    .getColumnType(i + 1)));
        }
        DatabaseMetaData dbMetaData = rs.getStatement().getConnection()
                .getMetaData();
        table.setupMetaData(dbMetaData);
        while (rs.next()) {
            addRow(rs, propertyTypes, table);
        }
        return table;
    }

    private void addRow(ResultSet rs, PropertyType[] propertyTypes,
            DataTable table) throws SQLException {

        DataRow row = table.addRow();
        for (int i = 0; i < propertyTypes.length; ++i) {
            Object value = propertyTypes[i].getValueType().getValue(rs, i + 1);
            row.setValue(propertyTypes[i].getColumnName(), value);
        }
        row.setState(RowStates.UNCHANGED);
    }
}