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

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * PostgreSQL用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class PostgreDialect extends StandardDialect {

    @Override
    public String getName() {
        return "postgre";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean needsParameterForResultSet() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (limit > 0) {
            buf.append(" limit ");
            buf.append(limit);
        }
        if (offset > 0) {
            buf.append(" offset ");
            buf.append(offset);
        }
        return buf.toString();
    }

    @Override
    public ValueType getValueType(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return ValueTypes.POSTGRE_RESULT_SET;
        }
        return ValueTypes.getValueType(clazz);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportIdentity() {
        return true;
    }

    @Override
    public boolean supportGetGeneratedKeys() {
        return false;
    }

    @Override
    public String getIdentitySelectString(final String tableName,
            final String columnName) {
        return new String(new StringBuilder(64).append("select currval('")
                .append(tableName).append('_').append(columnName).append(
                        "_seq')"));
    }

    @Override
    public boolean supportSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName) {
        return "values nextval for " + sequenceName;
    }

}
