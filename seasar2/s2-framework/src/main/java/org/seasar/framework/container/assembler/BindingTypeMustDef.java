/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalAutoBindingPropertyRuntimeException;
import org.seasar.framework.container.util.BindingUtil;

/**
 * バインディングタイプ定義のmust版です。
 * 
 * @author higa
 * 
 */
public class BindingTypeMustDef extends AbstractBindingTypeDef {

    /**
     * {@link BindingTypeMustDef}を作成します。
     * 
     * @param name
     */
    protected BindingTypeMustDef(String name) {
        super(name);
    }

    protected void doBind(ComponentDef componentDef, Field field,
            Object component) {

        if (!bindAuto(componentDef, field, component)) {
            throw new IllegalAutoBindingPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), field
                    .getName());
        }
    }

    protected void doBind(ComponentDef componentDef, PropertyDesc propertyDesc,
            Object component) {

        if (!bindAuto(componentDef, propertyDesc, component)) {
            throw new IllegalAutoBindingPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), propertyDesc
                    .getPropertyName());
        }
    }
}