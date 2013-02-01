/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import java.util.Date;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.InternalTestContext;

/**
 * @author taedium
 * 
 */
public class SimpleInternalTestContextTest extends S2TestCase {

    private InternalTestContext testCtx;

    @Override
    protected void setUp() throws Exception {
        include("SimpleInternalTestContextTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testInclude() throws Exception {
        testCtx.setTestClass(Bbb.class);
        testCtx.include("SimpleInternalTestContextTest2.dicon");

        assertEquals("aaa", getComponent("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testRegisterComponent() throws Exception {
        testCtx.setTestClass(Bbb.class);
        Aaa aaa = new Aaa();
        testCtx.register(aaa);
        Date date = new Date(0);
        testCtx.register(date, "hoge");

        assertEquals(aaa, getComponent(Aaa.class));
        assertEquals(date, getComponent("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testRegisterComponentClass() throws Exception {
        testCtx.setTestClass(Bbb.class);
        testCtx.setNamingConvention(new NamingConventionImpl());
        testCtx.register(Aaa.class);
        testCtx.register(Date.class, "hoge");

        assertNotNull(getComponent(Aaa.class));
        assertNotNull(getComponentDef((Aaa.class)).getComponentName());
        assertNotNull(getComponent("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testRegisterComponentDef() throws Exception {
        testCtx.setTestClass(Bbb.class);
        ComponentDef componentDef = new ComponentDefImpl(Aaa.class);
        testCtx.register(componentDef);

        assertNotNull(getComponent(Aaa.class));
    }

    /**
     * 
     */
    public static class Aaa {
    }

    /**
     * 
     */
    public static class Bbb {
    }
}
