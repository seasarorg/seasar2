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
 * where句を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class WhereClause {

    /**
     * WHEREのキーワードです。
     */
    public static final String WHERE_KEYWORD = " where ";

    /**
     * ANDのキーワードです。
     */
    public static final String AND_KEYWORD = " and ";

    /**
     * SQLです。
     */
    protected StringBuilder sql;

    /**
     * {@link WhereClause}を作成します。
     * 
     */
    public WhereClause() {
        this(200);
    }

    /**
     * {@link WhereClause}を作成します。
     * 
     * @param capacity
     *            初期容量
     */
    public WhereClause(int capacity) {
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
     * <p>
     * where句を追加します。
     * </p>
     * <p>
     * 最初に追加される条件には<code>where</code>が先頭に自動的に追加されます。
     * </p>
     * <p>
     * 2番目以降に追加される条件には<code>and</code>が先頭に自動的に追加されます。
     * </p>
     * 
     * @param condition
     *            条件
     * @return 追加したwhere句の長さを返します。
     */
    public int addAndSql(String condition) {
        int length = sql.length();
        if (length == 0) {
            sql.append(WHERE_KEYWORD).append(condition);
        } else {
            sql.append(AND_KEYWORD).append(condition);
        }
        return sql.length() - length;
    }

    /**
     * <p>
     * where句を追加します。
     * </p>
     * <p>
     * 最初に追加される条件には<code>where</code>が先頭に自動的に追加されます。
     * </p>
     * 
     * @param condition
     *            条件
     * @return 追加したwhere句の長さを返します。
     */
    public int addSql(String condition) {
        int length = sql.length();
        if (length == 0) {
            sql.append(WHERE_KEYWORD).append(condition);
        } else {
            sql.append(condition);
        }
        return sql.length() - length;
    }

    /**
     * 追加したwhere句を最後のほうから削除します。
     * 
     * @param length
     *            長さ
     */
    public void removeSql(int length) {
        sql.setLength(sql.length() - length);
    }
}
