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
package org.seasar.extension.j2ee;

import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author higa
 *
 */
public final class SingletonTransactionManager
	extends TransactionManagerWrapper {

	private String className_;
	private String methodName_;

	public SingletonTransactionManager(String className, String methodName) {
		className_ = className;
		methodName_ = methodName;
		Class clazz = ClassUtil.forName(className);
		Method method = ClassUtil.getMethod(clazz, methodName, null);
		setPhysicalTransactionManager(
			(TransactionManager) MethodUtil.invoke(method, clazz, null));
	}

	public String getClassName() {
		return className_;
	}

	public String getMethodName() {
		return methodName_;
	}
}
