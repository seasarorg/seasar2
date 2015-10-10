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
import org.seasar.framework.util.tiger.Pair;

/**
 * MS SQLServer用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class MssqlDialect extends StandardDialect {

    @Override
    public String getName() {
        return "mssql";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsOffset() {
        return false;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        String lowerSql = sql.toLowerCase();
        int startOfSelect = lowerSql.indexOf("select");
        int endOfSelect = startOfSelect + 6;
        if (" distinct".equals(lowerSql.substring(6, 15))) {
            endOfSelect = startOfSelect + 15;
        }
        buf.append(sql.substring(0, endOfSelect));
        buf.append(" top ");
        buf.append(offset + limit);
        buf.append(sql.substring(endOfSelect));
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
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return type != SelectForUpdateType.WAIT;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, final Pair<String, String>... aliases) {
        return "";
    }

    @Override
    public boolean supportsLockHint() {
        return true;
    }

    @Override
    public String getLockHintString(final SelectForUpdateType type,
            final int waitSeconds) {
        final StringBuilder buf = new StringBuilder(100)
                .append(" with (updlock, rowlock");
        if (type == SelectForUpdateType.NOWAIT) {
            buf.append(", nowait");
        }
        buf.append(")");
        return new String(buf);
    }

}
