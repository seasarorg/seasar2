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
import java.sql.SQLException;
import java.sql.Statement;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlFileExecutor;
import org.seasar.extension.jdbc.gen.exception.SqlFailedException;
import org.seasar.framework.log.Logger;

/**
 * {@link SqlFileExecutor}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlFileExecutorImpl implements SqlFileExecutor {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(SqlFileExecutorImpl.class);

    /** 方言 */
    protected GenDialect dialect;

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding;

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter;

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter;

    /** SQLファイルのトークナイザ */
    protected SqlFileTokenizer tokenizer;

    /**
     * @param dialect
     *            方言
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     * @param blockDelimiter
     *            SQLブロックの区切り文字
     */
    public SqlFileExecutorImpl(GenDialect dialect, String sqlFileEncoding,
            char statementDelimiter, String blockDelimiter) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (sqlFileEncoding == null) {
            throw new NullPointerException("sqlFileEncoding");
        }
        this.sqlFileEncoding = sqlFileEncoding;
        this.statementDelimiter = statementDelimiter;
        this.blockDelimiter = blockDelimiter;
        this.dialect = dialect;
        tokenizer = createSqlFileTokenizer();
    }

    public void execute(SqlExecutionContext context, File sqlFile) {
        logger.log("DS2JDBCGen0006", new Object[] { sqlFile.getPath() });
        SqlFileReader reader = createSqlFileReader(sqlFile);
        try {
            for (String sql = reader.readSql(); sql != null; sql = reader
                    .readSql()) {
                logger.debug(sql);
                Statement statement = context.getStatement();
                try {
                    statement.execute(sql);
                } catch (SQLException e) {
                    context.addException(new SqlFailedException(e, sqlFile
                            .getPath(), sql));
                }
            }
        } finally {
            reader.close();
        }
        logger.log("DS2JDBCGen0007", new Object[] { sqlFile.getPath() });
    }

    public boolean isTarget(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        return name.endsWith(".sql") || name.endsWith(".ddl");
    }

    /**
     * SQLファイルのトークナイザを作成します。
     * 
     * @return {@link SqlFileTokenizer}
     */
    protected SqlFileTokenizer createSqlFileTokenizer() {
        return new SqlFileTokenizer(statementDelimiter,
                blockDelimiter != null ? blockDelimiter : dialect
                        .getSqlBlockDelimiter());
    }

    /**
     * SQLファイルのリーダを作成します。
     * 
     * @param sqlFile
     *            SQLファイル
     * 
     * @return {@link SqlFileReader}の実装
     */
    protected SqlFileReader createSqlFileReader(File sqlFile) {
        return new SqlFileReader(sqlFile, sqlFileEncoding, tokenizer, dialect);
    }

}
