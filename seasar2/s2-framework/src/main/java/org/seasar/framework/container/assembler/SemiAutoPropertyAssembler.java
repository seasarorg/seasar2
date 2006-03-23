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
package org.seasar.framework.container.assembler;

import java.lang.reflect.Field;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;

/**
 * @author higa
 * 
 */
public class SemiAutoPropertyAssembler extends AbstractPropertyAssembler {

    /**
     * @param componentDef
     */
    public SemiAutoPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    public void assemble(Object component) {
        BeanDesc beanDesc = getBeanDesc(component);
        ComponentDef cd = getComponentDef();
        int size = cd.getPropertyDefSize();
        for (int i = 0; i < size; ++i) {
            PropertyDef propDef = cd.getPropertyDef(i);
            String propName = propDef.getPropertyName();
            Field field = getField(beanDesc, propName);
            PropertyDesc propDesc = getPropertyDesc(beanDesc, propName, field);
            BindingTypeDefFactory.SHOULD.bind(getComponentDef(), propDef,
                    propDesc, field, component);
        }
    }
}