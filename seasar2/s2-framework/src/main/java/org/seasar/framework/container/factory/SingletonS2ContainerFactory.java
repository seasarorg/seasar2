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
package org.seasar.framework.container.factory;

import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;

public final class SingletonS2ContainerFactory {

    private static String configPath = "app.dicon";

    private static ExternalContext externalContext;

    private static ExternalContextComponentDefRegister externalContextComponentDefRegister;

    private static S2Container container;

    private SingletonS2ContainerFactory() {
    }

    public static String getConfigPath() {
        return configPath;
    }

    public static void setConfigPath(String path) {
        configPath = path;
    }

    public static ExternalContext getExternalContext() {
        return externalContext;
    }

    public static void setExternalContext(ExternalContext extCtx) {
        externalContext = extCtx;
    }

    public static ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
        return externalContextComponentDefRegister;
    }

    public static void setExternalContextComponentDefRegister(
            ExternalContextComponentDefRegister extCtxComponentDefRegister) {
        externalContextComponentDefRegister = extCtxComponentDefRegister;
    }

    public static void init() {
        if (container != null) {
            return;
        }
        container = S2ContainerFactory.create(configPath);
        if (container.getExternalContext() == null) {
            if (externalContext != null) {
                container.setExternalContext(externalContext);
            }
        } else if (container.getExternalContext().getApplication() == null
                && externalContext != null) {
            container.getExternalContext().setApplication(
                    externalContext.getApplication());
        }
        if (container.getExternalContextComponentDefRegister() == null
                && externalContextComponentDefRegister != null) {
            container
                    .setExternalContextComponentDefRegister(externalContextComponentDefRegister);
        }
        container.init();
    }

    public static void destroy() {
        if (container == null) {
            return;
        }
        final ClassLoader loader = container.getClassLoader();
        container.destroy();
        container = null;
        S2ContainerFactory.destroy();
        BeanDescFactory.clear();
        Logger.release(loader);
    }

    public static S2Container getContainer() {
        if (container == null) {
            throw new EmptyRuntimeException("S2Container");
        }
        return container;
    }

    public static void setContainer(S2Container c) {
        container = c;
    }

    public static boolean hasContainer() {
        return container != null;
    }
}
