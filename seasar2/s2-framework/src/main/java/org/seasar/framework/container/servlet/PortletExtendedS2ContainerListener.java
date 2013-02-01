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
package org.seasar.framework.container.servlet;

import javax.servlet.ServletContext;

/**
 * Portlet用に拡張した {@link S2ContainerListener}です。
 * 
 * @author manhole
 * @author shinsuke
 */
public class PortletExtendedS2ContainerListener extends S2ContainerListener {

    protected void initializeContainer(ServletContext servletContext) {
        String configPath = servletContext.getInitParameter(CONFIG_PATH_KEY);
        PortletExtendedSingletonS2ContainerInitializer initializer = new PortletExtendedSingletonS2ContainerInitializer();
        initializer.setConfigPath(configPath);
        initializer.setApplication(servletContext);
        initializer.initialize();
    }

}
