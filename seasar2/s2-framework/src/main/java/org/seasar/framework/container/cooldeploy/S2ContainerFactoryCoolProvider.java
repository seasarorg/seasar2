/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.cooldeploy;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultProvider;

/**
 * COOL deploy用の
 * {@link org.seasar.framework.container.factory.S2ContainerFactory.Provider}です。
 * 
 * @author koichik
 * 
 */
public class S2ContainerFactoryCoolProvider extends DefaultProvider {

    /**
     * COOL deploy用のdiconファイルのパスです。
     */
    protected static final String DICON_PATH = "cooldeploy-autoregister.dicon";

    public S2Container create(final String path) {
        final S2Container container = super.create(path);
        include(container, DICON_PATH);
        return container;
    }

}
