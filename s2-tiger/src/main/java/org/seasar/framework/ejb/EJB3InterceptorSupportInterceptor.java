package org.seasar.framework.ejb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;

public class EJB3InterceptorSupportInterceptor implements MethodInterceptor {
    protected Class<?> interceptorClass;

    protected Method interceptorMethod;

    protected Field interceptorField;

    public EJB3InterceptorSupportInterceptor(final Class<?> interceptorClass,
            final Method interceptorMethod) {
        this.interceptorClass = interceptorClass;
        this.interceptorMethod = interceptorMethod;
        this.interceptorMethod.setAccessible(true);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (interceptorField == null) {
            final Class<?> targetClass = invocation.getThis().getClass();
            final String fieldName = EJB3InterceptorSupportInterType
                    .getFieldName(interceptorClass);
            interceptorField = ClassUtil.getDeclaredField(targetClass,
                    fieldName);
            interceptorField.setAccessible(true);
        }
        final Object interceptor = FieldUtil.get(interceptorField, invocation
                .getThis());
        final InvocationContext context = new InvocationContextImpl(invocation);
        return MethodUtil.invoke(interceptorMethod, interceptor,
                new Object[] { context });
    }
}
