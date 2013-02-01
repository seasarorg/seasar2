/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * 引数や戻り値を出力しない単純なトレース用の{@link MethodInterceptor}です。
 * 
 * @author higa
 * 
 */
public class SimpleTraceInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger
            .getLogger(SimpleTraceInterceptor.class);

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (!logger.isDebugEnabled()) {
            return invocation.proceed();
        }
        final StringBuffer buf = new StringBuffer(100);
        buf.append(getTargetClass(invocation).getName());
        buf.append("#");
        buf.append(invocation.getMethod().getName());
        logger.debug("BEGIN " + buf);
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            buf.append(" Throwable:").append(t);
            throw t;
        } finally {
            logger.debug("END " + buf);
        }
    }

}