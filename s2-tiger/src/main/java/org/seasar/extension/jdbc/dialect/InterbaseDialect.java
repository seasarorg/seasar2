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

import javax.persistence.GenerationType;

/**
 * Interbase用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class InterbaseDialect extends StandardDialect {

    @Override
    public String getName() {
        return "interbase";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 15);
        buf.append(sql);
        if (offset > 0) {
            buf.append(" rows ");
            buf.append(offset);
            buf.append(" to ");
            buf.append(limit);
        } else {
            buf.append(" rows ");
            buf.append(limit);
        }
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public boolean supportSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName) {
        return "select RDB$GENERATOR_NAME from RDB$GENERATORS";
    }

}
