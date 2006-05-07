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
package org.seasar.framework.container.hotdeploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.cooldeploy.ConventionNaming;
import org.seasar.framework.container.cooldeploy.DefaultConventionNaming;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.exception.EmptyRuntimeException;

public class OndemandBehavior extends DefaultProvider implements
        HotdeployListener, OndemandCreatorContainer {

    private ClassLoader originalClassLoader;

    private HotdeployClassLoader hotdeployClassLoader;

    private List creators = new ArrayList();

    private String rootPackageName;

    private Map componentDefCache = new HashMap();
    
    private ConventionNaming conventionNaming = new DefaultConventionNaming();

    public OndemandCreator getCreator(int index) {
        return (OndemandCreator) creators.get(index);
    }

    public int getCreatorSize() {
        return creators.size();
    }

    public void addCreator(OndemandCreator creator) {
        creators.add(creator);
        creator.setOndemandCreatorContainer(this);
    }

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public ConventionNaming getConventionNaming() {
        return conventionNaming;
    }

    public void setConventionNaming(ConventionNaming conventionNaming) {
        this.conventionNaming = conventionNaming;
    }

    public void start() {
        if (rootPackageName == null) {
            throw new EmptyRuntimeException("rootPackageName");
        }
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotdeployClassLoader = new HotdeployClassLoader(originalClassLoader);
        hotdeployClassLoader.setPackageName(rootPackageName);
        hotdeployClassLoader.addHotdeployListener(this);
        Thread.currentThread().setContextClassLoader(hotdeployClassLoader);
        S2ContainerImpl container = (S2ContainerImpl) SingletonS2ContainerFactory.getContainer();
        container.setClassLoader(hotdeployClassLoader);
    }

    public void stop() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        hotdeployClassLoader = null;
        originalClassLoader = null;
        BeanDescFactory.clear();
        componentDefCache.clear();
    }

    public void definedClass(Class clazz) {
        loadComponentDef(SingletonS2ContainerFactory
                .getContainer(), clazz);
    }
    
    

    public ComponentDef getComponentDef(Class targetClass) {
        return (ComponentDef) componentDefCache.get(targetClass);
    }

    protected ComponentDef getComponentDef(S2Container container, Object key) {
        ComponentDef cd = getComponentDefFromCache(key);
        if (cd != null) {
            return cd;
        }
        cd = super.getComponentDef(container, key);
        if (cd != null) {
            return cd;
        }
        if (key instanceof Class) {
            return getComponentDef(container, (Class) key);
        } else if (key instanceof String) {
            return getComponentDef(container, (String) key);
        } else {
            throw new IllegalArgumentException("key");
        }
    }

    protected ComponentDef getComponentDefFromCache(Object key) {
        return (ComponentDef) componentDefCache.get(key);
    }
    
    protected void loadComponentDef(S2Container container, Class clazz) {
        for (int i = 0; i < getCreatorSize(); ++i) {
            OndemandCreator creator = getCreator(i);
            if (creator.loadComponentDef(container, clazz)) {
                break;
            }
        }
    }

    protected ComponentDef getComponentDef(S2Container container, Class clazz) {
        for (int i = 0; i < getCreatorSize(); ++i) {
            OndemandCreator creator = getCreator(i);
            ComponentDef cd = creator.getComponentDef(container, clazz);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    protected ComponentDef getComponentDef(S2Container container,
            String componentName) {
        for (int i = 0; i < getCreatorSize(); ++i) {
            OndemandCreator creator = getCreator(i);
            ComponentDef cd = creator.getComponentDef(container,
                    componentName);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    public void register(ComponentDef componentDef) {
        componentDef.setContainer(SingletonS2ContainerFactory.getContainer());
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
        if (componentDefCache.put(key, componentDef) != null) {
            throw new IllegalStateException(key.toString());
        }
    }
}
