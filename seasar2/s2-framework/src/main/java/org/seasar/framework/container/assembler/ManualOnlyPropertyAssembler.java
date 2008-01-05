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
package org.seasar.framework.container.assembler;

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;

/**
 * 手動で設定されたものだけを対象にするプロパティアセンブラです。
 * 
 * @author higa
 * 
 */
public class ManualOnlyPropertyAssembler extends AbstractPropertyAssembler {

    /**
     * {@link ManualOnlyPropertyAssembler}を作成します。
     * 
     * @param componentDef
     */
    public ManualOnlyPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    public void assemble(Object component) {
        if (component == null) {
            return;
        }
        ComponentDef cd = getComponentDef();
        Set names = new HashSet();
        int size = cd.getPropertyDefSize();
        for (int i = 0; i < size; ++i) {
            PropertyDef propDef = cd.getPropertyDef(i);
            propDef.getAccessTypeDef().bind(getComponentDef(), propDef,
                    BindingTypeDefFactory.NONE, component);
            names.add(propDef.getPropertyName());
        }
        if (cd.isExternalBinding()) {
            bindExternally(getBeanDesc(component), cd, component, names);
        }
    }
}
