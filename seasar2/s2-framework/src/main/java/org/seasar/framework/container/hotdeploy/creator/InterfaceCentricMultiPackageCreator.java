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

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

public class InterfaceCentricMultiPackageCreator extends AbstractMultiPackageCreator {
        
    private String implementationPackageName = "impl";
    
    private String implementationSuffix = "Impl";
    
    public String getImplementationPackageName() {
        return implementationPackageName;
    }

    public void setImplementationPackageName(String implementationPackageName) {
        this.implementationPackageName = implementationPackageName;
    }

    public String getImplementationSuffix() {
        return implementationSuffix;
    }

    public void setImplementationSuffix(String implementationSuffix) {
        this.implementationSuffix = implementationSuffix;
    }
    
    protected Class getTargetClass(Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String packageName = ClassUtil.getPackageName(clazz);
        String targetClassName = packageName + "." + getImplementationPackageName() + "." + ClassUtil.getShortClassName(clazz) + implementationSuffix;
        if (ResourceUtil.getResourceAsFileNoException(ClassUtil.getResourcePath(targetClassName)) != null) {
            return ClassUtil.forName(targetClassName);
        }
        return clazz;
    }

    protected Class getTargetClass(String componentName) {
        String[] classNames = composeClassNames(componentName);
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