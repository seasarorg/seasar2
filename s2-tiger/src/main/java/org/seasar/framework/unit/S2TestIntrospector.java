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
package org.seasar.framework.unit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Test.None;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
@Component
public class S2TestIntrospector implements TestIntrospector {

    protected final Set<Class<? extends Annotation>> nonTestAnnotations = new HashSet<Class<? extends Annotation>>();

    protected final Set<Pattern> nonTestMethodNamePatterns = new HashSet<Pattern>();

    private String eachBeforeMethodPrefix = "before";

    private String eachAfterMethodPrefix = "after";

    @InitMethod
    public void init() {
        addNonTestAnnotation(Before.class);
        addNonTestAnnotation(After.class);
        if (eachBeforeMethodPrefix != null) {
            addNonTestMethodNamePattern(eachBeforeMethodPrefix + ".+");
        }
        if (eachAfterMethodPrefix != null) {
            addNonTestMethodNamePattern(eachAfterMethodPrefix + ".+");
        }
    }

    public String getEachAfterMethodPrefix() {
        return eachAfterMethodPrefix;
    }

    public void setEachAfterMethodPrefix(String eachAfterMethodPrefix) {
        this.eachAfterMethodPrefix = eachAfterMethodPrefix;
    }

    public String getEachBeforeMethodPrefix() {
        return eachBeforeMethodPrefix;
    }

    public void setEachBeforeMethodPrefix(String eachBeforeMethodPrefix) {
        this.eachBeforeMethodPrefix = eachBeforeMethodPrefix;
    }

    public void addNonTestAnnotation(Class<? extends Annotation> annotation) {
        nonTestAnnotations.add(annotation);
    }

    public void addNonTestMethodNamePattern(String pattern) {
        nonTestMethodNamePatterns.add(Pattern.compile(pattern));
    }

    public List<Method> getTestMethods(Class<?> testClass) {
        List<Method> results = new ArrayList<Method>();
        for (Class eachClass : getSuperClasses(testClass)) {
            Method[] methods = eachClass.getDeclaredMethods();
            for (Method eachMethod : methods) {
                if (isTestMethod(eachMethod)
                        && !isShadowed(eachMethod, results)) {
                    results.add(eachMethod);
                }
            }
        }
        return results;
    }

    protected boolean isTestMethod(Method method) {
        if (!hasNonTestAnnotation(method)) {
            if (!hasNonTestMethodName(method)) {
                if (hasValidSignature(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isShadowed(Method method, List<Method> results) {
        for (Method each : results) {
            if (isShadowed(method, each))
                return true;
        }
        return false;
    }

    protected boolean isShadowed(Method current, Method previous) {
        if (!previous.getName().equals(current.getName()))
            return false;
        if (previous.getParameterTypes().length != current.getParameterTypes().length)
            return false;
        for (int i = 0; i < previous.getParameterTypes().length; i++) {
            if (!previous.getParameterTypes()[i].equals(current
                    .getParameterTypes()[i]))
                return false;
        }
        return true;
    }

    protected List<Class> getSuperClasses(Class<?> testClass) {
        ArrayList<Class> results = new ArrayList<Class>();
        Class<?> current = testClass;
        while (current != null && current != Object.class) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results;
    }

    protected boolean hasValidSignature(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
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

    protected boolean hasNonTestAnnotation(Method method) {
        for (final Annotation each : method.getAnnotations()) {
            if (nonTestAnnotations.contains(each.annotationType())) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasNonTestMethodName(Method method) {
        for (final Pattern each : nonTestMethodNamePatterns) {
            Matcher matcher = each.matcher(method.getName());
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    public boolean isIgnored(Method method) {
        return method.isAnnotationPresent(Ignore.class);
    }

    public Class<? extends Throwable> expectedException(Method method) {
        Test annotation = method.getAnnotation(Test.class);
        if (annotation == null || annotation.expected() == None.class)
            return null;
        else
            return annotation.expected();
    }

    public long getTimeout(Method method) {
        Test annotation = method.getAnnotation(Test.class);
        if (annotation != null) {
            return annotation.timeout();
        }
        return 0;
    }

    public String getEachBeforeMethodName(Class<?> testClass, Method method) {
        if (eachBeforeMethodPrefix == null) {
            return null;
        }
        return eachBeforeMethodPrefix + StringUtil.capitalize(method.getName());
    }

    public String getEachAfterMethodName(Class<?> testClass, Method method) {
        if (eachAfterMethodPrefix == null) {
            return null;
        }
        return eachAfterMethodPrefix + StringUtil.capitalize(method.getName());
    }

}
