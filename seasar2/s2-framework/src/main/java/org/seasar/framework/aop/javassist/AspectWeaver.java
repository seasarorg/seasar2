/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.InterType;
import org.seasar.framework.exception.NoSuchFieldRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassPoolUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * アスペクトを織り込むクラスです。
 * 
 * @author koichik
 */
public class AspectWeaver {
    /**
     * エンハンスされるクラスにつけるプレフィックス。
     */
    public static final String PREFIX_ENHANCED_CLASS = "$$";

    /**
     * エンハンスされるクラスにつけるサフィックス。
     */
    public static final String SUFFIX_ENHANCED_CLASS = "$$EnhancedByS2AOP$$";

    /**
     * エンハンスされる{@link MethodInvocation}につけるサフィックス。
     */
    public static final String SUFFIX_METHOD_INVOCATION_CLASS = "$$MethodInvocation$$";

    /**
     * super(親クラス)のメソッドを呼び出すときのサフィックス。
     */
    public static final String SUFFIX_INVOKE_SUPER_METHOD = "$$invokeSuperMethod$$";

    /**
     * エンハンスされるクラス名の {@link Set}
     */
    protected static final Set enhancedClassNames = Collections
            .synchronizedSet(new HashSet());

    /**
     * ターゲットクラス
     */
    protected final Class targetClass;

    /**
     * パラメータ
     */
    protected final Map parameters;

    /**
     * エンハンスされるクラス名
     */
    protected final String enhancedClassName;

    /**
     * エンハンスされるクラスジェネレータ
     */
    protected final EnhancedClassGenerator enhancedClassGenerator;

    /**
     * メソッド呼び出しクラスの {@link List}
     */
    protected final List methodInvocationClassList = new ArrayList();

    /**
     * エンハンスされるクラス
     */
    protected Class enhancedClass;

    /**
     * クラスプール
     */
    protected ClassPool classPool;

    /**
     * {@link AspectWeaver}を作成します。
     * 
     * @param targetClass
     * @param parameters
     */
    public AspectWeaver(final Class targetClass, final Map parameters) {
        this.targetClass = targetClass;
        this.parameters = parameters;

        classPool = ClassPoolUtil.getClassPool(targetClass);
        enhancedClassName = getEnhancedClassName();
        enhancedClassGenerator = new EnhancedClassGenerator(classPool,
                targetClass, enhancedClassName);
    }

    /**
     * {@link MethodInterceptor}を設定します。
     * 
     * @param method
     * @param interceptors
     */
    public void setInterceptors(final Method method,
            final MethodInterceptor[] interceptors) {
        final String methodInvocationClassName = getMethodInvocationClassName(method);
        final MethodInvocationClassGenerator methodInvocationGenerator = new MethodInvocationClassGenerator(
                classPool, methodInvocationClassName, enhancedClassName);

        final String invokeSuperMethodName = createInvokeSuperMethod(method);
        methodInvocationGenerator.createProceedMethod(method,
                invokeSuperMethodName);
        enhancedClassGenerator.createTargetMethod(method,
                methodInvocationClassName);

        final Class methodInvocationClass = methodInvocationGenerator
                .toClass(ClassLoaderUtil.getClassLoader(targetClass));
        setStaticField(methodInvocationClass, "method", method);
        setStaticField(methodInvocationClass, "interceptors", interceptors);
        setStaticField(methodInvocationClass, "parameters", parameters);
        methodInvocationClassList.add(methodInvocationClass);
    }

    /**
     * {@link InterType}を追加します。
     * 
     * @param interTypes
     */
    public void setInterTypes(final InterType[] interTypes) {
        if (interTypes == null) {
            return;
        }

        for (int i = 0; i < interTypes.length; ++i) {
            enhancedClassGenerator.applyInterType(interTypes[i]);
        }
    }

    /**
     * クラスを生成します。
     * 
     * @return 生成されたクラス
     */
    public Class generateClass() {
        if (enhancedClass == null) {
            enhancedClass = enhancedClassGenerator.toClass(ClassLoaderUtil
                    .getClassLoader(targetClass));

            for (int i = 0; i < methodInvocationClassList.size(); ++i) {
                final Class methodInvocationClass = (Class) methodInvocationClassList
                        .get(i);
                setStaticField(methodInvocationClass, "targetClass",
                        targetClass);
            }
        }

        return enhancedClass;
    }

    /**
     * エンハンスされたクラス名を返します。
     * 
     * @return エンハンスされたクラス名
     */
    public String getEnhancedClassName() {
        final StringBuffer buf = new StringBuffer(200);
        final String targetClassName = targetClass.getName();
        final Package pkg = targetClass.getPackage();
        if (targetClassName.startsWith("java.")
                || (pkg != null && pkg.isSealed())) {
            buf.append(PREFIX_ENHANCED_CLASS);
        }
        buf.append(targetClassName).append(SUFFIX_ENHANCED_CLASS).append(
                Integer.toHexString(hashCode()));

        final int length = buf.length();
        for (int i = 0; enhancedClassNames.contains(new String(buf)); ++i) {
            buf.setLength(length);
            buf.append("_").append(i);
        }

        String name = new String(buf);
        enhancedClassNames.add(name);
        return name;
    }

    /**
     * エンハンスされた{@link MethodInvocation}のクラス名を返します。
     * 
     * @param method
     * @return
     */
    public String getMethodInvocationClassName(final Method method) {
        return enhancedClassName + SUFFIX_METHOD_INVOCATION_CLASS
                + method.getName() + methodInvocationClassList.size();
    }

    /**
     * superクラスのメソッドを呼び出すためのメソッド名を作成します。
     * 
     * @param method
     * @return
     */
    public String createInvokeSuperMethod(final Method method) {
        final String invokeSuperMethodName = PREFIX_ENHANCED_CLASS
                + method.getName() + SUFFIX_INVOKE_SUPER_METHOD;
        if (!MethodUtil.isAbstract(method)) {
            enhancedClassGenerator.createInvokeSuperMethod(method,
                    invokeSuperMethodName);
        }
        return invokeSuperMethodName;
    }

    /**
     * static filedに値を設定します。
     * 
     * @param clazz
     * @param name
     * @param value
     */
    public void setStaticField(final Class clazz, final String name,
            final Object value) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            FieldUtil.set(field, name, value);
            field.setAccessible(false);
        } catch (final NoSuchFieldException e) {
            throw new NoSuchFieldRuntimeException(enhancedClass, name, e);
        }
    }
}