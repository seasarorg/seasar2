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
package org.seasar.framework.container.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.log.Logger;

/**
 * {@link S2Container}用の {@link ServletContextListener}です。
 * 
 * @author manhole
 */
public class S2ContainerListener implements ServletContextListener {

    /**
     * 設定パスのキーです。
     */
    public static final String CONFIG_PATH_KEY = "org.seasar.framework.container.configPath";

    private static Logger logger = Logger.getLogger(S2ContainerListener.class);

    /**
     * コンテナを初期化します。
     * 
     * @param servletContext
     */
    protected void initializeContainer(ServletContext servletContext) {
        String configPath = servletContext.getInitParameter(CONFIG_PATH_KEY);
        SingletonS2ContainerInitializer initializer = new SingletonS2ContainerInitializer();
        initializer.setConfigPath(configPath);
        initializer.setApplication(servletContext);
        initializer.initialize();
    }

    public void contextInitialized(ServletContextEvent event) {
        logger.debug("S2Container initialize start");
        ServletContext servletContext = event.getServletContext();
        try {
            initializeContainer(servletContext);
        } catch (RuntimeException e) {
            logger.log(e);
            throw e;
        }
        logger.debug("S2Container initialize end");
    }

    public void contextDestroyed(ServletContextEvent event) {
        S2ContainerDestroyer.destroy();
    }

}
