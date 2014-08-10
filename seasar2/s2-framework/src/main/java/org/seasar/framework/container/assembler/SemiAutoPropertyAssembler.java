/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;

/**
 * セミオートでプロパティのバインディングを行うアセンブラです。
 * <p>
 * セミオートアセンブラは明示的に{@link org.seasar.framework.container.PropertyDef}の
 * 設定されたプロパティまたはフィールドのみをバインディングの対象とします。<br>
 * バインディングは{@link org.seasar.framework.container.BindingTypeShouldDef}
 * によって行われます。
 * </p>
 * <p>
 * バインディング対象となるプロパティは{@link org.seasar.framework.container.assembler.ManualOnlyPropertyAssembler}と
 * 同じですが、バインディングの方法が異なります。
 * </p>
 * 
 * @author higa
 */
public class SemiAutoPropertyAssembler extends AbstractPropertyAssembler {

    /**
     * {@link SemiAutoPropertyAssembler}を作成します。
     * 
     * @param componentDef
     */
    public SemiAutoPropertyAssembler(ComponentDef componentDef) {
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
            propDef.getAccessTypeDef().bind(
                    getComponentDef(),
                    propDef,
                    BindingTypeDefFactory
                            .getBindingTypeDef(BindingTypeDef.SHOULD_NAME),
                    component);
            names.add(propDef.getPropertyName());
        }
        if (cd.isExternalBinding()) {
            bindExternally(getBeanDesc(component), cd, component, names);
        }
    }
}
