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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.aop.impl.NestedMethodInvocation;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ArrayUtil;

/**
 * {@link InterceptorChain}を名前で追加できるようにしたものです。
 * 
 * @author koichik
 */
public class InterceptorAdapter extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    /**
     * コンポーネント定義の配列です。
     */
    protected ComponentDef[] interceptorDefs = new ComponentDef[0];

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(final S2Container container) {
        this.container = container;
    }

    /**
     * {@link MethodInterceptor}を名前を通じて追加します。
     * 
     * @param interceptorNames
     */
    public void add(final String interceptorNames) {
        interceptorDefs = (ComponentDef[]) ArrayUtil.add(interceptorDefs,
                container.getComponentDef(interceptorNames));
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final MethodInterceptor[] interceptors = new MethodInterceptor[interceptorDefs.length];
        for (int i = 0; i < interceptors.length; ++i) {
            interceptors[i] = (MethodInterceptor) interceptorDefs[i]
                    .getComponent();
        }
        final MethodInvocation nestInvocation = new NestedMethodInvocation(
                (S2MethodInvocation) invocation, interceptors);
        return nestInvocation.proceed();
    }
}