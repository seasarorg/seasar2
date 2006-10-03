package org.seasar.framework.container.factory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class Interceptor2 {
    public Interceptor2() {
    }

    @SuppressWarnings("unused")
    @AroundInvoke
    private Object invoke(InvocationContext context) throws Exception {
        String param = (String) context.getParameters()[0];
        context.setParameters(new Object[] { param + "-Before" });
        String result = (String) context.proceed();
        return result + "-After";
    }
}
