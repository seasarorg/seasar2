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
package org.seasar.extension.jdbc.gen.model;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;

/**
 * DDLのモデルです。
 * 
 * @author taedium
 */
public abstract class DdlModel extends GeneratedModel {

    /** テーブル標準名 */
    protected String canonicalTableName;

    /** 名前 */
    protected String name;

    /** 方言 */
    protected GenDialect dialect;

    /** SQLステートメントの区切り文字 */
    protected char delimiter;

    /** SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlKeywordCaseType sqlKeywordCaseType;

    /** SQLの識別子の大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlIdentifierCaseType sqlIdentifierCaseType;

    /** コメントを使用する場合{@code true} */
    protected boolean useComment;

    /**
     * 標準のテーブル名を返します。
     * 
     * @return テーブル標準名
     */
    public String getCanonicalTableName() {
        return canonicalTableName;
    }

    /**
     * 標準のテーブル名を設定します。
     * 
     * @param tableCanonicalName
     *            テーブル標準名
     */
    public void setCanonicalTableName(String tableCanonicalName) {
        this.canonicalTableName = tableCanonicalName;
    }

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方言を返します。
     * 
     * @return 方言
     */
    public GenDialect getDialect() {
        return dialect;
    }

    /**
     * 方言を設定します。
     * 
     * @param dialect
     *            方言
     */
    public void setDialect(GenDialect dialect) {
        this.dialect = dialect;
    }

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * SQLステートメントの区切り文字
     * 
     * @param delimiter
     *            SQLステートメントの区切り文字を設定します。
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型を返します。
     * 
     * @return SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型
     */
    public SqlKeywordCaseType getSqlKeywordCaseType() {
        return sqlKeywordCaseType;
    }

    /**
     * SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型を設定します。
     * 
     * @param sqlKeywordCaseType
     *            SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型
     */
    public void setSqlKeywordCaseType(SqlKeywordCaseType sqlKeywordCaseType) {
        this.sqlKeywordCaseType = sqlKeywordCaseType;
    }

    /**
     * SQLの識別子の大文字小文字を変換するかどうかを示す列挙型を返します。
     * 
     * @return SQLの識別子の大文字小文字を変換するかどうかを示す列挙型
     */
    public SqlIdentifierCaseType getSqlIdentifierCaseType() {
        return sqlIdentifierCaseType;
    }

    /**
     * SQLの識別子の大文字小文字を変換するかどうかを示す列挙型を設定します
     * 
     * @param sqlIdentifierCaseType
     *            SQLの識別子の大文字小文字を変換するかどうかを示す列挙型
     */
    public void setSqlIdentifierCaseType(
            SqlIdentifierCaseType sqlIdentifierCaseType) {
        this.sqlIdentifierCaseType = sqlIdentifierCaseType;
    }

    /**
     * コメントを使用する場合{@code true}を返します。
     * 
     * @return コメントを使用する場合{@code true}
     */
    public boolean isUseComment() {
        return useComment;
    }

    /**
     * コメントを使用する場合{@code true}を設定します。
     * 
     * @param useComment
     *            コメントを使用する場合{@code true}
     */
    public void setUseComment(boolean useComment) {
        this.useComment = useComment;
    }

    /**
     * キーワードの大文字小文字を変換します。
     * 
     * @param keyword
     *            キーワード
     * @return 変換された文字列
     */
    public String keyword(String keyword) {
        return sqlKeywordCaseType.convert(keyword);
    }

    /**
     * 識別子の大文字小文字を変換します。
     * 
     * @param identifier
     *            識別子
     * @return 変換された文字列
     */
    public String identifier(String identifier) {
        return sqlIdentifierCaseType.convert(identifier);
    }

    /**
     * CREATE TABLEでコメントを出力する場合{@code true}
     * 
     * @return CREATE TABLEでコメントを出力する場合{@code true}
     */
    public boolean isCommentInCreateTableSupported() {
        return useComment && dialect.supportsCommentInCreateTable();
    }

    /**
     * COMMENT ONを出力する場合{@code true}を返します。
     * 
     * @return COMMENT ONを出力する場合{@code true}
     */
    public boolean isCommentOnSupported() {
        return useComment && dialect.supportsCommentOn();
    }

}
