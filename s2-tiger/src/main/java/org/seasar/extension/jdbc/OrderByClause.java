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
 * order by句を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class OrderByClause {

    /**
     * SQLです。
     */
    protected StringBuilder sql;

    /**
     * {@link OrderByClause}を作成します。
     * 
     */
    public OrderByClause() {
        this(70);
    }

    /**
     * {@link OrderByClause}を作成します。
     * 
     * @param capacity
     *            初期容量
     */
    public OrderByClause(int capacity) {
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
     * order by句を追加します。
     * 
     * @param orderBy
     *            order by句
     */
    public void addSql(String orderBy) {
        if (sql.length() == 0) {
            sql.append(" order by ");
        }
        sql.append(orderBy);
    }
}
