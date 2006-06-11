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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;

/**
 * @author higa
 * 
 */
public class SimpleComponentDef implements ComponentDef {

    private Object component;

    private Class componentClass;

    private String componentName;

    private S2Container container;

    public SimpleComponentDef() {
    }

    public SimpleComponentDef(Class componentClass) {
        this(null, componentClass, null);
    }

    public SimpleComponentDef(Class componentClass, String componentName) {
        this(null, componentClass, componentName);
    }

    public SimpleComponentDef(Object component) {
        this(component, component.getClass());
    }

    public SimpleComponentDef(Object component, Class componentClass) {
        this(component, componentClass, null);
    }

    public SimpleComponentDef(Object component, String componentName) {
        this(component, component.getClass(), componentName);
    }

    public SimpleComponentDef(Object component, Class componentClass,
            String componentName) {

        this.component = component;
        this.componentClass = componentClass;
        this.componentName = componentName;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getComponent()
     */
    public Object getComponent() throws TooManyRegistrationRuntimeException {
        return component;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#injectDependency(java.lang.Object)
     */
    public void injectDependency(Object outerComponent) {
        throw new UnsupportedOperationException("injectDependency");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getContainer()
     */
    public S2Container getContainer() {
        return container;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#setContainer(org.seasar.framework.container.S2Container)
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getComponentClass()
     */
    public Class getComponentClass() {
        return componentClass;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getComponentName()
     */
    public String getComponentName() {
        return componentName;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#setComponentName(java.lang.String)
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getConcreteClass()
     */
    public Class getConcreteClass() {
        return componentClass;
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#addConstructorArgDef(org.seasar.framework.container.ConstructorArgDef)
     */
    public void addArgDef(ArgDef constructorArgDef) {
        throw new UnsupportedOperationException("addArgDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#addPropertyDef(org.seasar.framework.container.PropertyDef)
     */
    public void addPropertyDef(PropertyDef propertyDef) {
        throw new UnsupportedOperationException("addPropertyDef");
    }

    /*
     * @see org.seasar.framework.container.InitMethodDefAware#addInitMethodDef(org.seasar.framework.container.InitMethodDef)
     */
    public void addInitMethodDef(InitMethodDef methodDef) {
        throw new UnsupportedOperationException("addInitMethodDef");
    }

    /*
     * @see org.seasar.framework.container.DestroyMethodDefAware#addDestroyMethodDef(org.seasar.framework.container.DestroyMethodDef)
     */
    public void addDestroyMethodDef(DestroyMethodDef methodDef) {
        throw new UnsupportedOperationException("addDestroyMethodDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#addAspectDef(org.seasar.framework.container.AspectDef)
     */
    public void addAspectDef(AspectDef aspectDef) {
        throw new UnsupportedOperationException("addAspectDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#addInterTypeDef(org.seasar.framework.container.InterTypeDef)
     */
    public void addInterTypeDef(InterTypeDef interTypeDef) {
        throw new UnsupportedOperationException("addInterTypeDef");
    }

    /*
     * @see org.seasar.framework.container.ArgDefAware#getArgDefSize()
     */
    public int getArgDefSize() {
        throw new UnsupportedOperationException("getArgDefSize");
    }

    /*
     * @see org.seasar.framework.container.PropertyDefAware#getPropertyDefSize()
     */
    public int getPropertyDefSize() {
        throw new UnsupportedOperationException("getPropertyDefSize");
    }

    /*
     * @see org.seasar.framework.container.InitMethodDefAware#getInitMethodDefSize()
     */
    public int getInitMethodDefSize() {
        throw new UnsupportedOperationException("getInitMethodDefSize");
    }

    /*
     * @see org.seasar.framework.container.DestroyMethodDefAware#getDestroyMethodDefSize()
     */
    public int getDestroyMethodDefSize() {
        throw new UnsupportedOperationException("getDestroyMethodDefSize");
    }

    /*
     * @see org.seasar.framework.container.AspectDefAware#getAspectDefSize()
     */
    public int getAspectDefSize() {
        throw new UnsupportedOperationException("getAspectDefSize");
    }

    /*
     * @see org.seasar.framework.container.InterTypeDefAware#getInterTypeDefSize()
     */
    public int getInterTypeDefSize() {
        throw new UnsupportedOperationException("getInterTypeDefSize");
    }

    /*
     * @see org.seasar.framework.container.ArgDefAware#getArgDef(int)
     */
    public ArgDef getArgDef(int index) {
        throw new UnsupportedOperationException("getArgDef");
    }

    /*
     * @see org.seasar.framework.container.PropertyDefAware#getPropertyDef(int)
     */
    public PropertyDef getPropertyDef(int index) {
        throw new UnsupportedOperationException("getPropertyDef");
    }

    /*
     * @see org.seasar.framework.container.PropertyDefAware#getPropertyDef(java.lang.String)
     */
    public PropertyDef getPropertyDef(String propertyName) {
        throw new UnsupportedOperationException("getPropertyDef");
    }

    /*
     * @see org.seasar.framework.container.PropertyDefAware#hasPropertyDef(java.lang.String)
     */
    public boolean hasPropertyDef(String propertyName) {
        throw new UnsupportedOperationException("hasPropertyDef");
    }

    /*
     * @see org.seasar.framework.container.InitMethodDefAware#getInitMethodDef(int)
     */
    public InitMethodDef getInitMethodDef(int index) {
        throw new UnsupportedOperationException("getInitMethodDef");
    }

    /*
     * @see org.seasar.framework.container.DestroyMethodDefAware#getDestroyMethodDef(int)
     */
    public DestroyMethodDef getDestroyMethodDef(int index) {
        throw new UnsupportedOperationException("getDestroyMethodDef");
    }

    /*
     * @see org.seasar.framework.container.AspectDefAware#getAspectDef(int)
     */
    public AspectDef getAspectDef(int index) {
        throw new UnsupportedOperationException("getAspectDef");
    }

    /*
     * @see org.seasar.framework.container.InterTypeDefAware#getInterTypeDef(int)
     */
    public InterTypeDef getInterTypeDef(int index) {
        throw new UnsupportedOperationException("getInterTypeDef");
    }

    /*
     * @see org.seasar.framework.container.MetaDefAware#addMetaDef(org.seasar.framework.container.MetaDef)
     */
    public void addMetaDef(MetaDef metaDef) {
        throw new UnsupportedOperationException("addMetaDef");
    }

    /*
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(int)
     */
    public MetaDef getMetaDef(int index) {
        throw new UnsupportedOperationException("getMetaDef");
    }

    /*
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(java.lang.String)
     */
    public MetaDef getMetaDef(String name) {
        throw new UnsupportedOperationException("getMetaDef");
    }

    /*
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefs(java.lang.String)
     */
    public MetaDef[] getMetaDefs(String name) {
        throw new UnsupportedOperationException("getMetaDefs");
    }

    /*
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefSize()
     */
    public int getMetaDefSize() {
        throw new UnsupportedOperationException("getMetaDefSize");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getExpression()
     */
    public Expression getExpression() {
        throw new UnsupportedOperationException("getExpression");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#setExpression(java.lang.String)
     */
    public void setExpression(Expression expression) {
        throw new UnsupportedOperationException("setExpression");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getInstanceDef()
     */
    public InstanceDef getInstanceDef() {
        throw new UnsupportedOperationException("getInstanceDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#setInstanceDef(org.seasar.framework.container.InstanceDef)
     */
    public void setInstanceDef(InstanceDef instanceDef) {
        throw new UnsupportedOperationException("setInstanceDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#getAutoBindingDef()
     */
    public AutoBindingDef getAutoBindingDef() {
        throw new UnsupportedOperationException("getAutoBindingDef");
    }

    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        throw new UnsupportedOperationException("setAutoBindingDef");
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#init()
     */
    public void init() {
    }

    /*
     * @see org.seasar.framework.container.ComponentDef#destroy()
     */
    public void destroy() {
    }
}