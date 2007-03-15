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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3InterceptorDesc;
import org.seasar.framework.ejb.SEJBException;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * 
 */
public class EJB3InterceptorDescImpl implements EJB3InterceptorDesc {

    protected EJB3Desc ejb3desc;

    protected Class<?> interceptorClass;

    protected LinkedList<Method> interceptorMethods = new LinkedList<Method>();

    protected LinkedList<Method> postConstructMethods = new LinkedList<Method>();

    public EJB3InterceptorDescImpl(final EJB3Desc ejb3desc,
            final Class<?> interceptorClass) {
        this.ejb3desc = ejb3desc;
        this.interceptorClass = interceptorClass;
        ClassUtil.getConstructor(interceptorClass, null);
        detectInterceptorMethods();
        detectPostConstructMethod();
        if (interceptorMethods.isEmpty() && postConstructMethods.isEmpty()) {
            throw new SEJBException("ESSR0410", interceptorClass.getName());
        }
    }

    public EJB3Desc getEJB3Desc() {
        return ejb3desc;
    }

    public Class<?> getInterceptorClass() {
        return interceptorClass;
    }

    public List<Method> getInterceptorMethods() {
        return interceptorMethods;
    }

    public List<Method> getPostConstructMethods() {
        return postConstructMethods;
    }

    protected void detectInterceptorMethods() {
        final Set<String> methods = new HashSet<String>();
        for (Class<?> clazz = interceptorClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isBridge()) {
                    continue;
                }
                final Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1
                        || paramTypes[0] != InvocationContext.class) {
                    continue;
                }
                if (method.getReturnType() != Object.class) {
                    continue;
                }
                final String methodName = method.getName();
                if (methods.contains(methodName)) {
                    continue;
                }
                methods.add(methodName);

                final AroundInvoke aroundInvoke = method
                        .getAnnotation(AroundInvoke.class);
                if (aroundInvoke == null) {
                    continue;
                }
                interceptorMethods.addFirst(method);
            }
        }
    }

    protected void detectPostConstructMethod() {
        final Set<String> methods = new HashSet<String>();
        for (Class<?> clazz = interceptorClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isBridge()) {
                    continue;
                }
                final Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1
                        || paramTypes[0] != InvocationContext.class) {
                    continue;
                }
                if (method.getReturnType() != void.class) {
                    continue;
                }
                final String methodName = method.getName();
                if (methods.contains(methodName)) {
                    continue;
                }
                methods.add(methodName);

                final PostConstruct postConstruct = method
                        .getAnnotation(PostConstruct.class);
                if (postConstruct == null) {
                    continue;
                }
                postConstructMethods.addFirst(method);
            }
        }
    }
}
