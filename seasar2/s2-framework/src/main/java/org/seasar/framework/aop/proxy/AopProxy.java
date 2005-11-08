/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.javassist.AspectWeaver;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;

/**
 * 
 * @author higa
 */
public final class AopProxy implements Serializable {

	static final long serialVersionUID = 0L;

	private static Logger logger_ = Logger.getLogger(AopProxy.class);

	private Class targetClass_;

    private Class enhancedClass_;

	private Pointcut defaultPointcut_;

	private Map parameters_;

	public AopProxy(Class targetClass, Aspect[] aspects) {
		this(targetClass, aspects, null);
	}

	public AopProxy(Class targetClass, Aspect[] aspects, Map parameters) {
		parameters_ = parameters;
		setTargetClass(targetClass);
		setAspects(aspects);
	}

	private void setTargetClass(Class targetClass) {
		targetClass_ = targetClass;
		defaultPointcut_ = new PointcutImpl(targetClass);
	}

	private void setAspects(Aspect[] aspects) {
		if (aspects == null || aspects.length == 0) {
			throw new EmptyRuntimeException("aspects");
		}

		AspectWeaver weaver = new AspectWeaver(targetClass_, parameters_);

        for (int i = 0; i < aspects.length; ++i) {
            Aspect aspect = aspects[i];
            if (aspect.getPointcut() == null) {
                aspect.setPointcut(defaultPointcut_);
            }
        }

        Method[] methods = targetClass_.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            List interceptorList = new ArrayList();
            for (int j = 0; j < aspects.length; ++j) {
                Aspect aspect = aspects[j];
                if (aspect.getPointcut().isApplied(method)) {
                    interceptorList.add(aspect.getMethodInterceptor());
                }
            }
            if (interceptorList.size() > 0) {
                if (isApplicableAspect(method)) {
                    weaver.setInterceptors(method, (MethodInterceptor[]) interceptorList
                            .toArray(new MethodInterceptor[interceptorList.size()]));
                }
                else {
                    logger_.log("WSSR0009", new Object[] { targetClass_.getName(),
                            method.getName() });
                }
            }
        }

		enhancedClass_ = weaver.generateClass();
	}

	public Class getEnhancedClass() {
	    return enhancedClass_;
	}

	public Object create() {
        return ClassUtil.newInstance(enhancedClass_);
	}

	public Object create(Class[] argTypes, Object[] args) {
        final Constructor constructor = ClassUtil.getConstructor(enhancedClass_, argTypes);
        return ConstructorUtil.newInstance(constructor, args);
	}

	private boolean isApplicableAspect(Method method) {
		int mod = method.getModifiers();
		return !Modifier.isFinal(mod) && !Modifier.isStatic(mod);
	}
}