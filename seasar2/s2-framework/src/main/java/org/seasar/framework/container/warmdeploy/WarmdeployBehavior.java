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
package org.seasar.framework.container.warmdeploy;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;

public class WarmdeployBehavior extends DefaultProvider {

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

    protected ComponentDef getComponentDef(S2Container container, Object key) {
        ComponentDef cd = super.getComponentDef(container, key);
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
            SingletonS2ContainerFactory.getContainer().register(cd);
            S2ContainerUtil.putRegisterLog(cd);
            cd.init();
        }
        return cd;
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
}