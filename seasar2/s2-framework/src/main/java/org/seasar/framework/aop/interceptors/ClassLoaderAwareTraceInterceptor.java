/**
 *
 */
package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * @author shot
 */
public class ClassLoaderAwareTraceInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(TraceInterceptor.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuffer buf = new StringBuffer(256);
        Class targetClass = getTargetClass(invocation);
        ClassLoader targetClassLoader = targetClass.getClassLoader();
        if (targetClassLoader != null) {
            buf.append("<");
            buf.append(targetClassLoader.toString());
            buf.append(">");
        }
        buf.append(targetClass.getName());
        buf.append("#");
        buf.append(invocation.getMethod().getName());
        buf.append("(");
        Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                buf.append(args[i]);
                ClassLoader argsClassLoader = args[i].getClass()
                        .getClassLoader();
                if (argsClassLoader != null) {
                    buf.append("<");
                    buf.append(argsClassLoader.toString());
                    buf.append(">");
                }
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
            if (ret != null) {
                ClassLoader retClassLoader = ret.getClass().getClassLoader();
                if (retClassLoader != null) {
                    buf.append("<");
                    buf.append(retClassLoader.toString());
                    buf.append(">");
                }
            }
        } catch (Throwable t) {
            buf.append(" Throwable:");
            buf.append(t);
            cause = t;
        }
        logger.debug("END " + buf);
        if (cause != null) {
            throw cause;
            
        }
        return ret;
    }

}
