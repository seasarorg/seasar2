package org.seasar.framework.container.factory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class Interceptor1 {
    public Interceptor1() {
    }

    @AroundInvoke
    private Object invoke(InvocationContext context) throws Exception {
        String param = (String) context.getParameters()[0];
        context.setParameters(new Object[] { param + "-BEFORE" });
        String result = (String) context.proceed();
        return result + "-AFTER";
    }
}
