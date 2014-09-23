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
import java.util.Collection;

import org.junit.internal.runners.CompositeRunner;
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

    /**
     * {@link Parameters}が注釈されたすべてのメソッドを実行するランナーです。
     * 
     * @author taedium
     */
    public static class RunAllParameterMethods extends CompositeRunner {

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
