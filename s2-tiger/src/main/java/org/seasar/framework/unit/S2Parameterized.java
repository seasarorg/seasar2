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

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.junit.internal.runners.CompositeRunner;
import org.junit.internal.runners.MethodValidator;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author taedium
 * 
 */
public class S2Parameterized extends TestClassRunner {

    private static class TestClassRunnerForParameters extends
            S2TestClassMethodsRunner {
        private final Object[] parameters;

        private final int parameterSetNumber;

        private final Constructor constructor;

        private TestClassRunnerForParameters(Class<?> klass,
                Object[] parameters, int i) {
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

        private Constructor getOnlyConstructor() {
            Constructor[] constructors = getTestClass().getConstructors();
            assertEquals(1, constructors.length);
            return constructors[0];
        }
    }

    public static class RunAllParameterMethods extends CompositeRunner {
        private final Class<?> klass;

        public RunAllParameterMethods(Class<?> klass) throws Exception {
            super(klass.getName());
            this.klass = klass;
            int i = 0;
            for (final Object[] parameters : getParametersList()) {
                super.add(new TestClassRunnerForParameters(klass, parameters,
                        i++));
            }
        }

        @SuppressWarnings("unchecked")
        private Collection<Object[]> getParametersList()
                throws IllegalAccessException, InvocationTargetException,
                Exception {
            return (Collection) getParametersMethod().invoke(null);
        }

        private Method getParametersMethod() throws Exception {
            for (Method each : klass.getMethods()) {
                if (Modifier.isStatic(each.getModifiers())) {
                    Annotation[] annotations = each.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == Parameters.class)
                            return each;
                    }
                }
            }
            throw new Exception("No public static parameters method on class "
                    + getName());
        }
    }

    public S2Parameterized(final Class<?> klass) throws Exception {
        super(klass, new RunAllParameterMethods(klass));
    }

    @Override
    protected void validate(MethodValidator methodValidator) {
        methodValidator.validateStaticMethods();
        methodValidator.validateInstanceMethods();
    }
}
