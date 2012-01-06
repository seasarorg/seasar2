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
package org.seasar.framework.container.customizer;

import java.lang.reflect.Field;
import java.util.List;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.MetaDefImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class CustomizerChainTest extends S2FrameworkTestCase {

    /**
     * @throws Exception
     */
    public void testCustomize() throws Exception {
        ComponentDefImpl cd = new ComponentDefImpl();
        CustomizerChain chain = new CustomizerChain();
        HogeCustomizer customizer = new HogeCustomizer();
        chain.addCustomizer(customizer);
        chain.addCustomizer(customizer);
        chain.customize(cd);
        MetaDef[] mdefs = cd.getMetaDefs("hoge");
        assertEquals(2, mdefs.length);
    }

    /**
     * @throws Exception
     */
    public void testAddAspect() throws Exception {
        Field interceptorName = AspectCustomizer.class
                .getDeclaredField("interceptorNames");
        interceptorName.setAccessible(true);
        Field pointcut = AspectCustomizer.class.getDeclaredField("pointcut");
        pointcut.setAccessible(true);
        Field useLookupAdapter = AspectCustomizer.class
                .getDeclaredField("useLookupAdapter");
        useLookupAdapter.setAccessible(true);

        CustomizerChain chain = new CustomizerChain();
        chain.addAspectCustomizer("fooInterceptor");
        assertEquals(1, chain.getCustomizerSize());
        AspectCustomizer customizer = (AspectCustomizer) chain.getCustomizer(0);
        List interceptorNames = (List) interceptorName.get(customizer);
        assertEquals(1, interceptorNames.size());
        assertEquals("fooInterceptor", interceptorNames.get(0));
        assertNull(pointcut.get(customizer));
        assertNull(pointcut.get(customizer));
        assertFalse(((Boolean) useLookupAdapter.get(customizer)).booleanValue());

        chain.addAspectCustomizer("barInterceptor", "do.*");
        assertEquals(2, chain.getCustomizerSize());
        customizer = (AspectCustomizer) chain.getCustomizer(1);
        interceptorNames = (List) interceptorName.get(customizer);
        assertEquals(1, interceptorNames.size());
        assertEquals("barInterceptor", interceptorNames.get(0));
        assertEquals("do.*", pointcut.get(customizer));
        assertFalse(((Boolean) useLookupAdapter.get(customizer)).booleanValue());

        chain.addAspectCustomizer("bazInterceptor", true);
        assertEquals(3, chain.getCustomizerSize());
        customizer = (AspectCustomizer) chain.getCustomizer(2);
        interceptorNames = (List) interceptorName.get(customizer);
        assertEquals(1, interceptorNames.size());
        assertEquals("bazInterceptor", interceptorNames.get(0));
        assertNull(pointcut.get(customizer));
        assertTrue(((Boolean) useLookupAdapter.get(customizer)).booleanValue());

        chain.addAspectCustomizer("hogeInterceptor", ".*", true);
        assertEquals(4, chain.getCustomizerSize());
        customizer = (AspectCustomizer) chain.getCustomizer(3);
        interceptorNames = (List) interceptorName.get(customizer);
        assertEquals(1, interceptorNames.size());
        assertEquals("hogeInterceptor", interceptorNames.get(0));
        assertEquals(".*", pointcut.get(customizer));
        assertTrue(((Boolean) useLookupAdapter.get(customizer)).booleanValue());
    }

    /**
     * 
     */
    public static class HogeCustomizer implements ComponentCustomizer {

        public void customize(ComponentDef componentDef) {
            MetaDefImpl md = new MetaDefImpl("hoge");
            componentDef.addMetaDef(md);
        }

    }
}