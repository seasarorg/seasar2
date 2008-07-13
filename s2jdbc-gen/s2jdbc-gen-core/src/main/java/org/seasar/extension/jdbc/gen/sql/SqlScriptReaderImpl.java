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

    protected BufferedReader reader;

    protected boolean endOfFile;

    /**
     * @param sqlFile
     * @param sqlFileEncoding
     * @param tokenizer
     */
    public SqlScriptReaderImpl(File sqlFile, String sqlFileEncoding,
            SqlScriptTokenizer tokenizer) {
        if (sqlFile == null) {
            throw new NullPointerException("sqlFile");
        }
        if (sqlFileEncoding == null) {
            throw new NullPointerException("sqlFileEncoding");
        }
        if (tokenizer == null) {
            throw new NullPointerException("tokenizer");
        }
        this.sqlFile = sqlFile;
        this.sqlFileEncoding = sqlFileEncoding;
        this.tokenizer = tokenizer;
    }

    public String readSql() {
        if (endOfFile) {
            return null;
        }
        try {
            if (reader == null) {
                reader = createBufferedReader();
            }
            SqlBuffer buffer = new SqlBuffer();

            outerLoop: for (String line = reader.readLine();; line = reader
                    .readLine()) {
                tokenizer.addFragment(line);
                buffer.appendSpaceIfNecessary();

                innerLoop: for (;;) {
                    TokenType tokenType = tokenizer.nextToken();
                    String token = tokenizer.getToken();
                    switch (tokenType) {
                    case WORD:
                    case OTHER:
                    case QUOTE:
                        buffer.append(token);
                        break;
                    case END_OF_FRAGMENT:
                        break innerLoop;
                    case STATEMENT_DELIMITER:
                        if (tokenizer.isInSqlBlock()) {
                            buffer.append(token);
                            break;
                        }
                        break outerLoop;
                    case BLOCK_DELIMITER:
                        if (buffer.isEmpty()) {
                            break innerLoop;
                        }
                        break outerLoop;
                    case END_OF_FILE:
                        endOfFile = true;
                        break outerLoop;
                    default:
                        break;
                    }
                }
            }
            return (endOfFile && buffer.isEmpty()) ? null : buffer.toSql();
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

    protected static class SqlBuffer {

        protected StringBuilder buf = new StringBuilder(300);

        protected void append(String s) {
            buf.append(s);
        }

        protected void appendSpaceIfNecessary() {
            if (buf.length() > 0) {
                char lastChar = buf.charAt(buf.length() - 1);
                if (!Character.isWhitespace(lastChar)) {
                    buf.append(' ');
                }
            }
        }

        protected boolean isEmpty() {
            return buf.toString().trim().length() == 0;
        }

        protected String toSql() {
            return buf.toString().trim();
        }

    }
}
