/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.assembler;

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;

/**
 * プロパティアセンブラの自動版です。
 * 
 * @author higa
 * 
 */
public class AutoPropertyAssembler extends AbstractPropertyAssembler {

    /**
     * {@link AutoPropertyAssembler}を作成します。
     * 
     * @param componentDef
     */
    public AutoPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    public void assemble(Object component) {
        if (component == null) {
            return;
        }
        BeanDesc beanDesc = getBeanDesc(component);
        ComponentDef cd = getComponentDef();
        int size = cd.getPropertyDefSize();
        Set names = new HashSet();
        for (int i = 0; i < size; ++i) {
            PropertyDef propDef = cd.getPropertyDef(i);
            AccessTypeDef accessTypeDef = propDef.getAccessTypeDef();
            accessTypeDef.bind(cd, propDef, component);
            String propName = propDef.getPropertyName();
            names.add(propName);
        }
        if (cd.isExternalBinding()) {
            bindExternally(beanDesc, cd, component, names);
        }
        size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; ++i) {
            PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
            String propName = propDesc.getPropertyName();
            if (!names.contains(propName)) {
                BindingTypeDefFactory.getBindingTypeDef(
                        BindingTypeDef.SHOULD_NAME).bind(getComponentDef(),
                        null, propDesc, component);
            }
        }
    }
}