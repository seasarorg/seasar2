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
package org.seasar.framework.util;

/**
 * @author higa
 * 
 */
public class Tokenizer {

    public static final int TT_EOF = -1;

    public static final int TT_QUOTE = '\'';

    public static final int TT_WORD = -3;

    private static final int TT_NOTHING = -4;

    private static final int NEED_CHAR = Integer.MAX_VALUE;

    private static final int QUOTE = '\'';

    private static final byte CT_WHITESPACE = 1;

    private static final byte CT_ALPHA = 4;

    private static byte[] ctype = new byte[256];

    private String str;

    private int colno = 0;

    private int ttype = TT_NOTHING;

    private String sval;

    private char[] buf = new char[20];

    private int peekc = NEED_CHAR;

    private byte peekct = 0;

    static {
        setup();
    }

    public Tokenizer(String str) {
        this.str = str;
    }

    protected static void setup() {
        wordChars('a', 'z');
        wordChars('A', 'Z');
        wordChars('0', '9');
        wordChar('@');
        wordChar('|');
        wordChar('_');
        wordChar('?');
        wordChar('>');
        wordChar('=');
        wordChar('!');
        wordChar('<');
        wordChar('"');
        wordChar('~');
        wordChar('*');
        wordChar('.');
        // ordinaryChar('=');
        // ordinaryChar(',');
        whitespaceChars(0, ' ');
    }

    protected static void wordChars(int low, int hi) {
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

    protected static void wordChar(int val) {
        ctype[val] |= CT_ALPHA;
    }

    protected static void whitespaceChars(int low, int hi) {
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

    protected static void ordinaryChar(int ch) {
        if (ch >= 0 && ch < ctype.length) {
            ctype[ch] = 0;
        }
    }

    public final String getStringValue() {
        return sval;
    }

    public int nextToken() {
        initVal();
        if (processEOF()) {
            return ttype;
        }
        if (processWhitespace()) {
            return ttype;
        }
        if (processWord()) {
            return ttype;
        }
        if (processQuote()) {
            return ttype;
        }
        if (processOrdinary()) {
            return ttype;
        }
        return ttype = peekc;
    }

    public final String getReadString() {
        return str.substring(0, colno - 1);
    }

    private int read() {
        if (colno >= str.length()) {
            return -1;
        }
        return str.charAt(colno++);
    }

    private void initVal() {
        sval = null;
    }

    private boolean processEOF() {
        if (peekc < 0) {
            ttype = TT_EOF;
            return true;
        }
        if (peekc == NEED_CHAR) {
            peekc = read();
            if (peekc < 0) {
                ttype = TT_EOF;
                return true;
            }
        }
        return false;
    }

    private boolean processWhitespace() {
        peekct = peekc < 256 ? ctype[peekc] : CT_ALPHA;
        while ((peekct & CT_WHITESPACE) != 0) {
            if (peekc == '\r') {
                peekc = read();
                if (peekc == '\n') {
                    peekc = read();
                }
            } else {
                peekc = read();
            }
            if (peekc < 0) {
                ttype = TT_EOF;
                return true;
            }
            peekct = peekc < 256 ? ctype[peekc] : CT_ALPHA;
        }
        return false;
    }

    private boolean processWord() {
        if ((peekct & CT_ALPHA) != 0) {
            int i = 0;
            do {
                if (i >= buf.length) {
                    char nb[] = new char[buf.length * 2];
                    System.arraycopy(buf, 0, nb, 0, buf.length);
                    buf = nb;
                }
                buf[i++] = (char) peekc;
                peekc = read();
                peekct = peekc < 0 ? CT_WHITESPACE
                        : (peekc < 256 ? ctype[peekc] : CT_ALPHA);
            } while ((peekct & (CT_ALPHA)) != 0);
            sval = String.copyValueOf(buf, 0, i);
            ttype = TT_WORD;
            return true;
        }
        return false;
    }

    private boolean processQuote() {
        if (peekc == QUOTE) {
            ttype = QUOTE;
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
                if (i >= buf.length) {
                    char nb[] = new char[buf.length * 2];
                    System.arraycopy(buf, 0, nb, 0, buf.length);
                    buf = nb;
                }
                buf[i++] = (char) c;
                d = read();
            }
            peekc = d;
            sval = String.copyValueOf(buf, 0, i);
            return true;
        }
        return false;
    }

    private boolean processOrdinary() {
        if (peekct == 0) {
            ttype = peekc;
            peekc = read();
            peekct = peekc < 0 ? CT_WHITESPACE : (peekc < 256 ? ctype[peekc]
                    : CT_ALPHA);
            return true;
        }
        return false;
    }
}