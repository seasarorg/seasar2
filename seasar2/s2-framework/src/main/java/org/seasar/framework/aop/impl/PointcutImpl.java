/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ModifierUtil;

/**
 * {@link Pointcut}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PointcutImpl implements Pointcut, Serializable {

    static final long serialVersionUID = 0L;

    private String[] methodNames;

    private Pattern[] patterns;

    private Method method;

    /**
     * {@link PointcutImpl}を作成します。
     * 
     * @param targetClass
     * @throws EmptyRuntimeException
     */
    public PointcutImpl(Class targetClass) throws EmptyRuntimeException {

        if (targetClass == null) {
            throw new EmptyRuntimeException("targetClass");
        }
        setMethodNames(getMethodNames(targetClass));
    }

    /**
     * {@link PointcutImpl}を作成します。
     * 
     * @param methodNames
     * @throws EmptyRuntimeException
     */
    public PointcutImpl(String[] methodNames) throws EmptyRuntimeException {

        if (methodNames == null || methodNames.length == 0) {
            throw new EmptyRuntimeException("methodNames");
        }
        setMethodNames(methodNames);
    }

    /**
     * {@link PointcutImpl}を作成します。
     * 
     * @param method
     */
    public PointcutImpl(Method method) {
        this.method = method;
    }

    public boolean isApplied(Method targetMethod) {
        if (method != null) {
            return method.equals(targetMethod);
        }

        String methodName = targetMethod.getName();
        for (int i = 0; i < patterns.length; ++i) {
            if (patterns[i].matcher(methodName).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 対象になったメソッド名の配列を返します。
     * 
     * @return
     */
    public String[] getMethodNames() {
        return methodNames;
    }

    private void setMethodNames(String[] methodNames) {
        this.methodNames = methodNames;
        patterns = new Pattern[methodNames.length];
        for (int i = 0; i < patterns.length; ++i) {
            patterns[i] = Pattern.compile(methodNames[i]);
        }
    }

    private static String[] getMethodNames(Class targetClass) {
        Set methodNameSet = new HashSet();
        if (targetClass.isInterface()) {
            addInterfaceMethodNames(methodNameSet, targetClass);
        }
        for (Class clazz = targetClass; clazz != Object.class && clazz != null; clazz = clazz
                .getSuperclass()) {
            Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                addInterfaceMethodNames(methodNameSet, interfaces[i]);
            }
        }
        if (methodNameSet.isEmpty()) {
            addClassMethodNames(methodNameSet, targetClass);
        }
        return (String[]) methodNameSet
                .toArray(new String[methodNameSet.size()]);

    }

    private static void addInterfaceMethodNames(Set methodNameSet,
            Class interfaceClass) {
        Method[] methods = interfaceClass.getDeclaredMethods();
        for (int j = 0; j < methods.length; j++) {
            methodNameSet.add(methods[j].getName());
        }
        Class[] interfaces = interfaceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            addInterfaceMethodNames(methodNameSet, interfaces[i]);
        }
    }

    private static void addClassMethodNames(Set methodNameSet, Class clazz) {
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            if (MethodUtil.isSyntheticMethod(method)
                    || MethodUtil.isBridgeMethod(method)) {
                continue;
            }
            if (ModifierUtil.isFinal(method)) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            methodNameSet.add(methods[i].getName());
        }
    }
}
