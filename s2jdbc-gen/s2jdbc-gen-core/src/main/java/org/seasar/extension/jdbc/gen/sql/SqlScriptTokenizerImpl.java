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

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlScriptTokenizer;

import static org.seasar.extension.jdbc.gen.SqlScriptTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class SqlScriptTokenizerImpl implements SqlScriptTokenizer {

    protected GenDialect dialect;

    protected char statementDelimiter;

    protected String line;

    protected int pos;

    protected int nextPos;

    protected int length;

    protected String token;

    protected TokenType type;

    protected boolean commentBlockStarted;

    public SqlScriptTokenizerImpl(GenDialect dialect, char statementDelimiter) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
        this.statementDelimiter = statementDelimiter;
        type = END_OF_LINE;
    }

    public void addLine(String line) {
        if (line == null) {
            type = END_OF_FILE;
            return;
        }
        this.line = line;
        length = line.length();
        pos = 0;
        nextPos = 0;

        if (commentBlockStarted) {
            type = BLOCK_COMMENT;
        } else if (line.trim().equalsIgnoreCase(
                dialect.getSqlBlockDelimiter())) {
            type = BLOCK_DELIMITER;
            nextPos = length;
        } else {
            peek(pos);
        }
    }

    protected void peek(int index) {
        if (index < length) {
            char c = line.charAt(index);
            if (c == '\'') {
                type = QUOTE;
                pos = index;
                nextPos = index + 1;
            } else {
                int nextIndex = index + 1;
                if (nextIndex < length) {
                    char c2 = line.charAt(nextIndex);
                    if (c == '-' && c2 == '-') {
                        type = LINE_COMMENT;
                        pos = index;
                        nextPos = index + 2;
                    } else if (c == '/' && c2 == '*') {
                        type = START_OF_BLOCK_COMMENT;
                        pos = index;
                        nextPos = index + 2;
                    } else {
                        peekChar(index, c);
                    }
                } else {
                    peekChar(index, c);
                }
            }
        } else {
            type = END_OF_LINE;
            pos = length;
            nextPos = length;
        }
    }

    protected void peekChar(int index, char c) {
        if (c == statementDelimiter) {
            type = STATEMENT_DELIMITER;
        } else if (isOther(c)) {
            type = OTHER;
        } else {
            type = WORD;
        }
        pos = index;
        nextPos = index + 1;
    }

    public TokenType nextToken() {
        switch (type) {
        case END_OF_FILE:
            token = null;
            type = END_OF_FILE;
            return END_OF_FILE;
        case END_OF_LINE:
            token = "";
            type = END_OF_LINE;
            return END_OF_LINE;
        case BLOCK_DELIMITER:
            token = line;
            type = END_OF_LINE;
            return BLOCK_DELIMITER;
        case STATEMENT_DELIMITER:
            token = line.substring(pos, nextPos);
            peek(nextPos);
            return STATEMENT_DELIMITER;
        case LINE_COMMENT:
            token = line.substring(pos, length);
            type = END_OF_LINE;
            return LINE_COMMENT;
        case START_OF_BLOCK_COMMENT:
            token = line.substring(pos, nextPos);
            type = BLOCK_COMMENT;
            pos = pos + 2;
            nextPos = pos + 2;
            return START_OF_BLOCK_COMMENT;
        case BLOCK_COMMENT:
            for (int i = nextPos; i < length; i++) {
                char c = line.charAt(i);
                int nextIndex = i + 1;
                if (nextIndex < length) {
                    char c2 = line.charAt(nextIndex);
                    if (c == '*' && c2 == '/') {
                        commentBlockStarted = false;
                        token = line.substring(pos, i);
                        type = END_OF_BLOCK_COMMENT;
                        pos = i;
                        nextPos = i + 2;
                        return BLOCK_COMMENT;
                    }
                }
            }
            commentBlockStarted = true;
            token = line.substring(pos, length);
            type = END_OF_LINE;
            return BLOCK_COMMENT;
        case END_OF_BLOCK_COMMENT:
            token = line.substring(pos, nextPos);
            peek(nextPos);
            return END_OF_BLOCK_COMMENT;
        case QUOTE:
            for (int i = nextPos; i < length; i++) {
                char c = line.charAt(i);
                if (c == '\'') {
                    i++;
                    if (i >= length) {
                        token = line.substring(pos, i);
                        type = END_OF_LINE;
                        return QUOTE;
                    } else if (line.charAt(i) != '\'') {
                        token = line.substring(pos, i);
                        peek(i);
                        return QUOTE;
                    }
                }
            }
            token = line.substring(pos, length);
            type = END_OF_LINE;
            return QUOTE;
        case WORD:
            int wordStartPos = pos;
            for (; type == WORD && pos < length; peek(nextPos)) {
            }
            token = line.substring(wordStartPos, pos);
            return WORD;
        case OTHER:
            int otherStartPos = pos;
            for (; type == OTHER && pos < length; peek(nextPos)) {
            }
            token = line.substring(otherStartPos, pos);
            return OTHER;
        }

        throw new IllegalStateException(type.name());
    }

    protected boolean isOther(char c) {
        return Character.isWhitespace(c) || c == '=' || c == '?' || c == '<'
                || c == '>' || c == '(' || c == ')' || c == '!' || c == '*'
                || c == '-' || c == ',';
    }

    public String getToken() {
        return token;
    }

}
