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
package org.seasar.extension.jdbc.util;

import org.seasar.extension.jdbc.exception.SingleQuoteNotCloseRuntimeException;

/**
 * トークンを認識するクラスです。
 * 
 * @author higa
 * 
 */
public class QueryTokenizer {

    /**
     * EOFをあらわします。
     */
    public static final int TT_EOF = -1;

    /**
     * 単語をあらわします。
     */
    public static final int TT_WORD = -2;

    /**
     * 引用文をあらわします。
     */
    public static final int TT_QUOTE = -3;

    /**
     * 単語、引用文以外をあらわします。
     */
    public static final int TT_OTHER = -4;

    private String str;

    private int pos;

    private int nextPos;

    private int length;

    private String token;

    private int type = TT_EOF;

    /**
     * {@link QueryTokenizer}を作成します。
     * 
     * @param str
     *            ソースの文字列
     */
    public QueryTokenizer(String str) {
        this.str = str;
        length = str.length();
        peek(0);
    }

    /**
     * 次のトークンを前もって調べます。
     * 
     * @param index
     *            インデックス
     */
    protected void peek(int index) {
        if (index < length) {
            char c = str.charAt(index);
            if (Character.isWhitespace(c) || isOrdinary(c)) {
                type = TT_OTHER;
            } else if (c == '\'') {
                type = TT_QUOTE;
            } else {
                type = TT_WORD;
            }
            pos = index;
            nextPos = index + 1;
        } else {
            type = TT_EOF;
        }
    }

    /**
     * トークンのタイプを返します。
     * 
     * @return トークンのタイプ
     */
    public int nextToken() {
        if (type == TT_EOF) {
            return TT_EOF;
        }
        if (type == TT_QUOTE) {
            for (int i = nextPos; i < length; i++) {
                char c = str.charAt(i);
                if (c == '\'') {
                    i++;
                    if (i >= length) {
                        token = str.substring(pos, i);
                        type = TT_EOF;
                        return TT_QUOTE;
                    } else if (str.charAt(i) != '\'') {
                        token = str.substring(pos, i);
                        peek(i);
                        return TT_QUOTE;
                    }

                }
            }
            throw new SingleQuoteNotCloseRuntimeException(str);
        }
        for (int i = nextPos; i < length; i++) {
            char c = str.charAt(i);
            if (isOther(c)) {
                if (type == TT_WORD) {
                    token = str.substring(pos, i);
                    pos = i;
                    nextPos = pos + 1;
                    type = TT_OTHER;
                    return TT_WORD;
                }
            } else if (c == '\'') {
                if (type == TT_WORD) {
                    token = str.substring(pos, i);
                    pos = i;
                    nextPos = pos + 1;
                    type = TT_QUOTE;
                    return TT_WORD;
                }
                if (type == TT_OTHER) {
                    token = str.substring(pos, i);
                    pos = i;
                    nextPos = pos + 1;
                    type = TT_QUOTE;
                    return TT_OTHER;
                }
            } else {
                if (type == TT_OTHER) {
                    token = str.substring(pos, i);
                    pos = i;
                    nextPos = pos + 1;
                    type = TT_WORD;
                    return TT_OTHER;
                }
            }
        }
        token = str.substring(pos, length);
        if (type == TT_WORD) {
            type = TT_EOF;
            return TT_WORD;
        }
        type = TT_EOF;
        return TT_OTHER;
    }

    /**
     * 単語以外かどうかを返します。
     * 
     * @param c
     *            文字
     * @return 単語以外かどうか
     */
    protected boolean isOther(char c) {
        return Character.isWhitespace(c) || isOrdinary(c);
    }

    /**
     * 単独で文字として認識するかどうかを返します。
     * 
     * @param c
     *            文字
     * @return 単独で文字として認識するかどうか
     */
    protected boolean isOrdinary(char c) {
        return c == '=' || c == '?' || c == '<' || c == '>' || c == '('
                || c == ')' || c == '!' || c == '*' || c == '-' || c == ',';
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