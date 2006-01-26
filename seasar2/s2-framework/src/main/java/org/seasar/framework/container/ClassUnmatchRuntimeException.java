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
package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author higa
 *
 */
public class ClassUnmatchRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1967770604202235241L;

	private Class componentClass_;
	private Class realComponentClass_;
	
	/**
	 * @param componentKey
	 */
	public ClassUnmatchRuntimeException(
		Class componentClass,
		Class realComponentClass) {
		super("ESSR0069", new Object[] { componentClass.getName(),
			realComponentClass != null ? realComponentClass.getName() : "null"});
		componentClass_ = componentClass;
		realComponentClass_ = realComponentClass;
	}
	
	public Class getComponentClass() {
		return componentClass_;
	}
	
	public Class getRealComponentClass() {
		return realComponentClass_;
	}
}