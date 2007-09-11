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
package org.seasar.framework.container.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * {@link S2Container}用の {@link Filter}です。
 * 
 * @author higa
 * 
 */
public class S2ContainerFilter implements Filter {

    /**
     * HTTP セッションを無効にする場合のキーです。
     * 
     * @see #invalidateSession(ServletRequest)
     */
    public static final String INVALIDATE_SESSION = "Seasar2-invalidateSession";

    private static final Logger logger = Logger
            .getLogger(S2ContainerFilter.class);

    /**
     * {@link S2ContainerFilter}を作成します。
     */
    public S2ContainerFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        S2Container container = SingletonS2ContainerFactory.getContainer();
        ExternalContext externalContext = container.getExternalContext();
        if (externalContext == null) {
            throw new EmptyRuntimeException("externalContext");
        }
        externalContext.setRequest(request);
        externalContext.setResponse(response);

        try {
            chain.doFilter(request, response);
        } finally {
            externalContext.setRequest(null);
            externalContext.setResponse(null);
            invalidateSession(request);
        }
    }

    /**
     * リクエストの属性に{@link #INVALIDATE_SESSION}が{@link Boolean#TRUE}で設定されていた場合、
     * {@link HttpSession}を破棄します。
     * 
     * @param request
     *            リクエスト
     */
    protected void invalidateSession(final ServletRequest request) {
        final Object invalidateSession = request
                .getAttribute(INVALIDATE_SESSION);
        if (Boolean.TRUE.equals(invalidateSession)) {
            final HttpSession session = ((HttpServletRequest) request)
                    .getSession(false);
            if (session != null) {
                final String id = session.getId();
                session.invalidate();
                if (logger.isDebugEnabled()) {
                    logger.log("DSSR0117", new Object[] { id });
                }
            }
        }
    }

}
