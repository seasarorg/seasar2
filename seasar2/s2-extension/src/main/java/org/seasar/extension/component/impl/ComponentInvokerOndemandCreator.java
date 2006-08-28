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
package org.seasar.extension.component.impl;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * 
 */
public class ComponentInvokerOndemandCreator implements ComponentCreator {

    protected static final String COMPONENT_INVOKER_CLASS_NAME = "org.seasar.extension.component.impl.ComponentInvokerImpl";

    protected String componentInvokerName = "componentInvoker";

    public void setComponentInvokerName(final String componentInvokerName) {
        this.componentInvokerName = componentInvokerName;
    }

    public ComponentDef createComponentDef(final Class clazz) {
        return null;
    }

    public ComponentDef createComponentDef(final String componentName) {
        if (!componentName.equals(componentInvokerName)) {
            return null;
        }
        final ComponentDef cd = new ComponentDefImpl(ClassUtil
                .forName(COMPONENT_INVOKER_CLASS_NAME), componentInvokerName);
        final PropertyDef pd = new PropertyDefImpl("ondemand");
        pd.setExpression(new OgnlExpression("true"));
        cd.addPropertyDef(pd);
        // container.register(cd);
        // cd.init();
        return cd;
    }
}
