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
import java.lang.reflect.Modifier;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyAssembler;

/**
 * @author higa
 * 
 */
public abstract class AbstractPropertyAssembler extends AbstractAssembler
        implements PropertyAssembler {

    public AbstractPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }
    
    protected PropertyDesc getPropertyDesc(BeanDesc beanDesc, String propName, Field field) {
        PropertyDesc propDesc = null;
        if (field == null || beanDesc.hasPropertyDesc(propName)) {
            propDesc = beanDesc.getPropertyDesc(propName);
        }
        return propDesc;
    }
    
    protected Field getField(BeanDesc beanDesc, String propName) {
        Field field = null;
        if (beanDesc.hasField(propName)) {
            field = beanDesc.getField(propName);
            if (!isSettable(field)) {
                field = null;
            }
        }
        return field;
    }
    
    protected boolean isSettable(Field field) {
        int mod = field.getModifiers();
        return !Modifier.isStatic(mod) && !Modifier.isFinal(mod); 
    }
}