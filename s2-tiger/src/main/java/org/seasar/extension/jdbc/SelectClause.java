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
package org.seasar.extension.jdbc;

/**
 * select句を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class SelectClause {

    /**
     * SQLです。
     */
    protected StringBuilder sql;

    /**
     * {@link SelectClause}を作成します。
     * 
     */
    public SelectClause() {
        this(300);
    }

    /**
     * {@link SelectClause}を作成します。
     * 
     * @param capacity
     *            初期容量
     */
    public SelectClause(int capacity) {
        sql = new StringBuilder(capacity);
    }

    /**
     * SQLの長さを返します。
     * 
     * @return SQLの長さ
     */
    public int getLength() {
        return sql.length();
    }

    /**
     * SQLに変換します。
     * 
     * @return SQL
     */
    public String toSql() {
        return sql.toString();
    }

    /**
     * select句を追加します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     */
    public void addSql(String tableAlias, String columnName) {
        if (sql.length() > 0) {
            sql.append(", ");
        }
        sql.append(tableAlias);
        sql.append(".");
        sql.append(columnName);
    }

    /**
     * select句を追加します。
     * 
     * @param selectItem
     *            セレクト項目
     * @param columnName
     *            カラム名
     */
    public void addSql(String selectItem) {
        if (sql.length() > 0) {
            sql.append(", ");
        }
        sql.append(selectItem);
    }

}
