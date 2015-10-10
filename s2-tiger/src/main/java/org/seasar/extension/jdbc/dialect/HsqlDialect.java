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
package org.seasar.extension.jdbc.dialect;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.SelectForUpdateType;

/**
 * HSQLDB用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class HsqlDialect extends StandardDialect {

    /**
     * 一意制約違反を表す例外コード
     */
    protected static final int uniqueConstraintViolationCode = -104;

    @Override
    public String getName() {
        return "hsql";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (offset > 0) {
            buf.append(" limit ");
            buf.append(limit);
            buf.append(" offset ");
            buf.append(offset);
        } else {
            buf.append(" limit ");
            buf.append(limit);
        }
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean isInsertIdentityColumn() {
        return true;
    }

    @Override
    public String getIdentitySelectString(final String tableName,
            final String columnName) {
        return "call identity()";
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return "SELECT NEXT VALUE FOR "
                + sequenceName
                + " FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE table_name = 'SYSTEM_TABLES'";
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return false;
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final Integer code = getErrorCode(t);
        if (code != null) {
            return uniqueConstraintViolationCode == code.intValue();
        }
        return false;
    }

}
