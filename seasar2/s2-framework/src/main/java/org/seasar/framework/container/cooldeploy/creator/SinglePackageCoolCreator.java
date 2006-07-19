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
package org.seasar.framework.container.cooldeploy.creator;

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;

public class SinglePackageCoolCreator extends AbstractCoolCreator {

    private String middlePackageName;

    public SinglePackageCoolCreator(NamingConvention namingConvention) {
        super(namingConvention);
    }

    public String getMiddlePackageName() {
        return middlePackageName;
    }

    public void setMiddlePackageName(String middlePackageName) {
        this.middlePackageName = middlePackageName;
    }

    protected boolean isTargetMiddlePackage(String rootPackageName,
            String className) {
        String s = ClassUtil.concatName(rootPackageName, middlePackageName);
        if (s != null) {
            return className.startsWith(s);
        }
        return true;
    }
}