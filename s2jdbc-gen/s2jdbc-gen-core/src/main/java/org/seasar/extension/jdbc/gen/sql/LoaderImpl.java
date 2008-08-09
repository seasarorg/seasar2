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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.Loader;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class LoaderImpl implements Loader {

    protected static Logger logger = Logger.getLogger(LoaderImpl.class);

    protected GenDialect dialect;

    protected String dumpFileEncoding;

    protected Map<String, TableDesc> tableDescMap = new CaseInsensitiveMap();

    protected Map<String, File> dumpFileMap;

    protected DumpFileTokenizer tokenizer = new DumpFileTokenizer(',');

    protected char delimiter = ',';

    protected String extension = ".csv";

    protected int batchSize = 10;

    public LoaderImpl(GenDialect dialect, String dumpFileEncoding,
            List<TableDesc> tableDescList) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        if (tableDescList == null) {
            throw new NullPointerException("tableDescList");
        }
        this.dialect = dialect;
        this.dumpFileEncoding = dumpFileEncoding;
        for (TableDesc tableDesc : tableDescList) {
            tableDescMap.put(tableDesc.getFullName(), tableDesc);
        }
    }

    public void load(SqlExecutionContext sqlExecutionContext, File dumpFile) {
        logger.log("DS2JDBCGen0013", new Object[] { dumpFile.getPath() });
        String name = StringUtil.trimSuffix(dumpFile.getName(), extension);
        if (!tableDescMap.containsKey(name)) {
            return;
        }
        TableDesc tableDesc = tableDescMap.get(name);
        DumpFileReader reader = new DumpFileReader(dumpFile, dumpFileEncoding,
                tokenizer);
        try {
            List<String> columnNameList = reader.readLine();
            if (columnNameList == null) {
                return;
            }
            List<SqlType> sqlTypeList = getSqlTypeList(tableDesc,
                    columnNameList);
            String sql = buildSql(tableDesc, columnNameList);
            try {
                preLoadData(sqlExecutionContext, tableDesc);
                loadData(sqlExecutionContext, reader, sqlTypeList, sql);
                postLoadData(sqlExecutionContext, tableDesc);
            } catch (SQLRuntimeException e) {
                sqlExecutionContext.notifyException();
                if (dialect.isTableNotFound(e)) {
                    logger.log("DS2JDBCGen0012", new Object[] { tableDesc
                            .getFullName() });
                    return;
                }
                throw e;
            }
        } finally {
            reader.close();
        }
    }

    /**
     * @param sqlExecutionContext
     * @param tableDesc
     */
    protected void preLoadData(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc) {
        if (tableDesc.hasIdentityColumn() && dialect.supportsIdentityInsert()) {
            Statement statement = sqlExecutionContext.getStatement();
            String sql = dialect.getIdentityInsertOnStatement(tableDesc
                    .getFullName());
            logSql(sql);
            StatementUtil.execute(statement, sql);
        }
    }

    /**
     * @param sqlExecutionContext
     * @param reader
     * @param sqlTypeList
     * @param sql
     */
    protected void loadData(SqlExecutionContext sqlExecutionContext,
            DumpFileReader reader, List<SqlType> sqlTypeList, String sql) {
        PreparedStatement ps = sqlExecutionContext.getPreparedStatement(sql);
        List<String> valueList = null;
        boolean remaining = false;
        for (int i = 0; (valueList = reader.readLine()) != null; i++) {
            logSql(sql);
            bindArgs(ps, sqlTypeList, valueList);
            PreparedStatementUtil.addBatch(ps);
            if (batchSize > 0 && (i + 1) % batchSize == 0) {
                PreparedStatementUtil.executeBatch(ps);
                remaining = false;
            } else {
                remaining = true;
            }
        }
        if (remaining) {
            PreparedStatementUtil.executeBatch(ps);
        }
    }

    /**
     * @param sqlExecutionContext
     * @param tableDesc
     */
    protected void postLoadData(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc) {
        if (tableDesc.hasIdentityColumn() && dialect.supportsIdentityInsert()) {
            Statement statement = sqlExecutionContext.getStatement();
            String sql = dialect.getIdentityInsertOffStatement(tableDesc
                    .getFullName());
            logSql(sql);
            StatementUtil.execute(statement, sql);
        }
    }

    protected void logSql(String sql) {
        logger.log("DS2JDBCGen0007", new Object[] { sql });
    }

    protected void bindArgs(PreparedStatement ps, List<SqlType> sqlTypeList,
            List<String> valueList) {
        try {
            for (int i = 0; i < sqlTypeList.size(); i++) {
                SqlType sqlType = sqlTypeList.get(i);
                String value = valueList.get(i);
                sqlType.bindValue(ps, i + 1, value);
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
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

    protected List<SqlType> getSqlTypeList(TableDesc tableDesc,
            List<String> columnNameList) {
        List<SqlType> sqlTypeList = new ArrayList<SqlType>();
        for (int i = 0; i < columnNameList.size(); i++) {
            String columnName = columnNameList.get(i);
            ColumnDesc columnDesc = tableDesc.getColumnDesc(columnName);
            sqlTypeList.add(columnDesc.getSqlType());
        }
        return sqlTypeList;
    }
}
