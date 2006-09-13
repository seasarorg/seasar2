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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * @author higa
 * 
 */
public class TraceInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -8142348754572405060L;

    private static final Logger logger = Logger
            .getLogger(TraceInterceptor.class);

    protected int maxLengthOfCollection = 10;

    public void setMaxLengthOfCollection(final int maxLengthOfCollection) {
        this.maxLengthOfCollection = maxLengthOfCollection;
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final StringBuffer buf = new StringBuffer(100);
        buf.append(getTargetClass(invocation).getName());
        buf.append("#").append(invocation.getMethod().getName()).append("(");
        final Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                appendObject(buf, args[i]).append(", ");
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
            appendObject(buf, ret);
        } catch (final Throwable t) {
            buf.append(" Throwable:").append(t);
            cause = t;
        }
        logger.debug("END " + buf);
        if (cause == null) {
            return ret;
        }
        throw cause;
    }

    protected StringBuffer appendObject(final StringBuffer buf, final Object arg) {
        if (arg == null) {
            buf.append("null");
        } else if (arg.getClass().isArray()) {
            appendList(buf, Arrays.asList((Object[]) arg));
        } else if (arg instanceof Collection) {
            appendList(buf, (Collection) arg);
        } else {
            buf.append(arg);
        }
        return buf;
    }

    protected StringBuffer appendList(final StringBuffer buf,
            final Collection collection) {
        buf.append("[");
        int count = 0;
        for (final Iterator it = collection.iterator(); it.hasNext()
                && count < maxLengthOfCollection; ++count) {
            appendObject(buf, it.next()).append(", ");
        }
        if (count > 0) {
            buf.setLength(buf.length() - 2);
        }
        buf.append("]");
        return buf;
    }
}