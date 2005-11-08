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
package org.seasar.framework.beans;

import java.lang.reflect.Method;

/**
 * @author higa
 *
 */
public interface PropertyDesc {

	public String getPropertyName();

	public Class getPropertyType();

	public Method getReadMethod();

	public void setReadMethod(Method readMethod);
	
	public boolean hasReadMethod();

	public Method getWriteMethod();

	public void setWriteMethod(Method writeMethod);
	
	public boolean hasWriteMethod();

	public Object getValue(Object target);

	public void setValue(Object target, Object value);
	
	public Object convertIfNeed(Object value);
}
