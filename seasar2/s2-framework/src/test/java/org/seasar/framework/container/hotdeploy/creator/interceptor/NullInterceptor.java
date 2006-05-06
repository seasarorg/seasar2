package org.seasar.framework.container.hotdeploy.creator.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class NullInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation arg0) throws Throwable {
        return null;
    }
}
