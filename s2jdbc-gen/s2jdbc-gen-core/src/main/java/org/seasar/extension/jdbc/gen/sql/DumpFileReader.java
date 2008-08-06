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

    protected int rowNo = -1;

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

    public List<String> readRow() {
        if (isEndOfFile()) {
            return null;
        }
        try {
            List<String> row = readRowInternal();
            rowNo++;
            if (rowNo == 0) {
                headerColumnSize = row.size();
            } else {
                if (headerColumnSize != row.size()) {
                    throw new IllegalDumpColumnSizeRuntimeException(dumpFile
                            .getPath(), rowNo, row.size(), headerColumnSize);
                }
            }
            return row;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    protected List<String> readRowInternal() throws IOException {
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
                row.add(DumpUtil.decode(token));
            }
        }
        return row;
    }

    protected boolean read() throws IOException {
        if (tokenType == null || tokenType == TokenType.END_OF_BUFFER) {
            bufLength = reader.read(buf);
            if (bufLength > -1) {
                tokenizer.addChars(buf, bufLength);
            }
        }
        return bufLength > -1;
    }

    protected BufferedReader createBufferedReader() throws IOException {
        InputStream is = new FileInputStream(dumpFile);
        return new BufferedReader(new InputStreamReader(is, dumpFileEncoding));
    }

    protected boolean isEndOfFile() {
        return bufLength == -1;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void close() {
        CloseableUtil.close(reader);
    }
}
