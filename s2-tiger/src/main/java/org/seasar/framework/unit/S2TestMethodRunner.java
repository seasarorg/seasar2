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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.transaction.TransactionManager;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.env.Env;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class S2TestMethodRunner {

    private static class FailedBefore extends Exception {
        private static final long serialVersionUID = 1L;
    }

    protected static final String S2JUNIT4_PATH = "s2junit4.dicon";

    protected final Object test;

    protected final Class<?> testClass;

    protected final Method method;

    protected final RunNotifier notifier;

    protected final Description description;

    protected final S2TestIntrospector introspector;

    protected ClassLoader originalClassLoader;

    protected UnitClassLoader unitClassLoader;

    protected InternalTestContext testContext;

    protected boolean commitRequired;

    public S2TestMethodRunner(final Object test, final Method method,
            final RunNotifier notifier, final Description description,
            final S2TestIntrospector introspector) {
        this.test = test;
        this.testClass = test.getClass();
        this.method = method;
        this.notifier = notifier;
        this.description = description;
        this.introspector = introspector;
    }

    protected void addFailure(final Throwable e) {
        final Failure failure = new Failure(description, e);
        notifier.fireTestFailure(failure);
    }

    public void run() {
        if (introspector.isIgnored(method) || !isFulfilled()) {
            notifier.fireTestIgnored(description);
            return;
        }
        notifier.fireTestStarted(description);
        try {
            final long timeout = introspector.getTimeout(method);
            if (timeout > 0) {
                runWithTimeout(timeout);
            } else {
                runMethod();
            }
        } finally {
            notifier.fireTestFinished(description);
        }
    }

    protected boolean isFulfilled() {
        final Prerequisite classReq = testClass
                .getAnnotation(Prerequisite.class);
        final boolean classFulfilled = classReq == null ? true
                : isFulfilled(classReq.value());
        if (!classFulfilled) {
            return false;
        }
        final Prerequisite methodReq = method.getAnnotation(Prerequisite.class);
        return methodReq == null ? true : isFulfilled(methodReq.value());
    }

    protected boolean isFulfilled(final String expression) {
        final Object exp = OgnlUtil.parseExpression(expression);
        final Map<String, Object> ctx = CollectionsUtil.newHashMap();
        ctx.put("ENV", Env.getValue());
        Object result = OgnlUtil.getValue(exp, ctx, test);
        if (result instanceof Boolean) {
            return Boolean.class.cast(result);
        }
        return false;
    }

    protected void runWithTimeout(final long timeout) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
                runMethod();
                return null;
            }
        };
        final Future<Object> result = service.submit(callable);
        service.shutdown();
        try {
            final boolean terminated = service.awaitTermination(timeout,
                    TimeUnit.MILLISECONDS);
            if (!terminated) {
                service.shutdownNow();
            }
            result.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            addFailure(new Exception(String.format(
                    "test timed out after %d milliseconds", timeout)));
        } catch (Exception e) {
            addFailure(e);
        }
    }

    protected void runMethod() {
        try {
            setUpTestContext();
            try {
                runBefores();
                try {
                    runEachBefore();
                    testContext.initContainer();
                    try {
                        bindFields();
                        executeTestMethod();
                    } finally {
                        testContext.destroyContainer();
                    }
                } catch (final FailedBefore e) {
                } catch (final Throwable e) {
                    addFailure(e);
                } finally {
                    runEachAfter();
                }
            } catch (final FailedBefore e) {
            } finally {
                runAfters();
                tearDownTestContext();
            }
        } catch (final Throwable e) {
            addFailure(e);
        }
    }

    protected void setUpTestContext() throws Throwable {
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        unitClassLoader = new UnitClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(unitClassLoader);
        S2Container container;
        if (ResourceUtil.isExist(S2JUNIT4_PATH)) {
            container = S2ContainerFactory.create(S2JUNIT4_PATH);
        } else {
            container = new S2ContainerImpl();
        }
        SingletonS2ContainerFactory.setContainer(container);

        if (container.hasComponentDef(InternalTestContext.class)) {
            testContext = InternalTestContext.class.cast(container
                    .getComponent(InternalTestContext.class));
            testContext.setTestClass(testClass);
            testContext.setTestMethod(method);
        } else {
            return;
        }

        for (Class clazz = testClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {

            final Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (isAutoBindable(field)
                        && field.getType() == TestContext.class) {
                    field.setAccessible(true);
                    if (ReflectionUtil.getValue(field, test) != null) {
                        continue;
                    }
                    ReflectionUtil.setValue(field, test, testContext);
                }
            }
        }
    }

    protected void tearDownTestContext() throws Throwable {
        testContext = null;
        DisposableUtil.dispose();
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        unitClassLoader = null;
        originalClassLoader = null;
        Env.initialize();
    }

    protected void runBefores() throws FailedBefore {
        try {
            final List<Method> befores = introspector.getBeforeMethods(test
                    .getClass());
            for (final Method before : befores) {
                before.invoke(test);
            }
        } catch (final InvocationTargetException e) {
            addFailure(e.getTargetException());
            throw new FailedBefore();
        } catch (final Throwable e) {
            addFailure(e);
            throw new FailedBefore();
        }
    }

    protected void runAfters() {
        final List<Method> afters = introspector.getAfterMethods(test
                .getClass());
        for (Method after : afters) {
            try {
                after.invoke(test);
            } catch (final InvocationTargetException e) {
                addFailure(e.getTargetException());
            } catch (final Throwable e) {
                addFailure(e);
            }
        }
    }

    protected void runEachBefore() throws Throwable {
        try {
            final Method eachBefore = introspector.getEachBeforeMethod(
                    testClass, method);
            if (eachBefore == null) {
                return;
            }
            invokeNoException(eachBefore);
        } catch (final Throwable e) {
            addFailure(e);
            throw new FailedBefore();
        }
    }

    protected void runEachAfter() throws Throwable {
        try {
            final Method eachAfter = introspector.getEachAfterMethod(testClass,
                    method);
            if (eachAfter == null) {
                return;
            }
            invokeNoException(eachAfter);
        } catch (final Throwable e) {
            addFailure(e);
        }
    }

    protected void bindFields() throws Throwable {
        for (Class clazz = testClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {

            final Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                bindField(fields[i]);
            }
        }
    }

    protected void bindField(final Field field) {
        if (isAutoBindable(field)) {
            field.setAccessible(true);
            if (ReflectionUtil.getValue(field, test) != null) {
                return;
            }
            final String name = resolveComponentName(field);
            Object component = null;
            if (testContext.hasComponentDef(name)) {
                Class componentClass = testContext.getComponentDef(name)
                        .getComponentClass();
                if (componentClass == null) {
                    component = testContext.getComponent(name);
                    if (component != null) {
                        componentClass = component.getClass();
                    }
                }
                if (componentClass != null
                        && field.getType().isAssignableFrom(componentClass)) {
                    if (component == null) {
                        component = testContext.getComponent(name);
                    }
                } else {
                    component = null;
                }
            }
            if (component == null
                    && testContext.hasComponentDef(field.getType())) {
                component = testContext.getComponent(field.getType());
            }
            if (component != null) {
                ReflectionUtil.setValue(field, test, component);
            }
        }
    }

    protected boolean isAutoBindable(final Field field) {
        final int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                && !field.getType().isPrimitive();
    }

    protected String resolveComponentName(final Field filed) {
        final EJB ejb = filed.getAnnotation(EJB.class);
        if (ejb != null) {
            if (!StringUtil.isEmpty(ejb.beanName())) {
                return ejb.beanName();
            } else if (!StringUtil.isEmpty(ejb.name())) {
                return ejb.name();
            }
        }
        return normalizeName(filed.getName());
    }

    protected String normalizeName(final String name) {
        return StringUtil.replace(name, "_", "");
    }

    protected void executeTestMethod() {
        try {
            executeMethodBody();
            if (expectsException())
                addFailure(new AssertionError("Expected exception: "
                        + expectedException().getName()));
        } catch (final InvocationTargetException e) {
            final Throwable actual = e.getTargetException();
            if (!expectsException()) {
                addFailure(actual);
            } else if (isUnexpected(actual)) {
                String message = "Unexpected exception, expected<"
                        + expectedException().getName() + "> but was<"
                        + actual.getClass().getName() + ">";
                addFailure(new Exception(message, actual));
            }
        } catch (final Throwable e) {
            addFailure(e);
        }
    }

    protected void executeMethodBody() throws Throwable {
        TransactionManager tm = null;
        if (needsTransaction()) {
            try {
                tm = testContext.getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            testContext.prepareTestData();
            method.invoke(test);
        } finally {
            if (tm != null) {
                if (commitRequired) {
                    commitRequired = false;
                    tm.commit();
                } else {
                    tm.rollback();
                }
            }
        }
    }

    protected boolean needsTransaction() {
        final TxBehavior methodTxBehavior = method
                .getAnnotation(TxBehavior.class);
        final TxBehavior classTxBehavior = test.getClass().getAnnotation(
                TxBehavior.class);
        final TxBehavior txBehavior = methodTxBehavior != null ? methodTxBehavior
                : classTxBehavior;
        if (txBehavior != null) {
            switch (txBehavior.value()) {
            case COMMIT:
                commitRequired = true;
                return true;
            case ROLLBACK:
                return true;
            case NONE:
                return false;
            }
        }
        return true;
    }

    protected boolean expectsException() {
        return expectedException() != null;
    }

    protected boolean isUnexpected(final Throwable exception) {
        return !expectedException().isAssignableFrom(exception.getClass());
    }

    protected Class<? extends Throwable> expectedException() {
        return introspector.expectedException(method);
    }

    protected void invokeNoException(final Method method) throws Throwable {
        try {
            ReflectionUtil.invoke(method, test);
        } catch (NoSuchMethodRuntimeException ignore) {
        }
    }

}
