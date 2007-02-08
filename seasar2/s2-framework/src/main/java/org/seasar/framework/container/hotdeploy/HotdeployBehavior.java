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
package org.seasar.framework.container.hotdeploy;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.DisposableUtil;

public class HotdeployBehavior extends DefaultProvider {

    private static final Logger logger = Logger
            .getLogger(HotdeployBehavior.class);

    private ClassLoader originalClassLoader;

    private HotdeployClassLoader hotdeployClassLoader;

    private Map componentDefCache = new HashMap();

    private NamingConvention namingConvention;

    private ComponentCreator[] creators = new ComponentCreator[0];

    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public ComponentCreator[] getCreators() {
        return creators;
    }

    public void setCreators(ComponentCreator[] creators) {
        this.creators = creators;
    }

    public void start() {
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0108", null);
        }
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotdeployClassLoader = new HotdeployClassLoader(originalClassLoader,
                namingConvention);
        Thread.currentThread().setContextClassLoader(hotdeployClassLoader);
        S2ContainerImpl container = (S2ContainerImpl) SingletonS2ContainerFactory
                .getContainer();
        container.setClassLoader(hotdeployClassLoader);
    }

    public void stop() {
        DisposableUtil.dispose();
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        hotdeployClassLoader = null;
        originalClassLoader = null;
        componentDefCache.clear();
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0109", null);
        }
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
            cd = createComponentDef((Class) key);
        } else if (key instanceof String) {
            cd = createComponentDef((String) key);
        } else {
            throw new IllegalArgumentException("key");
        }
        if (cd != null) {
            register(cd);
            S2ContainerUtil.putRegisterLog(cd);
            cd.init();
        }
        return cd;
    }

    protected ComponentDef getComponentDefFromCache(Object key) {
        return (ComponentDef) componentDefCache.get(key);
    }

    protected ComponentDef createComponentDef(Class componentClass) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentClass);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    protected ComponentDef createComponentDef(String componentName) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentName);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    protected void register(ComponentDef componentDef) {
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
        ComponentDef previousCd = (ComponentDef) componentDefCache.get(key);
        if (previousCd == null) {
            componentDefCache.put(key, componentDef);
        } else {
            ComponentDef tmrcd = S2ContainerImpl.createTooManyRegistration(key,
                    previousCd, componentDef);
            componentDefCache.put(key, tmrcd);
        }
    }
}