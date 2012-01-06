/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.log.Logger;

/**
 * バインディングタイプ定義のshould版です。
 * 
 * @author higa
 * 
 */
public class BindingTypeShouldDef extends AbstractBindingTypeDef {

    private static Logger logger = Logger.getLogger(BindingTypeShouldDef.class);

    /**
     * {@link BindingTypeShouldDef}を作成します。
     * 
     * @param name
     */
    protected BindingTypeShouldDef(String name) {
        super(name);
    }

    protected void doBind(ComponentDef componentDef, Field field,
            Object component) {

        if (!bindAuto(componentDef, field, component)
                && BindingUtil.isAutoBindable(field.getType())) {
            logger.log("WSSR0008", new Object[] {
                    BindingUtil.getComponentClass(componentDef, component)
                            .getName(), field.getName() });
        }
    }

    protected void doBind(ComponentDef componentDef, PropertyDesc propertyDesc,
            Object component) {

        if (!bindAuto(componentDef, propertyDesc, component)
                && BindingUtil.isAutoBindable(propertyDesc.getPropertyType())) {
            logger.log("WSSR0008", new Object[] {
                    BindingUtil.getComponentClass(componentDef, component)
                            .getName(), propertyDesc.getPropertyName() });
        }
    }
}
