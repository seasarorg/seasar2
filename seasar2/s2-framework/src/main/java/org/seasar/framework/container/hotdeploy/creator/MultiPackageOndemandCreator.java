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
package org.seasar.framework.container.hotdeploy.creator;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

public class MultiPackageOndemandCreator extends
        AbstractOndemandCreator {

    private List middlePackageNames = new ArrayList();

    public MultiPackageOndemandCreator(NamingConvention namingConvention) {
        super(namingConvention);
    }

    public String[] getPackageNames(String rootPackageName) {
        String[] names = new String[middlePackageNames.size()];
        for (int i = 0; i < middlePackageNames.size(); ++i) {
            names[i] = ClassUtil.concatName(rootPackageName, (String) middlePackageNames.get(i));
        }
        return names;
    }

    public void addMiddlePackageName(String middlePackageName) {
        middlePackageNames.add(middlePackageName);
    }

    protected String[] composeClassNames(String rootPackageName, String componentName) {
        String[] names = StringUtil.split(componentName, "_");
        String shortClassName = StringUtil.capitalize(names[names.length - 1]);
        String[] pNames = getPackageNames(rootPackageName);
        String[] classNames = new String[pNames.length];
        for (int i = 0; i < pNames.length; ++i) {
            StringBuffer sb = new StringBuffer(100);
            concatName(sb, pNames[i]);
            for (int j = 0; j < names.length - 1; ++j) {
                concatName(sb, names[j]);
            }
            concatName(sb, shortClassName);
            classNames[i] = sb.toString();
        }
        return classNames;
    }

    protected boolean isTargetMiddlePackage(String rootPackageName, String className) {
        String[] pNames = getPackageNames(rootPackageName);
        if (pNames.length == 0) {
            return true;
        }
        for (int i = 0; i < pNames.length; ++i) {
            if (className.startsWith(pNames[i])) {
                return true;
            }
        }
        return false;
    }
    
    protected Class getTargetClass(String rootPackageName,
            String componentName) {
        String[] classNames = composeClassNames(rootPackageName,
                componentName);
        for (int i = 0; i < classNames.length; ++i) {
            String className = classNames[i];
            String path = ClassUtil.getResourcePath(className);
            if (ResourceUtil.getResourceAsFileNoException(path) != null) {
                return ClassUtil.forName(className);
            }
        }
        throw new ComponentNotFoundRuntimeException(componentName);
    }
}