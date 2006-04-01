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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * @author taedium
 * 
 */
public class S2FrameworkTestCaseAdapter {

    private S2FrameworkTestCase adaptee;
    
    public S2FrameworkTestCaseAdapter(S2FrameworkTestCase testCase) {
        this.adaptee = testCase;
    }

    public S2Container getContainer() {
        return adaptee.getContainer();
    }
    
    public Object getComponent(String componentName) {
        return adaptee.getComponent(componentName);
    }

    public Object getComponent(Class componentClass) {
        return adaptee.getComponent(componentClass);
    }

    public ComponentDef getComponentDef(String componentName) {
        return adaptee.getComponentDef(componentName);
    }

    public ComponentDef getComponentDef(Class componentClass) {
        return adaptee.getComponentDef(componentClass);
    }
    
    public void setUpContainer() throws Throwable {
        adaptee.setUpContainer();
    }

    public void tearDownContainer() throws Throwable {
        adaptee.tearDownContainer();
    }

    public void setUpAfterContainerInit() throws Throwable {
        adaptee.setUpAfterContainerInit();
    }

    public void tearDownBeforeContainerDestroy() throws Throwable {
        adaptee.tearDownBeforeContainerDestroy();
    }

    public void bindFields() throws Throwable {
        adaptee.bindFields();
    }

    public void setUpAfterBindFields() throws Throwable {
        adaptee.setUpAfterBindFields();
    }

    public void tearDownBeforeUnbindFields() throws Throwable {
        adaptee.tearDownBeforeUnbindFields();
    }

}
