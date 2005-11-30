/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

/**
 * @author higa
 *
 */
public class PropertyDefImpl extends ArgDefImpl implements PropertyDef {

	private String propertyName;
    
    private BindingTypeDef bindingTypeDef = BindingTypeDefFactory.SHOULD;
	
	public PropertyDefImpl(String propertyName) {
		this(propertyName, null);
	}
		
	public PropertyDefImpl(String propertyName, Object value) {
		super(value);
		this.propertyName = propertyName;
	}

	/*
	 * @see org.seasar.framework.container.PropertyDef#getPropertyName()
	 */
	public String getPropertyName() {
		return propertyName;
	}
    
    /*
     * @see org.seasar.framework.container.PropertyDef#getBindingTypeDef()
     */
    public BindingTypeDef getBindingTypeDef() {
        return bindingTypeDef;
    }
    
    public void setBindingTypeDef(BindingTypeDef bindingTypeDef) {
        this.bindingTypeDef = bindingTypeDef;
    }
}