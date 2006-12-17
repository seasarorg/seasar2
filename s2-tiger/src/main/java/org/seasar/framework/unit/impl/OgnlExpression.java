/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
 * @author nakamura
 * 
 */
public class OgnlExpression implements Expression {

    private final Object test;

    private final Map<String, Object> context;

    private Exception exception;

    public OgnlExpression(final Object test, final Map<String, Object> context) {
        this.test = test;
        this.context = context;
    }

    public Object evaluate(String expression) {
        Object exp = OgnlUtil.parseExpression(expression);
        return OgnlUtil.getValue(exp, context, test);
    }

    public Object evaluateSuppressException(String expression) {
        try {
            return Ognl.getValue(expression, context, test);
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

    public boolean hasException() {
        return exception != null;
    }

    public void throwExceptionIfNecessary() {
        if (hasException()) {
            throw new OgnlRuntimeException(
                    exception.getCause() == null ? exception : exception
                            .getCause());
        }
    }

}
