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
package org.seasar.framework.container.util;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;

/**
 * @author higa
 *
 */
public final class BindingUtil implements ContainerConstants {

	protected BindingUtil() {
	}

	public static final boolean isAutoBindable(Class clazz) {
		return clazz.isInterface();
	}

	public static final boolean isAutoBindable(Class[] classes) {
		for (int i = 0; i < classes.length; ++i) {
			if (!isAutoBindable(classes[i])) {
				return false;
			}
		}
		return true;
	}
    
    public static BeanDesc getBeanDesc(ComponentDef componentDef, Object component) {
        return BeanDescFactory.getBeanDesc(
            getComponentClass(componentDef, component));
    }
    
    public static Class getComponentClass(ComponentDef componentDef, Object component) {
        Class clazz = componentDef.getComponentClass();
        if (clazz != null) {
            return clazz;
        }
        return component.getClass();
    }
}