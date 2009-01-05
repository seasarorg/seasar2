/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;

/**
 * メソッドの実行後にHTTPセッションから属性を削除するインターセプタです。
 * 
 * @author koichik
 */
public class RemoveSessionInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    /** このコンポーネントを管理しているS2コンテナです。 */
    protected S2Container container;

    /** HTTPセッションから削除する属性の名前の配列です */
    protected String[] name;

    /**
     * <code>RemoveSessionInterceptor</code>のインスタンスを構築します。
     * 
     * @param container
     *            このコンポーネントを管理しているS2コンテナ
     */
    public RemoveSessionInterceptor(final S2Container container) {
        this.container = container;
    }

    /**
     * HTTPセッションから削除する属性の名前の配列を返します。
     * 
     * @return HTTPセッションから削除する属性の名前の配列
     */
    public String[] getName() {
        return name;
    }

    /**
     * HTTPセッションから削除する属性の名前の配列を設定します。
     * 
     * @param name
     *            HTTPセッションから削除する属性の名前の配列
     */
    public void setName(final String[] name) {
        this.name = name;
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = invocation.proceed();
        removeSession();
        return result;
    }

    /**
     * HTTPセッションから属性を削除します。
     * 
     */
    protected void removeSession() {
        if (name == null || name.length == 0) {
            return;
        }
        final ExternalContext context = container.getRoot()
                .getExternalContext();
        if (context == null) {
            return;
        }
        final HttpServletRequest request = (HttpServletRequest) context
                .getRequest();
        if (request == null) {
            return;
        }
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        for (int i = 0; i < name.length; ++i) {
            session.removeAttribute(name[i]);
        }
    }

}
