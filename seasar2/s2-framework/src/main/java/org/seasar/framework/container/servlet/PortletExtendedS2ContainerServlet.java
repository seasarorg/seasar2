/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

/**
 * Portlet用に拡張した {@link S2ContainerServlet}です。
 * 
 * @author shinsuke
 * 
 */
public class PortletExtendedS2ContainerServlet extends S2ContainerServlet {

    private static final long serialVersionUID = -7099205379599391521L;

    /**
     * {@link PortletExtendedS2ContainerServlet}を作成します。
     */
    public PortletExtendedS2ContainerServlet() {
        super();
    }

    protected void initializeContainer(String configPath) {
        PortletExtendedSingletonS2ContainerInitializer initializer = new PortletExtendedSingletonS2ContainerInitializer();
        initializer.setConfigPath(configPath);
        initializer.setApplication(getServletContext());
        initializer.initialize();
    }

}
