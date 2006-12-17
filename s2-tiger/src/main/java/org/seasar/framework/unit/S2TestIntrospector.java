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
import java.util.List;

/**
 * @author taedium
 * 
 */
public interface S2TestIntrospector {

    List<Method> getTestMethods(Class<?> testClass);

    boolean isIgnored(Method method);

    Class<? extends Throwable> expectedException(Method method);

    long getTimeout(Method method);

    List<String> getPrerequisiteExpressions(Class<?> testClass,
            Method testMethod);

    boolean needsTransaction(Class<?> testClass, Method testMethod);

    boolean requiresTransactionCommitment(Class<?> testClass, Method testMethod);

    boolean needsWarmDeploy(Class<?> testClass, Method method);

    void createMockInterceptor(Method testMethod, Expression expression,
            InternalTestContext context);

    List<Method> getBeforeClassMethods(Class<?> testClass);

    List<Method> getAfterClassMethods(Class<?> testClass);

    List<Method> getBeforeMethods(Class<?> testClass);

    List<Method> getAfterMethods(Class<?> testClass);

    Method getEachBeforeMethod(Class<?> testClass, Method testMethod);

    Method getEachAfterMethod(Class<?> testClass, Method testMethod);

}
