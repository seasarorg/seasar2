/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.cooldeploy;

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourcesUtil.Resources;

/**
 * {@link NamingConvention}に一致するコンポーネントを自動登録するクラスです。
 * 
 * @author higa
 * 
 */
public class CoolComponentAutoRegister implements ClassHandler {

    /**
     * InitMethodアノテーションの定義です。
     */
    public static final String INIT_METHOD = "registerAll";

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String container_BINDING = "bindingType=must";

    private S2Container container;

    private ComponentCreator[] creators;

    private NamingConvention namingConvention;

    /**
     * 登録されたクラスを保持するためのセットです。
     */
    protected Set registerdClasses = new HashSet();

    /**
     * {@link S2Container}を返します。
     * 
     * @return {@link S2Container}
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    /**
     * {@link ComponentCreator}の配列を返します。
     * 
     * @return {@link ComponentCreator}の配列
     */
    public ComponentCreator[] getCreators() {
        return creators;
    }

    /**
     * {@link ComponentCreator}の配列を設定します。
     * 
     * @param creators
     */
    public void setCreators(final ComponentCreator[] creators) {
        this.creators = creators;
    }

    /**
     * {@link NamingConvention}を返します。
     * 
     * @return {@link NamingConvention}
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * {@link NamingConvention}を設定します。
     * 
     * @param namingConvention
     */
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * 自動登録を行います。
     */
    public void registerAll() {
        try {
            final String[] rootPackageNames = namingConvention
                    .getRootPackageNames();
            if (rootPackageNames != null) {
                for (int i = 0; i < rootPackageNames.length; ++i) {
                    final Resources[] resourcesArray = ResourcesUtil
                            .getResourcesTypes(rootPackageNames[i]);
                    for (int j = 0; j < resourcesArray.length; ++j) {
                        final Resources resources = resourcesArray[j];
                        try {
                            resources.forEach(this);
                        } finally {
                            resources.close();
                        }
                    }
                }
            }
        } finally {
            registerdClasses.clear();
        }
    }

    public void processClass(final String packageName,
            final String shortClassName) {
        if (shortClassName.indexOf('$') != -1) {
            return;
        }
        String className = ClassUtil.concatName(packageName, shortClassName);
        if (!namingConvention.isTargetClassName(className)) {
            return;
        }
        Class clazz = ClassUtil.forName(className);
        if (namingConvention.isSkipClass(clazz)) {
            return;
        }
        if (container.getRoot().hasComponentDef(clazz)) {
            return;
        }
        ComponentDef cd = createComponentDef(clazz);
        if (cd == null) {
            return;
        }
        if (registerdClasses.contains(cd.getComponentClass())) {
            return;
        }
        container.getRoot().register(cd);
        registerdClasses.add(cd.getComponentClass());
        S2ContainerUtil.putRegisterLog(cd);
    }

    /**
     * {@link ComponentDef}を作成します。
     * 
     * @param componentClass
     * @return {@link ComponentDef}
     */
    protected ComponentDef createComponentDef(final Class componentClass) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentClass);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

}