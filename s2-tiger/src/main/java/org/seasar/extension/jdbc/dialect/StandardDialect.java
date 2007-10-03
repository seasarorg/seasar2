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
import org.seasar.extension.jdbc.types.ValueTypes;

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
}