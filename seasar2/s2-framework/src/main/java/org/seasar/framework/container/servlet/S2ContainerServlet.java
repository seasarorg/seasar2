/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.StringUtil;

public class S2ContainerServlet extends HttpServlet {

    private static final long serialVersionUID = 407266935204779128L;

    public static final String CONFIG_PATH_KEY = "configPath";

    public static final String DEBUG_KEY = "debug";

    public static final String COMMAND = "command";

    public static final String RESTART = "restart";

    private static S2ContainerServlet instance;

    private boolean debug;

    public S2ContainerServlet() {
        instance = this;
    }

    public static S2ContainerServlet getInstance() {
        return instance;
    }
    
    public static void clearInstance() {
        instance = null;
    }

    public void init() {
        String configPath = null;
        String debugStr = null;
        ServletConfig servletConfig = getServletConfig();
        if (servletConfig != null) {
            configPath = servletConfig.getInitParameter(CONFIG_PATH_KEY);
            debugStr = servletConfig.getInitParameter(DEBUG_KEY);
        }
        if (!StringUtil.isEmpty(debugStr)) {
            debug = Boolean.valueOf(debugStr).booleanValue();
        }
        SingletonS2ContainerInitializer initializer = new SingletonS2ContainerInitializer();
        initializer.setConfigPath(configPath);
        initializer.setApplication(getServletContext());
        initializer.initialize();
    }

    public void destroy() {
        SingletonS2ContainerFactory.destroy();
    }

    public static S2Container getContainer() {
        return SingletonS2ContainerFactory.getContainer();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String command = request.getParameter(COMMAND);
        if (debug && command != null && RESTART.equalsIgnoreCase(command)) {
            destroy();
            init();
            response.getWriter().write("S2ContainerServlet is restarted.");
        } else {
            response.getWriter().write("S2ContainerServlet is running.");
        }
    }

}
