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
package org.seasar.extension.jdbc.gen.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.DumpModel;
import org.seasar.extension.jdbc.gen.DumpModelFactory;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.util.DumpUtil;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * @author taedium
 * 
 */
public class DumpModelFactoryImpl implements DumpModelFactory {

    protected DataSource dataSource;

    protected String delimiter;

    /**
     * @param dataSource
     */
    public DumpModelFactoryImpl(DataSource dataSource, String delimiter) {
        this.dataSource = dataSource;
        this.delimiter = delimiter;
    }

    public DumpModel getDumpModel(TableDesc tableDesc) {
        DumpModel dumpModel = new DumpModel();
        dumpModel.setDelimiter(delimiter);
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            dumpModel.addColumnName(columnDesc.getName());
        }
        doRow(dumpModel, tableDesc);
        return dumpModel;
    }

    protected void doRow(DumpModel dumpModel, TableDesc tableDesc) {
        SqlType[] sqlTypes = new SqlType[tableDesc.getColumnDescList().size()];
        for (int i = 0; i < tableDesc.getColumnDescList().size(); i++) {
            ColumnDesc columnDesc = tableDesc.getColumnDescList().get(i);
            sqlTypes[i] = columnDesc.getSqlType();
        }
        String sql = buildSql(tableDesc);
        Connection conn = DataSourceUtil.getConnection(dataSource);
        try {
            PreparedStatement ps = ConnectionUtil.prepareStatement(conn, sql);
            try {
                ResultSet rs = PreparedStatementUtil.executeQuery(ps);
                try {
                    addRows(dumpModel, sqlTypes, rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                } finally {
                    ResultSetUtil.close(rs);
                }
            } finally {
                StatementUtil.close(ps);
            }
        } finally {
            ConnectionUtil.close(conn);
        }
    }

    protected String buildSql(TableDesc tableDesc) {
        StringBuilder buf = new StringBuilder(200);
        buf.append("select ");
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            buf.append(columnDesc.getName());
            buf.append(", ");
        }
        if (!tableDesc.getColumnDescList().isEmpty()) {
            buf.setLength(buf.length() - 2);
        }
        buf.append(" from ");
        buf.append(tableDesc.getFullName());
        return buf.toString();
    }

    protected void addRows(DumpModel dumpModel, SqlType[] sqlTypes, ResultSet rs)
            throws SQLException {
        for (; rs.next();) {
            List<String> row = new ArrayList<String>();
            for (int i = 0; i < sqlTypes.length; i++) {
                SqlType sqlType = sqlTypes[i];
                String value = sqlType.getValue(rs, i + 1);
                row.add(DumpUtil.encode(value));
            }
            dumpModel.addRow(row);
        }
    }

}
