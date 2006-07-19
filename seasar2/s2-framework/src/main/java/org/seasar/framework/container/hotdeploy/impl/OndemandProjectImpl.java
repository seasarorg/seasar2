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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandProject;
import org.seasar.framework.container.hotdeploy.OndemandS2Container;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;

public class OndemandProjectImpl implements OndemandProject {

    private String rootPackageName;

    private List ignorePackageNames = new ArrayList();

    private OndemandCreator[] creators = new OndemandCreator[0];

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public List getIgnorePackageNames() {
        return ignorePackageNames;
    }

    public void addIgnorePackageName(String packageName) {
        ignorePackageNames.add(packageName);
    }

    public OndemandCreator[] getCreators() {
        return creators;
    }

    public void setCreators(OndemandCreator[] creators) {
        this.creators = creators;
    }

    public boolean loadComponentDef(OndemandS2Container container, Class clazz) {
        for (int i = 0; i < creators.length; ++i) {
            OndemandCreator creator = creators[i];
            if (creator.loadComponentDef(container, rootPackageName, clazz)) {
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
                    rootPackageName, clazz);
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
                        rootPackageName, componentName);
                if (cd != null) {
                    return cd;
                }
            } catch (ClassNotFoundRuntimeException ignore) {
            }
        }
        return null;
    }

    public int matchClassName(String className) {
        if (rootPackageName != null && !className.startsWith(rootPackageName)) {
            return UNMATCH;
        }
        String base = rootPackageName == null ? "" : rootPackageName + ".";
        for (int i = 0; i < ignorePackageNames.size(); ++i) {
            if (className.startsWith(base + ignorePackageNames.get(i))) {
                return IGNORE;
            }
        }
        return MATCH;
    }
}