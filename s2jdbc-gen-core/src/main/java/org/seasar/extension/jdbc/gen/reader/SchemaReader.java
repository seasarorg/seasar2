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
package org.seasar.extension.jdbc.gen.reader;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.ResultSetUtil;

/**
 * @author taedium
 * 
 */
public class SchemaReader {

    protected DatabaseMetaData metaData;

    protected GenDialect dialect;

    public SchemaReader(DatabaseMetaData metaData, GenDialect dialect) {
        this.metaData = metaData;
        this.dialect = dialect;
    }

    public List<TableModel> getTableModels(String schemaName) {
        String schema = schemaName != null ? schemaName : getDefaultSchema();
        List<TableModel> result = new ArrayList<TableModel>();
        for (String table : getTables(schema)) {
            if (!dialect.isUserTable(table)) {
                continue;
            }
            TableModel tableModel = new TableModel();
            tableModel.setName(table);

            Set<String> primaryKeys = getPrimaryKeys(schema, table);
            for (ColumnModel cm : getColumnModels(schema, table)) {
                if (primaryKeys.contains(cm.getName())) {
                    cm.setPrimaryKey(true);
                }
                tableModel.addColumnModel(cm);
            }
            result.add(tableModel);
        }
        return result;
    }

    protected String getDefaultSchema() {
        String userName = DatabaseMetaDataUtil.getUserName(metaData);
        return dialect.getDefaultSchema(userName);
    }

    protected List<String> getTables(String schemaName) {
        List<String> result = new ArrayList<String>();
        try {
            ResultSet rs = metaData.getTables(null, schemaName, null,
                    new String[] { "TABLE" });
            try {
                while (rs.next()) {
                    result.add(rs.getString(3));
                }
                return result;
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    protected List<ColumnModel> getColumnModels(String schemaName,
            String tableName) {
        List<ColumnModel> result = new ArrayList<ColumnModel>();
        try {
            ResultSet rs = metaData.getColumns(null, schemaName, tableName,
                    null);
            try {
                while (rs.next()) {
                    ColumnModel cm = new ColumnModel();
                    cm.setName(rs.getString(4));
                    cm.setSqlType(rs.getInt(5));
                    cm.setTypeName(rs.getString(6));
                    cm.setLength(rs.getInt(7));
                    cm.setScale(rs.getInt(9));
                    cm.setNullable(rs.getBoolean(11));
                    result.add(cm);
                }
                return result;
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    protected Set<String> getPrimaryKeys(String schema, String tableName) {
        Set<String> result = new HashSet<String>();
        try {
            ResultSet rs = metaData.getPrimaryKeys(null, schema, tableName);
            try {
                while (rs.next()) {
                    result.add(rs.getString(4));
                }
            } finally {
                ResultSetUtil.close(rs);
            }
            return result;
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}
