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
package org.seasar.extension.dataset.states;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.PrimaryKeyNotFoundRuntimeException;
import org.seasar.extension.dataset.RowState;

/**
 * 削除用の {@link RowState}です。
 * 
 * @author higa
 * 
 */
public class RemovedState extends AbstractRowState {

    public String toString() {
        return "REMOVED";
    }

    protected SqlContext getSqlContext(DataRow row) {
        DataTable table = row.getTable();
        StringBuffer buf = new StringBuffer(100);
        List argList = new ArrayList();
        List argTypeList = new ArrayList();
        buf.append("DELETE FROM ");
        buf.append(table.getTableName());
        buf.append(" WHERE ");
        boolean hasPrimaryKey = false;
        for (int i = 0; i < table.getColumnSize(); ++i) {
            DataColumn column = table.getColumn(i);
            if (column.isPrimaryKey()) {
                hasPrimaryKey = true;
                buf.append(column.getColumnName());
                buf.append(" = ? AND ");
                argList.add(row.getValue(i));
                argTypeList.add(column.getColumnType().getType());
            }
        }
        if (!hasPrimaryKey) {
            throw new PrimaryKeyNotFoundRuntimeException(table.getTableName());
        }
        buf.setLength(buf.length() - 5);
        return new SqlContext(buf.toString(), argList.toArray(),
                (Class[]) argTypeList.toArray(new Class[argTypeList.size()]));
    }
    /*
     * protected String getSql(DataTable table) { String sql = (String)
     * sqlCache_.get(table); if (sql == null) { sql = createSql(table);
     * sqlCache_.put(table, sql); } return sql; }
     * 
     * private static String createSql(DataTable table) { StringBuffer buf = new
     * StringBuffer(100); buf.append("DELETE FROM ");
     * buf.append(table.getTableName()); buf.append(" WHERE "); for (int i = 0;
     * i < table.getColumnSize(); ++i) { DataColumn column = table.getColumn(i);
     * if (column.isPrimaryKey()) { buf.append(column.getColumnName());
     * buf.append(" = ? AND "); } } buf.setLength(buf.length() - 5); return
     * buf.toString(); }
     * 
     * protected Object[] getArgs(DataRow row) { DataTable table =
     * row.getTable(); List bindVariables = new ArrayList(); for (int i = 0; i <
     * table.getColumnSize(); ++i) { DataColumn column = table.getColumn(i); if
     * (column.isPrimaryKey()) { bindVariables.add(row.getValue(i)); } } return
     * bindVariables.toArray(); }
     */
}