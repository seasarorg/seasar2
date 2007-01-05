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
package org.seasar.framework.container.factory;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.StringUtil;

public class AspectDefFactory {

    protected AspectDefFactory() {
    }

    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            Pointcut pointcut) {
        AspectDef aspectDef = new AspectDefImpl(pointcut);
        aspectDef.setValue(interceptor);
        return aspectDef;
    }

    public static AspectDef createAspectDef(String interceptorName,
            Pointcut pointcut) {
        AspectDef aspectDef = new AspectDefImpl(pointcut);
        aspectDef.setExpression(new OgnlExpression(interceptorName));
        return aspectDef;
    }

    public static AspectDef createAspectDef(String interceptorName,
            String pointcutStr) {
        Pointcut pointcut = createPointcut(pointcutStr);
        return createAspectDef(interceptorName, pointcut);
    }

    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            String pointcutStr) {
        Pointcut pointcut = createPointcut(pointcutStr);
        return createAspectDef(interceptor, pointcut);
    }

    public static AspectDef createAspectDef(String interceptorName,
            Method method) {
        Pointcut pointcut = createPointcut(method);
        return createAspectDef(interceptorName, pointcut);
    }

    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            Method method) {
        Pointcut pointcut = createPointcut(method);
        return createAspectDef(interceptor, pointcut);
    }

    public static Pointcut createPointcut(String pointcutStr) {
        if (!StringUtil.isEmpty(pointcutStr)) {
            String[] methodNames = StringUtil.split(pointcutStr, ", \n");
            return new PointcutImpl(methodNames);
        }
        return null;
    }

    public static Pointcut createPointcut(Class clazz) {
        return new PointcutImpl(clazz);
    }

    public static Pointcut createPointcut(Method method) {
        if (method != null) {
            return new PointcutImpl(method);
        }
        return null;
    }
}
