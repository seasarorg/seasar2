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
            StringBuilder buf = new StringBuilder();
            boolean spaceRequired = false;
            outerLoop: for (String line = reader.readLine();; line = reader
                    .readLine()) {
                tokenizer.addFragment(line);
                innerLoop: for (;;) {
                    TokenType tokenType = tokenizer.nextToken();
                    String token = tokenizer.getToken();

                    switch (tokenType) {
                    case SQL:
                    case QUOTE:
                        if (spaceRequired) {
                            buf.append(" ");
                        }
                        spaceRequired = true;
                        buf.append(token);
                        break;
                    case START_OF_BLOCK_COMMENT:
                    case BLOCK_COMMENT:
                    case END_OF_BLOCK_COMMENT:
                    case LINE_COMMENT:
                        break;
                    case END_OF_FRAGMENT:
                        break innerLoop;
                    case STATEMENT_DELIMITER:
                    case BLOCK_DELIMITER:
                        break outerLoop;
                    case END_OF_FILE:
                        endOfFile = true;
                        break outerLoop;
                    }
                }
            }
            return buf.toString();
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
}
