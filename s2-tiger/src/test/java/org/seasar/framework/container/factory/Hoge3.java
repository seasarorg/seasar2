package org.seasar.framework.container.factory;

public class Hoge3 {

    public static final String COMPONENT = "name = aaa, instance = prototype, autoBinding = property";
    
    public static final String ASPECT = "value=aop.traceInterceptor, pointcut=setAaa";

    public static final String INTER_TYPE = "fieldInterType";

    public static final String aaa_BINDING = "aaa2";

    public static final String bbb_BINDING = "bindingType=none";
    
    public static final String INIT_METHOD = "init";

    public void setAaa(String aaa) {
    }

    public void setBbb(String bbb) {
    }
    
    public void init() {
    }
}
