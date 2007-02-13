/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.aop.interceptors.SimpleTraceInterceptor;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.Greeting;
import org.seasar.framework.container.autoregister.GreetingInterceptor;
import org.seasar.framework.container.customizer.AspectCustomizer.LookupAdaptorInterceptor;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class AspectCustomizerTest extends S2FrameworkTestCase {

    private S2Container child;

    public void setUpCustomize() throws Exception {
        include("AspectCustomizerTest.dicon");
    }

    public void testCustomize() throws Exception {
        ComponentDef cd = child.getComponentDef(Greeting.class);
        assertEquals(3, cd.getAspectDefSize());
        AspectDef ad = cd.getAspectDef(0);
        assertEquals(TraceInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        ad = cd.getAspectDef(1);
        assertEquals(GreetingInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        ad = cd.getAspectDef(2);
        assertEquals(SimpleTraceInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());

        Greeting greeting = (Greeting) cd.getComponent();
        assertNotNull("1", greeting);
        assertEquals("2", "Hello", greeting.greet());
    }

    public void setUpDelegate() throws Exception {
        include("AspectCustomizerTest2.dicon");
    }

    public void testDelegate() throws Exception {
        ComponentDef cd = child.getComponentDef(Greeting.class);
        assertEquals(2, cd.getAspectDefSize());
        AspectDef ad = cd.getAspectDef(0);
        assertEquals(LookupAdaptorInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());

        Greeting greeting = (Greeting) cd.getComponent();
        assertNotNull("1", greeting);
        String result1 = greeting.greet();
        assertNotNull(result1);
        String result2 = greeting.greet();
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

}
