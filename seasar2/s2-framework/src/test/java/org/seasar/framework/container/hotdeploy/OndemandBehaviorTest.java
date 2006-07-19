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

import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.hotdeploy.creator.SinglePackageOndemandCreator;
import org.seasar.framework.container.hotdeploy.impl.OndemandProjectImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class OndemandBehaviorTest extends S2FrameworkTestCase {

    private ClassLoader originalLoader;

    private OndemandBehavior ondemand;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        ondemand = new OndemandBehavior();
        SinglePackageOndemandCreator creator = new SinglePackageOndemandCreator(convention);
        OndemandProjectImpl project = new OndemandProjectImpl();
        project.setRootPackageName(ClassUtil.getPackageName(getClass()));
        project.setCreators(new OndemandCreator[] { creator });
        ondemand.addProject(project);
        S2ContainerBehavior.setProvider(ondemand);
    }

    protected void tearDown() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    public void testStartStop() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getClass());
        ondemand.start();
        assertEquals("1", HotdeployClassLoader.class, Thread.currentThread()
                .getContextClassLoader().getClass());
        ondemand.stop();
        assertSame("2", originalLoader, Thread.currentThread()
                .getContextClassLoader());
        assertNotSame("3", beanDesc, BeanDescFactory.getBeanDesc(getClass()));
    }

    public void testCreateComponentDef() throws Exception {
        ondemand.start();
        Class clazz = ClassUtil.forName(ClassUtil.getPackageName(getClass())
                + ".Hoge");
        assertNotNull("1", ondemand.acquireFromGetComponentDef(getContainer(),
                clazz));
        ondemand.stop();
    }

    public void testGetComponentDefFromCache() throws Exception {
        ondemand.start();
        Class clazz = ClassUtil.forName(ClassUtil.getPackageName(getClass())
                + ".Hoge");
        ComponentDef cd = ondemand.acquireFromGetComponentDef(getContainer(),
                clazz);
        assertSame("1", cd, ondemand.acquireFromGetComponentDef(getContainer(),
                clazz));
        ondemand.stop();
    }

    public void testDefinedClass() throws Exception {
        ondemand.start();
        Object o = getComponent("hoge");
        Method m = o.getClass().getMethod("greet", null);
        assertEquals("1", "Hello", m.invoke(o, null));
        ondemand.stop();
    }
}