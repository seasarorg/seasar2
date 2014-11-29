/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
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
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

/**
 * {@link Parameters}が注釈されたメソッドを持つテストクラスを扱うランナーです。
 * 
 * @author taedium
 */
public class S2Parameterized extends S2TestClassRunner {

    private static class TestClassRunnerForParameters extends
            S2TestClassMethodsRunner {

        private final Object[] parameters;

        private final int parameterSetNumber;

        private final Constructor<?> constructor;

        private TestClassRunnerForParameters(final Class<?> klass,
                final Object[] parameters, int i) {
            super(klass);
            this.parameters = parameters;
            this.parameterSetNumber = i;
            this.constructor = getOnlyConstructor();
        }

        @Override
        protected Object createTest() throws Exception {
            return constructor.newInstance(parameters);
        }

        @Override
        protected String getName() {
            return String.format("[%s]", parameterSetNumber);
        }

        @Override
        protected String testName(final Method method) {
            return String
                    .format("%s[%s]", method.getName(), parameterSetNumber);
        }

        private Constructor<?> getOnlyConstructor() {
            final Constructor<?>[] constructors = getTestClass()
                    .getConstructors();
            assertEquals(1, constructors.length);
            return constructors[0];
        }

        @Override
        public void run(final RunNotifier notifier) {
            runMethods(notifier);
        }

    }

    /*
     * copy from: org.junit.internal.runners.CompositeRunner @JUnit4.4
     *
     * JUnit license: Eclipse Public License - v 1.0
     * https://github.com/junit-team/junit/blob/master/LICENSE-junit.txt
     */
    static class MyCompositeRunner extends Runner implements Filterable,
            Sortable {

        private final List<Runner> fRunners = new ArrayList<Runner>();

        private final String fName;

        public MyCompositeRunner(final String name) {
            fName = name;
        }

        @Override
        public void run(final RunNotifier notifier) {
            runChildren(notifier);
        }

        protected void runChildren(final RunNotifier notifier) {
            for (final Runner each : fRunners)
                each.run(notifier);
        }

        @Override
        public Description getDescription() {
            final Description spec = Description.createSuiteDescription(fName);
            for (final Runner runner : fRunners)
                spec.addChild(runner.getDescription());
            return spec;
        }

        public List<Runner> getRunners() {
            return fRunners;
        }

        public void addAll(final List<? extends Runner> runners) {
            fRunners.addAll(runners);
        }

        public void add(final Runner runner) {
            fRunners.add(runner);
        }

        public void filter(final Filter filter) throws NoTestsRemainException {
            for (final Iterator<Runner> iter = fRunners.iterator(); iter
                    .hasNext();) {
                final Runner runner = iter.next();
                if (filter.shouldRun(runner.getDescription()))
                    filter.apply(runner);
                else
                    iter.remove();
            }
        }

        protected String getName() {
            return fName;
        }

        public void sort(final Sorter sorter) {
            Collections.sort(fRunners, new Comparator<Runner>() {

                public int compare(final Runner o1, final Runner o2) {
                    return sorter.compare(o1.getDescription(),
                            o2.getDescription());
                }
            });
            for (final Runner each : fRunners) {
                sorter.apply(each);
            }
        }
    }

    /**
     * {@link Parameters}が注釈されたすべてのメソッドを実行するランナーです。
     * 
     * @author taedium
     */
    public static class RunAllParameterMethods extends MyCompositeRunner {

        private final Class<?> klass;

        /**
         * インスタンスを構築します。
         * 
         * @param klass
         *            テストクラス
         * @throws Exception
         *             何らかの例外が発生した場合
         */
        public RunAllParameterMethods(final Class<?> klass) throws Exception {
            super(klass.getName());
            this.klass = klass;
            int i = 0;
            for (final Object each : getParametersList()) {
                if (each instanceof Object[]) {
                    super.add(new TestClassRunnerForParameters(klass,
                            Object[].class.cast(each), i++));
                } else {
                    throw new Exception(String.format(
                            "%s.%s() must return a Collection of arrays.",
                            this.klass.getName(), getParametersMethod()
                                    .getName()));
                }
            }
        }

        private Collection<?> getParametersList()
                throws IllegalAccessException, InvocationTargetException,
                Exception {
            return Collection.class.cast(getParametersMethod().invoke(null));
        }

        private Method getParametersMethod() throws Exception {
            for (final Method each : klass.getMethods()) {
                if (each.isBridge() || each.isSynthetic()) {
                    continue;
                }
                if (Modifier.isStatic(each.getModifiers())) {
                    final Annotation[] annotations = each.getAnnotations();
                    for (final Annotation annotation : annotations) {
                        if (annotation.annotationType() == Parameters.class)
                            return each;
                    }
                }
            }
            throw new Exception("No public static parameters method on class "
                    + getName());
        }

        @Override
        public void run(final RunNotifier notifier) {
            new S2TestClassMethodsRunner(klass) {
                @Override
                protected void runMethods(final RunNotifier notifier) {
                    runChildren(notifier);
                }
            }.run(notifier);
        }
    }

    /**
     * インスタンスを構築します。
     * 
     * @param klass
     *            テストクラス
     * @throws Exception
     *             何らかの例外が発生した場合
     */
    public S2Parameterized(final Class<?> klass) throws Exception {
        super(klass, new RunAllParameterMethods(klass));
    }

    @Override
    protected void validate(final S2MethodValidator methodValidator) {
        methodValidator.validateStaticMethods();
        methodValidator.validateInstanceMethods();
    }
}
