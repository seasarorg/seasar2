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
package org.seasar.framework.container.ognl;

import java.util.Map;

import ognl.ObjectPropertyAccessor;
import ognl.OgnlException;

import org.seasar.framework.container.S2Container;

/**
 * @author higa
 *
 */
public class S2ContainerPropertyAccessor extends ObjectPropertyAccessor {

	public Object getProperty(Map cx, Object target, Object name)
		throws OgnlException {

		S2Container container = (S2Container) target;
		String componentName = name.toString();
		return container.getComponent(componentName);
	}

}
