/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.creator.DxoCreator;
import org.seasar.framework.container.customizer.AspectCustomizer;
import org.seasar.framework.container.hotdeploy.creator.interceptor.HelloInterceptor;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class DxoHotdeployCreatorTest extends HotdeployCreatorTestCase {

    protected ComponentCreator newOndemandCreator(NamingConvention convention) {
        AspectCustomizer aspectCustomizer = new AspectCustomizer();
        aspectCustomizer.setInterceptorName("helloInterceptor");
        DxoCreator creator = new DxoCreator(convention);
        creator.setDxoCustomizer(aspectCustomizer);
        return creator;
    }

    protected void setUp() {
        register(HelloInterceptor.class, "helloInterceptor");
        super.setUp();
    }

    /**
     * @throws Exception
     */
    public void testIsTargetByComponentName() throws Exception {
        String name = "aaa_hogeDxo";
        ComponentDef cd = getComponentDef(name);
        assertNotNull("1", cd);
        assertEquals("2", name, cd.getComponentName());
        assertTrue("3", getContainer().hasComponentDef("bbbDtoDxo"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetByClass() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        Class clazz = ClassUtil.forName(packageName + ".web.aaa.HogeDxo");
        Class clazz2 = ClassUtil.forName(packageName + ".dxo.BbbDtoDxo");
        assertTrue("1", getContainer().hasComponentDef(clazz));
        assertTrue("2", getContainer().hasComponentDef(clazz2));
    }
}