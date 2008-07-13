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
package org.seasar.extension.jdbc.gen;

/**
 * @author taedium
 * 
 */
public interface SqlScriptTokenizer {

    enum TokenType {
        /** */
        QUOTE,
        /** */
        LINE_COMMENT,
        /** */
        START_OF_BLOCK_COMMENT,
        /** */
        BLOCK_COMMENT,
        /** */
        END_OF_BLOCK_COMMENT,
        /** */
        STATEMENT_DELIMITER,
        /** */
        BLOCK_DELIMITER,
        /** */
        SQL,
        /** */
        SQL_STATEMENT,
        /** */
        SQL_BLOCK,
        /** */
        END_OF_FRAGMENT,
        /** */
        END_OF_FILE
    }

    void addFragment(String fragment);

    TokenType nextToken();

    String getToken();
}
