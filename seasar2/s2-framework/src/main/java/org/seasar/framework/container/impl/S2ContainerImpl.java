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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ognl.OgnlRuntime;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.ContainerNotRegisteredRuntimeException;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationComponentDef;
import org.seasar.framework.container.ognl.S2ContainerPropertyAccessor;
import org.seasar.framework.container.util.MetaDefSupport;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class S2ContainerImpl implements S2Container, ContainerConstants {

    private Map componentDefMap = new HashMap();

    private List componentDefList = new ArrayList();

    private String namespace;

    private String path;

    private List children = new ArrayList();

    private CaseInsensitiveMap descendants = new CaseInsensitiveMap();

    private S2Container root;

    private ExternalContext externalContext;

    private ExternalContextComponentDefRegister externalContextComponentDefRegister;

    private MetaDefSupport metaDefSupport = new MetaDefSupport(this);

    private boolean inited = false;

    private boolean hotswapMode = false;

    private ClassLoader classLoader = null;

    static {
        OgnlRuntime.setPropertyAccessor(S2Container.class,
                new S2ContainerPropertyAccessor());
    }

    public S2ContainerImpl() {
        root = this;
        register0(new SimpleComponentDef(this, CONTAINER_NAME));
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    public S2Container getRoot() {
        return root;
    }

    public void setRoot(S2Container root) {
        this.root = root;
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponent(java.lang.Object)
     */
    public Object getComponent(Object componentKey) {
        assertParameterIsNotNull(componentKey, "componentKey");
        ComponentDef cd = S2ContainerBehavior.acquireFromGetComponent(this,
                componentKey);
        if (cd == null) {
            return null;
        }
        return cd.getComponent();
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponent(java.lang.Object)
     */
    public Object[] findComponents(Object componentKey) {
        assertParameterIsNotNull(componentKey, "componentKey");
        ComponentDef[] componentDefs = findComponentDefs(componentKey);
        Object[] components = new Object[componentDefs.length];
        for (int i = 0; i < componentDefs.length; ++i) {
            components[i] = componentDefs[i].getComponent();
        }
        return components;
    }

    /**
     * @see org.seasar.framework.container.S2Container#injectDependency(java.lang.Object)
     */
    public void injectDependency(Object outerComponent) {
        injectDependency(outerComponent, outerComponent.getClass());
    }

    /**
     * @see org.seasar.framework.container.S2Container#injectDependency(java.lang.Object,
     *      java.lang.Class)
     */
    public void injectDependency(Object outerComponent, Class componentClass) {
        assertParameterIsNotNull(outerComponent, "outerComponent");
        assertParameterIsNotNull(componentClass, "componentClass");
        ComponentDef cd = S2ContainerBehavior.acquireFromInjectDependency(this,
                componentClass);
        if (cd != null) {
            cd.injectDependency(outerComponent);
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#injectDependency(java.lang.Object,
     *      java.lang.String)
     */
    public void injectDependency(Object outerComponent, String componentName) {
        assertParameterIsNotNull(outerComponent, "outerComponent");
        assertParameterIsNotEmpty(componentName, "componentName");
        ComponentDef cd = S2ContainerBehavior.acquireFromInjectDependency(this,
                componentName);
        if (cd != null) {
            cd.injectDependency(outerComponent);
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#register(java.lang.Object)
     */
    public void register(Object component) {
        assertParameterIsNotNull(component, "component");
        register(new SimpleComponentDef(component));
    }

    public void register(Object component, String componentName) {
        assertParameterIsNotNull(component, "component");
        assertParameterIsNotEmpty(componentName, "componentName");
        register(new SimpleComponentDef(component, componentName));
    }

    /**
     * @see org.seasar.framework.container.S2Container#register(java.lang.Class)
     */
    public void register(Class componentClass) {
        assertParameterIsNotNull(componentClass, "componentClass");
        register(new ComponentDefImpl(componentClass));
    }

    /**
     * @see org.seasar.framework.container.S2Container#register(java.lang.Class,
     *      java.lang.String)
     */
    public void register(Class componentClass, String componentName) {
        assertParameterIsNotNull(componentClass, "componentClass");
        assertParameterIsNotEmpty(componentName, "componentName");
        register(new ComponentDefImpl(componentClass, componentName));
    }

    /**
     * @see org.seasar.framework.container.S2Container#register(org.seasar.framework.container.ComponentDef)
     */
    public synchronized void register(ComponentDef componentDef) {
        assertParameterIsNotNull(componentDef, "componentDef");
        register0(componentDef);
        componentDefList.add(componentDef);
    }

    protected void register0(ComponentDef componentDef) {
        if (componentDef.getContainer() == null) {
            componentDef.setContainer(this);
        }
        registerByClass(componentDef);
        registerByName(componentDef);
    }

    protected void registerByClass(ComponentDef componentDef) {
        Class[] classes = getAssignableClasses(componentDef.getComponentClass());
        for (int i = 0; i < classes.length; ++i) {
            registerMap(classes[i], componentDef);
        }
    }

    protected void registerByName(ComponentDef componentDef) {
        String componentName = componentDef.getComponentName();
        if (componentName != null) {
            registerMap(componentName, componentDef);
        }
    }

    protected void registerMap(Object key, ComponentDef componentDef) {
        if (componentDefMap.containsKey(key)) {
            processTooManyRegistration(key, componentDef);
        } else {
            componentDefMap.put(key, componentDef);
        }
    }
    
    protected void registerMapIfNotContains(Object key, ComponentDef componentDef) {
        if (!componentDefMap.containsKey(key)) {
            componentDefMap.put(key, componentDef);
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponentDefSize()
     */
    public synchronized int getComponentDefSize() {
        return componentDefList.size();
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponentDef(int)
     */
    public synchronized ComponentDef getComponentDef(int index) {
        return (ComponentDef) componentDefList.get(index);
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponentDef(java.lang.Object)
     */
    public ComponentDef getComponentDef(Object key)
            throws ComponentNotFoundRuntimeException {
        assertParameterIsNotNull(key, "key");
        return S2ContainerBehavior.acquireFromGetComponentDef(this, key);
    }

    /**
     * @see org.seasar.framework.container.S2Container#findComponentDefs(java.lang.Object)
     */
    public ComponentDef[] findComponentDefs(Object key)
            throws ComponentNotFoundRuntimeException {
        assertParameterIsNotNull(key, "key");
        ComponentDef cd = internalGetComponentDef(key);
        if (cd == null) {
            return new ComponentDef[0];
        } else if (cd instanceof TooManyRegistrationComponentDefImpl) {
            return ((TooManyRegistrationComponentDefImpl) cd)
                    .getComponentDefs();
        }
        return new ComponentDef[] { cd };
    }

    protected synchronized ComponentDef internalGetComponentDef(Object key) {
        ComponentDef cd = (ComponentDef) componentDefMap.get(key);
        if (cd != null) {
            return cd;
        }
        if (key instanceof String) {
            String name = (String) key;
            int index = name.indexOf(NS_SEP);
            if (index > 0) {
                String ns = name.substring(0, index);
                if (internalGetComponentDef(ns) != null) {
                    S2Container child = (S2Container) getComponent(ns);
                    name = name.substring(index + 1);
                    if (child.hasComponentDef(name)) {
                        return child.getComponentDef(name);
                    }
                }
            }
        }
        /*
        for (int i = 0; i < getChildSize(); ++i) {
            S2Container child = getChild(i);
            if (child.hasComponentDef(key)) {
                return child.getComponentDef(key);
            }
        }
        */
        return null;
    }

    /**
     * @see org.seasar.framework.container.S2Container#hasComponentDef(java.lang.Object)
     */
    public boolean hasComponentDef(Object componentKey) {
        assertParameterIsNotNull(componentKey, "componentKey");
        return S2ContainerBehavior.acquireFromHasComponentDef(this,
                componentKey) != null;
    }

    /**
     * @see org.seasar.framework.container.S2Container#hasDescendant(java.lang.String)
     */
    public synchronized boolean hasDescendant(String path) {
        assertParameterIsNotEmpty(path, "path");
        return descendants.containsKey(path);
    }

    public synchronized S2Container getDescendant(String path) {
        S2Container descendant = (S2Container) descendants.get(path);
        if (descendant != null) {
            return descendant;
        }
        throw new ContainerNotRegisteredRuntimeException(path);
    }

    public synchronized void registerDescendant(S2Container descendant) {
        assertParameterIsNotNull(descendant, "descendant");
        descendants.put(descendant.getPath(), descendant);
    }

    /**
     * @see org.seasar.framework.container.S2Container#include(org.seasar.framework.container.S2Container)
     */
    public synchronized void include(S2Container child) {
        assertParameterIsNotNull(child, "child");
        child.setRoot(getRoot());
        ((S2ContainerImpl) child).addComponentDefsToParent(this);
        children.add(child);
        String ns = child.getNamespace();
        if (ns != null) {
            registerMap(ns, new S2ContainerComponentDef(child, ns));
        }
    }
    
    protected void addComponentDefsToParent(S2Container parent) {
        S2ContainerImpl pimpl = (S2ContainerImpl) parent;
        for (Iterator i = componentDefMap.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            if (namespace == key) {
                continue;
            }
            ComponentDef cd = (ComponentDef) componentDefMap.get(key);
            if (isNeedNSForKey(key)) {
                pimpl.registerMapIfNotContains(namespace + NS_SEP + key, cd);
            } else {
                pimpl.registerMapIfNotContains(key, cd);
            }
            
        }
    }
    
    protected boolean isNeedNSForKey(Object key) {
        return key instanceof String && ((String) key).indexOf(NS_SEP) < 0 && namespace != null;
    }

    /**
     * @see org.seasar.framework.container.S2Container#getChildSize()
     */
    public synchronized int getChildSize() {
        return children.size();
    }

    /**
     * @see org.seasar.framework.container.S2Container#getChild(int)
     */
    public synchronized S2Container getChild(int index) {
        return (S2Container) children.get(index);
    }

    /**
     * @see org.seasar.framework.container.S2Container#init()
     */
    public void init() {
        if (inited) {
            return;
        }
        ExternalContextComponentDefRegister register = getRoot().getExternalContextComponentDefRegister();
        if (register != null) {
            register.registerComponentDefs(this);
        }
        final ClassLoader currentLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            for (int i = 0; i < getChildSize(); ++i) {
                getChild(i).init();
            }
            for (int i = 0; i < getComponentDefSize(); ++i) {
                getComponentDef(i).init();
            }
            inited = true;
        } finally {
            Thread.currentThread().setContextClassLoader(currentLoader);
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#destroy()
     */
    public void destroy() {
        if (!inited) {
            return;
        }

        final ClassLoader currentLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            for (int i = getComponentDefSize() - 1; 0 <= i; --i) {
                try {
                    getComponentDef(i).destroy();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
            for (int i = getChildSize() - 1; 0 <= i; --i) {
                getChild(i).destroy();
            }
            inited = false;
        } finally {
            Thread.currentThread().setContextClassLoader(currentLoader);
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#getNamespace()
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @see org.seasar.framework.container.S2Container#setNamespace(java.lang.String)
     */
    public synchronized void setNamespace(String namespace) {
        componentDefMap.remove(namespace);
        this.namespace = namespace;
        componentDefMap.put(namespace, new SimpleComponentDef(this, namespace));
    }

    /**
     * @see org.seasar.framework.container.S2Container#getPath()
     */
    public String getPath() {
        return path;
    }

    /**
     * @see org.seasar.framework.container.S2Container#setPath(java.lang.String)
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @see org.seasar.framework.container.S2Container#getExternalContext()
     */
    public ExternalContext getExternalContext() {
        return externalContext;
    }

    /**
     * @see org.seasar.framework.container.S2Container#setExternalContext(org.seasar.framework.container.ExternalContext)
     */
    public void setExternalContext(ExternalContext externalContext) {
        this.externalContext = externalContext;
    }

    /**
     * @see org.seasar.framework.container.S2Container#getExternalContextComponentDefRegister()
     */
    public ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
        return externalContextComponentDefRegister;
    }

    /**
     * @see org.seasar.framework.container.S2Container#setExternalContextComponentDefRegister(org.seasar.framework.container.ExternalContextComponentDefRegister)
     */
    public void setExternalContextComponentDefRegister(
            ExternalContextComponentDefRegister register) {
        this.externalContextComponentDefRegister = register;
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#addMetaDef(org.seasar.framework.container.MetaDef)
     */
    public void addMetaDef(MetaDef metaDef) {
        metaDefSupport.addMetaDef(metaDef);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(int)
     */
    public MetaDef getMetaDef(int index) {
        return metaDefSupport.getMetaDef(index);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(java.lang.String)
     */
    public MetaDef getMetaDef(String name) {
        return metaDefSupport.getMetaDef(name);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefs(java.lang.String)
     */
    public MetaDef[] getMetaDefs(String name) {
        return metaDefSupport.getMetaDefs(name);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefSize()
     */
    public int getMetaDefSize() {
        return metaDefSupport.getMetaDefSize();
    }

    public boolean isHotswapMode() {
        return hotswapMode;
    }

    public void setHotswapMode(boolean hotswapMode) {
        this.hotswapMode = hotswapMode;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    private static Class[] getAssignableClasses(Class componentClass) {
        Set classes = new HashSet();
        for (Class clazz = componentClass; clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {

            addAssignableClasses(classes, clazz);
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    private static void addAssignableClasses(Set classes, Class clazz) {

        classes.add(clazz);
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            addAssignableClasses(classes, interfaces[i]);
        }
    }

    private void processTooManyRegistration(Object key,
            ComponentDef componentDef) {

        ComponentDef cd = (ComponentDef) componentDefMap.get(key);
        if (cd instanceof TooManyRegistrationComponentDef) {
            ((TooManyRegistrationComponentDef) cd)
                    .addComponentDef(componentDef);
        } else {
            TooManyRegistrationComponentDef tmrcf = new TooManyRegistrationComponentDefImpl(
                    key);
            tmrcf.addComponentDef(cd);
            tmrcf.addComponentDef(componentDef);
            componentDefMap.put(key, tmrcf);
        }
    }

    protected void assertParameterIsNotNull(Object parameter, String name) {
        if (parameter == null) {
            throw new IllegalArgumentException(name);
        }
    }

    protected void assertParameterIsNotEmpty(String parameter, String name) {
        if (StringUtil.isEmpty(parameter)) {
            throw new IllegalArgumentException(name);
        }
    }
}