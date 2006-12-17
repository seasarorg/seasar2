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

import org.seasar.extension.dataset.DataSet;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.ComponentDef;

/**
 * @author taedium
 * 
 */
public interface TestContext {

    void register(Class<?> componentClass);

    void register(Class<?> componentClass, String componentName);

    void register(Object component);

    void register(Object component, String componentName);

    void register(ComponentDef componentDef);

    void include(String path);

    void setAutoIncluding(boolean autoIncluding);

    void setAutoPreparing(boolean autoPreparing);

    DataSet getExpected();

    String getTestClassPackagePath();

    String getTestClassShortName();

    String getTestMethodName();

    MockInterceptor getMockInterceptor(int index);
    
    int getMockInterceptorSize();
}
