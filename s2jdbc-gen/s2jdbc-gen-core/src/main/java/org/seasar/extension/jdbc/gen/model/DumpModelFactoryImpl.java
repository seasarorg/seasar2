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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.DumpModel;
import org.seasar.extension.jdbc.gen.DumpModelFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.util.DumpUtil;
import org.seasar.extension.jdbc.gen.util.StatementUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;

/**
 * @author taedium
 * 
 */
public class DumpModelFactoryImpl implements DumpModelFactory {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(DumpModelFactoryImpl.class);

    protected GenDialect dialect;

    protected char delimiter;

    /**
     * @param dataSource
     */
    public DumpModelFactoryImpl(GenDialect dialect, char delimiter) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
        this.delimiter = delimiter;
    }

    public DumpModel getDumpModel(TableDesc tableDesc,
            SqlExecutionContext sqlExecutionContext) {
        DumpModel dumpModel = new DumpModel();
        dumpModel.setName(tableDesc.getFullName());
        dumpModel.setDelimiter(delimiter);
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            dumpModel.addColumnName(columnDesc.getName());
        }
        doRow(dumpModel, tableDesc, sqlExecutionContext);
        return dumpModel;
    }

    protected void doRow(DumpModel dumpModel, TableDesc tableDesc,
            SqlExecutionContext sqlExecutionContext) {
        SqlType[] sqlTypes = createSqlTypes(tableDesc);
        Statement statement = sqlExecutionContext.getStatement();
        try {
            ResultSet rs = StatementUtil.executeQuery(statement,
                    buildSql(tableDesc));
            try {
                addRows(dumpModel, sqlTypes, rs);
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLRuntimeException e) {
            sqlExecutionContext.notifyException();
            if (!dialect.isTableNotFound(e)) {
                logger.log(e);
                throw e;
            }
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

    protected void addRows(DumpModel dumpModel, SqlType[] sqlTypes, ResultSet rs) {
        for (; ResultSetUtil.next(rs);) {
            List<String> row = new ArrayList<String>();
            for (int i = 0; i < sqlTypes.length; i++) {
                SqlType sqlType = sqlTypes[i];
                String value = null;
                try {
                    value = sqlType.getValue(rs, i + 1);
                } catch (SQLException e) {
                    logger.log(e);
                }
                row.add(DumpUtil.encode(value));
            }
            dumpModel.addRow(row);
        }
    }

    protected SqlType[] createSqlTypes(TableDesc tableDesc) {
        SqlType[] sqlTypes = new SqlType[tableDesc.getColumnDescList().size()];
        for (int i = 0; i < tableDesc.getColumnDescList().size(); i++) {
            ColumnDesc columnDesc = tableDesc.getColumnDescList().get(i);
            sqlTypes[i] = columnDesc.getSqlType();
        }
        return sqlTypes;
    }
}
