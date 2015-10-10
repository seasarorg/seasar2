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

import java.lang.reflect.Array;
import java.util.List;

import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SqlContext;
import org.seasar.framework.util.OgnlUtil;

/**
 * INのバインド変数用の{@link Node}です。
 * 
 * @author higa
 * 
 */
public class ParenBindVariableNode extends AbstractNode {

    private String expression;

    private Object parsedExpression;

    /**
     * <code>ParenBindVariableNode</code>を作成します。
     * 
     * @param expression
     */
    public ParenBindVariableNode(String expression) {
        this.expression = expression;
        this.parsedExpression = OgnlUtil.parseExpression(expression);
    }

    /**
     * 式を返します。
     * 
     * @return
     */
    public String getExpression() {
        return expression;
    }

    public void accept(SqlContext ctx) {
        Object var = OgnlUtil.getValue(parsedExpression, ctx);
        if (var instanceof List) {
            bindArray(ctx, ((List) var).toArray());
        } else if (var == null) {
            return;
        } else if (var.getClass().isArray()) {
            bindArray(ctx, var);
        } else {
            ctx.addSql("?", var, var.getClass());
        }

    }

    /**
     * @param ctx
     * @param array
     */
    protected void bindArray(SqlContext ctx, Object array) {
        int length = Array.getLength(array);
        if (length == 0) {
            return;
        }
        Class clazz = null;
        for (int i = 0; i < length; ++i) {
            Object o = Array.get(array, i);
            if (o != null) {
                clazz = o.getClass();
            }
        }
        ctx.addSql("(");
        Object value = Array.get(array, 0);
        ctx.addSql("?", value, clazz);
        for (int i = 1; i < length; ++i) {
            ctx.addSql(", ");
            value = Array.get(array, i);
            ctx.addSql("?", value, clazz);
        }
        ctx.addSql(")");
    }
}