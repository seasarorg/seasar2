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
package org.seasar.framework.aop.javassist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javassist.ClassPool;
import javassist.CtClass;

import org.seasar.framework.aop.InterType;

/**
 * @author koichik
 */
public class EnhancedClassGenerator extends AbstractGenerator {
    // instance fields
    protected final Class targetClass;

    protected final String enhancedClassName;

    protected CtClass enhancedClass;

    public EnhancedClassGenerator(final ClassPool classPool,
            final Class targetClass, final String enhancedClassName) {
        super(classPool);
        this.targetClass = targetClass;
        this.enhancedClassName = enhancedClassName;

        setupClass();
        setupInterface();
        setupConstructor();
    }

    public void createTargetMethod(final Method method,
            final String methodInvocationClassName) {
        createMethod(enhancedClass, method, createTargetMethodSource(method,
                methodInvocationClassName));
    }

    public void createInvokeSuperMethod(final Method method,
            final String invokeSuperMethodName) {
        createMethod(enhancedClass, method.getModifiers(), method
                .getReturnType(), invokeSuperMethodName, method
                .getParameterTypes(), method.getExceptionTypes(),
                createInvokeSuperMethodSource(method));
    }

    public void applyInterType(final InterType interType) {
        interType.introduce(targetClass, enhancedClass);
    }

    public Class toClass(final ClassLoader classLoader) {
        final Class clazz = toClass(classLoader, enhancedClass);
        enhancedClass.detach();
        enhancedClass = null;
        return clazz;
    }

    public void setupClass() {
        final Class superClass = (targetClass.isInterface()) ? Object.class
                : targetClass;
        enhancedClass = createCtClass(enhancedClassName, superClass);
    }

    public void setupInterface() {
        if (targetClass.isInterface()) {
            setInterface(enhancedClass, targetClass);
        }
    }

    public void setupConstructor() {
        final Constructor[] constructors = targetClass
                .getDeclaredConstructors();
        if (constructors.length == 0) {
            createDefaultConstructor(enhancedClass);
        } else {
            for (int i = 0; i < constructors.length; ++i) {
                final int modifier = constructors[i].getModifiers();
                final Package pkg = targetClass.getPackage();
                if (Modifier.isPublic(modifier)
                        || Modifier.isProtected(modifier)
                        || (!Modifier.isPrivate(modifier)
                                && !targetClass.getName().startsWith("java.") && (pkg == null || !pkg
                                .isSealed()))) {
                    createConstructor(enhancedClass, constructors[i]);
                }
            }
        }
    }

    public static String createTargetMethodSource(final Method method,
            final String methodInvocationClassName) {
        final StringBuffer buf = new StringBuffer(200);
        buf.append("Object result = new ").append(methodInvocationClassName)
                .append("(this, $args).proceed();");
        final Class returnType = method.getReturnType();
        if (returnType.equals(void.class)) {
            buf.append("return;");
        } else if (returnType.isPrimitive()) {
            buf.append("return ($r) ((result == null) ? ");
            if (returnType.equals(boolean.class)) {
                buf.append("false : ");
            } else {
                buf.append("0 : ");
            }
            buf.append(fromObject(returnType, "result")).append(");");
        } else {
            buf.append("return ($r) result;");
        }
        String code = new String(buf);

        final Class[] exceptionTypes = normalizeExceptionTypes(method
                .getExceptionTypes());
        if (exceptionTypes.length != 1
                || !exceptionTypes[0].equals(Throwable.class)) {
            code = aroundTryCatchBlock(exceptionTypes, code);
        }

        return "{" + code + "}";
    }

    public static String createInvokeSuperMethodSource(final Method method) {
        return "{" + "return ($r) super." + method.getName() + "($$);" + "}";
    }

    public static Class[] normalizeExceptionTypes(final Class[] exceptionTypes) {
        final List list = new LinkedList();
        outer: for (int i = 0; i < exceptionTypes.length; ++i) {
            final Class currentException = exceptionTypes[i];
            final ListIterator it = list.listIterator();
            while (it.hasNext()) {
                final Class comparisonException = (Class) it.next();
                if (comparisonException.isAssignableFrom(currentException)) {
                    continue outer;
                }
                if (currentException.isAssignableFrom(comparisonException)) {
                    it.remove();
                }
            }
            list.add(currentException);
        }
        return (Class[]) list.toArray(new Class[list.size()]);
    }

    public static String aroundTryCatchBlock(final Class[] exceptionTypes,
            final String code) {
        final TryBlockSupport tryBlock = new TryBlockSupport(code);

        boolean needRuntimeException = true;
        boolean needError = true;
        for (int i = 0; i < exceptionTypes.length; ++i) {
            final Class exceptionType = exceptionTypes[i];
            tryBlock.addCatchBlock(exceptionType, "throw e;");
            if (exceptionType.equals(RuntimeException.class)) {
                needRuntimeException = false;
            }
            if (exceptionType.equals(Error.class)) {
                needError = false;
            }
        }

        if (needRuntimeException) {
            tryBlock.addCatchBlock(RuntimeException.class, "throw e;");
        }
        if (needError) {
            tryBlock.addCatchBlock(Error.class, "throw e;");
        }
        tryBlock.addCatchBlock(Throwable.class,
                "throw new java.lang.reflect.UndeclaredThrowableException(e);");

        return tryBlock.getSourceCode();
    }
}