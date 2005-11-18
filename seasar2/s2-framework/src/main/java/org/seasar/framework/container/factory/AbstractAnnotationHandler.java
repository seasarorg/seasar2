/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

public abstract class AbstractAnnotationHandler implements AnnotationHandler {

    protected static final String COMPONENT = "COMPONENT";

    protected static final String NAME = "name";

    protected static final String INSTANCE = "instance";

    protected static final String AUTO_BINDING = "autoBinding";

    protected static final String BINDING_SUFFIX = "_BINDING";
    
    protected static final String BINDING_TYPE = "bindingType";

    protected static final String VALUE = "value";
    
    protected static final String ASPECT = "ASPECT";
    
    protected static final String INIT_METHOD = "INIT_METHOD";
    
    protected static final String INTERCEPTOR = "interceptor";
    
    protected static final String POINTCUT = "pointcut";

    public ComponentDef createComponentDef(String className, InstanceDef instanceDef) {
        return createComponentDef(ClassUtil.forName(className), instanceDef);
    }

    public ComponentDef createComponentDefWithDI(String className, InstanceDef instanceDef) {
        return createComponentDefWithDI(ClassUtil.forName(className), instanceDef);
    }
    
    public ComponentDef createComponentDefWithDI(Class componentClass, InstanceDef instanceDef) {
        ComponentDef componentDef = createComponentDef(componentClass, instanceDef);
        appendDI(componentDef);
        return componentDef;
    }
    
    public void appendDI(ComponentDef componentDef) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentDef.getComponentClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.hasWriteMethod()) {
                continue;
            }
            PropertyDef propDef = createPropertyDef(beanDesc, pd);
            if (propDef == null) {
                continue;
            }
            componentDef.addPropertyDef(propDef);
        }
        appendAspect(componentDef);
        appendInitMethod(componentDef);
    }
    
    protected ComponentDef createComponentDefInternal(Class componentClass, InstanceDef instanceDef) {
        ComponentDef componentDef = new ComponentDefImpl(componentClass);
        if (instanceDef != null) {
            componentDef.setInstanceDef(instanceDef);
        }
        return componentDef;
    }

    protected PropertyDef createPropertyDef(String propertyName, String expression, String bindingTypeName) {
        PropertyDef propertyDef = new PropertyDefImpl(propertyName);
        if (!StringUtil.isEmpty(bindingTypeName)) {
            BindingTypeDef bindingTypeDef = BindingTypeDefFactory.getBindingTypeDef(bindingTypeName);
            propertyDef.setBindingTypeDef(bindingTypeDef);
        }
        if (!StringUtil.isEmpty(expression)) {
            propertyDef.setExpression(expression);
        }
        return propertyDef;
    }
}