/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

/**
 * {@link PropertyDef}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PropertyDefImpl extends ArgDefImpl implements PropertyDef {

    private String propertyName;

    private BindingTypeDef bindingTypeDef = BindingTypeDefFactory
            .getBindingTypeDef(BindingTypeDef.SHOULD_NAME);

    private AccessTypeDef accessTypeDef = AccessTypeDefFactory.PROPERTY;

    /**
     * {@link PropertyDefImpl}を作成します。
     * 
     * @param propertyName
     */
    public PropertyDefImpl(String propertyName) {
        this(propertyName, null);
    }

    /**
     * {@link PropertyDefImpl}を作成します。
     * 
     * @param propertyName
     * @param value
     */
    public PropertyDefImpl(String propertyName, Object value) {
        super(value);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public AccessTypeDef getAccessTypeDef() {
        return accessTypeDef;
    }

    public void setAccessTypeDef(AccessTypeDef accessTypeDef) {
        this.accessTypeDef = accessTypeDef;
    }

    public BindingTypeDef getBindingTypeDef() {
        return bindingTypeDef;
    }

    public void setBindingTypeDef(BindingTypeDef bindingTypeDef) {
        this.bindingTypeDef = bindingTypeDef;
    }
}