/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.proxy;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.InterType;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.javassist.AspectWeaver;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 
 * @author higa
 */
public final class AopProxy implements Serializable {

    static final long serialVersionUID = 0L;

    private static Logger logger = Logger.getLogger(AopProxy.class);
    
    private static final Method IS_BRIDGE_METHOD = getIsBridgeMethod();

    private final Class targetClass;

    private final Class enhancedClass;

    private final Pointcut defaultPointcut;

    private final AspectWeaver weaver;

    public AopProxy(final Class targetClass, final Aspect[] aspects) {
        this(targetClass, aspects, null, null);
    }

    public AopProxy(final Class targetClass, final Aspect[] aspects, final InterType[] interTypes) {
        this(targetClass, aspects, interTypes, null);
    }

    public AopProxy(final Class targetClass, final Aspect[] aspects, final Map parameters) {
        this(targetClass, aspects, null, parameters);
    }

    public AopProxy(final Class targetClass, final Aspect[] aspects,
            final InterType[] interTypes, final Map parameters) {
        if ((aspects == null || aspects.length == 0) && (interTypes == null
                || interTypes.length == 0)) {
            throw new EmptyRuntimeException("aspects and interTypes");
        }

        this.targetClass = targetClass;
        defaultPointcut = new PointcutImpl(targetClass);

        weaver = new AspectWeaver(targetClass, parameters);
        setupAspects(aspects);
        weaver.setInterTypes(interTypes);
        enhancedClass = weaver.generateClass();
    }

    private void setupAspects(Aspect[] aspects) {
        if (aspects == null || aspects.length == 0) {
            return;
        }

        for (int i = 0; i < aspects.length; ++i) {
            Aspect aspect = aspects[i];
            if (aspect.getPointcut() == null) {
                aspect.setPointcut(defaultPointcut);
            }
        }

        Method[] methods = targetClass.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            if (isBridgeMethod(method)) {
                continue;
            }

            List interceptorList = new ArrayList();
            for (int j = 0; j < aspects.length; ++j) {
                Aspect aspect = aspects[j];
                if (aspect.getPointcut().isApplied(method)) {
                    interceptorList.add(aspect.getMethodInterceptor());
                }
            }
            if (interceptorList.size() == 0) {
                continue;
            }
            if (!isApplicableAspect(method)) {
                logger.log("WSSR0009", new Object[] { targetClass.getName(),
                        method.getName() });
                continue;
            }
            weaver.setInterceptors(method,
                    (MethodInterceptor[]) interceptorList
                            .toArray(new MethodInterceptor[interceptorList
                                    .size()]));
        }
    }

    public Class getEnhancedClass() {
        return enhancedClass;
    }

    public Object create() {
        return ClassUtil.newInstance(enhancedClass);
    }

    public Object create(Class[] argTypes, Object[] args) {
        final Constructor constructor = ClassUtil.getConstructor(enhancedClass,
                argTypes);
        return ConstructorUtil.newInstance(constructor, args);
    }

    private boolean isApplicableAspect(Method method) {
        int mod = method.getModifiers();
        return !Modifier.isFinal(mod) && !Modifier.isStatic(mod);
    }
    
    private boolean isBridgeMethod(final Method method) {
        if (IS_BRIDGE_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_BRIDGE_METHOD, method, null))
                .booleanValue();
    }

    private static Method getIsBridgeMethod() {
        try {
            return Method.class.getMethod("isBridge", null);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}