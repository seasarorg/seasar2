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
package org.seasar.extension.jdbc.gen.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * @author taedium
 * 
 */
public class LoaderImpl {

    protected DataSource dataSource;

    protected char delimiter;

    protected String encoding;

    public void load(TableDesc tableDesc, File dumpFile) {
        DumpFileReader dumpFileReader = null;
        try {
            List<String> columnNameList = dumpFileReader.readLine();
            String sql = buildSql(tableDesc, columnNameList);
            SqlType[] sqlTypes = getSqlTypes(tableDesc, columnNameList);
            Connection conn = DataSourceUtil.getConnection(dataSource);
            try {
                PreparedStatement ps = ConnectionUtil.prepareStatement(conn,
                        sql);
                try {
                    for (List<String> line = dumpFileReader.readLine(); line != null; line = dumpFileReader
                            .readLine()) {
                        executeUpdate(ps, sqlTypes, line);
                    }
                } finally {
                    StatementUtil.close(ps);
                }
            } finally {
                ConnectionUtil.close(conn);
            }
        } finally {
            dumpFileReader.close();
        }
    }

    protected void executeUpdate(PreparedStatement ps, SqlType[] sqlTypes,
            List<String> valueList) {
        try {
            bindArgs(ps, sqlTypes, valueList);
            PreparedStatementUtil.executeUpdate(ps);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    protected void bindArgs(PreparedStatement ps, SqlType[] sqlTypes,
            List<String> valueList) throws SQLException {
        for (int i = 0; i < sqlTypes.length; i++) {
            String value = valueList.get(i);
            sqlTypes[i].bindValue(ps, i + 1, value);
        }
    }

    protected String buildSql(TableDesc tableDesc, List<String> columnNameList) {
        StringBuilder buf = new StringBuilder();
        buf.append("insert into ");
        buf.append(tableDesc.getFullName());
        buf.append(" (");
        for (String columnName : columnNameList) {
            buf.append(columnName);
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(") values (");
        for (int i = 0; i < columnNameList.size(); i++) {
            buf.append("?, ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(")");
        return buf.toString();
    }

    protected SqlType[] getSqlTypes(TableDesc tableDesc,
            List<String> columnNameList) {
        SqlType[] sqlTypes = new SqlType[columnNameList.size()];
        for (int i = 0; i < columnNameList.size(); i++) {
            String columnName = columnNameList.get(i);
            ColumnDesc columnDesc = tableDesc.getColumnDesc(columnName);
            sqlTypes[i] = columnDesc.getSqlType();
        }
        return sqlTypes;
    }
}
