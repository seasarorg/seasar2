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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlScriptReader;
import org.seasar.extension.jdbc.gen.SqlScriptTokenizer;
import org.seasar.extension.jdbc.gen.SqlScriptTokenizer.TokenType;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.InputStreamReaderUtil;

/**
 * @author taedium
 * 
 */
public class SqlScriptReaderImpl implements SqlScriptReader {

    protected static Logger logger = Logger
            .getLogger(SqlScriptReaderImpl.class);

    protected File sqlFile;

    protected String sqlFileEncoding;

    protected SqlScriptTokenizer tokenizer;

    protected GenDialect dialect;

    protected BufferedReader reader;

    protected SqlBuilder builder;

    protected boolean endOfFile;

    /**
     * @param sqlFile
     * @param sqlFileEncoding
     * @param tokenizer
     */
    public SqlScriptReaderImpl(File sqlFile, String sqlFileEncoding,
            SqlScriptTokenizer tokenizer, GenDialect dialect) {
        if (sqlFile == null) {
            throw new NullPointerException("sqlFile");
        }
        if (sqlFileEncoding == null) {
            throw new NullPointerException("sqlFileEncoding");
        }
        if (tokenizer == null) {
            throw new NullPointerException("tokenizer");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.sqlFile = sqlFile;
        this.sqlFileEncoding = sqlFileEncoding;
        this.tokenizer = tokenizer;
        this.dialect = dialect;
    }

    public String readSql() {
        if (endOfFile) {
            return null;
        }
        try {
            if (reader == null) {
                reader = createBufferedReader();
            }
            builder = new SqlBuilder();
            readLineLoop: for (;;) {
                tokenizer.addLine(reader.readLine());
                builder.notifyLineChanged();
                nextTokenLoop: for (;;) {
                    builder.set(tokenizer.nextToken(), tokenizer.getToken());
                    builder.build();
                    if (builder.isTokenRequired()) {
                        continue;
                    } else if (builder.isLineRequired()) {
                        break nextTokenLoop;
                    } else if (builder.isCompleted()) {
                        break readLineLoop;
                    }
                    throw new IllegalStateException("builder");
                }
            }
            return builder.getSql();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public void close() {
        CloseableUtil.close(reader);
    }

    protected BufferedReader createBufferedReader() throws IOException {
        logger.log("DS2JDBCGen0006", new Object[] { sqlFile.getName() });
        InputStream is = FileInputStreamUtil.create(sqlFile);
        return new BufferedReader(InputStreamReaderUtil.create(is,
                sqlFileEncoding));
    }

    protected class SqlBuilder {

        protected TokenType tokenType;

        protected String token;

        protected boolean tokenRequired;

        protected boolean lineRequired;

        protected boolean completed;

        protected StringBuilder buf = new StringBuilder(300);

        protected List<String> wordList = new ArrayList<String>();

        protected boolean inSqlBlock;

        protected boolean delimited;

        protected boolean lineChanged;

        protected SqlBuilder() {
        }

        protected void set(TokenType tokenType, String token) {
            this.tokenType = tokenType;
            this.token = token;
            setTokenRequired(true);
        }

        protected void build() {
            switch (tokenType) {
            case WORD:
                wordList.add(token);
                append(token);
                setTokenRequired(true);
                break;
            case QUOTE:
            case OTHER:
                append(token);
                setTokenRequired(true);
                break;
            case END_OF_LINE:
                if (delimited) {
                    setCompleted(true);
                } else {
                    setLineRequired(true);
                }
                break;
            case STATEMENT_DELIMITER:
                if (isInSqlBlock()) {
                    append(token);
                } else {
                    delimited = true;
                }
                setTokenRequired(true);
                break;
            case BLOCK_DELIMITER:
                if (isEmpty()) {
                    setLineRequired(true);
                } else {
                    setCompleted(true);
                }
                break;
            case END_OF_FILE:
                endOfFile = true;
                setCompleted(true);
                break;
            default:
                setTokenRequired(true);
                break;
            }
        }

        /**
         * @return Returns the tokenRequired.
         */
        protected boolean isTokenRequired() {
            return tokenRequired;
        }

        /**
         * @param tokenRequired
         *            The tokenRequired to set.
         */
        protected void setTokenRequired(boolean tokenRequired) {
            this.tokenRequired = tokenRequired;
            lineRequired = false;
            completed = false;
        }

        /**
         * @return Returns the lineRequired.
         */
        protected boolean isLineRequired() {
            return lineRequired;
        }

        /**
         * @param lineRequired
         *            The lineRequired to set.
         */
        protected void setLineRequired(boolean lineRequired) {
            this.lineRequired = lineRequired;
            tokenRequired = false;
            completed = false;
        }

        /**
         * @return Returns the built.
         */
        protected boolean isCompleted() {
            return completed;
        }

        /**
         * @param completed
         *            The built to set.
         */
        protected void setCompleted(boolean completed) {
            this.completed = completed;
            tokenRequired = false;
            lineRequired = false;
        }

        protected void append(String s) {
            if (!delimited) {
                appendWhitespaceIfNecessary();
                buf.append(s);
            }
        }

        protected void appendWhitespaceIfNecessary() {
            if (!lineChanged) {
                return;
            }
            if (buf.length() > 0) {
                char lastChar = buf.charAt(buf.length() - 1);
                if (!Character.isWhitespace(lastChar)) {
                    buf.append(' ');
                }
            }
            lineChanged = false;
        }

        protected void notifyLineChanged() {
            lineChanged = true;
        }

        protected boolean isInSqlBlock() {
            if (inSqlBlock) {
                return true;
            }
            inSqlBlock = dialect.isSqlBlockStartWords(wordList);
            return inSqlBlock;
        }

        protected boolean isEmpty() {
            return buf.toString().trim().length() == 0;
        }

        protected String getSql() {
            String sql = buf.toString().trim();
            return endOfFile && sql.length() == 0 ? null : sql;
        }
    }

}
