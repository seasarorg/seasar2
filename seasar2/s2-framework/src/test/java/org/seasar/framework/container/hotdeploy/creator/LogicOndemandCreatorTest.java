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

import java.lang.reflect.Method;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.autoregister.AspectCustomizer;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.creator.interceptor.HelloInterceptor;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class LogicOndemandCreatorTest extends OndemandCreatorTestCase {

    protected OndemandCreator newOndemandCreator(NamingConvention convention) {
        AspectCustomizer aspectCustomizer = new AspectCustomizer();
        aspectCustomizer.setInterceptorName("helloInterceptor");
        LogicOndemandCreator creator = new LogicOndemandCreator(convention);
        creator.setLogicCustomizer(aspectCustomizer);
        return creator;
    }

    protected void setUp() {
        register(HelloInterceptor.class, "helloInterceptor");
        super.setUp();
    }

    public void testIsTargetByName() throws Exception {
        String name = "cccLogic";
        ComponentDef cd = getComponentDef(name);
        assertNotNull("1", cd);
        assertEquals("2", name, cd.getComponentName());
    }

    public void testIsTargetByClass() throws Exception {
        Class clazz = ClassUtil.forName(ClassUtil.getPackageName(getClass())
                + ".logic.CccLogic");
        assertNotNull("1", getComponent(clazz));
    }

    public void testAspect() throws Exception {
        Object cccLogic = getComponent("cccLogic");
        Method m = cccLogic.getClass().getMethod("greet", null);
        assertEquals("1", "Hello", m.invoke(cccLogic, null));
    }

    public void testGetComponentClassName() throws Exception {
        String name = "cccLogic";
        String className = creator.getComponentClassName(ondemand,
                rootPackageName, name);
        assertNotNull("1", className);
        assertEquals("2", rootPackageName + ".logic.CccLogic", className);
    }
}