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
package org.seasar.extension.jdbc.gen.command;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author taedium
 * 
 */
public class S2ContainerFactorySupport {

    protected String configPath;

    public S2ContainerFactorySupport(String configPath) {
        this.configPath = configPath;
    }

    protected boolean initialized;

    public void init() {
        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(configPath);
            SingletonS2ContainerFactory.init();
        }
    }

    protected void destory() {
        if (initialized) {
            SingletonS2ContainerFactory.destroy();
        }
    }
}
