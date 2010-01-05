/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;

/**
 * ThreadSafeな {@link S2Container}の実装です。
 * 
 * @author koichik
 * 
 */
public class ThreadSafeS2ContainerImpl extends S2ContainerImpl {

    public void addMetaDef(final MetaDef metaDef) {
        synchronized (getRoot()) {
            super.addMetaDef(metaDef);
        }
    }

    public void addParent(final S2Container parent) {
        synchronized (getRoot()) {
            super.addParent(parent);
        }
    }

    public void destroy() {
        synchronized (getRoot()) {
            super.destroy();
        }
    }

    public ComponentDef[] findAllComponentDefs(final Object componentKey) {
        synchronized (getRoot()) {
            return super.findAllComponentDefs(componentKey);
        }
    }

    public ComponentDef[] findLocalComponentDefs(final Object componentKey) {
        synchronized (getRoot()) {
            return super.findLocalComponentDefs(componentKey);
        }
    }

    public S2Container getChild(final int index) {
        synchronized (getRoot()) {
            return super.getChild(index);
        }
    }

    public int getChildSize() {
        synchronized (getRoot()) {
            return super.getChildSize();
        }
    }

    public ClassLoader getClassLoader() {
        synchronized (getRoot()) {
            return super.getClassLoader();
        }
    }

    public ComponentDef getComponentDef(final int index) {
        synchronized (getRoot()) {
            return super.getComponentDef(index);
        }
    }

    public int getComponentDefSize() {
        synchronized (getRoot()) {
            return super.getComponentDefSize();
        }
    }

    protected int getContainerPosition(S2Container container) {
        synchronized (getRoot()) {
            return super.getContainerPosition(container);
        }
    }

    public S2Container getDescendant(final String path) {
        synchronized (getRoot()) {
            return super.getDescendant(path);
        }
    }

    public ExternalContext getExternalContext() {
        synchronized (getRoot()) {
            return super.getExternalContext();
        }
    }

    public ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
        synchronized (getRoot()) {
            return super.getExternalContextComponentDefRegister();
        }
    }

    public MetaDef getMetaDef(final int index) {
        synchronized (getRoot()) {
            return super.getMetaDef(index);
        }
    }

    public MetaDef getMetaDef(final String name) {
        synchronized (getRoot()) {
            return super.getMetaDef(name);
        }
    }

    public MetaDef[] getMetaDefs(final String name) {
        synchronized (getRoot()) {
            return super.getMetaDefs(name);
        }
    }

    public int getMetaDefSize() {
        synchronized (getRoot()) {
            return super.getMetaDefSize();
        }
    }

    public String getNamespace() {
        synchronized (getRoot()) {
            return super.getNamespace();
        }
    }

    public S2Container getParent(final int index) {
        synchronized (getRoot()) {
            return super.getParent(index);
        }
    }

    public int getParentSize() {
        synchronized (getRoot()) {
            return super.getParentSize();
        }
    }

    public boolean hasDescendant(final String path) {
        synchronized (getRoot()) {
            return super.hasDescendant(path);
        }
    }

    public void include(final S2Container child) {
        synchronized (getRoot()) {
            super.include(child);
        }
    }

    public void init() {
        synchronized (getRoot()) {
            super.init();
        }
    }

    protected ComponentDef internalGetComponentDef(final Object key) {
        synchronized (getRoot()) {
            return super.internalGetComponentDef(key);
        }
    }

    public void register(final ComponentDef componentDef) {
        synchronized (getRoot()) {
            super.register(componentDef);
        }
    }

    public void register0(final ComponentDef componentDef) {
        synchronized (getRoot()) {
            super.register0(componentDef);
        }
    }

    public void registerDescendant(final S2Container descendant) {
        synchronized (getRoot()) {
            super.registerDescendant(descendant);
        }
    }

    public void registerMap(final Object key, final ComponentDef componentDef,
            final S2Container container) {
        synchronized (getRoot()) {
            super.registerMap(key, componentDef, container);
        }
    }

    public void setClassLoader(final ClassLoader classLoader) {
        synchronized (getRoot()) {
            super.setClassLoader(classLoader);
        }
    }

    public void setExternalContext(final ExternalContext externalContext) {
        synchronized (getRoot()) {
            super.setExternalContext(externalContext);
        }
    }

    public void setExternalContextComponentDefRegister(
            final ExternalContextComponentDefRegister register) {
        synchronized (getRoot()) {
            super.setExternalContextComponentDefRegister(register);
        }
    }

    public void setNamespace(final String namespace) {
        synchronized (getRoot()) {
            super.setNamespace(namespace);
        }
    }

}
