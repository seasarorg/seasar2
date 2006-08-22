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

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

public class SinglePackageOndemandCreator extends AbstractOndemandCreator {

    private String middlePackageName;

    public SinglePackageOndemandCreator(NamingConvention namingConvention) {
        super(namingConvention);
    }

    public String getMiddlePackageName() {
        return middlePackageName;
    }

    public void setMiddlePackageName(String middlePackageName) {
        this.middlePackageName = middlePackageName;
    }

    protected String composeClassName(String rootPackageName,
            String componentName) {
        StringBuffer sb = new StringBuffer(100);
        concatName(sb, rootPackageName);
        concatName(sb, middlePackageName);
        String[] names = StringUtil.split(componentName, "_");
        for (int i = 0; i < names.length - 1; ++i) {
            concatName(sb, names[i]);
        }
        String shortClassName = StringUtil.capitalize(names[names.length - 1]);
        concatName(sb, shortClassName);
        return sb.toString();
    }

    protected boolean isTargetMiddlePackage(String rootPackageName,
            String className) {
        String s = ClassUtil.concatName(rootPackageName, middlePackageName);
        if (s != null) {
            return className.startsWith(s);
        }
        return true;
    }

    protected Class getTargetClass(String subsystemPackageName,
            String componentName) {
        String className = composeClassName(subsystemPackageName, componentName);
        if (className != null) {
            return ClassUtil.forName(className);
        }
        return null;
    }

    protected String getTargetClassName(String rootPackageName,
            String componentName) {
        String className = composeClassName(rootPackageName, componentName);
        if (isExist(className)) {
            return className;
        }
        return null;
    }
}