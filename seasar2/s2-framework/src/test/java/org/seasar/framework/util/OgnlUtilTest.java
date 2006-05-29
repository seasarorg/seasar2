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
package org.seasar.framework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ognl.ClassResolver;
import ognl.OgnlContext;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author YOKOTA Takehiko
 */
public class OgnlUtilTest extends TestCase {
    public void testAddClassResolverIfNecessary() {
        S2Container container = new S2ContainerImpl() {
            public ClassLoader getClassLoader() {
                return null;
            }
        };
        Map ctx = OgnlUtil.addClassResolverIfNecessary(null, container);
        assertNull("1", ctx);

        Map origCtx = new HashMap();
        ctx = OgnlUtil.addClassResolverIfNecessary(origCtx, container);
        assertSame("2", origCtx, ctx);

        container = new S2ContainerImpl() {
            public ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        };
        ctx = OgnlUtil.addClassResolverIfNecessary(null, container);
        assertNotNull("3", ctx);
        assertTrue("4", ctx instanceof OgnlContext);
        OgnlContext octx = (OgnlContext) ctx;
        assertNotNull("5", octx.getClassResolver());

        origCtx = new HashMap();
        origCtx.put("a", "A");
        ctx = OgnlUtil.addClassResolverIfNecessary(origCtx, container);
        assertNotNull("6", ctx);
        assertTrue("7", ctx instanceof OgnlContext);
        octx = (OgnlContext) ctx;
        assertNotNull("8", octx.getClassResolver());
        assertEquals("9", "A", origCtx.get("a"));
    }

    public void testClassResolverImpl() throws Exception {
        ClassResolver resolver = new OgnlUtil.ClassResolverImpl(getClass()
                .getClassLoader());
        Class clazz = resolver.classForName(
                "org.seasar.framework.container.impl.S2ContainerImpl", null);
        assertSame("1", S2ContainerImpl.class, clazz);
        try {
            clazz = resolver.classForName("Integer", null);
        } catch (ClassNotFoundException ex) {
            fail("2");
        }
        assertNotNull("3", clazz);
    }
}
