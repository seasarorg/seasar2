/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * モック用の{@link MethodInterceptor}です。
 * 
 * @author higa
 * 
 */
public class MockInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 6438214603532050462L;

    private Map returnValueMap = new HashMap();

    private Map throwableMap = new HashMap();

    private Map invokedMap = new HashMap();

    private Map argsMap = new HashMap();

    /**
     * {@link MockInterceptor}を作成します。
     */
    public MockInterceptor() {
    }

    /**
     * {@link MockInterceptor}を作成します。
     * 
     * @param value
     */
    public MockInterceptor(Object value) {
        setReturnValue(value);
    }

    /**
     * デフォルトの戻り値を設定します。
     * 
     * @param returnValue
     */
    public void setReturnValue(Object returnValue) {
        setReturnValue(null, returnValue);
    }

    /**
     * 指定したメソッドに対する戻り値を設定します。
     * 
     * @param methodName
     * @param returnValue
     */
    public void setReturnValue(String methodName, Object returnValue) {
        returnValueMap.put(methodName, returnValue);
    }

    /**
     * デフォルトでスローされる例外を設定します。
     * 
     * @param throwable
     */
    public void setThrowable(Throwable throwable) {
        setThrowable(null, throwable);
    }

    /**
     * 指定したメソッドを呼び出したときに、 スローされる例外を設定します。
     * 
     * @param methodName
     * @param throwable
     */
    public void setThrowable(String methodName, Throwable throwable) {
        throwableMap.put(methodName, throwable);
    }

    /**
     * メソッドが呼び出されたどうかを返します。
     * 
     * @param methodName
     * @return
     */
    public boolean isInvoked(String methodName) {
        return invokedMap.containsKey(methodName);
    }

    /**
     * 呼び出されたメソッドの引数を返します。
     * 
     * @param methodName
     * @return
     */
    public Object[] getArgs(String methodName) {
        return (Object[]) argsMap.get(methodName);
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        invokedMap.put(invocation.getMethod().getName(), Boolean.TRUE);
        argsMap.put(methodName, invocation.getArguments());
        if (throwableMap.containsKey(methodName)) {
            throw (Throwable) throwableMap.get(methodName);
        } else if (throwableMap.containsKey(null)) {
            throw (Throwable) throwableMap.get(null);
        } else if (returnValueMap.containsKey(methodName)) {
            return returnValueMap.get(methodName);
        } else {
            return returnValueMap.get(null);
        }
    }
}