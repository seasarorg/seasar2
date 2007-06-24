/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.util.AopProxyUtil;
import org.seasar.framework.container.util.ArgDefSupport;
import org.seasar.framework.container.util.AspectDefSupport;
import org.seasar.framework.container.util.DestroyMethodDefSupport;
import org.seasar.framework.container.util.InitMethodDefSupport;
import org.seasar.framework.container.util.InterTypeDefSupport;
import org.seasar.framework.container.util.MetaDefSupport;
import org.seasar.framework.container.util.PropertyDefSupport;

/**
 * {@link ComponentDef}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class ComponentDefImpl implements ComponentDef, ContainerConstants {

    private Class componentClass;

    private String componentName;

    private Class concreteClass;

    private S2Container container;

    private Expression expression;

    private ArgDefSupport argDefSupport = new ArgDefSupport();

    private PropertyDefSupport propertyDefSupport = new PropertyDefSupport();

    private InitMethodDefSupport initMethodDefSupport = new InitMethodDefSupport();

    private DestroyMethodDefSupport destroyMethodDefSupport = new DestroyMethodDefSupport();

    private AspectDefSupport aspectDefSupport = new AspectDefSupport();

    private InterTypeDefSupport interTypeDefSupport = new InterTypeDefSupport();

    private MetaDefSupport metaDefSupport = new MetaDefSupport();

    private InstanceDef instanceDef = InstanceDefFactory.SINGLETON;

    private AutoBindingDef autoBindingDef = AutoBindingDefFactory.AUTO;

    private ComponentDeployer componentDeployer;

    private boolean externalBinding = false;

    /**
     * {@link ComponentDefImpl}を作成します。
     */
    public ComponentDefImpl() {
    }

    /**
     * {@link ComponentDefImpl}を作成します。
     * 
     * @param componentClass
     */
    public ComponentDefImpl(Class componentClass) {
        this(componentClass, null);
    }

    /**
     * {@link ComponentDefImpl}を作成します。
     * 
     * @param componentClass
     * @param componentName
     */
    public ComponentDefImpl(Class componentClass, String componentName) {
        this.componentClass = componentClass;
        setComponentName(componentName);
    }

    public Object getComponent() {
        return getComponentDeployer().deploy();
    }

    public void injectDependency(Object outerComponent) {
        getComponentDeployer().injectDependency(outerComponent);
    }

    public Class getComponentClass() {
        return componentClass;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Class getConcreteClass() {
        if (concreteClass == null) {
            ClassLoader oldLoader = Thread.currentThread()
                    .getContextClassLoader();
            try {
                ClassLoader loader = (container != null ? container
                        .getClassLoader() : null);
                if (loader != null) {
                    Thread.currentThread().setContextClassLoader(loader);
                }
                concreteClass = AopProxyUtil.getConcreteClass(this);
            } finally {
                Thread.currentThread().setContextClassLoader(oldLoader);
            }
        }
        return concreteClass;
    }

    public S2Container getContainer() {
        return container;
    }

    public void setContainer(S2Container container) {
        this.container = container;
        argDefSupport.setContainer(container);
        metaDefSupport.setContainer(container);
        propertyDefSupport.setContainer(container);
        initMethodDefSupport.setContainer(container);
        destroyMethodDefSupport.setContainer(container);
        aspectDefSupport.setContainer(container);
        interTypeDefSupport.setContainer(container);
    }

    public void addArgDef(ArgDef argDef) {
        argDefSupport.addArgDef(argDef);
    }

    public void addPropertyDef(PropertyDef propertyDef) {
        propertyDefSupport.addPropertyDef(propertyDef);
    }

    public void addInitMethodDef(InitMethodDef methodDef) {
        initMethodDefSupport.addInitMethodDef(methodDef);
    }

    public void addDestroyMethodDef(DestroyMethodDef methodDef) {
        destroyMethodDefSupport.addDestroyMethodDef(methodDef);
    }

    public void addAspectDef(AspectDef aspectDef) {
        aspectDefSupport.addAspectDef(aspectDef);
        concreteClass = null;
    }

    public void addAspectDef(int index, AspectDef aspectDef) {
        aspectDefSupport.addAspectDef(index, aspectDef);
        concreteClass = null;
    }

    public void addInterTypeDef(InterTypeDef interTypeDef) {
        interTypeDefSupport.addInterTypeDef(interTypeDef);
        concreteClass = null;
    }

    public int getArgDefSize() {
        return argDefSupport.getArgDefSize();
    }

    public int getPropertyDefSize() {
        return propertyDefSupport.getPropertyDefSize();
    }

    public int getInitMethodDefSize() {
        return initMethodDefSupport.getInitMethodDefSize();
    }

    public int getDestroyMethodDefSize() {
        return destroyMethodDefSupport.getDestroyMethodDefSize();
    }

    public int getAspectDefSize() {
        return aspectDefSupport.getAspectDefSize();
    }

    public int getInterTypeDefSize() {
        return interTypeDefSupport.getInterTypeDefSize();
    }

    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    public void init() {
        getConcreteClass();
        getComponentDeployer().init();
    }

    public void destroy() {
        getComponentDeployer().destroy();
        componentClass = null;
        componentName = null;
        concreteClass = null;
        container = null;
        expression = null;
        argDefSupport = null;
        propertyDefSupport = null;
        initMethodDefSupport = null;
        destroyMethodDefSupport = null;
        aspectDefSupport = null;
        interTypeDefSupport = null;
        metaDefSupport = null;
        instanceDef = null;
        autoBindingDef = null;
        componentDeployer = null;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public ArgDef getArgDef(int index) {
        return argDefSupport.getArgDef(index);
    }

    public PropertyDef getPropertyDef(int index) {
        return propertyDefSupport.getPropertyDef(index);
    }

    public PropertyDef getPropertyDef(String propertyName) {
        if (hasPropertyDef(propertyName)) {
            return propertyDefSupport.getPropertyDef(propertyName);
        }
        throw new PropertyNotFoundRuntimeException(componentClass, propertyName);
    }

    public boolean hasPropertyDef(String propertyName) {
        return propertyDefSupport.hasPropertyDef(propertyName);
    }

    public InitMethodDef getInitMethodDef(int index) {
        return initMethodDefSupport.getInitMethodDef(index);
    }

    public DestroyMethodDef getDestroyMethodDef(int index) {
        return destroyMethodDefSupport.getDestroyMethodDef(index);
    }

    public AspectDef getAspectDef(int index) {
        return aspectDefSupport.getAspectDef(index);
    }

    public InterTypeDef getInterTypeDef(int index) {
        return interTypeDefSupport.getInterTypeDef(index);
    }

    public void addMetaDef(MetaDef metaDef) {
        metaDefSupport.addMetaDef(metaDef);
    }

    public MetaDef getMetaDef(int index) {
        return metaDefSupport.getMetaDef(index);
    }

    public MetaDef getMetaDef(String name) {
        return metaDefSupport.getMetaDef(name);
    }

    public MetaDef[] getMetaDefs(String name) {
        return metaDefSupport.getMetaDefs(name);
    }

    public int getMetaDefSize() {
        return metaDefSupport.getMetaDefSize();
    }

    /**
     * {@link ComponentDeployer}を返します。
     * 
     * @return {@link ComponentDeployer}
     */
    public ComponentDeployer getComponentDeployer() {
        if (componentDeployer == null) {
            componentDeployer = instanceDef.createComponentDeployer(this);
        }
        return componentDeployer;
    }

    public boolean isExternalBinding() {
        return externalBinding;
    }

    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
    }

}