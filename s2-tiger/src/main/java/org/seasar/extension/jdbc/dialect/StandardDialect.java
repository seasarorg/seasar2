/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.dialect;

import java.util.List;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.FromClause;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.util.QueryTokenizer;
import org.seasar.framework.util.StringUtil;

/**
 * 標準的な方言をあつかうクラスです
 * 
 * @author higa
 * 
 */
public class StandardDialect implements DbmsDialect {

    public String getName() {
        return null;
    }

    public boolean supportsLimit() {
        return false;
    }

    public boolean supportsOffset() {
        return supportsLimit();
    }

    public boolean supportsOffsetWithoutLimit() {
        return supportsOffset();
    }

    public boolean supportsCursor() {
        return true;
    }

    public String convertLimitSql(String sql, int offset, int limit) {
        return sql;
    }

    public ValueType getValueType(Class<?> clazz) {
        return ValueTypes.getValueType(clazz);
    }

    public void setupJoin(FromClause fromClause, WhereClause whereClause,
            JoinType joinType, String tableName, String tableAlias,
            String fkTableAlias, String pkTableAlias,
            List<JoinColumnMeta> joinColumnMetaList) {
        fromClause.addSql(joinType, tableName, tableAlias, fkTableAlias,
                pkTableAlias, joinColumnMetaList);

    }

    /**
     * 行番号ファンクション名を返します。
     * 
     * @return 行番号ファンクション名
     */
    public String getRowNumberFunctionName() {
        return "row_number()";
    }

    /**
     * 行番号を使ったSQLに変換します。
     * 
     * @param sql
     *            SQL
     * @param offset
     *            オフセット
     * @param limit
     *            リミット
     * @return offset、limitつきのSQL
     * @throws OrderByNotFoundRuntimeException
     *             <code>order by</code>が見つからない場合。
     */
    protected String convertLimitSqlByRowNumber(String sql, int offset,
            int limit) throws OrderByNotFoundRuntimeException {
        StringBuilder buf = new StringBuilder(sql.length() + 150);
        String lowerSql = sql.toLowerCase();
        int startOfSelect = lowerSql.indexOf("select");
        buf.append(sql.substring(0, startOfSelect));
        buf.append("select * from ( select temp_.*, ");
        buf.append(getRowNumberFunctionName()).append(" over(");
        int orderByIndex = lowerSql.lastIndexOf("order by");
        if (orderByIndex > 0) {
            String orderBy = sql.substring(orderByIndex);
            buf.append(convertOrderBy(orderBy));
            sql = StringUtil.rtrim(sql.substring(0, orderByIndex));
        } else {
            throw new OrderByNotFoundRuntimeException(sql);
        }
        buf.append(") as rownumber_ from ( ");
        buf.append(sql.substring(startOfSelect));
        buf.append(" ) as temp_");
        buf.append(" ) as temp2_ where rownumber_ >= ");
        buf.append(offset + 1);
        if (limit > 0) {
            buf.append(" and rownumber_ <= ");
            buf.append(offset + limit);
        }
        return buf.toString();
    }

    /**
     * order by句のテーブルのエイリアスを一時的なテーブル名に変換します。
     * 
     * @param orderBy
     *            order by句
     * @return 変換後のorder by句
     */
    protected String convertOrderBy(String orderBy) {
        StringBuilder sb = new StringBuilder(10 + orderBy.length());
        QueryTokenizer tokenizer = new QueryTokenizer(orderBy);
        for (int type = tokenizer.nextToken(); type != QueryTokenizer.TT_EOF; type = tokenizer
                .nextToken()) {
            String token = tokenizer.getToken();
            if (type == QueryTokenizer.TT_WORD) {
                String[] names = StringUtil.split(token, ".");
                if (names.length == 2) {
                    sb.append("temp_.").append(names[1]);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }
}