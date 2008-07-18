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
 * SQLスクリプト内のトークンを認識するクラスです。
 * 
 * @author taedium
 */
public interface SqlScriptTokenizer {

    /**
     * トークンのタイプを表します。
     * 
     * @author taedium
     */
    enum TokenType {
        /** 引用符に囲まれたトークン */
        QUOTE,

        /** 1行コメントのトークン */
        LINE_COMMENT,

        /** ブロックコメントの始まりを表すトークン */
        START_OF_BLOCK_COMMENT,

        /** ブロックコメントのトークン */
        BLOCK_COMMENT,

        /** ブロックコメントの終わりを表すトークン */
        END_OF_BLOCK_COMMENT,

        /** SQLステートメントの区切りを表すトークン */
        STATEMENT_DELIMITER,

        /** SQLブロックの区切りを表すトークン */
        BLOCK_DELIMITER,

        /** SQL内の単語を表すトークン */
        WORD,

        /** 空白など単語以外を表すトークン */
        OTHER,

        /** 一行の終わりを表すトークン */
        END_OF_LINE,

        /** ファイルの終わりを表すトークン */
        END_OF_FILE
    }

    /**
     * SQLステートメントやSQLブロックの1行を追加します。
     * 
     * @param line
     *            SQLステートメントやSQLブロックの1行
     */
    void addLine(String line);

    /**
     * 次のトークンタイプを返します。
     * 
     * @return 次のトークンタイプを
     */
    TokenType nextToken();

    /**
     * トークンを返します。
     * 
     * @return トークン
     */
    String getToken();

}
