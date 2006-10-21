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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ognl.OgnlRuntime;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.ContainerNotRegisteredRuntimeException;
import org.seasar.framework.container.CyclicReferenceRuntimeException;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationComponentDef;
import org.seasar.framework.container.ognl.S2ContainerPropertyAccessor;
import org.seasar.framework.container.util.MetaDefSupport;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.container.util.Traversal;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class S2ContainerImpl implements S2Container, ContainerConstants {

    private static final Logger logger = Logger
            .getLogger(S2ContainerImpl.class);

    private Map componentDefMap = new HashMap();

    private List componentDefList = new ArrayList();

    private String namespace;

    private String path;

    private List children = new ArrayList();

    private Map childPositions = new HashMap();

    private List parents = new ArrayList();

    private CaseInsensitiveMap descendants = new CaseInsensitiveMap();

    private S2Container root;

    private ExternalContext externalContext;

    private ExternalContextComponentDefRegister externalContextComponentDefRegister;

    private MetaDefSupport metaDefSupport = new MetaDefSupport(this);

    private boolean inited = false;

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
        this.root = root != null ? root : this;
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
        return toComponentArray(componentKey, componentDefs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.container.S2Container#findAllComponents(java.lang.Object)
     */
    public Object[] findAllComponents(Object componentKey)
            throws CyclicReferenceRuntimeException {
        assertParameterIsNotNull(componentKey, "componentKey");
        ComponentDef[] componentDefs = findAllComponentDefs(componentKey);
        return toComponentArray(componentKey, componentDefs);
    }

    /**
     * @see org.seasar.framework.container.S2Container#findLocalComponents(java.lang.Object)
     */
    public Object[] findLocalComponents(Object componentKey)
            throws CyclicReferenceRuntimeException {
        assertParameterIsNotNull(componentKey, "componentKey");
        ComponentDef[] componentDefs = findLocalComponentDefs(componentKey);
        return toComponentArray(componentKey, componentDefs);
    }

    protected Object[] toComponentArray(Object componentKey,
            ComponentDef[] componentDefs) {
        int length = componentDefs.length;
        Object[] components = (componentKey instanceof Class) ? (Object[]) Array
                .newInstance((Class) componentKey, length)
                : new Object[length];
        for (int i = 0; i < length; ++i) {
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
    public void register(ComponentDef componentDef) {
        assertParameterIsNotNull(componentDef, "componentDef");
        register0(componentDef);
        componentDefList.add(componentDef);
    }

    public void register0(ComponentDef componentDef) {
        if (componentDef.getContainer() == null) {
            componentDef.setContainer(this);
        }
        registerByClass(componentDef);
        registerByName(componentDef);
    }

    protected void registerByClass(ComponentDef componentDef) {
        Class[] classes = S2ContainerUtil.getAssignableClasses(componentDef
                .getComponentClass());
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
        registerMap(key, componentDef, this);
    }

    public void registerMap(Object key, ComponentDef componentDef,
            S2Container container) {
        int position = getContainerPosition(container);
        ComponentDefHolder holder = (ComponentDefHolder) componentDefMap
                .get(key);
        if (holder == null) {
            holder = new ComponentDefHolder(position, componentDef);
            componentDefMap.put(key, holder);
        } else if (position > holder.getPosition()) {
            return;
        } else if (position < holder.getPosition()) {
            holder.setPosition(position);
            holder.setComponentDef(componentDef);
        } else if (container != this) {
            holder.setComponentDef(componentDef);
        } else {
            holder.setComponentDef(createTooManyRegistration(key, holder
                    .getComponentDef(), componentDef));
        }

        registerParent(key, holder.getComponentDef());
    }

    protected void registerParent(Object key, ComponentDef componentDef) {
        for (int i = 0; i < getParentSize(); i++) {
            S2Container parent = (S2Container) getParent(i);
            parent.registerMap(key, componentDef, this);
            if (isNeedNS(key, componentDef)) {
                parent
                        .registerMap(namespace + NS_SEP + key, componentDef,
                                this);
            }
        }
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponentDefSize()
     */
    public int getComponentDefSize() {
        return componentDefList.size();
    }

    /**
     * @see org.seasar.framework.container.S2Container#getComponentDef(int)
     */
    public ComponentDef getComponentDef(int index) {
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
        return toComponentDefArray(cd);
    }

    /**
     * @see org.seasar.framework.container.S2Container#findAllComponentDefs(java.lang.Object)
     */
    public ComponentDef[] findAllComponentDefs(final Object componentKey) {
        assertParameterIsNotNull(componentKey, "componentKey");
        final List componentDefs = new ArrayList();
        Traversal.forEachContainer(this, new Traversal.S2ContainerHandler() {
            public Object processContainer(S2Container container) {
                componentDefs.addAll(Arrays.asList(container
                        .findLocalComponentDefs(componentKey)));
                return null;
            }
        });
        return (ComponentDef[]) componentDefs
                .toArray(new ComponentDef[componentDefs.size()]);
    }

    /**
     * @see org.seasar.framework.container.S2Container#findLocalComponentDefs(java.lang.Object)
     */
    public ComponentDef[] findLocalComponentDefs(Object componentKey) {
        ComponentDefHolder holder = (ComponentDefHolder) componentDefMap
                .get(componentKey);
        if (holder == null || holder.getPosition() > 0) {
            return new ComponentDef[0];
        }
        return toComponentDefArray(holder.getComponentDef());
    }

    protected ComponentDef[] toComponentDefArray(ComponentDef cd) {
        if (cd == null) {
            return new ComponentDef[0];
        } else if (cd instanceof TooManyRegistrationComponentDefImpl) {
            return ((TooManyRegistrationComponentDefImpl) cd)
                    .getComponentDefs();
        }
        return new ComponentDef[] { cd };
    }

    protected ComponentDef internalGetComponentDef(Object key) {
        ComponentDefHolder holder = (ComponentDefHolder) componentDefMap
                .get(key);
        if (holder != null) {
            return holder.getComponentDef();
        }
        if (key instanceof String) {
            String name = (String) key;
            int index = name.indexOf(NS_SEP);
            if (index > 0) {
                String ns = name.substring(0, index);
                name = name.substring(index + 1);
                if (ns.equals(namespace)) {
                    return internalGetComponentDef(name);
                }
            }
        }
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
    public boolean hasDescendant(String path) {
        assertParameterIsNotEmpty(path, "path");
        return descendants.containsKey(path);
    }

    public S2Container getDescendant(String path) {
        S2Container descendant = (S2Container) descendants.get(path);
        if (descendant != null) {
            return descendant;
        }
        throw new ContainerNotRegisteredRuntimeException(path);
    }

    public void registerDescendant(S2Container descendant) {
        assertParameterIsNotNull(descendant, "descendant");
        descendants.put(descendant.getPath(), descendant);
    }

    /**
     * @see org.seasar.framework.container.S2Container#include(org.seasar.framework.container.S2Container)
     */
    public void include(S2Container child) {
        assertParameterIsNotNull(child, "child");
        children.add(child);
        childPositions.put(child, new Integer(children.size()));
        child.setRoot(getRoot());
        child.addParent(this);
    }

    protected int getContainerPosition(S2Container container) {
        if (container == this) {
            return 0;
        }
        return ((Integer) childPositions.get(container)).intValue();
    }

    protected boolean isNeedNS(Object key, ComponentDef cd) {
        return key instanceof String && namespace != null;
    }

    public int getChildSize() {
        return children.size();
    }

    public S2Container getChild(int index) {
        return (S2Container) children.get(index);
    }

    public int getParentSize() {
        return parents.size();
    }

    public S2Container getParent(int index) {
        return (S2Container) parents.get(index);
    }

    public void addParent(S2Container parent) {
        parents.add(parent);
        for (Iterator it = componentDefMap.entrySet().iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            Object key = entry.getKey();
            ComponentDefHolder holder = (ComponentDefHolder) entry.getValue();
            ComponentDef cd = holder.getComponentDef();
            parent.registerMap(key, cd, this);
            if (isNeedNS(key, cd)) {
                parent.registerMap(namespace + NS_SEP + key, cd, this);
            }
        }
    }

    public void init() {
        if (inited) {
            return;
        }
        final ExternalContextComponentDefRegister register = getRoot()
                .getExternalContextComponentDefRegister();
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
                    logger.error("ESSR0017", t);
                }
            }
            for (int i = getChildSize() - 1; 0 <= i; --i) {
                getChild(i).destroy();
            }

            componentDefMap = null;
            componentDefList = null;
            namespace = null;
            path = null;
            children = null;
            childPositions = null;
            parents = null;
            descendants = null;
            externalContext = null;
            externalContextComponentDefRegister = null;
            metaDefSupport = null;
            classLoader = null;
            root = this;
            inited = false;
        } finally {
            Thread.currentThread().setContextClassLoader(currentLoader);
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        componentDefMap.remove(namespace);
        this.namespace = namespace;
        registerMap(namespace, new S2ContainerComponentDef(this, namespace));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ExternalContext getExternalContext() {
        return externalContext;
    }

    public void setExternalContext(ExternalContext externalContext) {
        this.externalContext = externalContext;
    }

    public ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
        return externalContextComponentDefRegister;
    }

    public void setExternalContextComponentDefRegister(
            ExternalContextComponentDefRegister register) {
        this.externalContextComponentDefRegister = register;
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

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static ComponentDef createTooManyRegistration(Object key,
            ComponentDef currentComponentDef, ComponentDef newComponentDef) {

        if (currentComponentDef instanceof TooManyRegistrationComponentDef) {
            ((TooManyRegistrationComponentDef) currentComponentDef)
                    .addComponentDef(newComponentDef);
            return currentComponentDef;
        } else {
            TooManyRegistrationComponentDef tmrcf = new TooManyRegistrationComponentDefImpl(
                    key);
            tmrcf.addComponentDef(currentComponentDef);
            tmrcf.addComponentDef(newComponentDef);
            return tmrcf;
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

    static class ComponentDefHolder {
        private int position;

        private ComponentDef componentDef;

        public ComponentDefHolder(int position, ComponentDef componentDef) {
            this.position = position;
            this.componentDef = componentDef;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ComponentDef getComponentDef() {
            return componentDef;
        }

        public void setComponentDef(ComponentDef componentDef) {
            this.componentDef = componentDef;
        }
    }

}