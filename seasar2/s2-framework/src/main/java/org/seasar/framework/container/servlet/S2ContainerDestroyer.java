/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.DriverManagerUtil;

/**
 * S2コンテナの後処理を行うクラスです。
 * 
 * @author koichik
 * @since 2.4.43
 */
public class S2ContainerDestroyer {

    /**
     * S2コンテナの後処理を行います。
     */
    public static void destroy() {
        SingletonS2ContainerFactory.destroy();
        DriverManagerUtil.deregisterAllDrivers();

        try {
            final Class clazz = Class
                    .forName("org.seasar.extension.timer.TimeoutManager");
            final Method getInstance = clazz.getMethod("getInstance", null);
            final Object instance = getInstance.invoke(null, null);
            final Method stop = clazz.getMethod("stop",
                    new Class[] { long.class });
            stop.invoke(instance, new Object[] { new Integer(1000) });
        } catch (final Throwable ignore) {
        }

        try {
            final Class locatorClass = Class
                    .forName("org.seasar.extension.jdbc.SqlLogRegistryLocator");
            final Method getInstance = locatorClass.getMethod("getInstance",
                    null);
            final Object instance = getInstance.invoke(null, null);
            if (instance != null) {
                final Class registryClass = Class
                        .forName("org.seasar.extension.jdbc.SqlLogRegistry");
                final Method clear = registryClass.getMethod("clear", null);
                clear.invoke(instance, null);
            }
        } catch (final Throwable ignore) {
        }
    }
}
