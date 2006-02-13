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
package org.seasar.extension.component.impl;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

public class ComponentInvokerImpl implements ComponentInvoker {

	private S2Container container;

	public Object invoke(String componentName, String methodName, Object[] args)
			throws Throwable {

		Object component = container.getRoot().getComponent(componentName);
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(component.getClass());
		try {
			return beanDesc.invoke(component, methodName, args);
		} catch (InvocationTargetRuntimeException e) {
			throw e.getCause();
		}
	}

	public void setContainer(S2Container container) {
		this.container = container;
	}
}