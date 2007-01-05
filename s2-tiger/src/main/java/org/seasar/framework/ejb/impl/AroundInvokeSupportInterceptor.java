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
package org.seasar.framework.ejb.impl;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author koichik
 * 
 */
public class AroundInvokeSupportInterceptor implements MethodInterceptor {
    protected Method aroundInvokeMethod;

    public AroundInvokeSupportInterceptor(final Method method) {
        this.aroundInvokeMethod = method;
        aroundInvokeMethod.setAccessible(true);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final InvocationContext context = new InvocationContextImpl(invocation);
        return aroundInvokeMethod.invoke(invocation.getThis(), context);
    }
}
