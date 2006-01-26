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
package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * @author higa
 * 
 */
public class TraceInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -8142348754572405060L;

    private static Logger logger = Logger.getLogger(TraceInterceptor.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuffer buf = new StringBuffer(100);
        buf.append(getTargetClass(invocation).getName());
        buf.append("#");
        buf.append(invocation.getMethod().getName());
        buf.append("(");
        Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                buf.append(args[i]);
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        Object ret = null;
        Throwable cause = null;
        logger.debug("BEGIN " + buf);
        try {
            ret = invocation.proceed();
            buf.append(" : ");
            buf.append(ret);
        } catch (Throwable t) {
            buf.append(" Throwable:");
            buf.append(t);
            cause = t;
        }
        logger.debug("END " + buf);
        if (cause == null) {
            return ret;
        }
        throw cause;
    }
}