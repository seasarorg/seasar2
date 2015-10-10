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
package org.seasar.framework.mock.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ResultSetMetaData}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockResultSetMetaData implements ResultSetMetaData {

    private List columnMetaDataList = new ArrayList();

    /**
     * カラムのメタデータを返します。
     * 
     * @param index
     *            インデックス
     * @return カラムのメタデータ
     */
    public MockColumnMetaData getColumnMetaData(int index) {
        return (MockColumnMetaData) columnMetaDataList.get(index - 1);
    }

    /**
     * カラムのメタデータを追加します。
     * 
     * @param columnMetaData
     *            カラムのメタデータ
     */
    public void addColumnMetaData(MockColumnMetaData columnMetaData) {
        columnMetaDataList.add(columnMetaData);
    }

    public String getCatalogName(int column) throws SQLException {
        return getColumnMetaData(column).getCatalogName();
    }

    public String getColumnClassName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnClassName();
    }

    public int getColumnCount() throws SQLException {
        return columnMetaDataList.size();
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        return getColumnMetaData(column).getColumnDisplaySize();
    }

    public String getColumnLabel(int column) throws SQLException {
        return getColumnMetaData(column).getColumnLabel();
    }

    public String getColumnName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnName();
    }

    public int getColumnType(int column) throws SQLException {
        return getColumnMetaData(column).getColumnType();
    }

    public String getColumnTypeName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnTypeName();
    }

    public int getPrecision(int column) throws SQLException {
        return getColumnMetaData(column).getPrecision();
    }

    public int getScale(int column) throws SQLException {
        return getColumnMetaData(column).getScale();
    }

    public String getSchemaName(int column) throws SQLException {
        return getColumnMetaData(column).getSchemaName();
    }

    public String getTableName(int column) throws SQLException {
        return getColumnMetaData(column).getTableName();
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        return getColumnMetaData(column).isAutoIncrement();
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        return getColumnMetaData(column).isCaseSensitive();
    }

    public boolean isCurrency(int column) throws SQLException {
        return getColumnMetaData(column).isCurrency();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        return getColumnMetaData(column).isDefinitelyWritable();
    }

    public int isNullable(int column) throws SQLException {
        return getColumnMetaData(column).isNullable();
    }

    public boolean isReadOnly(int column) throws SQLException {
        return getColumnMetaData(column).isReadOnly();
    }

    public boolean isSearchable(int column) throws SQLException {
        return getColumnMetaData(column).isSearchable();
    }

    public boolean isSigned(int column) throws SQLException {
        return getColumnMetaData(column).isSigned();
    }

    public boolean isWritable(int column) throws SQLException {
        return getColumnMetaData(column).isWritable();
    }

    /**
     * カラム番号を返します。
     * 
     * @param columnName
     *            カラム名
     * @return カラム番号
     * @throws SQLException
     *             カラム名が見つからなかった場合。
     */
    public int findColumn(String columnName) throws SQLException {
        for (int i = 1; i <= getColumnCount(); i++) {
            MockColumnMetaData columnMetaData = getColumnMetaData(i);
            if (columnName.equals(columnMetaData.getColumnName())) {
                return i;
            }
        }
        throw new SQLException(columnName + " not found.");
    }
}