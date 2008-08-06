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

import static org.seasar.extension.jdbc.gen.sql.DumpFileTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class DumpFileTokenizer {

    enum TokenType {
        VALUE,

        NULL,

        DELIMETER,

        END_OF_LINE,

        END_OF_BUFFER,

        UNKOWN
    }

    protected StringBuilder buf = new StringBuilder(200);

    protected int pos;

    protected int nextPos;

    protected int length;

    protected char delimiter;

    protected TokenType type = END_OF_LINE;

    protected String token;

    public DumpFileTokenizer(char delimiter) {
        this.delimiter = delimiter;
    }

    public void addChars(char[] chars, int len) {
        buf.append(chars, 0, len);
        length = buf.length();
        peek(pos);
    }

    protected void peek(int index) {
        if (index < length) {
            pos = index;
            char c = buf.charAt(index);
            if (c == '"') {
                for (int i = index + 1; i < length; i++) {
                    c = buf.charAt(i);
                    if (c == '"') {
                        i++;
                        if (i >= length) {
                            type = END_OF_BUFFER;
                            return;
                        } else if (buf.charAt(i) != '"') {
                            type = VALUE;
                            nextPos = i;
                            return;
                        }
                    }
                }
                type = END_OF_BUFFER;
            } else if (c == delimiter) {
                if (type == END_OF_LINE || type == DELIMETER) {
                    type = NULL;
                    nextPos = index;
                } else {
                    type = DELIMETER;
                    nextPos = index + 1;
                }
            } else if (c == '\r') {
                int i = index + 1;
                if (i >= length) {
                    type = END_OF_BUFFER;
                } else if (buf.charAt(i) == '\n') {
                    if (type == END_OF_LINE || type == DELIMETER) {
                        type = NULL;
                        nextPos = index;
                    } else {
                        type = END_OF_LINE;
                        nextPos = i + 1;
                    }
                }
            } else {
                for (int i = index; i < length; i++) {
                    c = buf.charAt(i);
                    if (c == delimiter) {
                        type = VALUE;
                        nextPos = i;
                        return;
                    } else if (c == '\r') {
                        int j = i + 1;
                        if (j < length && buf.charAt(j) == '\n') {
                            type = VALUE;
                            nextPos = i;
                            return;
                        }
                    }
                }
                type = END_OF_BUFFER;
            }
        } else {
            type = END_OF_BUFFER;
        }
    }

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
        case DELIMETER:
            token = buf.substring(pos, nextPos);
            peek(nextPos);
            return DELIMETER;
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
            type = UNKOWN;
            return END_OF_BUFFER;
        }

        throw new IllegalStateException(type.name());
    }

    public String getToken() {
        return token;
    }
}
