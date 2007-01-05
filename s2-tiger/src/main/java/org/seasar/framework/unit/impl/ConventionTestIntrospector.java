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
package org.seasar.framework.unit.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author taedium
 * 
 */
public class ConventionTestIntrospector extends AnnotationTestIntrospector {

    protected final Set<Class<? extends Annotation>> nonTestAnnotations = CollectionsUtil
            .newHashSet();

    protected final Set<Pattern> nonTestMethodNamePatterns = new HashSet<Pattern>();

    protected String beforeClassMethodName = "beforeClass";

    protected String afterClassMethodName = "afterClass";

    protected String beforeMethodName = "before";

    protected String afterMethodName = "after";

    protected String recordMethodName = "record";

    @InitMethod
    public void init() {
        addNonTestAnnotation(beforeAnnotation);
        addNonTestAnnotation(afterAnnotation);
        if (beforeMethodName != null) {
            addNonTestMethodNamePattern(beforeMethodName + ".*");
        }
        if (afterMethodName != null) {
            addNonTestMethodNamePattern(afterMethodName + ".*");
        }
        if (recordMethodName != null) {
            addNonTestMethodNamePattern(recordMethodName + ".+");
        }
    }

    public void setBeforeClassMethodName(final String beforeClassMethodName) {
        this.beforeClassMethodName = beforeClassMethodName;
    }

    public void setAfterClassMethodName(final String afterClassMethodName) {
        this.afterClassMethodName = afterClassMethodName;
    }

    public void setBeforeMethodName(final String beforeMethodName) {
        this.beforeMethodName = beforeMethodName;
    }

    public void setAfterMethodName(final String afterMethodName) {
        this.afterMethodName = afterMethodName;
    }

    public void setRecordMethodName(final String recordMethodName) {
        this.recordMethodName = recordMethodName;
    }

    public void addNonTestAnnotation(
            final Class<? extends Annotation> annotation) {

        nonTestAnnotations.add(annotation);
    }

    public void addNonTestMethodNamePattern(final String pattern) {
        nonTestMethodNamePatterns.add(Pattern.compile(pattern));
    }

    @Override
    public List<Method> getBeforeClassMethods(final Class<?> clazz) {
        final List<Method> methods = super.getBeforeClassMethods(clazz);
        final Method method = getMethod(clazz, beforeClassMethodName);
        if (method != null) {
            if (hasValidStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getAfterClassMethods(final Class<?> clazz) {
        final List<Method> methods = super.getAfterClassMethods(clazz);
        final Method method = getMethod(clazz, afterClassMethodName);
        if (method != null) {
            if (hasValidStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getBeforeMethods(final Class<?> clazz) {
        final List<Method> methods = super.getBeforeMethods(clazz);
        final Method method = getMethod(clazz, beforeMethodName);
        if (method != null) {
            if (hasValidNonStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getAfterMethods(final Class<?> clazz) {
        final List<Method> methods = super.getAfterMethods(clazz);
        final Method method = getMethod(clazz, afterMethodName);
        if (method != null) {
            if (hasValidNonStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public Method getEachBeforeMethod(final Class<?> clazz, final Method method) {
        if (beforeMethodName == null) {
            return null;
        }
        final String methodName = beforeMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public Method getEachAfterMethod(final Class<?> clazz, final Method method) {
        if (afterMethodName == null) {
            return null;
        }
        final String methodName = afterMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public Method getEachRecordMethod(final Class<?> clazz, final Method method) {
        if (recordMethodName == null) {
            return null;
        }
        final String methodName = recordMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public List<Method> getTestMethods(final Class<?> clazz) {
        final List<Method> results = new ArrayList<Method>();
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            final Method[] methods = eachClass.getDeclaredMethods();
            for (final Method eachMethod : methods) {
                if (isTestMethod(eachMethod)
                        && !isShadowed(eachMethod, results)) {
                    results.add(eachMethod);
                }
            }
        }
        return results;
    }

    protected List<Class<?>> getSuperClasses(final Class<?> clazz) {
        final ArrayList<Class<?>> results = new ArrayList<Class<?>>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results;
    }

    protected boolean isShadowed(final Method method, final List<Method> results) {
        for (final Method each : results) {
            if (isShadowed(method, each))
                return true;
        }
        return false;
    }

    protected boolean isShadowed(final Method current, final Method previous) {
        if (!previous.getName().equals(current.getName())) {
            return false;
        }
        if (previous.getParameterTypes().length != current.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < previous.getParameterTypes().length; i++) {
            if (!previous.getParameterTypes()[i].equals(current
                    .getParameterTypes()[i]))
                return false;
        }
        return true;
    }

    protected boolean isTestMethod(final Method method) {
        if (!hasNonTestAnnotation(method)) {
            if (!hasNonTestMethodName(method)) {
                if (hasValidNonStaticSignature(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean hasNonTestAnnotation(final Method method) {
        for (final Annotation each : method.getAnnotations()) {
            if (nonTestAnnotations.contains(each.annotationType())) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasNonTestMethodName(final Method method) {
        for (final Pattern each : nonTestMethodNamePatterns) {
            final Matcher matcher = each.matcher(method.getName());
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasValidStaticSignature(final Method method) {
        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return hasValidSignature(method);
    }

    protected boolean hasValidNonStaticSignature(final Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return hasValidSignature(method);
    }

    protected boolean hasValidSignature(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        return true;
    }

    protected Method getMethod(final Class<?> clazz, final String methodName) {
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            final Method[] methods = eachClass.getDeclaredMethods();
            for (final Method eachMethod : methods) {
                if (eachMethod.getName().equals(methodName)) {
                    return eachMethod;
                }
            }
        }
        return null;
    }

}
