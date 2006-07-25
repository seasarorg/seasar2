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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.autoregister.AspectCustomizer;
import org.seasar.framework.container.hotdeploy.OndemandBehavior;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.creator.interceptor.HelloInterceptor;
import org.seasar.framework.container.hotdeploy.impl.OndemandProjectImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class DxoOndemandCreatorTest extends S2FrameworkTestCase {

    private ClassLoader originalLoader;

    private OndemandBehavior ondemand;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        OndemandProjectImpl project = new OndemandProjectImpl();
        project.setRootPackageName(ClassUtil.getPackageName(getClass()));
        ondemand = new OndemandBehavior();
        DxoOndemandCreator creator = new DxoOndemandCreator(convention);
        AspectCustomizer aspectCustomizer = new AspectCustomizer();
        aspectCustomizer.setInterceptorName("helloInterceptor");
        register(HelloInterceptor.class, "helloInterceptor");
        creator.setDxoCustomizer(aspectCustomizer);
        project.setCreators(new OndemandCreator[] { creator });
        ondemand.addProject(project);
        S2ContainerBehavior.setProvider(ondemand);
        ondemand.start();
    }

    protected void tearDown() {
        ondemand.stop();
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    public void testIsTargetByComponentName() throws Exception {
        String name = "aaa_hogeDxo";
        ComponentDef cd = getComponentDef(name);
        assertNotNull("1", cd);
        assertEquals("2", name, cd.getComponentName());
        assertTrue("3", getContainer().hasComponentDef("bbbDtoDxo"));
    }

    public void testIsTargetByClass() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        Class clazz = ClassUtil.forName(packageName + ".web.aaa.HogeDxo");
        Class clazz2 = ClassUtil.forName(packageName + ".dxo.BbbDtoDxo");
        assertTrue("1", getContainer().hasComponentDef(clazz));
        assertTrue("2", getContainer().hasComponentDef(clazz2));
    }
}