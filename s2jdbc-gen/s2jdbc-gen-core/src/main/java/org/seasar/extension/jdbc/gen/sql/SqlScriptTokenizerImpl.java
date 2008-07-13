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

import org.seasar.extension.jdbc.gen.SqlScriptTokenizer;

import static org.seasar.extension.jdbc.gen.SqlScriptTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class SqlScriptTokenizerImpl implements SqlScriptTokenizer {

    protected char statementDelimiter;

    protected String blockDelimiter;

    protected String fragment;

    protected int pos;

    protected int nextPos;

    protected int length;

    protected String token;

    protected TokenType type;

    protected boolean blockCommentOpened;

    /**
     * @param statementDelimiter
     * @param blockDelimiter
     */
    public SqlScriptTokenizerImpl(char statementDelimiter, String blockDelimiter) {
        this.statementDelimiter = statementDelimiter;
        this.blockDelimiter = blockDelimiter;
        type = END_OF_FRAGMENT;
    }

    public void addFragment(String fragment) {
        if (type != END_OF_FRAGMENT) {
            throw new IllegalStateException(type.name());
        }
        if (fragment == null) {
            type = END_OF_FILE;
            return;
        }
        this.fragment = fragment;
        length = fragment.length();
        pos = 0;
        nextPos = 0;

        if (blockCommentOpened) {
            type = BLOCK_COMMENT;
        } else if (fragment.trim().equalsIgnoreCase(blockDelimiter)) {
            type = BLOCK_DELIMITER;
            nextPos = length;
        } else {
            peek(pos);
        }
    }

    protected void peek(int index) {
        if (index < length) {
            char c = fragment.charAt(index);
            if (c == '\'') {
                type = QUOTE;
                pos = index;
                nextPos = index + 1;
            } else if (c == statementDelimiter) {
                type = STATEMENT_DELIMITER;
                pos = index;
                nextPos = index + 1;
            } else {
                int nextIndex = index + 1;
                if (nextIndex < length) {
                    char c2 = fragment.charAt(nextIndex);
                    if (c == '-' && c2 == '-') {
                        type = LINE_COMMENT;
                        pos = index;
                        nextPos = index + 2;
                    } else if (c == '/' && c2 == '*') {
                        type = START_OF_BLOCK_COMMENT;
                        pos = index;
                        nextPos = index + 2;
                    } else {
                        type = SQL;
                        pos = index;
                        nextPos = index + 1;
                    }
                } else {
                    type = SQL;
                    pos = index;
                    nextPos = index + 1;
                }
            }
        } else {
            type = END_OF_FRAGMENT;
            pos = length;
            nextPos = length;
        }
    }

    public TokenType nextToken() {
        switch (type) {
        case END_OF_FILE:
            token = null;
            type = END_OF_FILE;
            return END_OF_FILE;
        case END_OF_FRAGMENT:
            token = "";
            type = END_OF_FRAGMENT;
            return END_OF_FRAGMENT;
        case BLOCK_DELIMITER:
            token = fragment;
            type = END_OF_FRAGMENT;
            return BLOCK_DELIMITER;
        case STATEMENT_DELIMITER:
            token = fragment.substring(pos, nextPos);
            peek(nextPos);
            return STATEMENT_DELIMITER;
        case LINE_COMMENT:
            token = fragment.substring(pos, length);
            type = END_OF_FRAGMENT;
            return LINE_COMMENT;
        case START_OF_BLOCK_COMMENT:
            token = fragment.substring(pos, nextPos);
            type = BLOCK_COMMENT;
            pos = pos + 2;
            nextPos = pos + 2;
            return START_OF_BLOCK_COMMENT;
        case BLOCK_COMMENT:
            for (int i = nextPos; i < length; i++) {
                char c = fragment.charAt(i);
                int nextIndex = i + 1;
                if (nextIndex < length) {
                    char c2 = fragment.charAt(nextIndex);
                    if (c == '*' && c2 == '/') {
                        blockCommentOpened = false;
                        token = fragment.substring(pos, i);
                        type = END_OF_BLOCK_COMMENT;
                        pos = i;
                        nextPos = i + 2;
                        return BLOCK_COMMENT;
                    }
                }
            }
            blockCommentOpened = true;
            token = fragment.substring(pos, length);
            type = END_OF_FRAGMENT;
            return BLOCK_COMMENT;
        case END_OF_BLOCK_COMMENT:
            token = fragment.substring(pos, nextPos);
            peek(nextPos);
            return END_OF_BLOCK_COMMENT;
        case QUOTE:
            for (int i = nextPos; i < length; i++) {
                char c = fragment.charAt(i);
                if (c == '\'') {
                    i++;
                    if (i >= length) {
                        token = fragment.substring(pos, i);
                        type = END_OF_FRAGMENT;
                        return QUOTE;
                    } else if (fragment.charAt(i) != '\'') {
                        token = fragment.substring(pos, i);
                        peek(i);
                        return QUOTE;
                    }
                }
            }
            token = fragment.substring(pos, length);
            type = END_OF_FRAGMENT;
            return QUOTE;
        case SQL:
            int startPos = pos;
            for (; type == SQL && pos < length; peek(nextPos)) {
            }
            token = fragment.substring(startPos, pos);
            return SQL;
        }

        throw new IllegalStateException(type.name());
    }

    public String getToken() {
        return token;
    }

}
