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
package org.seasar.framework.aop.interceptors;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author higa
 *  
 */
public class MockInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 6438214603532050462L;

	private Map returnValueMap_ = new HashMap();

	private Map throwableMap_ = new HashMap();

	private Map invokedMap_ = new HashMap();

	private Map argsMap_ = new HashMap();

	public MockInterceptor() {
	}
	
	public MockInterceptor(Object value) {
		setReturnValue(value);
	}

	public void setReturnValue(Object returnValue) {
		setReturnValue(null, returnValue);
	}
	
	public void setReturnValue(String methodName, Object returnValue) {
		returnValueMap_.put(methodName, returnValue);
	}

	public void setThrowable(Throwable throwable) {
		setThrowable(null, throwable);
	}
	
	public void setThrowable(String methodName, Throwable throwable) {
		throwableMap_.put(methodName, throwable);
	}

	public boolean isInvoked(String methodName) {
		return invokedMap_.containsKey(methodName);
	}

	public Object[] getArgs(String methodName) {
		return (Object[]) argsMap_.get(methodName);
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName(); 
		invokedMap_.put(invocation.getMethod().getName(), Boolean.TRUE);
		argsMap_.put(methodName, invocation
				.getArguments());
		if (throwableMap_.containsKey(methodName)) {
			throw (Throwable) throwableMap_.get(methodName);
		} else if (throwableMap_.containsKey(null)) {
			throw (Throwable) throwableMap_.get(null);
		} else if (returnValueMap_.containsKey(methodName)) {
			return returnValueMap_.get(methodName);
		} else {
			return returnValueMap_.get(null);
		}
	}
}