/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.MethodUtil;

/**
 * HOT deployに対応させるための{@link MethodInterceptor}です。
 * 
 * @author koichik
 */
public class HotAwareDelegateInterceptor implements MethodInterceptor {

    private static final long serialVersionUID = 1L;

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    /**
     * ターゲット名です。
     */
    protected String targetName;

    /**
     * {@link HotAwareDelegateInterceptor}を作成します。
     */
    public HotAwareDelegateInterceptor() {
    }

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container.getRoot();
    }

    /**
     * ターゲット名を設定します。
     * 
     * @param targetName
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (targetName == null) {
            throw new EmptyRuntimeException("targetName");
        }
        Method method = invocation.getMethod();
        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        } else {
            final Object target = container.getComponent(targetName);
            return MethodUtil.invoke(method, target, invocation.getArguments());
        }
    }

}
