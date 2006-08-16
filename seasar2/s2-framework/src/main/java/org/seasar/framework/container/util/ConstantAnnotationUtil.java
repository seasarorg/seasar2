/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class ConstantAnnotationUtil {

    protected ConstantAnnotationUtil() {
    }

    public static Map convertExpressionToMap(String expression) {
        if (StringUtil.isEmpty(expression)) {
            return null;
        }
        Tokenizer tokenizer = new Tokenizer(expression);
        Map ret = new HashMap();
        for (int token = tokenizer.nextToken(); token != Tokenizer.TT_EOF; token = tokenizer
                .nextToken()) {
            String s = tokenizer.getStringValue();
            token = tokenizer.nextToken();
            if (token == Tokenizer.TT_EQUAL) {
                token = tokenizer.nextToken();
                String s2 = tokenizer.getStringValue();
                ret.put(s, s2);
                tokenizer.nextToken();
            } else if (token == Tokenizer.TT_COMMA) {
                ret.put(null, s);
            } else if (token == Tokenizer.TT_EOF) {
                ret.put(null, s);
                break;
            }
        }
        return ret;
    }

    public static boolean isConstantAnnotation(Field field) {
        return ModifierUtil.isPublicStaticFinalField(field)
                && field.getType().equals(String.class);
    }

    public static class Tokenizer {

        public static final int TT_EOF = -1;

        public static final int TT_QUOTE = '\'';

        public static final int TT_EQUAL = '=';

        public static final int TT_COMMA = ',';

        public static final int TT_WORD = -3;

        private static final int TT_NOTHING = -4;

        private static final int NEED_CHAR = Integer.MAX_VALUE;

        private static final int QUOTE = '\'';

        private static final byte CT_WHITESPACE = 1;

        private static final byte CT_ALPHA = 4;

        private static byte[] ctype = new byte[256];

        private String str_;

        private int colno_ = 0;

        private int ttype_ = TT_NOTHING;

        private String sval_;

        private char[] buf_ = new char[20];

        private int peekc_ = NEED_CHAR;

        private byte peekct_ = 0;

        static {
            setup();
        }

        public Tokenizer(String str) {
            str_ = str;
        }

        private static void setup() {
            wordChars('a', 'z');
            wordChars('A', 'Z');
            wordChars('0', '9');
            wordChars('@', '@');
            wordChars('|', '|');
            wordChars('_', '_');
            wordChars('?', '?');
            wordChars('>', '>');
            wordChars('=', '=');
            wordChars('!', '!');
            wordChars('<', '<');
            wordChars('"', '"');
            wordChars('~', '~');
            wordChars('*', '*');
            ordinaryChar('=');
            ordinaryChar(',');
            whitespaceChars(0, ' ');
        }

        private static void wordChars(int low, int hi) {
            if (low < 0) {
                low = 0;
            }
            if (hi >= ctype.length) {
                hi = ctype.length - 1;
            }
            while (low <= hi) {
                ctype[low++] |= CT_ALPHA;
            }
        }

        private static void whitespaceChars(int low, int hi) {
            if (low < 0) {
                low = 0;
            }
            if (hi >= ctype.length) {
                hi = ctype.length - 1;
            }
            while (low <= hi) {
                ctype[low++] = CT_WHITESPACE;
            }
        }

        private static void ordinaryChar(int ch) {
            if (ch >= 0 && ch < ctype.length) {
                ctype[ch] = 0;
            }
        }

        public final int getTokenType() {
            return ttype_;
        }

        public final String getStringValue() {
            return sval_;
        }

        public int nextToken() {
            initVal();
            if (processEOF()) {
                return ttype_;
            }
            if (processWhitespace()) {
                return ttype_;
            }
            if (processWord()) {
                return ttype_;
            }
            if (processQuote()) {
                return ttype_;
            }
            if (processOrdinary()) {
                return ttype_;
            }
            return ttype_ = peekc_;
        }

        public final String getReadString() {
            return str_.substring(0, colno_ - 1);
        }

        private int read() {
            if (colno_ >= str_.length()) {
                return -1;
            }
            return str_.charAt(colno_++);
        }

        private void initVal() {
            sval_ = null;
        }

        private boolean processEOF() {
            if (peekc_ < 0) {
                ttype_ = TT_EOF;
                return true;
            }
            if (peekc_ == NEED_CHAR) {
                peekc_ = read();
                if (peekc_ < 0) {
                    ttype_ = TT_EOF;
                    return true;
                }
            }
            return false;
        }

        private boolean processWhitespace() {
            peekct_ = peekc_ < 256 ? ctype[peekc_] : CT_ALPHA;
            while ((peekct_ & CT_WHITESPACE) != 0) {
                if (peekc_ == '\r') {
                    peekc_ = read();
                    if (peekc_ == '\n') {
                        peekc_ = read();
                    }
                } else {
                    peekc_ = read();
                }
                if (peekc_ < 0) {
                    ttype_ = TT_EOF;
                    return true;
                }
                peekct_ = peekc_ < 256 ? ctype[peekc_] : CT_ALPHA;
            }
            return false;
        }

        private boolean processWord() {
            if ((peekct_ & CT_ALPHA) != 0) {
                int i = 0;
                do {
                    if (i >= buf_.length) {
                        char nb[] = new char[buf_.length * 2];
                        System.arraycopy(buf_, 0, nb, 0, buf_.length);
                        buf_ = nb;
                    }
                    buf_[i++] = (char) peekc_;
                    peekc_ = read();
                    peekct_ = peekc_ < 0 ? CT_WHITESPACE
                            : (peekc_ < 256 ? ctype[peekc_] : CT_ALPHA);
                } while ((peekct_ & (CT_ALPHA)) != 0);
                sval_ = String.copyValueOf(buf_, 0, i);
                ttype_ = TT_WORD;
                return true;
            }
            return false;
        }

        private boolean processQuote() {
            if (peekc_ == QUOTE) {
                ttype_ = QUOTE;
                int i = 0;
                int d = read();
                int c = d;
                while (d >= 0) {
                    if (d == QUOTE) {
                        int d2 = read();
                        if (d2 == QUOTE) {
                            c = QUOTE;
                        } else {
                            d = d2;
                            break;
                        }
                    } else {
                        c = d;
                    }
                    if (i >= buf_.length) {
                        char nb[] = new char[buf_.length * 2];
                        System.arraycopy(buf_, 0, nb, 0, buf_.length);
                        buf_ = nb;
                    }
                    buf_[i++] = (char) c;
                    d = read();
                }
                peekc_ = d;
                sval_ = String.copyValueOf(buf_, 0, i);
                return true;
            }
            return false;
        }

        private boolean processOrdinary() {
            if (peekct_ == 0) {
                ttype_ = peekc_;
                peekc_ = read();
                peekct_ = peekc_ < 0 ? CT_WHITESPACE
                        : (peekc_ < 256 ? ctype[peekc_] : CT_ALPHA);
                return true;
            }
            return false;
        }
    }
}