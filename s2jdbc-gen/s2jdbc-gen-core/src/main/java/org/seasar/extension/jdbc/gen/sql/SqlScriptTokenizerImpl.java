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

import java.util.ArrayList;
import java.util.List;

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

    protected String fragment;

    protected int pos;

    protected int nextPos;

    protected int length;

    protected String token;

    protected TokenType type;

    protected boolean commentBlockStarted;

    protected SqlBlockContext sqlBlockContext;

    public SqlScriptTokenizerImpl(GenDialect dialect, char statementDelimiter) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
        this.statementDelimiter = statementDelimiter;
        sqlBlockContext = new SqlBlockContext(dialect);
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

        if (commentBlockStarted) {
            type = BLOCK_COMMENT;
        } else if (fragment.trim().equalsIgnoreCase(
                dialect.getSqlBlockDelimiter())) {
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
                        peekChar(index, c);
                    }
                } else {
                    peekChar(index, c);
                }
            }
        } else {
            type = END_OF_FRAGMENT;
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
        case END_OF_FRAGMENT:
            token = "";
            type = END_OF_FRAGMENT;
            return END_OF_FRAGMENT;
        case BLOCK_DELIMITER:
            token = fragment;
            type = END_OF_FRAGMENT;
            sqlBlockContext.clear();
            return BLOCK_DELIMITER;
        case STATEMENT_DELIMITER:
            token = fragment.substring(pos, nextPos);
            peek(nextPos);
            if (!sqlBlockContext.isInBlock()) {
                sqlBlockContext.clear();
            }
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
                        commentBlockStarted = false;
                        token = fragment.substring(pos, i);
                        type = END_OF_BLOCK_COMMENT;
                        pos = i;
                        nextPos = i + 2;
                        return BLOCK_COMMENT;
                    }
                }
            }
            commentBlockStarted = true;
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
        case WORD:
            int wordStartPos = pos;
            for (; type == WORD && pos < length; peek(nextPos)) {
            }
            token = fragment.substring(wordStartPos, pos);
            sqlBlockContext.addWord(token);
            return WORD;
        case OTHER:
            int otherStartPos = pos;
            for (; type == OTHER && pos < length; peek(nextPos)) {
            }
            token = fragment.substring(otherStartPos, pos);
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

    public boolean isInSqlBlock() {
        return sqlBlockContext.isInBlock();
    }

    protected static class SqlBlockContext {

        protected List<String> wordList = new ArrayList<String>();

        protected GenDialect dialect;

        protected boolean inBlock;

        protected SqlBlockContext(GenDialect dialect) {
            this.dialect = dialect;
        }

        protected void addWord(String word) {
            wordList.add(word.toLowerCase());
        }

        protected boolean isInBlock() {
            if (inBlock) {
                return true;
            }
            inBlock = dialect.isSqlBlockStartWords(wordList);
            return inBlock;
        }

        protected void clear() {
            wordList.clear();
            inBlock = false;
        }
    }

}
