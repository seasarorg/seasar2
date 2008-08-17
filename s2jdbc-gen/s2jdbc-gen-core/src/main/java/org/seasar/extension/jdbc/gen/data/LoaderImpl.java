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
package org.seasar.extension.jdbc.gen.data;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.DatabaseDesc;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.Loader;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.exception.DumpFileEmptyRuntimeException;
import org.seasar.extension.jdbc.gen.exception.LoadFailedRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link Loader}の実装クラスです。
 * 
 * @author taedium
 */
public class LoaderImpl implements Loader {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(LoaderImpl.class);

    /** 方言 */
    protected GenDialect dialect;

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding;

    /** トークナイザ */
    protected DumpFileTokenizer tokenizer;

    /** 区切り文字 */
    protected char delimiter = ',';

    /** 拡張子 */
    protected String extension = ".csv";

    /** バッチサイズ */
    protected int batchSize;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     * @param batchSize
     *            バッチサイズ
     */
    public LoaderImpl(GenDialect dialect, String dumpFileEncoding, int batchSize) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        if (batchSize < 0) {
            throw new IllegalArgumentException("batchSize");
        }
        this.dialect = dialect;
        this.dumpFileEncoding = dumpFileEncoding;
        this.batchSize = batchSize;
        tokenizer = createDumpFileTokenizer();
    }

    public void load(SqlExecutionContext sqlExecutionContext,
            DatabaseDesc databaseDesc, File dumpFile) {
        String name = StringUtil.trimSuffix(dumpFile.getName(), extension);
        TableDesc tableDesc = databaseDesc.getTableDesc(name);
        if (tableDesc == null) {
            return;
        }
        logger.log("DS2JDBCGen0013", new Object[] { dumpFile.getPath() });
        DumpFileReader reader = createDumpFileReader(dumpFile);
        try {
            List<String> columnNameList = reader.readLine();
            if (columnNameList == null) {
                throw new DumpFileEmptyRuntimeException(dumpFile.getPath());
            }
            List<SqlType> sqlTypeList = getSqlTypeList(tableDesc,
                    columnNameList);
            String sql = buildSql(tableDesc, columnNameList);
            try {
                preLoadData(sqlExecutionContext, tableDesc);
                loadData(sqlExecutionContext, reader, sqlTypeList, sql);
                postLoadData(sqlExecutionContext, tableDesc);
            } catch (SQLRuntimeException e) {
                if (dialect.isTableNotFound(e)) {
                    logger.log("DS2JDBCGen0012", new Object[] { tableDesc
                            .getFullName() });
                    sqlExecutionContext.notifyException();
                } else {
                    LoadFailedRuntimeException ex = new LoadFailedRuntimeException(
                            e, dumpFile.getPath(), reader.getLineNumber());
                    sqlExecutionContext.addException(ex);
                }
            }
        } finally {
            reader.close();
        }
        logger.log("DS2JDBCGen0014", new Object[] { dumpFile.getPath() });
    }

    public boolean isTarget(File file) {
        return file != null && file.getName().endsWith(extension);
    }

    /**
     * データのロード前に処理します。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param tableDesc
     *            テーブル記述
     */
    protected void preLoadData(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc) {
        if (tableDesc.hasIdentityColumn()
                && dialect.supportsIdentityInsertControlStatement()) {
            Statement statement = sqlExecutionContext.getStatement();
            String sql = dialect.getIdentityInsertEnableStatement(tableDesc
                    .getFullName());
            StatementUtil.execute(statement, sql);
        }
    }

    /**
     * データをロードします。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param reader
     *            リーダ
     * @param sqlTypeList
     *            {@link SqlType}のリスト
     * @param sql
     *            SQL
     */
    protected void loadData(SqlExecutionContext sqlExecutionContext,
            DumpFileReader reader, List<SqlType> sqlTypeList, String sql) {
        PreparedStatement ps = sqlExecutionContext.getPreparedStatement(sql);
        List<String> valueList = null;
        boolean remaining = false;
        for (int i = 0; (valueList = reader.readLine()) != null; i++) {
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
     * データのロード後に処理します。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param tableDesc
     *            テーブル記述
     */
    protected void postLoadData(SqlExecutionContext sqlExecutionContext,
            TableDesc tableDesc) {
        if (tableDesc.hasIdentityColumn()
                && dialect.supportsIdentityInsertControlStatement()) {
            Statement statement = sqlExecutionContext.getStatement();
            String sql = dialect.getIdentityInsertDisableStatement(tableDesc
                    .getFullName());
            StatementUtil.execute(statement, sql);
        }
    }

    /**
     * 引数をバインドします。
     * 
     * @param ps
     *            準備されたステートメント
     * @param sqlTypeList
     *            {@link SqlType}のリスト
     * @param valueList
     *            値のリスト
     */
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

    /**
     * SQLを組み立てます。
     * 
     * @param tableDesc
     *            テーブル記述
     * @param columnNameList
     *            カラム名のリスト
     * @return SQL
     */
    protected String buildSql(TableDesc tableDesc, List<String> columnNameList) {
        StringBuilder buf = new StringBuilder();
        buf.append("insert into ");
        buf.append(dialect.quote(tableDesc.getFullName()));
        buf.append(" (");
        for (String columnName : columnNameList) {
            buf.append(dialect.quote(columnName));
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

    /**
     * {@link SqlType}のリストを返します。
     * 
     * @param tableDesc
     *            テーブル記述
     * @param columnNameList
     *            カラム名のリスト
     * @return {@link SqlType}のリストを返します。
     */
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

    /**
     * ダンプファイルのトークナイザを作成します。
     * 
     * @return ダンプファイルのトークナイザ
     */
    protected DumpFileTokenizer createDumpFileTokenizer() {
        return new DumpFileTokenizer(delimiter);
    }

    /**
     * ダンプファイルのリーダを作成します。
     * 
     * @param dumpFile
     *            ダンプファイル
     * @return ダンプファイルのリーダ
     */
    protected DumpFileReader createDumpFileReader(File dumpFile) {
        return new DumpFileReader(dumpFile, dumpFileEncoding, tokenizer);
    }
}
