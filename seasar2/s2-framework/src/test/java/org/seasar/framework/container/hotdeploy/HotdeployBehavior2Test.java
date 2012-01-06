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
package org.seasar.framework.container.hotdeploy;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.creator.ActionCreator;
import org.seasar.framework.container.creator.ConverterCreator;
import org.seasar.framework.container.creator.DaoCreator;
import org.seasar.framework.container.creator.DtoCreator;
import org.seasar.framework.container.creator.DxoCreator;
import org.seasar.framework.container.creator.HelperCreator;
import org.seasar.framework.container.creator.InterceptorCreator;
import org.seasar.framework.container.creator.LogicCreator;
import org.seasar.framework.container.creator.PageCreator;
import org.seasar.framework.container.creator.ServiceCreator;
import org.seasar.framework.container.creator.ValidatorCreator;
import org.seasar.framework.container.hotdeploy.creator.web.aaa.HogePage;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class HotdeployBehavior2Test extends S2FrameworkTestCase {

    private ClassLoader originalLoader;

    private HotdeployBehavior ondemand;

    private String rootPackageName = ClassUtil.getPackageName(getClass())
            + ".creator";

    protected void setUp() {
        register(HogePage.class);
        originalLoader = Thread.currentThread().getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(rootPackageName);
        ondemand = new HotdeployBehavior();
        ondemand.setNamingConvention(convention);
        ondemand.setCreators(new ComponentCreator[] {

        new ConverterCreator(convention), new DaoCreator(convention),
                new DtoCreator(convention), new DxoCreator(convention),
                new HelperCreator(convention),
                new InterceptorCreator(convention),
                new LogicCreator(convention), new ServiceCreator(convention),
                new ValidatorCreator(convention),
                new ActionCreator(convention), new PageCreator(convention) });
        S2ContainerBehavior.setProvider(ondemand);
        ondemand.start();
    }

    protected void tearDown() {
        ondemand.stop();
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    /**
     * @throws Exception
     */
    public void testCreateComponentDef() throws Exception {
        Class clazz = ClassUtil
                .forName(rootPackageName + ".web.aaa.HogeAction");
        assertNotNull(ondemand
                .acquireFromGetComponentDef(getContainer(), clazz));
    }

    /**
     * @throws Exception
     */
    public void testHasComponentDef() throws Exception {
        assertTrue(getContainer().hasComponentDef("aaa_hogeAction"));
        assertFalse(getContainer().hasComponentDef("xxx"));
    }

    /**
     * @throws Exception
     */
    public void testGetComponentDefFromCache() throws Exception {
        Class clazz = ClassUtil
                .forName(rootPackageName + ".web.aaa.HogeAction");
        ComponentDef cd = ondemand.acquireFromGetComponentDef(getContainer(),
                clazz);
        assertSame(cd, ondemand.acquireFromGetComponentDef(getContainer(),
                clazz));
    }

    /**
     * @throws Exception
     */
    public void testGetComponent() throws Exception {
        Object o = getComponent("aaa_hogeAction");
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(o.getClass());
        PropertyDesc pd = beanDesc.getPropertyDesc("hogePage");
        assertEquals(HogePage.class, pd.getPropertyType());
    }

    /**
     * @throws Exception
     */
    public void testGetComponent2() throws Exception {
        try {
            getComponent("aaa_HogeAction");
            fail();
        } catch (ComponentNotFoundRuntimeException e) {
            assertEquals("aaa_HogeAction", e.getComponentKey());
        }
    }
}