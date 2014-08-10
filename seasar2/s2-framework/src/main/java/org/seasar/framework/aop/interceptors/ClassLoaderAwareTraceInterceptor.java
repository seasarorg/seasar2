/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
 * {@link ClassLoader}の情報も出力する{@link TraceInterceptor}です。
 * 
 * @author shot
 */
public class ClassLoaderAwareTraceInterceptor extends TraceInterceptor {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger
            .getLogger(TraceInterceptor.class);

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (!logger.isDebugEnabled()) {
            return invocation.proceed();
        }
        final StringBuffer buf = new StringBuffer(256);
        appendClassLoader(buf, invocation.getThis());
        final Class targetClass = getTargetClass(invocation);
        buf.append(targetClass.getName());
        buf.append("#");
        buf.append(invocation.getMethod().getName());
        buf.append("(");
        final Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                final Object arg = args[i];
                appendObject(buf, arg);
                appendClassLoader(buf, arg);
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
            appendObject(buf, ret);
            appendClassLoader(buf, ret);
        } catch (final Throwable t) {
            buf.append(" Throwable:").append(t);
            cause = t;
        }
        logger.debug("END " + buf);
        if (cause != null) {
            throw cause;

        }
        return ret;
    }

    /**
     * クラスローダの情報を追加します。
     * 
     * @param buf
     *            バッファ
     * @param obj
     *            オブジェクト
     * @return 結果のバッファ
     */
    protected StringBuffer appendClassLoader(final StringBuffer buf,
            final Object obj) {
        if (obj != null) {
            final ClassLoader classLoader = obj.getClass().getClassLoader();
            if (classLoader != null) {
                buf.append("<").append(classLoader.toString()).append(">");
            }
        }
        return buf;
    }
}
