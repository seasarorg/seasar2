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
package org.seasar.framework.aop.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.aop.interceptors.InterceptorChain;

/**
 * ネストした{@link MethodInvocation}です。 {@link InterceptorChain}で使われます。
 * 
 * @author koichik
 */
public class NestedMethodInvocation implements S2MethodInvocation {
    // instance fields
    private final S2MethodInvocation parent;

    private final MethodInterceptor[] interceptors;

    private int interceptorsIndex;

    /**
     * {@link NestedMethodInvocation}を作成します。
     * 
     * @param parent
     * @param interceptors
     */
    public NestedMethodInvocation(final S2MethodInvocation parent,
            final MethodInterceptor[] interceptors) {
        this.parent = parent;
        this.interceptors = interceptors;
    }

    public Object proceed() throws Throwable {
        if (interceptorsIndex < interceptors.length) {
            return interceptors[interceptorsIndex++].invoke(this);
        }
        return parent.proceed();
    }

    public Object getThis() {
        return parent.getThis();
    }

    public Object[] getArguments() {
        return parent.getArguments();
    }

    public Method getMethod() {
        return parent.getMethod();
    }

    public AccessibleObject getStaticPart() {
        return parent.getStaticPart();
    }

    public Class getTargetClass() {
        return parent.getTargetClass();
    }

    public Object getParameter(final String name) {
        return parent.getParameter(name);
    }
}