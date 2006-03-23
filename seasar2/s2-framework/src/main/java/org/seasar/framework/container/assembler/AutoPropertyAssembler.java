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
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;

/**
 * @author higa
 *  
 */
public class AutoPropertyAssembler extends AbstractPropertyAssembler {

	/**
	 * @param componentDef
	 */
	public AutoPropertyAssembler(ComponentDef componentDef) {
		super(componentDef);
	}

	public void assemble(Object component) {
		BeanDesc beanDesc = getBeanDesc(component);
        ComponentDef cd = getComponentDef();
        int size = cd.getPropertyDefSize();
        Set names = new HashSet();
        for (int i = 0; i < size; ++i) {
            PropertyDef propDef = cd.getPropertyDef(i);
            BindingTypeDef bindingTypeDef = propDef.getBindingTypeDef();
            String propName = propDef.getPropertyName();
            Field field = getField(beanDesc, propName);
            PropertyDesc propDesc = getPropertyDesc(beanDesc, propName, field);
            bindingTypeDef.bind(cd, propDef, propDesc, field, component);
            names.add(propName);
        }
		for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
			PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
			String propName = propDesc.getPropertyName();
            if (!names.contains(propName)) {
                Field field = getField(beanDesc, propName);
                BindingTypeDefFactory.SHOULD.bind(cd, null, propDesc, field, component);
            }
		}
	}
}