package org.seasar.framework.container.factory;

import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.annotation.tiger.InterType;

@Aspect(value="aop.traceInterceptor", pointcut="getAaa")
@InterType("fieldInterType")
public class Hoge {
    public String getAaa() {
        return null;
    }

    @InitMethod
    public void init() {
    }
}
