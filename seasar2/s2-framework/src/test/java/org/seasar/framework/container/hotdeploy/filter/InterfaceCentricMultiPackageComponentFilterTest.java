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
package org.seasar.framework.container.hotdeploy.filter;

import org.seasar.framework.container.hotdeploy.OndemandBehavior;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class InterfaceCentricMultiPackageComponentFilterTest extends S2FrameworkTestCase {
    
    private ClassLoader originalLoader;
    
    private OndemandBehavior ondemand;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        ondemand = new OndemandBehavior();
        ondemand.setRootPackageName(ClassUtil.getPackageName(getClass()));
        InterfaceCentricMultiPackageComponentFilter filter = new InterfaceCentricMultiPackageComponentFilter();
        filter.addMiddlePackageName("web");
        filter.addMiddlePackageName("dxo");
        filter.setNameSuffix("Dxo");
        ondemand.addComponentFilter(filter);
        S2ContainerBehavior.setProvider(ondemand);
        ondemand.start();
    }
    
    protected void tearDown() {
        ondemand.stop();
        S2ContainerBehavior.setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }
    
    public void testIsTargetByComponentName() throws Exception {
        assertTrue("1", getContainer().hasComponentDef("aaa_hogeDxo"));
        assertTrue("2", getContainer().hasComponentDef("bbbDtoDxo"));
        assertNotNull("3", getComponent("bbbDtoDxo"));
    }
    
    public void testIsTargetByClass() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        Class clazz = ClassUtil.forName(packageName + ".web.aaa.HogeDxo");
        Class clazz2 = ClassUtil.forName(packageName + ".dxo.BbbDtoDxo");
        assertTrue("1", getContainer().hasComponentDef(clazz));
        assertTrue("2", getContainer().hasComponentDef(clazz2));
    }
}