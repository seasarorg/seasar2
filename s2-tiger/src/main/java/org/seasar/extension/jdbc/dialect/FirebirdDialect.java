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

/**
 * Firebird用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class FirebirdDialect extends StandardDialect {

    @Override
    public String getName() {
        return "firebird";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        String lowerSql = sql.toLowerCase();
        int startOfSelect = lowerSql.indexOf("select");
        buf.append(sql.substring(0, startOfSelect + 6));
        if (offset > 0) {
            buf.append(" first ");
            buf.append(offset + limit);
            buf.append(" skip ");
            buf.append(offset);
        } else {
            buf.append(" first ");
            buf.append(limit);
        }
        buf.append(sql.substring(startOfSelect + 6));
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return "select gen_id( " + sequenceName + ", " + allocationSize
                + " ) from RDB$DATABASE";
    }

}
