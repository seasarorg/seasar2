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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.Before;
import org.junit.internal.runners.BeforeAndAfterRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class S2TestMethodRunner extends BeforeAndAfterRunner implements
        TestMethod {

    protected static Provider provider = new DefaultProvider();

    protected final Object test;

    protected final Method method;

    protected final RunNotifier notifier;

    protected final Description description;

    public Description getDescription() {
        return description;
    }

    public RunNotifier getRunNotifier() {
        return notifier;
    }

    public Object getTest() {
        return test;
    }

    public Method getMethod() {
        return method;
    }

    public S2TestMethodRunner(Object test, Method method, RunNotifier notifier,
            Description description) {
        super(test.getClass(), Before.class, After.class, test);
        this.test = test;
        this.method = method;
        this.notifier = notifier;
        this.description = description;
    }

    public void run() {
        getProvider().run(this, this);
    }

    @Override
    protected void runUnprotected() {
        getProvider().runUnprotected(this);
    }

    @Override
    protected void addFailure(Throwable e) {
        getProvider().addFailure(e, this);
    }

    protected static Provider getProvider() {
        return provider;
    }

    protected static void setProvider(final Provider p) {
        provider = p;
    }

    public interface Provider {

        void run(BeforeAndAfterRunner runner, TestMethod testMethod);

        void runUnprotected(TestMethod testMethod);

        void addFailure(Throwable e, TestMethod testMethod);
    }

    @Component
    public static class DefaultProvider implements Provider {

        protected static final String JAVAEE5_PATH = "javaee5.dicon";

        protected boolean commitRequired;

        protected S2Container container;

        protected DataSource dataSource;

        protected DataAccessor dataAccessor;

        protected TestIntrospector introspector;

        public TestIntrospector getTestIntrospector() {
            return introspector;
        }

        @Binding(bindingType = BindingType.MAY)
        public void setTestIntrospector(TestIntrospector introspector) {
            this.introspector = introspector;
        }

        public void addFailure(Throwable e, TestMethod testMethod) {
            Failure failure = new Failure(testMethod.getDescription(), e);
            testMethod.getRunNotifier().fireTestFailure(failure);
        }

        public void run(BeforeAndAfterRunner runner, TestMethod testMethod) {
            RunNotifier notifier = testMethod.getRunNotifier();
            Description description = testMethod.getDescription();
            if (isIgnored(testMethod)) {
                notifier.fireTestIgnored(description);
                return;
            }
            notifier.fireTestStarted(description);
            try {
                long timeout = getTimeout(testMethod);
                if (timeout > 0)
                    runWithTimeout(timeout, runner, testMethod);
                else
                    runMethod(runner, testMethod);
            } finally {
                notifier.fireTestFinished(description);
            }
        }

        protected void runWithTimeout(long timeout,
                final BeforeAndAfterRunner runner, final TestMethod testMethod) {
            ExecutorService service = Executors.newSingleThreadExecutor();
            Callable<Object> callable = new Callable<Object>() {
                public Object call() throws Exception {
                    runMethod(runner, testMethod);
                    return null;
                }
            };
            Future<Object> result = service.submit(callable);
            service.shutdown();
            try {
                boolean terminated = service.awaitTermination(timeout,
                        TimeUnit.MILLISECONDS);
                if (!terminated)
                    service.shutdownNow();
                result.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                addFailure(new Exception(String.format(
                        "test timed out after %d milliseconds", timeout)),
                        testMethod);
            } catch (Exception e) {
                addFailure(e, testMethod);
            }
        }

        protected void runMethod(BeforeAndAfterRunner runner,
                TestMethod testMethod) {
            try {
                setUpContainer(testMethod);
                try {
                    runBeforeRunProtected(testMethod);
                    try {
                        runProtected(runner, testMethod);
                    } finally {
                        runAfterRunProtected(testMethod);
                    }
                } finally {
                    tearDownContainer(testMethod);
                }
            } catch (Throwable e) {
                addFailure(e, testMethod);
            }
        }

        protected void setUpContainer(TestMethod testMethod) {
            container = new S2ContainerImpl();
            SingletonS2ContainerFactory.setContainer(container);
        }

        protected void tearDownContainer(TestMethod testMethod) {
            SingletonS2ContainerFactory.setContainer(null);
            container = null;
        }

        protected void runBeforeRunProtected(TestMethod testMethod)
                throws Throwable {

            Class testClass = testMethod.getTest().getClass();

            if (ResourceUtil.isExist(JAVAEE5_PATH)) {
                include(JAVAEE5_PATH);
            }
            String defaultDiconName = ClassUtil.getShortClassName(testClass)
                    + ".dicon";
            String convertedPath = ResourceUtil.convertPath(defaultDiconName,
                    testClass);
            if (ResourceUtil.isExist(convertedPath)) {
                include(convertedPath);
            }

            if (getContainer().hasComponentDef(DataSource.class)) {
                dataSource = (DataSource) getContainer().getComponent(
                        DataSource.class);
                dataAccessor = new DataAccessor(testClass, dataSource);
            }
        }

        protected void runAfterRunProtected(TestMethod testMethod)
                throws Throwable {
            dataSource = null;
            dataAccessor = null;
        }

        protected void runProtected(BeforeAndAfterRunner runner,
                TestMethod testMethod) {
            runner.runProtected();
        }

        public void runUnprotected(TestMethod testMethod) {
            Object test = testMethod.getTest();
            Method method = testMethod.getMethod();
            try {
                runEachBeforeMethod(test, method);
                try {
                    getContainer().init();
                    try {
                        bindFields(test);
                        executeTestMethod(testMethod);
                    } finally {
                        getContainer().destroy();
                    }
                } finally {
                    runEachAfterMethod(test, method);
                }
            } catch (Throwable e) {
                addFailure(e, testMethod);
            }
        }

        protected void runEachBeforeMethod(Object test, Method method)
                throws Throwable {

            String methodName = getTestIntrospector().getEachBeforeMethodName(
                    test.getClass(), method);
            if (methodName != null) {
                invoke(test, methodName);
            }
        }

        protected void runEachAfterMethod(Object test, Method method)
                throws Throwable {

            String methodName = getTestIntrospector().getEachAfterMethodName(
                    test.getClass(), method);
            if (methodName != null) {
                invoke(test, methodName);
            }
        }

        protected void bindFields(Object test) throws Throwable {
            for (Class clazz = test.getClass(); clazz != Object.class; clazz = clazz
                    .getSuperclass()) {

                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; ++i) {
                    bindField(test, fields[i]);
                }
            }
        }

        protected void bindField(Object test, Field field) {
            if (isAutoBindable(field)) {
                field.setAccessible(true);
                if (FieldUtil.get(field, test) != null) {
                    return;
                }
                String name = normalizeName(field.getName());
                Object component = null;
                if (getContainer().hasComponentDef(name)) {
                    Class componentClass = getContainer().getComponentDef(name)
                            .getComponentClass();
                    if (componentClass == null) {
                        component = getContainer().getComponent(name);
                        if (component != null) {
                            componentClass = component.getClass();
                        }
                    }
                    if (componentClass != null
                            && field.getType().isAssignableFrom(componentClass)) {
                        if (component == null) {
                            component = getContainer().getComponent(name);
                        }
                    } else {
                        component = null;
                    }
                }
                if (component == null
                        && getContainer().hasComponentDef(field.getType())) {
                    component = getContainer().getComponent(field.getType());
                }
                if (component != null) {
                    FieldUtil.set(field, test, component);
                } else {
                    if (field.getType().isAssignableFrom(DataAccessor.class)) {
                        if (getDataAccessor() != null) {
                            FieldUtil.set(field, test, getDataAccessor());
                            return;
                        }
                    }
                }
            }
        }

        protected boolean isAutoBindable(Field field) {
            int modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers)
                    && !Modifier.isFinal(modifiers)
                    && !field.getType().isPrimitive();
        }

        protected String normalizeName(String name) {
            return StringUtil.replace(name, "_", "");
        }

        protected void executeTestMethod(TestMethod testMethod) {
            try {
                executeMethodBody(testMethod.getTest(), testMethod.getMethod());
                if (expectsException(testMethod))
                    addFailure(new AssertionError("Expected exception: "
                            + expectedException(testMethod).getName()),
                            testMethod);
            } catch (InvocationTargetException e) {
                Throwable actual = e.getTargetException();
                if (!expectsException(testMethod))
                    addFailure(actual, testMethod);
                else if (isUnexpected(actual, testMethod)) {
                    String message = "Unexpected exception, expected<"
                            + expectedException(testMethod).getName()
                            + "> but was<" + actual.getClass().getName() + ">";
                    addFailure(new Exception(message, actual), testMethod);
                }
            } catch (Throwable e) {
                addFailure(e, testMethod);
            }
        }

        protected void executeMethodBody(Object test, Method method)
                throws Throwable {
            TransactionManager tm = null;
            if (needsTransaction(test, method)) {
                try {
                    tm = (TransactionManager) getContainer().getComponent(
                            TransactionManager.class);
                    tm.begin();
                } catch (Throwable t) {
                    System.err.println(t);
                }
            }
            try {
                readXlsWriteDb(test, method);
                method.invoke(test);
            } finally {
                if (tm != null) {
                    if (isCommitRequired()) {
                        setCommitRequired(false);
                        tm.commit();
                    } else {
                        tm.rollback();
                    }
                }
            }
        }

        protected void readXlsWriteDb(Object test, Method method) {
            Class testClass = test.getClass();
            String methodXls = ClassUtil.getShortClassName(testClass) + "_"
                    + method.getName() + ".xls";
            String methodXlsPath = ResourceUtil.convertPath(methodXls,
                    testClass);

            if (ResourceUtil.isExist(methodXlsPath)) {
                getDataAccessor().readXlsWriteDb(methodXlsPath);
            } else {
                String classXls = ClassUtil.getShortClassName(testClass)
                        + ".xls";
                String classXlsPath = ResourceUtil.convertPath(classXls,
                        testClass);
                if (ResourceUtil.isExist(classXlsPath)) {
                    getDataAccessor().readXlsWriteDb(classXlsPath);
                }
            }
        }

        protected void invoke(Object test, String methodName) throws Throwable {
            try {
                Method method = ClassUtil.getMethod(test.getClass(),
                        methodName, null);
                MethodUtil.invoke(method, test, null);
            } catch (NoSuchMethodRuntimeException ignore) {
            }
        }

        protected boolean expectsException(TestMethod testMethod) {
            return expectedException(testMethod) != null;
        }

        protected boolean isUnexpected(Throwable exception,
                TestMethod testMethod) {
            return !expectedException(testMethod).isAssignableFrom(
                    exception.getClass());
        }

        protected boolean isIgnored(TestMethod testMethod) {
            return getTestIntrospector().isIgnored(testMethod.getMethod());
        }

        protected Class<? extends Throwable> expectedException(
                TestMethod testMethod) {
            return getTestIntrospector().expectedException(
                    testMethod.getMethod());
        }

        protected long getTimeout(TestMethod testMethod) {
            return getTestIntrospector().getTimeout(testMethod.getMethod());
        }

        protected boolean needsTransaction(Object test, Method method) {
            TxBehavior methodTxBehavior = method
                    .getAnnotation(TxBehavior.class);
            TxBehavior classTxBehavior = test.getClass().getAnnotation(
                    TxBehavior.class);
            TxBehavior txBehavior = methodTxBehavior != null ? methodTxBehavior
                    : classTxBehavior;
            if (txBehavior != null) {
                switch (txBehavior.value()) {
                case COMMIT:
                    setCommitRequired(true);
                    return true;
                case ROLLBACK:
                    return true;
                case NONE:
                    return false;
                }
            }
            return true;
        }

        protected void setCommitRequired(boolean commitRequired) {
            this.commitRequired = commitRequired;
        }

        protected boolean isCommitRequired() {
            return commitRequired;
        }

        protected S2Container getContainer() {
            return container;
        }

        protected void include(String path) {
            S2ContainerFactory.include(getContainer(), path);
        }

        protected DataSource getDataSource() {
            return dataSource;
        }

        protected DataAccessor getDataAccessor() {
            return dataAccessor;
        }
    }
}
