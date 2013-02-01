/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.impl.S2ContainerBehavior;

/**
 * HOT deploy用の {@link Filter}です。
 * 
 * @author higa
 * 
 */
public class HotdeployFilter implements Filter {

    private static final String MODE = "mode";

    private static final String AUTO = "auto";

    private static final String MANUAL = "manual";

    private static final String PATH = "/hotdeploy";

    private static final String KEY = HotdeployFilter.class.getName();

    private boolean autoMode = true;

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
        if (HotdeployUtil.isHotdeploy()) {
            doHotdeployFilter((HttpServletRequest) request,
                    (HttpServletResponse) response, chain);
            return;
        }
        chain.doFilter(request, response);
    }

    private void doHotdeployFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String path = request.getPathInfo();
        if (PATH.equals(path)) {
            changeMode(request, response);
            return;
        }

        if (request.getAttribute(KEY) != null) {
            ClassLoader cl = (ClassLoader) request.getAttribute(KEY);
            Thread.currentThread().setContextClassLoader(cl);
            chain.doFilter(request, response);
            return;
        }

        HotdeployBehavior ondemand = (HotdeployBehavior) S2ContainerBehavior
                .getProvider();
        synchronized (HotdeployFilter.class) {
            ondemand.start();
            HotdeployHttpServletRequest hotdeployRequest = new HotdeployHttpServletRequest(
                    (HttpServletRequest) request);
            try {
                request.setAttribute(KEY, Thread.currentThread()
                        .getContextClassLoader());
                chain.doFilter(hotdeployRequest, response);
            } finally {
                HotdeployHttpSession session = (HotdeployHttpSession) hotdeployRequest
                        .getSession(false);
                if (session != null) {
                    session.flush();
                }
                request.removeAttribute(KEY);
                ondemand.stop();
            }
        }
    }

    private void changeMode(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException,
            ServletException {
        synchronized (HotdeployFilter.class) {
            HotdeployBehavior ondemand = (HotdeployBehavior) S2ContainerBehavior
                    .getProvider();
            response.setContentType("text/html");
            final PrintWriter writer = response.getWriter();
            final String mode = request.getParameter(MODE);
            if (AUTO.equals(mode) || MANUAL.equals(mode)) {
                autoMode = AUTO.equals(mode);
                ondemand.setKeep(!autoMode);
                writer.println("<html><body>HOT deploy mode : " + mode
                        + "</body></html>");
            } else {
                writer
                        .println("<html><body>ERROR! Invalid HOT deploy mode : "
                                + mode
                                + "<br>Valid values : 'auto' or 'manual'</body></html>");
            }
        }
    }
}