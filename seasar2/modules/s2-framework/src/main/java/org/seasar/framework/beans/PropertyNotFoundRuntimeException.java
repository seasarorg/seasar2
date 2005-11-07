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

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author higa
 *
 */
public class PropertyNotFoundRuntimeException
	extends SRuntimeException {

    private static final long serialVersionUID = -5177019197796206774L;

	private Class targetClass_;
	private String propertyName_;

	public PropertyNotFoundRuntimeException(
		Class componentClass,
		String propertyName) {
		super(
			"ESSR0065",
			new Object[] { componentClass.getName(), propertyName});
		targetClass_ = componentClass;
		propertyName_ = propertyName;
	}

	public Class getTargetClass() {
		return targetClass_;
	}
	
	public String getPropertyName() {
		return propertyName_;
	}
}