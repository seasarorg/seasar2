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
package org.seasar.framework.container.hotdeploy;

import junit.framework.TestCase;

import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class OndemandBehavior2Test extends TestCase {
    
    private OndemandBehavior ondemand;

    protected void setUp() {
        String basePath = ClassUtil.getPackageName(getClass()).replace('.', '/') + "/"; 
        S2ContainerFactory.configure(basePath + "hotdeploy.dicon");
        SingletonS2ContainerFactory.setConfigPath(basePath + "app.dicon");
        SingletonS2ContainerFactory.init();
        ondemand = (OndemandBehavior) S2ContainerBehavior.getProvider();
        ondemand.start();
    }
    
    protected void tearDown() {
        ondemand.stop();
        SingletonS2ContainerFactory.destroy();
    }

    public void testAll() throws Exception {
    }
}