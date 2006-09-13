/**
 *
 */
package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * @author shot
 */
public class ClassLoaderAwareTraceInterceptor extends TraceInterceptor {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TraceInterceptor.class);

    public Object invoke(final MethodInvocation invocation) throws Throwable {
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
