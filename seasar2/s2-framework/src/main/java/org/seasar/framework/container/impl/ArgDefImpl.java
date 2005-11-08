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

import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.MetaDefSupport;
import org.seasar.framework.util.OgnlUtil;

/**
 * @author higa
 *
 */
public class ArgDefImpl implements ArgDef {

	private Object value_;
	private S2Container container_;
	private String expression_;
	private Object exp_;
	private ComponentDef childComponentDef_;
	private MetaDefSupport metaDefSupport_ = new MetaDefSupport();

	public ArgDefImpl() {
	}
	
	public ArgDefImpl(Object value) {
		setValue(value);
	}

	/**
	 * @see org.seasar.framework.container.ConstructorArgDef#getReturnValue()
	 */
	public final Object getValue() {
		if (exp_ != null) {
			return OgnlUtil.getValue(exp_, container_);
		}
		if (childComponentDef_ != null) {
			return childComponentDef_.getComponent();
		}
		return value_;
	}

	public final void setValue(Object value) {
		value_ = value;
	}
    
    public boolean isValueGettable() {
        return value_ != null || childComponentDef_ != null || exp_ != null;
    }
	
	/**
	 * @see org.seasar.framework.container.ArgDef#getContainer()
	 */
	public final S2Container getContainer() {
		return container_;
	}

	/**
	 * @see org.seasar.framework.container.ArgDef#setContainer(org.seasar.framework.container.S2Container)
	 */
	public final void setContainer(S2Container container) {
		container_ = container;
		if (childComponentDef_ != null) {
			childComponentDef_.setContainer(container);
		}
		metaDefSupport_.setContainer(container);
	}

	/**
	 * @see org.seasar.framework.container.ArgDef#getExpression()
	 */
	public final String getExpression() {
		return expression_;
	}
	/**
	 * @see org.seasar.framework.container.ArgDef#setExpression(java.lang.String)
	 */
	public final void setExpression(String expression) {
		expression_ = expression;
		exp_ = OgnlUtil.parseExpression(expression);
	}

	/**
	 * @see org.seasar.framework.container.ArgDef#setChildComponentDef(org.seasar.framework.container.ComponentDef)
	 */
	public final void setChildComponentDef(ComponentDef componentDef) {
		if (container_ != null) {
			componentDef.setContainer(container_);
		}
		childComponentDef_ = componentDef;
	}
	
	/**
	 * @see org.seasar.framework.container.MetaDefAware#addMetaDef(org.seasar.framework.container.MetaDef)
	 */
	public void addMetaDef(MetaDef metaDef) {
		metaDefSupport_.addMetaDef(metaDef);
	}
	
	/**
	 * @see org.seasar.framework.container.MetaDefAware#getMetaDef(int)
	 */
	public MetaDef getMetaDef(int index) {
		return metaDefSupport_.getMetaDef(index);
	}
	
	/**
	 * @see org.seasar.framework.container.MetaDefAware#getMetaDef(java.lang.String)
	 */
	public MetaDef getMetaDef(String name) {
		return metaDefSupport_.getMetaDef(name);
	}
	
	/**
	 * @see org.seasar.framework.container.MetaDefAware#getMetaDefs(java.lang.String)
	 */
	public MetaDef[] getMetaDefs(String name) {
		return metaDefSupport_.getMetaDefs(name);
	}
	
	/**
	 * @see org.seasar.framework.container.MetaDefAware#getMetaDefSize()
	 */
	public int getMetaDefSize() {
		return metaDefSupport_.getMetaDefSize();
	}
}
