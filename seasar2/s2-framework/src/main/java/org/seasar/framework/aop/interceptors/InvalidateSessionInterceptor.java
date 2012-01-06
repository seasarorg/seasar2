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

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.filter.S2ContainerFilter;

/**
 * メソッドの実行後にHTTPセッションを破棄するインターセプタです。
 * <p>
 * 実際のHTTPセッションの破棄は、{@link S2ContainerFilter}によって行われます。
 * </p>
 * 
 * @author koichik
 * @see S2ContainerFilter
 */
public class InvalidateSessionInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    /** このコンポーネントを管理しているS2コンテナ */
    protected S2Container container;

    /**
     * インスタンスを構築します。
     * 
     * @param container
     *            このコンポーネントを管理しているS2コンテナ
     */
    public InvalidateSessionInterceptor(S2Container container) {
        this.container = container.getRoot();
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = invocation.proceed();
        invalidate();
        return result;
    }

    /**
     * HTTPセッションを破棄します。
     */
    protected void invalidate() {
        final ExternalContext externalContext = container.getExternalContext();
        if (externalContext != null) {
            final Map requestMap = externalContext.getRequestMap();
            requestMap.put(S2ContainerFilter.INVALIDATE_SESSION, Boolean.TRUE);
        }
    }

}
