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

import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;

/**
 * @author taedium
 * 
 */
public interface InternalTestContext extends TestContext {

    void setTestClass(Class<?> testClass);

    void setTestMethod(Method testMethod);

    void setExpression(Expression expressionContext);

    void setTestIntrospector(S2TestIntrospector introspector);

    void initContainer();

    void destroyContainer();

    void prepareTestData();

    <T> T getComponent(Class<T> componentKey);

    Object getComponent(Object componentKey);

    boolean hasComponentDef(Object componentKey);

    ComponentDef getComponentDef(final int index);

    ComponentDef getComponentDef(final Object componentKey);

    void addMockInterceptor(MockInterceptor mockInterceptor);

    void addAspecDef(Object componentKey, AspectDef aspectDef);
}
