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

import java.util.List;

import org.seasar.framework.util.StringUtil;

/**
 * from句を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class FromClause {

    /**
     * SQLです。
     */
    protected StringBuilder sql;

    /**
     * {@link FromClause}を作成します。
     * 
     */
    public FromClause() {
        this(100);
    }

    /**
     * {@link FromClause}を作成します。
     * 
     * @param capacity
     *            初期容量
     */
    public FromClause(int capacity) {
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
     * from句を追加します。
     * 
     * @param tableName
     *            テーブル名
     * @param tableAlias
     *            テーブル別名
     * 
     */
    public void addSql(String tableName, String tableAlias) {
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" ");
        sql.append(tableAlias);
    }

    /**
     * from句を追加します。
     * 
     * @param tableName
     *            テーブル名
     * @param tableAlias
     *            テーブル別名
     * @param lockHint
     *            ロック用のヒント
     */
    public void addSql(String tableName, String tableAlias, String lockHint) {
        sql.append(" from ").append(tableName).append(" ").append(tableAlias)
                .append(lockHint);
    }

    /**
     * 結合用のSQLを追加します。
     * 
     * @param joinType
     *            結合タイプ
     * @param tableName
     *            テーブル名
     * @param tableAlias
     *            テーブル別名
     * @param fkTableAlias
     *            外部キーを持つテーブルの別名
     * @param pkTableAlias
     *            主キーを持つテーブルの別名
     * @param joinColumnMetaList
     *            結合カラムメタデータのリスト
     * @param lockHint
     *            ロックヒント
     * @param condition
     *            付加的な結合条件
     */
    public void addSql(JoinType joinType, String tableName, String tableAlias,
            String fkTableAlias, String pkTableAlias,
            List<JoinColumnMeta> joinColumnMetaList, String lockHint,
            String condition) {
        switch (joinType) {
        case INNER:
            sql.append(" inner join ");
            break;
        case LEFT_OUTER:
            sql.append(" left outer join ");
            break;
        }
        sql.append(tableName).append(" ").append(tableAlias).append(lockHint)
                .append(" on");
        for (int i = 0; i < joinColumnMetaList.size(); i++) {
            if (i == 0) {
                sql.append(" ");
            } else {
                sql.append(" and ");
            }
            JoinColumnMeta jcm = joinColumnMetaList.get(i);
            sql.append(fkTableAlias).append(".").append(jcm.getName()).append(
                    " = ");
            sql.append(pkTableAlias).append(".").append(
                    jcm.getReferencedColumnName());
        }
        if (!StringUtil.isEmpty(condition)) {
            if (sql.length() > 0) {
                sql.append(" and ");
            }
            sql.append(condition);
        }
    }

}
