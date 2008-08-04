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

    protected boolean endOfFile;

    char[] chars = new char[BUF_SIZE];

    int readSize = 0;

    /**
     * @param dumpFile
     * @param dumpFileEncoding
     * @param tokenizer
     */
    public DumpFileReader(File dumpFile, String dumpFileEncoding,
            DumpFileTokenizer tokenizer) {
        this.dumpFile = dumpFile;
        this.dumpFileEncoding = dumpFileEncoding;
        this.tokenizer = tokenizer;
    }

    public List<String> readRow() {
        try {
            List<String> row = new ArrayList<String>();
            if (reader == null) {
                reader = createBufferedReader();
                read();
            }
            if (endOfFile) {
                return null;
            }
            nextTokenLoop: for (;;) {
                switch (tokenizer.nextToken()) {
                case VALUE:
                case NULLVALUE:
                    row.add(DumpUtil.decode(tokenizer.getToken()));
                    break;
                case END_OF_BUFFER:
                    read();
                    if (endOfFile) {
                        String token = tokenizer.getToken();
                        if (StringUtil.isEmpty(token)) {
                            return null;
                        }
                        row.add(DumpUtil.decode(token));
                        break nextTokenLoop;
                    }
                    break;
                case END_OF_LINE:
                    break nextTokenLoop;
                default:
                    break;
                }
            }
            return row;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    protected void read() throws IOException {
        if ((readSize = reader.read(chars)) > -1) {
            tokenizer.addChars(chars, readSize);
        } else {
            endOfFile = true;
        }
    }

    protected BufferedReader createBufferedReader() throws IOException {
        InputStream is = new FileInputStream(dumpFile);
        return new BufferedReader(new InputStreamReader(is, dumpFileEncoding));
    }

    public void close() {
        CloseableUtil.close(reader);
    }
}
