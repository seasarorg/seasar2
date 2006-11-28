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
package org.seasar.framework.unit.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.internal.runners.TestIntrospector;
import org.seasar.framework.unit.S2TestIntrospector;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.unit.annotation.WarmDeploy;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author taedium
 * 
 */
public class AnnotationTestIntrospector implements S2TestIntrospector {

    protected Class<? extends Annotation> beforeClassAnnotation = BeforeClass.class;

    protected Class<? extends Annotation> afterClassAnnotation = AfterClass.class;

    protected Class<? extends Annotation> beforeAnnotation = Before.class;

    protected Class<? extends Annotation> afterAnnotation = After.class;

    public void setBeforeAnnotation(
            final Class<? extends Annotation> beforeAnnotation) {

        this.beforeAnnotation = beforeAnnotation;
    }

    public void setAfterAnnotation(
            final Class<? extends Annotation> afterAnnotation) {

        this.afterAnnotation = afterAnnotation;
    }

    public List<Method> getBeforeClassMethods(final Class<?> testClass) {
        return new TestIntrospector(testClass)
                .getTestMethods(beforeClassAnnotation);
    }

    public List<Method> getAfterClassMethods(final Class<?> testClass) {
        return new TestIntrospector(testClass)
                .getTestMethods(afterClassAnnotation);
    }

    public List<Method> getBeforeMethods(final Class<?> testClass) {
        return new TestIntrospector(testClass).getTestMethods(beforeAnnotation);
    }

    public List<Method> getAfterMethods(final Class<?> testClass) {
        return new TestIntrospector(testClass).getTestMethods(afterAnnotation);
    }

    public List<Method> getTestMethods(final Class<?> testClass) {
        return new TestIntrospector(testClass).getTestMethods(Test.class);
    }

    public Method getEachBeforeMethod(final Class<?> testClass,
            final Method testMethod) {

        return null;
    }

    public Method getEachAfterMethod(final Class<?> testClass,
            final Method testMethod) {

        return null;
    }

    public Class<? extends Throwable> expectedException(final Method method) {
        final Test annotation = method.getAnnotation(Test.class);
        if (annotation == null || annotation.expected() == None.class) {
            return null;
        }
        return annotation.expected();
    }

    public long getTimeout(final Method method) {
        final Test annotation = method.getAnnotation(Test.class);
        if (annotation != null) {
            return annotation.timeout();
        }
        return 0;
    }

    public boolean isIgnored(final Method method) {
        return method.isAnnotationPresent(Ignore.class);
    }

    public List<String> getPrerequisiteExpressions(Class<?> testClass,
            Method testMethod) {

        final List<String> expressions = CollectionsUtil.newArrayList();
        final Prerequisite classPrereq = testClass
                .getAnnotation(Prerequisite.class);
        if (classPrereq != null) {
            expressions.add(classPrereq.value());
        }
        final Prerequisite methodPrereq = testMethod
                .getAnnotation(Prerequisite.class);
        if (methodPrereq != null) {
            expressions.add(methodPrereq.value());
        }
        return expressions;
    }

    public boolean needsTransaction(Class<?> testClass, Method testMethod) {
        final TxBehaviorType type = getTxBehaviorType(testClass, testMethod);
        return type == null || type != TxBehaviorType.NONE;
    }

    public boolean requiresTransactionCommitment(Class<?> testClass,
            Method testMethod) {
        final TxBehaviorType type = getTxBehaviorType(testClass, testMethod);
        return type != null && type == TxBehaviorType.COMMIT;
    }

    public TxBehaviorType getTxBehaviorType(Class<?> testClass,
            Method testMethod) {

        final TxBehavior methodTxBehavior = testMethod
                .getAnnotation(TxBehavior.class);
        final TxBehavior classTxBehavior = testClass
                .getAnnotation(TxBehavior.class);
        final TxBehavior behavior = methodTxBehavior != null ? methodTxBehavior
                : classTxBehavior;
        if (behavior == null) {
            return null;
        }
        return behavior.value();
    }

    public boolean needsWarmDeploy(final Class<?> testClass,
            final Method testMethod) {
        final WarmDeploy methodDeploy = testMethod
                .getAnnotation(WarmDeploy.class);
        final WarmDeploy classDeploy = testClass
                .getAnnotation(WarmDeploy.class);
        final WarmDeploy warmDeploy = methodDeploy != null ? methodDeploy
                : classDeploy;
        return warmDeploy == null ? true : warmDeploy.value();
    }

}
