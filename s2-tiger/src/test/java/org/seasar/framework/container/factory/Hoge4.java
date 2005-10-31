package org.seasar.framework.container.factory;

import org.seasar.framework.container.annotation.tiger.Aspect;

public class Hoge4 {

    @Aspect("aop.traceInterceptor")
    public String getAaa() {
        return null;
    }

    public String getAaa(String aaa) {
        return aaa;
    }
}
