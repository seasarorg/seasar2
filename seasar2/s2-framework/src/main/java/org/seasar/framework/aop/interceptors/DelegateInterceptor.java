/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.MethodUtil;

/**
 * あるオブジェクトへの呼び出しを別のオブジェクトに転送する{@link MethodInterceptor}です。
 * 
 * @author higa
 * 
 */
public class DelegateInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 3613140488663554089L;

    private Object target;

    private BeanDesc beanDesc;

    private Map methodNameMap = new HashMap();

    /**
     * {@link DelegateInterceptor}を作成します。
     */
    public DelegateInterceptor() {
    }

    /**
     * {@link DelegateInterceptor}を作成します。
     * 
     * @param target
     */
    public DelegateInterceptor(Object target) {
        setTarget(target);
    }

    /**
     * ターゲットのオブジェクトを返します。
     * 
     * @return target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * ターゲットのオブジェクトを設定します。
     * 
     * @param target
     */
    public void setTarget(Object target) {
        this.target = target;
        beanDesc = BeanDescFactory.getBeanDesc(target.getClass());
    }

    /**
     * 転送するメソッドの組を追加します。
     * 
     * @param methodName
     * @param targetMethodName
     */
    public void addMethodNameMap(String methodName, String targetMethodName) {
        methodNameMap.put(methodName, targetMethodName);
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (target == null) {
            throw new EmptyRuntimeException("target");
        }
        Method method = invocation.getMethod();
        String methodName = method.getName();
        if (methodNameMap.containsKey(methodName)) {
            methodName = (String) methodNameMap.get(methodName);
        }
        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        } else if (beanDesc.hasMethod(methodName)) {
            return beanDesc.invoke(target, methodName, invocation
                    .getArguments());
        } else {
            throw new MethodNotFoundRuntimeException(
                    getTargetClass(invocation), methodName, invocation
                            .getArguments());
        }
    }
}