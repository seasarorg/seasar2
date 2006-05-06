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
package org.seasar.framework.container.hotdeploy.creator;

import org.seasar.framework.container.hotdeploy.OndemandBehavior;
import org.seasar.framework.container.hotdeploy.creator.SimpleSinglePackageCreator;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class SimpleSinglePackageCreatorTest extends S2FrameworkTestCase {
    
    private ClassLoader originalLoader;
    
    private OndemandBehavior ondemand;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        ondemand = new OndemandBehavior();
        ondemand.setRootPackageName(ClassUtil.getPackageName(getClass()));
        SimpleSinglePackageCreator creator = new SimpleSinglePackageCreator();
        creator.setMiddlePackageName("web");
        creator.setNameSuffix("Page");
        ondemand.addCreator(creator);
        S2ContainerBehavior.setProvider(ondemand);
        ondemand.start();
    }
    
    protected void tearDown() {
        ondemand.stop();
        S2ContainerBehavior.setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    public void testComposeComponentName() throws Exception {
        assertNotNull("1", getComponent("aaa_hogePage"));
    }
    
    public void testIsTargetByComponentName() throws Exception {
        assertTrue("1", getContainer().hasComponentDef("aaa_hogePage"));
        assertFalse("2", getContainer().hasComponentDef("aaa_xxx"));
    }
    
    public void testIsTargetByClass() throws Exception {
        Class clazz = ClassUtil.forName(ClassUtil.getPackageName(getClass()) + ".web.aaa.HogePage");
        assertTrue("1", getContainer().hasComponentDef(clazz));
        assertFalse("2", getContainer().hasComponentDef(getClass()));
    }
}