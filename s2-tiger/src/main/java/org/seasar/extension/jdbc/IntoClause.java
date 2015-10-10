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
package org.seasar.extension.jdbc;

/**
 * into句を組み立てるクラスです。
 * 
 * @author koichik
 */
public class IntoClause {

    /** into句を組み立てる文字列バッファ */
    protected final StringBuilder sql;

    /**
     * インスタンスを構築します。
     * 
     */
    public IntoClause() {
        this(300);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param capacity
     *            文字列バッファの初期容量
     */
    public IntoClause(final int capacity) {
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
        return new String(sql);
    }

    /**
     * into句を追加します。
     * 
     * @param columnName
     *            カラム名
     */
    public void addSql(final String columnName) {
        if (sql.length() == 0) {
            sql.append(" (").append(columnName).append(')');
        } else {
            sql.setLength(sql.length() - 1);
            sql.append(", ").append(columnName).append(')');
        }
    }

}
