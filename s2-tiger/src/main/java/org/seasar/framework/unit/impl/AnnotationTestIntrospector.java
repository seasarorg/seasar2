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
import java.util.List;
import java.util.Map;

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
import org.seasar.framework.env.Env;
import org.seasar.framework.unit.Expression;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.S2TestIntrospector;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.RootDicon;
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

    public List<Method> getBeforeClassMethods(final Class<?> clazz) {
        return new TestIntrospector(clazz)
                .getTestMethods(beforeClassAnnotation);
    }

    public List<Method> getAfterClassMethods(final Class<?> clazz) {
        return new TestIntrospector(clazz).getTestMethods(afterClassAnnotation);
    }

    public List<Method> getBeforeMethods(final Class<?> clazz) {
        return new TestIntrospector(clazz).getTestMethods(beforeAnnotation);
    }

    public List<Method> getAfterMethods(final Class<?> clazz) {
        return new TestIntrospector(clazz).getTestMethods(afterAnnotation);
    }

    public List<Method> getTestMethods(final Class<?> clazz) {
        return new TestIntrospector(clazz).getTestMethods(Test.class);
    }

    public Method getEachBeforeMethod(final Class<?> clazz, final Method method) {
        return null;
    }

    public Method getEachAfterMethod(final Class<?> clazz, final Method method) {
        return null;
    }

    public Method getEachRecordMethod(final Class<?> clazz, final Method method) {
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

    public boolean isFulfilled(final Class<?> clazz, final Method method,
            final Object test) {
        if (!enablePrerequisite) {
            return true;
        }
        if (clazz.isAnnotationPresent(Prerequisite.class)) {
            final String source = clazz.getAnnotation(Prerequisite.class)
                    .value();
            final Expression exp = createExpression(source, method, test);
            if (!isFulfilled(exp)) {
                return false;
            }
        }
        if (method.isAnnotationPresent(Prerequisite.class)) {
            final String source = method.getAnnotation(Prerequisite.class)
                    .value();
            final Expression exp = createExpression(source, method, test);
            if (!isFulfilled(exp)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isFulfilled(final Expression expression) {
        final Object result = expression.evaluateNoException();
        if (expression.isMethodFailed()) {
            System.err.println(expression.getException());
            return false;
        }
        expression.throwExceptionIfNecessary();
        if (result instanceof Boolean && Boolean.class.cast(result)) {
            return true;
        }
        return false;
    }

    public boolean needsTransaction(final Class<?> clazz, final Method method) {
        final TxBehaviorType type = getTxBehaviorType(clazz, method);
        return type == null || type != TxBehaviorType.NONE;
    }

    public boolean requiresTransactionCommitment(final Class<?> clazz,
            final Method method) {
        final TxBehaviorType type = getTxBehaviorType(clazz, method);
        return type != null && type == TxBehaviorType.COMMIT;
    }

    protected TxBehaviorType getTxBehaviorType(final Class<?> clazz,
            final Method method) {
        if (method.isAnnotationPresent(TxBehavior.class)) {
            return method.getAnnotation(TxBehavior.class).value();
        }
        if (clazz.isAnnotationPresent(TxBehavior.class)) {
            return clazz.getAnnotation(TxBehavior.class).value();
        }
        return null;
    }

    public boolean needsWarmDeploy(final Class<?> clazz, final Method method) {
        if (method.isAnnotationPresent(WarmDeploy.class)) {
            return method.getAnnotation(WarmDeploy.class).value();
        }
        if (clazz.isAnnotationPresent(WarmDeploy.class)) {
            return clazz.getAnnotation(WarmDeploy.class).value();
        }
        return true;
    }

    public void createMock(final Method method, final Object test,
            final InternalTestContext context) {
        final Mock mock = method.getAnnotation(Mock.class);
        if (mock != null) {
            createMock(mock, method, test, context);
        } else {
            final Mocks mocks = method.getAnnotation(Mocks.class);
            if (mocks != null) {
                for (final Mock each : mocks.value()) {
                    createMock(each, method, test, context);
                }
            }
        }
    }

    protected void createMock(final Mock mock, final Method method,
            final Object test, final InternalTestContext context) {
        final MockInterceptor mi = new MockInterceptor();
        if (!StringUtil.isEmpty(mock.returnValue())) {
            final Expression exp = createExpression(mock.returnValue(), method,
                    test);
            mi.setReturnValue(exp.evaluate());
        }
        if (!StringUtil.isEmpty(mock.throwable())) {
            final Expression exp = createExpression(mock.throwable(), method,
                    test);
            final Object result = exp.evaluate();
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

    protected Expression createExpression(final String source,
            final Method method, final Object test) {
        final Map<String, Object> ctx = CollectionsUtil.newHashMap();
        ctx.put("ENV", Env.getValue());
        ctx.put("method", method);
        return new OgnlExpression(source, test, ctx);
    }

    public String getRootDicon(final Class<?> clazz, final Method method) {
        if (method.isAnnotationPresent(RootDicon.class)) {
            return method.getAnnotation(RootDicon.class).value();
        }
        if (clazz.isAnnotationPresent(RootDicon.class)) {
            return clazz.getAnnotation(RootDicon.class).value();
        }
        return null;
    }

}
