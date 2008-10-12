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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.internal.data.DumpFileTokenizer.TokenType;
import org.seasar.extension.jdbc.gen.internal.exception.IllegalDumpColumnSizeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.DumpUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * ダンプファイルのリーダです。
 * 
 * @author taedium
 */
public class DumpFileReader {

    /** バッファのサイズ */
    protected static final int BUF_SIZE = 8192;

    /** ダンプファイル */
    protected File dumpFile;

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding;

    /** トークナイザ */
    protected DumpFileTokenizer tokenizer;

    /** リーダ */
    protected BufferedReader reader;

    /** バッファ */
    protected char[] buf = new char[BUF_SIZE];

    /** バッファ内の値の長さ */
    protected int length;

    /** トークンタイプ */
    protected TokenType tokenType;

    /** 行番号 */
    protected int lineNumber;

    /** ヘッダーの列数 */
    protected int headerColumnSize;

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFile
     *            ダンプファイル
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     * @param tokenizer
     *            トークナイザ
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

    /**
     * 一行を読みます。
     * 
     * @return ファイルの終端に達していなければ一行を表すリスト、ファイルの終端に達していれば{@code null}
     */
    public List<String> readLine() {
        if (isEndOfFile()) {
            return null;
        }
        try {
            lineNumber++;
            List<String> valueList = readLineInternal();
            if (valueList == null) {
                return null;
            }
            if (lineNumber == 1) {
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

    /**
     * 内部的に一行を読みます。
     * 
     * @return ファイルの終端に達していなければ一行を表すリスト、ファイルの終端に達していれば{@code null}
     * @throws IOException
     *             何らかのIO例外が発生した場合
     */
    protected List<String> readLineInternal() throws IOException {
        if (reader == null) {
            reader = createBufferedReader();
        }
        Line line = new Line();
        readLoop: for (; read();) {
            nextTokenLoop: for (;;) {
                tokenType = tokenizer.nextToken();
                switch (tokenType) {
                case VALUE:
                case NULL:
                    String token = tokenizer.getToken();
                    line.add(token);
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
        return line.toList();
    }

    /**
     * バッファにデータを読み込みます。
     * 
     * @return ファイルの終端でない場合{@code true}、ファイルの終端の場合{@code false}
     * @throws IOException
     *             何らかのIO例外が発生した場合
     */
    protected boolean read() throws IOException {
        if (tokenType == null || tokenType == TokenType.END_OF_BUFFER) {
            length = reader.read(buf);
            if (!isEndOfFile()) {
                tokenizer.addChars(buf, length);
            }
        }
        return !isEndOfFile();
    }

    /**
     * {@link BufferedReader}を作成します。
     * 
     * @return {@link BufferedReader}
     * @throws IOException
     *             何らかのIO例外が発生した場合
     */
    protected BufferedReader createBufferedReader() throws IOException {
        InputStream is = new FileInputStream(dumpFile);
        return new BufferedReader(new InputStreamReader(is, dumpFileEncoding));
    }

    /**
     * ファイルの終端に達している場合{@code true}
     * 
     * @return ファイルの終端に達している場合{@code true}
     */
    protected boolean isEndOfFile() {
        return length < 0;
    }

    /**
     * 読み込んだ行番号を返します。
     * <p>
     * 行番号は1から始まります。
     * </p>
     * 
     * @return 行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * クローズします。
     */
    public void close() {
        CloseableUtil.close(reader);
    }

    /**
     * 行を表すクラスです。
     * 
     * @author taedium
     */
    protected class Line {

        /** 値のリスト */
        protected List<String> valueList = new ArrayList<String>(30);

        /**
         * 値のリストを返します。
         * 
         * @return 値のリスト
         */
        public List<String> toList() {
            if (isEndOfFile() && tokenType == TokenType.END_OF_BUFFER) {
                String value = tokenizer.getToken();
                if (value.endsWith("\r")) {
                    value = StringUtil.rtrim(value, "\r");
                }
                if (!StringUtil.isEmpty(value)) {
                    addInternal(value);
                }
            }
            return !valueList.isEmpty() ? valueList : null;
        }

        /**
         * 値を追加します。
         * 
         * @param value
         *            値
         */
        public void add(String value) {
            addInternal(value);
        }

        /**
         * 内部的に値を追加します。
         * 
         * @param value
         *            値
         */
        protected void addInternal(String value) {
            valueList.add(DumpUtil.decode(value));
        }
    }

}
