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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;

/**
 * @author taedium
 * 
 */
public class S2TestClassMethodsRunner extends Runner implements Filterable,
        Sortable {

    protected static Provider provider = new DefaultProvider();

    protected final List<Method> testMethods;

    protected final Class<?> testClass;

    public S2TestClassMethodsRunner(Class<?> klass) {
        testClass = klass;
        testMethods = getProvider().getTestMethods(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        if (testMethods.isEmpty())
            testAborted(notifier, getDescription());
        for (Method method : testMethods) {
            invokeTestMethod(method, notifier);
        }
    }

    protected void testAborted(RunNotifier notifier, Description description) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, new Exception(
                "No runnable methods")));
        notifier.fireTestFinished(description);
    }

    @Override
    public Description getDescription() {
        Description spec = Description.createSuiteDescription(getName());
        for (Method method : testMethods)
            spec.addChild(methodDescription(method));
        return spec;
    }

    protected String getName() {
        return getTestClass().getName();
    }

    protected Object createTest() throws Exception {
        return getTestClass().getConstructor().newInstance();
    }

    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        Object test = null;
        try {
            test = createTest();
        } catch (Exception e) {
            testAborted(notifier, methodDescription(method));
            return;
        }
        createMethodRunner(test, method, notifier).run();
    }

    protected S2TestMethodRunner createMethodRunner(Object test, Method method,
            RunNotifier notifier) {
        return new S2TestMethodRunner(test, method, notifier,
                methodDescription(method));
    }

    protected String testName(Method method) {
        return method.getName();
    }

    protected Description methodDescription(Method method) {
        return Description.createTestDescription(getTestClass(),
                testName(method));
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        for (Iterator iter = testMethods.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            if (!filter.shouldRun(methodDescription(method)))
                iter.remove();
        }
        if (testMethods.isEmpty())
            throw new NoTestsRemainException();
    }

    public void sort(final Sorter sorter) {
        Collections.sort(testMethods, new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                return sorter.compare(methodDescription(o1),
                        methodDescription(o2));
            }
        });
    }

    protected Class<?> getTestClass() {
        return testClass;
    }

    protected static Provider getProvider() {
        return provider;
    }

    protected static void setProvider(final Provider p) {
        provider = p;
    }

    public interface Provider {
        List<Method> getTestMethods(Class<?> clazz);
    }

    @Component
    public static class DefaultProvider implements Provider {

        private TestIntrospector testIntrospector;

        public TestIntrospector getTestIntrospector() {
            return testIntrospector;
        }

        @Binding(bindingType = BindingType.MAY)
        public void setTestIntrospector(TestIntrospector testIntrospector) {
            this.testIntrospector = testIntrospector;
        }

        public List<Method> getTestMethods(Class<?> clazz) {
            return testIntrospector.getTestMethods(clazz);
        }

    }
}
