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

import static org.seasar.extension.jdbc.gen.internal.data.DumpFileTokenizer.TokenType.*;

/**
 * ダンプファイル内のトークンを認識するクラスです。
 * 
 * @author taedium
 */
public class DumpFileTokenizer {

    /**
     * トークンタイプ
     * 
     * @author taedium
     */
    public enum TokenType {
        /** 値 */
        VALUE,

        /** null */
        NULL,

        /** 区切り文字 */
        DELIMITER,

        /** 行の終端 */
        END_OF_LINE,

        /** バッファーの終端 */
        END_OF_BUFFER,

        /** ファイルの終端 */
        END_OF_FILE
    }

    /** バッファ */
    protected StringBuilder buf = new StringBuilder(200);

    /** バッファ内の現在位置 */
    protected int pos;

    /** バッファ内の次の位置 */
    protected int nextPos;

    /** バッファの長さ */
    protected int length;

    /** 区切り文字 */
    protected char delimiter;

    /** トークンのタイプ */
    protected TokenType type = END_OF_LINE;

    /** トークン */
    protected String token;

    /** ファイルの終端の場合{@code true} */
    protected boolean endOfFile;

    /**
     * インスタンスを構築します。
     * 
     * @param delimiter
     *            区切り文字
     */
    public DumpFileTokenizer(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 文字をchar配列として追加します。
     * 
     * @param chars
     *            char配列としての文字
     * @param len
     *            有効な文字の長さ、ファイルの終端の場合-1
     */
    public void addChars(char[] chars, int len) {
        if (endOfFile) {
            throw new IllegalStateException("endOfFile");
        }
        if (len < 0) {
            endOfFile = true;
        } else {
            buf.append(chars, 0, len);
        }
        length = buf.length();
        peek(pos);
    }

    /**
     * 次のトークンを前もって調べます。
     * 
     * @param index
     *            開始インデックス
     */
    protected void peek(int index) {
        if (index < length) {
            pos = index;
            char c = buf.charAt(index);
            if (c == '"') {
                for (int i = index + 1; i < length; i++) {
                    c = buf.charAt(i);
                    if (c == '"') {
                        i++;
                        if (i < length) {
                            c = buf.charAt(i);
                            if (c != '"') {
                                for (int j = i; j < length; j++) {
                                    c = buf.charAt(j);
                                    if (c == delimiter || isEndOfLine(j)) {
                                        type = VALUE;
                                        nextPos = j;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                type = endOfFile ? END_OF_FILE : END_OF_BUFFER;
            } else if (c == delimiter) {
                if (type == END_OF_LINE || type == DELIMITER) {
                    type = NULL;
                    nextPos = index;
                } else {
                    type = DELIMITER;
                    nextPos = index + 1;
                }
            } else if (isEndOfLine(index)) {
                if (type == END_OF_LINE || type == DELIMITER) {
                    type = NULL;
                    nextPos = index;
                } else {
                    type = END_OF_LINE;
                    nextPos = isCRLF(index) ? index + 2 : index + 1;
                }
            } else if (c == '\r') {
                if (type == END_OF_LINE || type == DELIMITER) {
                    type = NULL;
                    nextPos = index;
                } else {
                    type = endOfFile ? END_OF_FILE : END_OF_BUFFER;
                }
            } else {
                for (int i = index; i < length; i++) {
                    c = buf.charAt(i);
                    if (c == delimiter || isEndOfLine(i)) {
                        type = VALUE;
                        nextPos = i;
                        return;
                    }
                }
                type = endOfFile ? END_OF_FILE : END_OF_BUFFER;
            }
        } else {
            type = endOfFile ? END_OF_FILE : END_OF_BUFFER;
        }
    }

    /**
     * 行の終端の場合{@code true}を返します。
     * 
     * @param index
     *            開始インデックス
     * @return 行の終端の場合{@code true}
     */
    protected boolean isEndOfLine(int index) {
        char c = buf.charAt(index);
        if (c == '\r') {
            if (index + 1 < length || endOfFile) {
                return true;
            }
        } else if (c == '\n') {
            return true;
        }
        return false;
    }

    /**
     * CRLFを表す場合{@code true}
     * 
     * @param index
     *            開始インデックス
     * @return CRLFを表す場合{@code true}
     */
    protected boolean isCRLF(int index) {
        char c = buf.charAt(index);
        if (c == '\r') {
            int i = index + 1;
            return i < length && buf.charAt(i) == '\n';
        }
        return false;
    }

    /**
     * 次のトークンタイプを返します。
     * 
     * @return 次のトークンタイプ
     */
    public TokenType nextToken() {
        switch (type) {
        case VALUE:
            token = buf.substring(pos, nextPos);
            peek(nextPos);
            return VALUE;
        case NULL:
            token = buf.substring(pos, nextPos);
            peek(nextPos);
            return NULL;
        case DELIMITER:
            token = buf.substring(pos, nextPos);
            peek(nextPos);
            return DELIMITER;
        case END_OF_LINE:
            token = buf.substring(pos, nextPos);
            buf.delete(0, nextPos);
            buf.trimToSize();
            length = buf.length();
            pos = 0;
            nextPos = 0;
            peek(0);
            return END_OF_LINE;
        case END_OF_BUFFER:
            token = buf.substring(pos);
            return END_OF_BUFFER;
        case END_OF_FILE:
            token = buf.substring(pos);
            return END_OF_FILE;
        }

        throw new IllegalStateException(type.name());
    }

    /**
     * トークンを返します。
     * 
     * @return トークン
     */
    public String getToken() {
        return token;
    }
}
