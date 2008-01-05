/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.MethodUtil;

/**
 * prototypeのコンポーネントに呼び出しを転送する{@link MethodInterceptor}です。
 * 
 * @author koichik
 */
public class PrototypeDelegateInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -6138917007687873314L;

    private S2Container container;

    private String targetName;

    private BeanDesc beanDesc;

    private Map methodNameMap = new HashMap();

    /**
     * {@link PrototypeDelegateInterceptor}を作成します。
     * 
     * @param container
     */
    public PrototypeDelegateInterceptor(final S2Container container) {
        this.container = container;
    }

    /**
     * ターゲットのコンポーネント名を返します。
     * 
     * @return ターゲットのコンポーネント名
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * ターゲットのコンポーネント名を設定します。
     * 
     * @param targetName
     */
    public void setTargetName(final String targetName) {
        this.targetName = targetName;
    }

    /**
     * 転送するメソッドの組を追加します。
     * 
     * @param methodName
     * @param targetMethodName
     */
    public void addMethodNameMap(final String methodName,
            final String targetMethodName) {
        methodNameMap.put(methodName, targetMethodName);
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (targetName == null) {
            throw new EmptyRuntimeException("targetName");
        }

        final Method method = invocation.getMethod();
        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        }

        String methodName = method.getName();
        if (methodNameMap.containsKey(methodName)) {
            methodName = (String) methodNameMap.get(methodName);
        }

        final Object target = container.getComponent(targetName);
        if (beanDesc == null) {
            beanDesc = BeanDescFactory.getBeanDesc(target.getClass());
        }

        if (!beanDesc.hasMethod(methodName)) {
            throw new MethodNotFoundRuntimeException(
                    getTargetClass(invocation), methodName, invocation
                            .getArguments());
        }

        return beanDesc.invoke(target, methodName, invocation.getArguments());
    }
}