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
package org.seasar.framework.container.hotdeploy.impl;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.autoregister.impl.AbstractAutoRegisterProject;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandProject;
import org.seasar.framework.container.hotdeploy.OndemandS2Container;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;

public class OndemandProjectImpl extends AbstractAutoRegisterProject implements
        OndemandProject {

    private OndemandCreator[] creators = new OndemandCreator[0];

    public OndemandCreator[] getCreators() {
        return creators;
    }

    public void setCreators(OndemandCreator[] creators) {
        this.creators = creators;
    }

    public boolean loadComponentDef(OndemandS2Container container, Class clazz) {
        for (int i = 0; i < creators.length; ++i) {
            OndemandCreator creator = creators[i];
            if (creator
                    .loadComponentDef(container, getRootPackageName(), clazz)) {
                return true;
            }
        }
        return false;
    }

    public ComponentDef getComponentDef(OndemandS2Container container,
            Class clazz) {
        for (int i = 0; i < creators.length; ++i) {
            OndemandCreator creator = creators[i];
            ComponentDef cd = creator.getComponentDef(container,
                    getRootPackageName(), clazz);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    public ComponentDef getComponentDef(OndemandS2Container container,
            String componentName) {
        for (int i = 0; i < creators.length; ++i) {
            OndemandCreator creator = creators[i];
            try {
                ComponentDef cd = creator.getComponentDef(container,
                        getRootPackageName(), componentName);
                if (cd != null) {
                    return cd;
                }
            } catch (ClassNotFoundRuntimeException ignore) {
            }
        }
        return null;
    }

    public String fromComponentNameToClassName(OndemandS2Container container,
            String componentName) {

        if (componentName == null) {
            return null;
        }
        for (int i = 0; i < creators.length; ++i) {
            OndemandCreator creator = creators[i];
            try {
                ComponentDef cd = creator.getComponentDef(container,
                        getRootPackageName(), componentName);
                if (cd != null) {
                    return cd.getComponentClass().getName();
                }
            } catch (ClassNotFoundRuntimeException ex) {
                return ex.getClassName();
            }
        }
        return null;
    }
}