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
package org.seasar.framework.aop.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 *
 */
public final class PointcutImpl implements Pointcut, Serializable {

	static final long serialVersionUID = 0L;
	private String[] methodNames_;
	private Pattern[] patterns_;
    private Method method_;

	public PointcutImpl(Class targetClass)
		throws EmptyRuntimeException {

		if (targetClass == null) {
			throw new EmptyRuntimeException("targetClass");
		}
		setMethodNames(getMethodNames(targetClass));
	}

	public PointcutImpl(String[] methodNames)
		throws EmptyRuntimeException {

		if (methodNames == null || methodNames.length == 0) {
			throw new EmptyRuntimeException("methodNames");
		}
		setMethodNames(methodNames);
	}

    public PointcutImpl(Method method) {
        method_ = method;
    }

    public boolean isApplied(Method method) {
        if (method_ != null) {
            return method_.equals(method);
        }

        String methodName = method.getName();
        for (int i = 0; i < patterns_.length; ++i) {
			if (patterns_[i].matcher(methodName).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public String[] getMethodNames() {
		return methodNames_;
	}
	
	private void setMethodNames(String[] methodNames) {
		methodNames_ = methodNames;
		patterns_ = new Pattern[methodNames.length];
		for (int i = 0; i < patterns_.length; ++i) {
			patterns_[i] = Pattern.compile(methodNames[i]);
		}
	}

	private static String[] getMethodNames(Class targetClass) {
		Set methodNameSet = new HashSet();
		if (targetClass.isInterface()) {
			addInterfaceMethodNames(methodNameSet, targetClass);
		}
		for (Class clazz = targetClass;
			clazz != Object.class && clazz != null;
			clazz = clazz.getSuperclass()) {
			Class[] interfaces = clazz.getInterfaces();
			for (int i = 0; i < interfaces.length; ++i) {
				addInterfaceMethodNames(methodNameSet, interfaces[i]);
			}
		}
		return (String[]) methodNameSet.toArray(
			new String[methodNameSet.size()]);
	
	}
	
	private static void addInterfaceMethodNames(Set methodNameSet, Class interfaceClass) {	
		Method[] methods = interfaceClass.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			methodNameSet.add(methods[j].getName());
		}
		Class[] interfaces = interfaceClass.getInterfaces();
		for (int i = 0; i < interfaces.length; ++i) {
			addInterfaceMethodNames(methodNameSet, interfaces[i]);
		}
	}
}
