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
package org.seasar.framework.container.hotdeploy;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seasar.framework.container.impl.S2ContainerBehavior;

/**
 * HOT deploy用の {@link Filter}です。
 * 
 * @author higa
 * 
 */
public class HotdeployFilter implements Filter {

    private static final String KEY = HotdeployFilter.class.getName();

    /**
     * {@link HotdeployFilter}を作成します。
     */
    public HotdeployFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request.getAttribute(KEY) == null) {
            S2ContainerBehavior.Provider provider = S2ContainerBehavior
                    .getProvider();
            if (provider instanceof HotdeployBehavior) {
                synchronized (HotdeployFilter.class) {
                    HotdeployBehavior ondemand = (HotdeployBehavior) provider;
                    ondemand.start();
                    try {
                        request.setAttribute(KEY, Thread.currentThread()
                                .getContextClassLoader());
                        chain.doFilter(request, response);
                    } finally {
                        request.removeAttribute(KEY);
                        ondemand.stop();
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            ClassLoader cl = (ClassLoader) request.getAttribute(KEY);
            Thread.currentThread().setContextClassLoader(cl);
            chain.doFilter(request, response);
        }
    }
}