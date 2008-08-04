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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.Loader;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.util.ExclusionFilenameFilter;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class LoaderImpl implements Loader {

    protected File dumpDir;

    protected String dumpFileEncoding;

    protected List<TableDesc> tableDescList;

    protected Map<String, File> dumpFileMap;

    protected DumpFileTokenizer tokenizer = new DumpFileTokenizer(',');

    protected char delimiter = ',';

    protected String extension = ".csv";

    public LoaderImpl(File dumpDir, String dumpFileEncoding,
            List<TableDesc> tableDescList) {
        if (dumpDir == null) {
            throw new NullPointerException("dumpDir");
        }
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        if (tableDescList == null) {
            throw new NullPointerException("tableDescList");
        }
        this.dumpDir = dumpDir;
        this.dumpFileEncoding = dumpFileEncoding;
        this.tableDescList = tableDescList;
        dumpFileMap = createDumpFileMap();
    }

    protected Map<String, File> createDumpFileMap() {
        File[] dumpFiles = dumpDir.listFiles(new ExclusionFilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        });

        Map<String, File> map = new CaseInsensitiveMap();
        if (dumpFiles != null) {
            for (File file : dumpFiles) {
                String fileName = file.getName();
                String tableName = StringUtil.trimSuffix(fileName, extension);
                map.put(tableName, file);
            }
        }
        return map;
    }

    public void load(SqlExecutionContext sqlExecutionContext) {
        for (TableDesc tableDesc : tableDescList) {
            File dumpFile = dumpFileMap.get(tableDesc.getFullName());
            if (dumpFile != null) {
                loadDumpFile(sqlExecutionContext, tableDesc, dumpFile);
            }
        }
    }

    protected void loadDumpFile(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc, File dumpFile) {
        DumpFileReader reader = new DumpFileReader(dumpFile, dumpFileEncoding,
                tokenizer);
        try {
            List<String> columnNameList = reader.readRow();
            if (columnNameList == null) {
                return;
            }
            SqlType[] sqlTypes = getSqlTypes(tableDesc, columnNameList);
            String sql = buildSql(tableDesc, columnNameList);
            PreparedStatement ps = sqlExecutionContext
                    .getPreparedStatement(sql);
            for (List<String> valueList = reader.readRow(); valueList != null; valueList = reader
                    .readRow()) {
                executeUpdate(ps, sqlTypes, valueList);
            }
        } finally {
            reader.close();
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
            if (tableDesc.getColumnDesc(columnName) != null) {
                buf.append(columnName);
                buf.append(", ");
            }
        }
        buf.setLength(buf.length() - 2);
        buf.append(") values (");
        for (String columnName : columnNameList) {
            if (tableDesc.getColumnDesc(columnName) != null) {
                buf.append("?, ");
            }
        }
        buf.setLength(buf.length() - 2);
        buf.append(")");
        return buf.toString();
    }

    protected SqlType[] getSqlTypes(TableDesc tableDesc,
            List<String> columnNameList) {
        List<SqlType> sqlTypes = new ArrayList<SqlType>();
        for (int i = 0; i < columnNameList.size(); i++) {
            String columnName = columnNameList.get(i);
            ColumnDesc columnDesc = tableDesc.getColumnDesc(columnName);
            if (columnDesc != null) {
                sqlTypes.add(columnDesc.getSqlType());
            }
        }
        return sqlTypes.toArray(new SqlType[sqlTypes.size()]);
    }
}
