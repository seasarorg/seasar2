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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.framework.exception.SRuntimeException;
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
                boolean fail = dumpTableWithSort(sqlExecutionContext,
                        tableDesc, writer);
                if (fail) {
                    dumpTable(sqlExecutionContext, tableDesc, writer);
                }
                logger.log("DS2JDBCGen0016", new Object[] {
                        tableDesc.getFullName(), dumpFile.getPath(),
                        writer.getLineNumber() - 1 });
            } finally {
                writer.close();
            }
        }
    }

    /**
     * テーブルのデータをソートしてダンプします。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param tableDesc
     *            テーブル記述
     * @param writer
     *            ライタ
     * @return ソート用のカラムが存在しない場合{@code true}
     */
    protected boolean dumpTableWithSort(
            SqlExecutionContext sqlExecutionContext, TableDesc tableDesc,
            DumpFileWriter writer) {
        String sql = buildSqlWithSort(tableDesc);
        sqlExecutionContext.begin();
        try {
            Statement statement = sqlExecutionContext.getStatement();
            dumpRows(statement, writer, sql);
        } catch (Exception e) {
            if (dialect.isTableNotFound(e)) {
                logger.log("DS2JDBCGen0012", new Object[] { tableDesc
                        .getFullName() });
                sqlExecutionContext.notifyException();
                writer.writeHeaderOnly();
            } else if (dialect.isColumnNotFound(e)) {
                logger.log("DS2JDBCGen0018", new Object[] { sql });
                sqlExecutionContext.notifyException();
                return true;
            } else {
                sqlExecutionContext.addException(new SRuntimeException(
                        "ES2JDBCGen0021", new Object[] { e }, e));
            }
        } finally {
            sqlExecutionContext.end();
        }
        return false;
    }

    /**
     * テーブルのデータをダンプします。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param tableDesc
     *            テーブル記述
     * @param writer
     *            ライタ
     */
    protected void dumpTable(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc, DumpFileWriter writer) {
        String sql = buildSql(tableDesc);
        sqlExecutionContext.begin();
        try {
            Statement statement = sqlExecutionContext.getStatement();
            dumpRows(statement, writer, sql);
        } catch (Exception e) {
            if (dialect.isTableNotFound(e)) {
                logger.log("DS2JDBCGen0012", new Object[] { tableDesc
                        .getFullName() });
                sqlExecutionContext.notifyException();
                writer.writeHeaderOnly();
            } else {
                sqlExecutionContext.addException(new SRuntimeException(
                        "ES2JDBCGen0021", new Object[] { e }, e));
            }
        } finally {
            sqlExecutionContext.end();
        }
    }

    /**
     * データをダンプします。
     * 
     * @param statement
     *            SQLの文
     * @param writer
     *            ライタ
     * @param sql
     *            SQL
     * @throws SQLException
     *             SQLに関する例外が発生した場合
     */
    protected void dumpRows(Statement statement, DumpFileWriter writer,
            String sql) throws SQLException {
        logger.debug(sql);
        ResultSet rs = statement.executeQuery(sql);
        try {
            writer.writeRows(rs);
        } finally {
            ResultSetUtil.close(rs);
        }
    }

    /**
     * ORDER BY句をもつSQLを組み立てます。
     * 
     * @param tableDesc
     *            テーブル記述
     * @return SQL
     */
    protected String buildSqlWithSort(TableDesc tableDesc) {
        StringBuilder buf = new StringBuilder();
        buf.append("select * from ");
        buf.append(tableDesc.getFullName());
        if (tableDesc.getPrimaryKeyDesc() != null) {
            List<String> pkColumnNameList = tableDesc.getPrimaryKeyDesc()
                    .getColumnNameList();
            if (pkColumnNameList.size() > 0) {
                buf.append(" order by ");
                for (String columnName : pkColumnNameList) {
                    buf.append(columnName);
                    buf.append(", ");
                }
                buf.setLength(buf.length() - 2);
            }
        }
        return buf.toString();
    }

    /**
     * SQLを組み立てます。
     * 
     * @param tableDesc
     *            テーブル記述
     * @return SQL
     */
    protected String buildSql(TableDesc tableDesc) {
        return "select * from " + tableDesc.getFullName();
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
        return new DumpFileWriter(dumpFile, tableDesc, dialect,
                dumpFileEncoding, delimiter);
    }

}
