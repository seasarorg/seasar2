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

import java.lang.reflect.Field;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.container.ComponentDef;

/**
 * {@link MethodInterceptor}のインスタンス属性(lifecycle)が、 対象となるコンポーネントと異なる場合に使用します。
 * 
 * @author koichik
 * 
 */
public class InterceptorLifecycleAdapter extends AbstractInterceptor {
    private static final long serialVersionUID = 1L;

    /**
     * {@link InterceptorLifecycleAdapter}を作成します。
     */
    public InterceptorLifecycleAdapter() {
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (!(invocation instanceof S2MethodInvocation)) {
            return invocation.proceed();
        }

        final ComponentDef cd = getComponentDef(invocation);
        if (cd == null) {
            return invocation.proceed();
        }

        final Object target = cd.getComponent();
        final Field targetField = invocation.getClass().getDeclaredField(
                "target");
        targetField.setAccessible(true);
        targetField.set(invocation, target);
        return invocation.proceed();
    }

}
