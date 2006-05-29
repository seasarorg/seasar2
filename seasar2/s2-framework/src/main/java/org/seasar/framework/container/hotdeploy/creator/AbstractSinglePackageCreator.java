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

import org.seasar.framework.container.cooldeploy.ConventionNaming;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandCreatorContainer;
import org.seasar.framework.util.StringUtil;

public abstract class AbstractSinglePackageCreator extends
        AbstractOndemandCreator implements OndemandCreator {

    private String middlePackageName;

    public String getMiddlePackageName() {
        return middlePackageName;
    }

    public void setMiddlePackageName(String middlePackageName) {
        this.middlePackageName = middlePackageName;
    }

    protected String composeClassName(String componentName) {
        String prePackageName = null;
        String shortClassName = componentName;
        int pos = componentName.indexOf('_');
        if (pos > 0) {
            prePackageName = componentName.substring(0, pos);
            shortClassName = componentName.substring(pos + 1);
        }
        shortClassName = StringUtil.capitalize(shortClassName);
        String rootPackageName = getRootPackageName();
        StringBuffer sb = new StringBuffer(100);
        concatName(sb, rootPackageName);
        concatName(sb, middlePackageName);
        concatName(sb, prePackageName);
        concatName(sb, shortClassName);
        return sb.toString();
    }

    protected boolean isTargetMiddlePackage(String className) {
        if (middlePackageName != null
                && className.indexOf(middlePackageName) < 0) {
            return false;
        }
        return true;
    }

    protected String composeComponentName(String className) {
        OndemandCreatorContainer con = getOndemandCreatorContainer();
        ConventionNaming naming = con.getConventionNaming();
        return naming.defineName(getRootPackageName(), middlePackageName,
                getNameSuffix(), className);
    }

    protected Class getTargetClass(Class clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    protected Class getTargetClass(String componentName) {
        // TODO Auto-generated method stub
        return null;
    }

}