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
package org.seasar.extension.sql.node;

import org.seasar.extension.sql.SqlContext;

/**
 * @author higa
 * 
 */
public class PrefixSqlNode extends AbstractNode {

    private String prefix;

    private String sql;

    public PrefixSqlNode(String prefix, String sql) {
        this.prefix = prefix;
        this.sql = sql;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSql() {
        return sql;
    }

    public void accept(SqlContext ctx) {
        if (ctx.isEnabled()) {
            ctx.addSql(prefix);
        }
        ctx.addSql(sql);
    }
}