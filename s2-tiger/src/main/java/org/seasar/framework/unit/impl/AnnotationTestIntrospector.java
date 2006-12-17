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
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.unit.Expression;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.S2TestIntrospector;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.unit.annotation.WarmDeploy;
import org.seasar.framework.util.StringUtil;
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

    protected boolean enableIgnore = true;

    protected boolean enablePrerequisite = true;

    public void setBeforeClassAnnotation(
            final Class<? extends Annotation> beforeClassAnnotation) {
        this.beforeClassAnnotation = beforeClassAnnotation;
    }

    public void setAfterClassAnnotation(
            final Class<? extends Annotation> afterClassAnnotation) {
        this.afterClassAnnotation = afterClassAnnotation;
    }

    public void setBeforeAnnotation(
            final Class<? extends Annotation> beforeAnnotation) {
        this.beforeAnnotation = beforeAnnotation;
    }

    public void setAfterAnnotation(
            final Class<? extends Annotation> afterAnnotation) {
        this.afterAnnotation = afterAnnotation;
    }

    public void setEnableIgnore(boolean enableIgnore) {
        this.enableIgnore = enableIgnore;
    }

    public void setEnablePrerequisite(boolean enablePrerequisite) {
        this.enablePrerequisite = enablePrerequisite;
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
        if (enableIgnore) {
            return method.isAnnotationPresent(Ignore.class);
        }
        return false;
    }

    public List<String> getPrerequisiteExpressions(final Class<?> testClass,
            final Method testMethod) {
        final List<String> expressions = CollectionsUtil.newArrayList();
        if (enablePrerequisite) {
            if (testClass.isAnnotationPresent(Prerequisite.class)) {
                expressions.add(testClass.getAnnotation(Prerequisite.class)
                        .value());
            }
            if (testMethod.isAnnotationPresent(Prerequisite.class)) {
                expressions.add(testMethod.getAnnotation(Prerequisite.class)
                        .value());
            }
        } else {
            expressions.add(Boolean.toString(true));
        }
        return expressions;
    }

    public boolean needsTransaction(final Class<?> testClass,
            final Method testMethod) {
        final TxBehaviorType type = getTxBehaviorType(testClass, testMethod);
        return type == null || type != TxBehaviorType.NONE;
    }

    public boolean requiresTransactionCommitment(final Class<?> testClass,
            final Method testMethod) {
        final TxBehaviorType type = getTxBehaviorType(testClass, testMethod);
        return type != null && type == TxBehaviorType.COMMIT;
    }

    protected TxBehaviorType getTxBehaviorType(final Class<?> testClass,
            final Method testMethod) {
        if (testMethod.isAnnotationPresent(TxBehavior.class)) {
            return testMethod.getAnnotation(TxBehavior.class).value();
        }
        if (testClass.isAnnotationPresent(TxBehavior.class)) {
            return testClass.getAnnotation(TxBehavior.class).value();
        }
        return null;
    }

    public boolean needsWarmDeploy(final Class<?> testClass,
            final Method testMethod) {
        if (testMethod.isAnnotationPresent(WarmDeploy.class)) {
            return testMethod.getAnnotation(WarmDeploy.class).value();
        }
        if (testClass.isAnnotationPresent(WarmDeploy.class)) {
            return testClass.getAnnotation(WarmDeploy.class).value();
        }
        return true;
    }

    public void createMockInterceptor(final Method testMethod,
            final Expression expression, final InternalTestContext context) {
        final Mock mock = testMethod.getAnnotation(Mock.class);
        if (mock != null) {
            createMockInterceptor(mock, expression, context);
        } else {
            final Mocks mocks = testMethod.getAnnotation(Mocks.class);
            if (mocks != null) {
                for (final Mock each : mocks.value()) {
                    createMockInterceptor(each, expression, context);
                }
            }
        }
    }

    protected void createMockInterceptor(final Mock mock,
            final Expression expression, final InternalTestContext context) {
        final MockInterceptor mi = new MockInterceptor();
        if (!StringUtil.isEmpty(mock.returnValue())) {
            mi.setReturnValue(expression.evaluate(mock.returnValue()));
        }
        if (!StringUtil.isEmpty(mock.throwable())) {
            final Object result = expression.evaluate(mock.throwable());
            mi.setThrowable(Throwable.class.cast(result));
        }
        Pointcut pc = null;
        if (StringUtil.isEmpty(mock.pointcut())) {
            pc = AspectDefFactory.createPointcut(mock.target());
        } else {
            pc = AspectDefFactory.createPointcut(mock.pointcut());
        }
        final Object componentKey = StringUtil.isEmpty(mock.targetName()) ? mock
                .target()
                : mock.targetName();
        final AspectDef aspectDef = AspectDefFactory.createAspectDef(mi, pc);
        context.addAspecDef(componentKey, aspectDef);
        context.addMockInterceptor(mi);
    }
}
