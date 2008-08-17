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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.StatementUtil;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;

/**
 * {@link Dumper}の実装クラスです。
 * 
 * @author taedium
 */
public class DumperImpl implements Dumper {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DumperImpl.class);

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding;

    /** 方言 */
    protected GenDialect dialect;

    /** 拡張子 */
    protected String extension = ".csv";

    /** 区切り文字 */
    protected char delimiter = ',';

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     */
    public DumperImpl(GenDialect dialect, String dumpFileEncoding) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        this.dialect = dialect;
        this.dumpFileEncoding = dumpFileEncoding;
    }

    public void dump(SqlExecutionContext sqlExecutionContext,
            DatabaseDesc databaseDesc, File dumpDir) {
        for (TableDesc tableDesc : databaseDesc.getTableDescList()) {
            String fileName = tableDesc.getCanonicalName() + extension;
            File dumpFile = new File(dumpDir, fileName);
            logger.log("DS2JDBCGen0015", new Object[] {
                    tableDesc.getFullName(), dumpFile.getPath() });
            DumpFileWriter writer = createDumpFileWriter(dumpFile, tableDesc);
            try {
                dumpTable(sqlExecutionContext, tableDesc, writer);
            } finally {
                writer.close();
            }
            logger.log("DS2JDBCGen0016", new Object[] {
                    tableDesc.getFullName(), dumpFile.getPath() });
        }
    }

    /**
     * @param sqlExecutionContext
     * @param tableDesc
     * @param writer
     */
    private void dumpTable(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc, DumpFileWriter writer) {
        List<String> columnNameList = getColumnNameList(tableDesc);
        writer.writeHeader(columnNameList);
        String sql = buildSql(tableDesc);
        Statement statement = sqlExecutionContext.getStatement();
        try {
            ResultSet rs = StatementUtil.executeQuery(statement, sql);
            try {
                for (; ResultSetUtil.next(rs);) {
                    writer.writeRowData(rs);
                }
            } finally {
                ResultSetUtil.close(rs);
            }
        } catch (SQLRuntimeException e) {
            if (dialect.isTableNotFound(e)) {
                logger.log("DS2JDBCGen0012", new Object[] { tableDesc
                        .getFullName() });
                sqlExecutionContext.notifyException();
            } else {
                sqlExecutionContext.addException(e);
            }
        }
    }

    /**
     * SQLを組み立てます。
     * 
     * @param tableDesc
     *            テーブル記述
     * @return SQL
     */
    protected String buildSql(TableDesc tableDesc) {
        StringBuilder buf = new StringBuilder(200);
        buf.append("select ");
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            if (isIgnoreColumn(columnDesc)) {
                continue;
            }
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

    /**
     * カラム名のリストを返します。
     * 
     * @param tableDesc
     *            テーブル記述
     * @return カラム名のリスト
     */
    protected List<String> getColumnNameList(TableDesc tableDesc) {
        List<String> columnNameList = new ArrayList<String>();
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            if (isIgnoreColumn(columnDesc)) {
                continue;
            }
            columnNameList.add(columnDesc.getName());
        }
        return columnNameList;
    }

    /**
     * 無視すべきカラムの場合{@code true}を返します。
     * 
     * @param columnDesc
     *            カラム記述
     * @return 無視すべきカラムの場合{@code true}
     */
    protected boolean isIgnoreColumn(ColumnDesc columnDesc) {
        return columnDesc.isIdentity() && !dialect.supportsIdentityInsert();
    }

    /**
     * ダンプファイルのライタを返します。
     * 
     * @param dumpFile
     *            ダンプファイル
     * @param tableDesc
     *            テーブル記述
     * @return ダンプファイルのライタ
     */
    protected DumpFileWriter createDumpFileWriter(File dumpFile,
            TableDesc tableDesc) {
        List<SqlType> sqlTypeList = getSqlTypeList(tableDesc);
        return new DumpFileWriter(dumpFile, sqlTypeList, dumpFileEncoding,
                delimiter);
    }

    /**
     * SQL型のリスト
     * 
     * @param tableDesc
     *            テーブル記述
     * @return SQL型のリスト
     */
    protected List<SqlType> getSqlTypeList(TableDesc tableDesc) {
        int size = tableDesc.getColumnDescList().size();
        List<SqlType> sqlTypeList = new ArrayList<SqlType>(size);
        for (int i = 0; i < tableDesc.getColumnDescList().size(); i++) {
            ColumnDesc columnDesc = tableDesc.getColumnDescList().get(i);
            if (isIgnoreColumn(columnDesc)) {
                continue;
            }
            sqlTypeList.add(columnDesc.getSqlType());
        }
        return sqlTypeList;
    }
}
