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
package org.seasar.extension.sql.node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.extension.sql.SqlContext;
import org.seasar.extension.sql.context.SqlContextImpl;

/**
 * WHERE句のための<code>Node</code>です。
 * 
 * @author higa
 * 
 */
public class AddWhereIfNode extends ContainerNode {

    Pattern pat = Pattern.compile("\\s*(order\\sby)|$)");

    /**
     * <code>AddWhereIfNode</code>を作成します。
     */
    public AddWhereIfNode() {
    }

    public void accept(SqlContext ctx) {
        SqlContext childCtx = new SqlContextImpl(ctx);
        super.accept(childCtx);
        if (childCtx.isEnabled()) {
            String sql = childCtx.getSql();
            Matcher m = pat.matcher(sql);
            if (!m.lookingAt()) {
                sql = " WHERE " + sql;
            }
            ctx.addSql(sql, childCtx.getBindVariables(), childCtx
                    .getBindVariableTypes());
        }
    }

}