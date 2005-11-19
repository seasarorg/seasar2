package org.seasar.framework.container.factory;

import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.InitMethod;

@Aspect(value="aop.traceInterceptor", pointcut="getAaa")
public class Hoge {
    public String getAaa() {
        return null;
    }

    @InitMethod
    public void init() {
    }
}
