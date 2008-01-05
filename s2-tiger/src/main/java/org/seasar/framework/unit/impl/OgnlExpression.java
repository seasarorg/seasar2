/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import java.util.Map;

import ognl.MethodFailedException;
import ognl.Ognl;
import ognl.OgnlException;

import org.seasar.framework.exception.OgnlRuntimeException;
import org.seasar.framework.unit.Expression;
import org.seasar.framework.util.OgnlUtil;

/**
 * OGNL式を表すクラスです。
 * 
 * @author nakamura
 */
public class OgnlExpression implements Expression {

    private final String source;

    private final Object root;

    private final Map<String, Object> context;

    private Exception exception;

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            式の文字列表現
     * @param root
     *            OGNL式のルートとなるオブジェクト
     * @param context
     *            OGNL式のコンテキスト
     */
    public OgnlExpression(final String source, final Object root,
            final Map<String, Object> context) {
        this.source = source;
        this.root = root;
        this.context = context;
    }

    public Object evaluate() {
        Object exp = OgnlUtil.parseExpression(source);
        return OgnlUtil.getValue(exp, context, root);
    }

    public Object evaluateNoException() {
        try {
            return Ognl.getValue(source, context, root);
        } catch (OgnlException e) {
            exception = e;
        }
        return null;
    }

    public boolean isMethodFailed() {
        return hasException() && exception instanceof MethodFailedException;
    }

    public Exception getException() {
        return exception;
    }

    public void throwExceptionIfNecessary() {
        if (hasException()) {
            throw new OgnlRuntimeException(
                    exception.getCause() == null ? exception : exception
                            .getCause());
        }
    }

    /**
     * 例外を持っている場合<code>true</code>を返します。
     * 
     * @return 例外を持っている場合<code>true</code>、持っていない場合<code>false</code>
     */
    protected boolean hasException() {
        return exception != null;
    }
}
