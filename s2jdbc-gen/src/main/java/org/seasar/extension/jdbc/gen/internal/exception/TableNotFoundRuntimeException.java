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
package org.seasar.extension.jdbc.gen.internal.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 条件に合うテーブルが1つも見つからない場合にスローされる例外です。
 * 
 * @author taedium
 */
public class TableNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** 方言クラス名 */
    protected String dialectClassName;

    /** スキーマ名 */
    protected String schemaName;

    /** 読み取り対象とするテーブル名のパターン */
    protected String tableNamePattern;

    /** 読み取り非対象とするテーブル名のパターン */
    protected String ignoreTableNamePattern;

    /**
     * インスタンスを構築します。
     * 
     * @param dialectClassName
     *            方言クラス名
     * @param schemaName
     *            スキーマ名
     * @param tableNamePattern
     *            読み取り対象とするテーブル名のパターン
     * @param ignoreTableNamePattern
     *            読み取り非対象とするテーブル名のパターン
     */
    public TableNotFoundRuntimeException(String dialectClassName,
            String schemaName, String tableNamePattern,
            String ignoreTableNamePattern) {
        super("ES2JDBCGen0032", new Object[] { dialectClassName, schemaName,
                tableNamePattern, ignoreTableNamePattern });
        this.dialectClassName = dialectClassName;
        this.schemaName = schemaName;
        this.tableNamePattern = tableNamePattern;
        this.ignoreTableNamePattern = ignoreTableNamePattern;
    }

    /**
     * 方言クラス名を返します。
     * 
     * @return 方言クラス名
     */
    public String getDialectClassName() {
        return dialectClassName;
    }

    /**
     * 方言クラス名を設定します。
     * 
     * @param dialectClassName
     *            方言クラス名
     */
    public void setDialectClassName(String dialectClassName) {
        this.dialectClassName = dialectClassName;
    }

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * 読み取り対象とするテーブル名のパターンを返します。
     * 
     * @return 読み取り対象とするテーブル名のパターン
     */
    public String getTableNamePattern() {
        return tableNamePattern;
    }

    /**
     * 読み取り対象とするテーブル名のパターンを設定します。
     * 
     * @param tableNamePattern
     *            読み取り対象とするテーブル名のパターン
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * 読み取り非対象とするテーブル名のパターンを返します。
     * 
     * @return 読み取り非対象とするテーブル名のパターン
     */
    public String getIgnoreTableNamePattern() {
        return ignoreTableNamePattern;
    }

    /**
     * 読み取り非対象とするテーブル名のパターンを設定します。
     * 
     * @param ignoreTableNamePattern
     *            読み取り非対象とするテーブル名のパターン
     */
    public void setIgnoreTableNamePattern(String ignoreTableNamePattern) {
        this.ignoreTableNamePattern = ignoreTableNamePattern;
    }

}
