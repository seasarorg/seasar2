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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;

/**
 * @author koichik
 * 
 */
public class RemoveSessionInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    protected S2Container container;

    protected String name;

    public RemoveSessionInterceptor(final S2Container container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            removeSession();
        }
    }

    protected void removeSession() {
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
        session.removeAttribute(name);
    }

}
