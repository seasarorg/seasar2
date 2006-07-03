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

import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

public class SinglePackageCreator extends
        AbstractOndemandCreator implements OndemandCreator {

    private String middlePackageName;

    public SinglePackageCreator(NamingConvention namingConvention) {
        super(namingConvention);
    }

    public String getMiddlePackageName() {
        return middlePackageName;
    }

    public void setMiddlePackageName(String middlePackageName) {
        this.middlePackageName = middlePackageName;
    }

    protected String composeClassName(String subsystemPackageName, String componentName) {
        String prePackageName = null;
        String shortClassName = componentName;
        int pos = componentName.indexOf('_');
        if (pos > 0) {
            prePackageName = componentName.substring(0, pos);
            shortClassName = componentName.substring(pos + 1);
        }
        shortClassName = StringUtil.capitalize(shortClassName);
        String rootPackageName = getNamingConvention().getRootPackageName();
        StringBuffer sb = new StringBuffer(100);
        concatName(sb, rootPackageName);
        concatName(sb, subsystemPackageName);
        concatName(sb, middlePackageName);
        concatName(sb, prePackageName);
        concatName(sb, shortClassName);
        return sb.toString();
    }

    protected boolean isTargetMiddlePackage(String subsystemPackageName,
            String className) {
        String s = ClassUtil
                .concatName(subsystemPackageName, middlePackageName);
        if (s != null && className.indexOf(s) < 0) {
            return false;
        }
        return true;
    }
    
    protected Class getTargetClass(String subsystemPackageName, String componentName) {
        String className = composeClassName(subsystemPackageName, componentName);
        return ClassUtil.forName(className);
    }
}