/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.S2Container;

/**
 * {@link S2Container}用の {@link ComponentDef}です。
 * 
 * @author higa
 * 
 */
public class S2ContainerComponentDef extends SimpleComponentDef {

    /**
     * {@link S2ContainerComponentDef}を作成します。
     * 
     * @param container
     * @param name
     */
    public S2ContainerComponentDef(S2Container container, String name) {
        super(container, name);
    }

    public S2Container getContainer() {
        return (S2Container) super.getComponent();
    }

    /**
     * @see org.seasar.framework.container.ComponentDef#getComponent()
     */
    public Object getComponent() {
        return getContainer();
    }

    /**
     * @see org.seasar.framework.container.ComponentDef#init()
     */
    public void init() {
        getContainer().init();
    }

    /**
     * @see org.seasar.framework.container.ComponentDef#destroy()
     */
    public void destroy() {
        getContainer().destroy();
    }
}
