/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.factory;

import javax.ejb.Stateful;
import javax.interceptor.AroundInvoke;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

/**
 * 
 */
@Stateful
@Interceptors(Interceptor1.class)
public class Hoge14 implements IHoge14 {

    public String foo(String param) {
        return param + "-foo";
    }

    @Interceptors(Interceptor2.class)
    public String bar(String param) {
        return param + "-bar";
    }

    @ExcludeClassInterceptors
    @Interceptors(Interceptor2.class)
    public String baz(String param) {
        return param + "-baz";
    }

    /**
     * @param param
     * @return
     */
    public String hoge(String param) {
        return param + "-hoge";
    }

    @SuppressWarnings("unused")
    @AroundInvoke
    private Object interceptor(InvocationContext context) throws Exception {
        String param = (String) context.getParameters()[0];
        context.setParameters(new Object[] { param + "-before" });
        String result = (String) context.proceed();
        return result + "-after";
    }
}
