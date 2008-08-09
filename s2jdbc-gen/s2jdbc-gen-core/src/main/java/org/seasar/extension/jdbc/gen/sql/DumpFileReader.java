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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.exception.IllegalDumpColumnSizeRuntimeException;
import org.seasar.extension.jdbc.gen.sql.DumpFileTokenizer.TokenType;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.util.DumpUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class DumpFileReader {

    protected static final int BUF_SIZE = 8192;

    protected File dumpFile;

    protected String dumpFileEncoding;

    protected DumpFileTokenizer tokenizer;

    protected BufferedReader reader;

    protected char[] buf = new char[BUF_SIZE];

    protected int bufLength;

    protected TokenType tokenType = null;

    protected int lineNumber = -1;

    protected int headerColumnSize;

    /**
     * @param dumpFile
     * @param dumpFileEncoding
     * @param tokenizer
     */
    public DumpFileReader(File dumpFile, String dumpFileEncoding,
            DumpFileTokenizer tokenizer) {
        if (dumpFile == null) {
            throw new NullPointerException("dumpFile");
        }
        if (dumpFileEncoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        if (tokenizer == null) {
            throw new NullPointerException("tokenizer");
        }
        this.dumpFile = dumpFile;
        this.dumpFileEncoding = dumpFileEncoding;
        this.tokenizer = tokenizer;
    }

    public List<String> readLine() {
        if (isEndOfFile()) {
            return null;
        }
        try {
            List<String> valueList = readLineInternal();
            if (valueList == null) {
                return null;
            }
            lineNumber++;
            if (lineNumber == 0) {
                headerColumnSize = valueList.size();
            } else {
                if (headerColumnSize != valueList.size()) {
                    throw new IllegalDumpColumnSizeRuntimeException(dumpFile
                            .getPath(), lineNumber, valueList.size(),
                            headerColumnSize);
                }
            }
            return valueList;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    protected List<String> readLineInternal() throws IOException {
        if (reader == null) {
            reader = createBufferedReader();
        }
        List<String> row = new ArrayList<String>(30);
        readLoop: for (; read();) {
            nextTokenLoop: for (;;) {
                tokenType = tokenizer.nextToken();
                switch (tokenType) {
                case VALUE:
                    String token = tokenizer.getToken();
                    row.add(DumpUtil.decode(token));
                    break;
                case NULL:
                    row.add(null);
                    break;
                case END_OF_BUFFER:
                    break nextTokenLoop;
                case END_OF_LINE:
                    break readLoop;
                default:
                    break;
                }
            }
        }
        if (isEndOfFile()) {
            if (tokenType == TokenType.END_OF_BUFFER) {
                String token = tokenizer.getToken();
                if (!StringUtil.isEmpty(token)) {
                    row.add(DumpUtil.decode(token));
                }
            }
        }
        return !row.isEmpty() ? row : null;
    }

    protected boolean read() throws IOException {
        if (tokenType == null || tokenType == TokenType.END_OF_BUFFER) {
            bufLength = reader.read(buf);
            if (!isEndOfFile()) {
                tokenizer.addChars(buf, bufLength);
            }
        }
        return !isEndOfFile();
    }

    protected BufferedReader createBufferedReader() throws IOException {
        InputStream is = new FileInputStream(dumpFile);
        return new BufferedReader(new InputStreamReader(is, dumpFileEncoding));
    }

    protected boolean isEndOfFile() {
        return bufLength < 0;
    }

    public int getLineNo() {
        return lineNumber;
    }

    public void close() {
        CloseableUtil.close(reader);
    }
}
