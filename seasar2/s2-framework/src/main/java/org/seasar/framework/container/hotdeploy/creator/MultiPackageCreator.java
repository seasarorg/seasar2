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

public class MultiPackageCreator extends
        AbstractOndemandCreator {

    private List middlePackageNames = new ArrayList();

    public MultiPackageCreator(NamingConvention namingConvention) {
        super(namingConvention);
    }

    public String[] getMiddlePackageNames(String subsystemPackageName) {
        String[] names = new String[middlePackageNames.size()];
        for (int i = 0; i < middlePackageNames.size(); ++i) {
            names[i] = ClassUtil.concatName(subsystemPackageName, (String) middlePackageNames.get(i));
        }
        return names;
    }

    public void addMiddlePackageName(String middlePackageName) {
        middlePackageNames.add(middlePackageName);
    }

    protected String[] composeClassNames(String subsystemPackageName, String componentName) {
        String prePackageName = null;
        String shortClassName = componentName;
        int pos = componentName.indexOf('_');
        if (pos > 0) {
            prePackageName = componentName.substring(0, pos);
            shortClassName = componentName.substring(pos + 1);
        }
        shortClassName = StringUtil.capitalize(shortClassName);
        String rootPackageName = getNamingConvention().getRootPackageName();
        String[] mpNames = getMiddlePackageNames(subsystemPackageName);
        String[] classNames = new String[mpNames.length];
        for (int i = 0; i < mpNames.length; ++i) {
            StringBuffer sb = new StringBuffer(100);
            concatName(sb, rootPackageName);
            concatName(sb, mpNames[i]);
            concatName(sb, prePackageName);
            concatName(sb, shortClassName);
            classNames[i] = sb.toString();
        }
        return classNames;
    }

    protected boolean isTargetMiddlePackage(String subsystemPackageName, String className) {
        String[] mpNames = getMiddlePackageNames(subsystemPackageName);
        if (mpNames.length == 0) {
            return true;
        }
        for (int i = 0; i < mpNames.length; ++i) {
            if (className.indexOf(mpNames[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    protected Class getTargetClass(String subsystemPackageName,
            String componentName) {
        String[] classNames = composeClassNames(subsystemPackageName,
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