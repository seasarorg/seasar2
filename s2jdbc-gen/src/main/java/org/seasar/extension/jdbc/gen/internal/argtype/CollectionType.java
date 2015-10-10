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
package org.seasar.extension.jdbc.gen.internal.argtype;

import java.util.ArrayList;
import java.util.Collection;

import org.seasar.extension.jdbc.gen.internal.util.ArgumentUtil;
import org.seasar.framework.util.StringUtil;

import static org.seasar.extension.jdbc.gen.internal.argtype.CollectionType.TokenType.*;

/**
 * {@link Collection}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            コレクションの要素の型
 */
public class CollectionType<T> implements ArgumentType<Collection<? extends T>> {

    /** 開き角括弧 */
    protected static String OPEN_BRACKET = "[";

    /** 閉じ角括弧 */
    protected static String CLOSE_BRACKET = "]";

    /** コレクションの要素に対応する{@link ArgumentType} */
    protected ArgumentType<T> argumentType;

    /**
     * インスタンスを構築します。
     * 
     * @param argumentType
     *            引数の型
     */
    public CollectionType(ArgumentType<T> argumentType) {
        this.argumentType = argumentType;
    }

    public Collection<? extends T> toObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        Collection<T> collection = createCollection();
        String s = StringUtil.ltrim(value, OPEN_BRACKET);
        s = StringUtil.rtrim(s, CLOSE_BRACKET);
        Tokenizer tokenizer = new Tokenizer(s);
        nextTokenLoop: for (;;) {
            switch (tokenizer.nextToken()) {
            case QUOTED_VALUE:
            case VALUE:
                String token = tokenizer.getToken();
                collection.add(argumentType.toObject(token));
                break;
            case END:
                break nextTokenLoop;
            }
        }
        return collection;
    }

    /**
     * コレクション型のインスタンスを作成します。
     * 
     * @return コレクション型のインスタンス
     */
    protected Collection<T> createCollection() {
        return new ArrayList<T>();
    }

    public String toText(Collection<? extends T> value) {
        if (value == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        buf.append(OPEN_BRACKET);
        for (T o : value) {
            buf.append(argumentType.toText(o));
            buf.append(",");
        }
        if (value.size() > 0) {
            buf.setLength(buf.length() - 1);
        }
        buf.append(CLOSE_BRACKET);
        return buf.toString();
    }

    /**
     * トークンのタイプです。
     * 
     * @author taedium
     */
    protected static enum TokenType {
        /** クォートされた値 */
        QUOTED_VALUE,
        /** クォートされていない値 */
        VALUE,
        /** 区切り文字 */
        DELIMITER,
        /** トークンの最後 */
        END
    }

    /**
     * リストの引数の値を区切るクラスです。
     * 
     * @author taedium
     */
    protected static class Tokenizer {

        /** 区切り文字 */
        protected char delimiter = ArgumentUtil.DELIMITER_CHAR;

        /** 引用句 */
        protected char quote = ArgumentUtil.QUOTE_CHAR;

        /** 値 */
        protected String value;

        /** 長さ */
        protected int length;

        /** 位置 */
        protected int pos;

        /** 次の位置 */
        protected int nextPos;

        /** トークン */
        protected String token;

        /** トークンのタイプ */
        protected TokenType type;

        /**
         * インスタンスを構築します。
         * 
         * @param value
         *            値
         */
        protected Tokenizer(String value) {

            this.value = value;
            this.length = value.length();
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
                char c = value.charAt(index);
                if (c == delimiter) {
                    type = DELIMITER;
                    pos = index;
                    nextPos = index + 1;
                } else if (c == quote) {
                    type = QUOTED_VALUE;
                    pos = index;
                    nextPos = index + 1;
                } else {
                    type = VALUE;
                    pos = index;
                    nextPos = index;
                }
            } else {
                type = TokenType.END;
            }
        }

        /**
         * 次のトークンを返します。
         * 
         * @return 次のトークン
         */
        protected TokenType nextToken() {
            switch (type) {
            case QUOTED_VALUE:
                for (int i = nextPos; i < length; i++) {
                    char c = value.charAt(i);
                    if (c == quote) {
                        i++;
                        if (i >= length) {
                            token = value.substring(pos, i);
                            type = END;
                            return QUOTED_VALUE;
                        } else if (value.charAt(i) == delimiter) {
                            token = value.substring(pos, i);
                            peek(i);
                            return QUOTED_VALUE;
                        }
                    }
                }
                token = value.substring(pos, length);
                type = END;
                return QUOTED_VALUE;
            case VALUE:
                for (int i = nextPos; i < length; i++) {
                    char c = value.charAt(i);
                    if (c == delimiter) {
                        token = value.substring(pos, i);
                        peek(i);
                        return VALUE;
                    }
                }
                token = value.substring(pos, length);
                type = END;
                return QUOTED_VALUE;
            case DELIMITER:
                token = value.substring(pos, nextPos);
                peek(nextPos);
                return DELIMITER;
            case END:
                token = null;
                return END;
            }
            throw new IllegalStateException("type");
        }

        /**
         * トークンを返します。
         * 
         * @return トークン
         */
        protected String getToken() {
            return token;
        }
    }

}