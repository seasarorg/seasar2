/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.aop.impl.NestedMethodInvocation;
import org.seasar.framework.util.ArrayUtil;

/**
 * @author higa
 * 
 */
public class InterceptorChain extends AbstractInterceptor {

    private static final long serialVersionUID = 1983914340945607081L;

    private MethodInterceptor[] interceptors = new MethodInterceptor[0];

    public void add(MethodInterceptor interceptor) {
        interceptors = (MethodInterceptor[]) ArrayUtil.add(interceptors,
                interceptor);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodInvocation nestInvocation = new NestedMethodInvocation(
                (S2MethodInvocation) invocation, interceptors);
        return nestInvocation.proceed();
    }
}