/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.external;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.convention.impl.NamingConventionImpl;

/**
 * @author koichik
 */
public class RebuildableExternalContextMapTest extends TestCase {

    ClassLoader originalLoader;

    protected void setUp() throws Exception {
        super.setUp();
        originalLoader = Thread.currentThread().getContextClassLoader();
    }

    protected void tearDown() throws Exception {
        Thread.currentThread().setContextClassLoader(originalLoader);
        HotdeployUtil.clearHotdeploy();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName("org.seasar.framework.external");
        HotdeployUtil.setHotdeploy(true);
        ClassLoader hotLoader = new HotdeployClassLoader(originalLoader,
                convention);
        Thread.currentThread().setContextClassLoader(hotLoader);
        Foo foo = new Foo("hoge");
        new TestMap().put("foo", foo);
        Foo foo2 = (Foo) new TestMap().get("foo");
        assertSame(foo, foo2);
        assertSame(foo2, new TestMap().get("foo"));
        assertEquals("hoge", foo2.name);

        ClassLoader hotLoader2 = new HotdeployClassLoader(originalLoader,
                convention);
        Thread.currentThread().setContextClassLoader(hotLoader2);
        Foo foo3 = (Foo) new TestMap().get("foo");
        assertNotSame(foo, foo3);
        assertNotSame(foo2, foo3);
        assertSame(foo3, new TestMap().get("foo"));
        assertEquals("hoge", foo2.name);
    }

    /**
     * 
     */
    public static class TestMap extends RebuildableExternalContextMap {

        static Map map = new HashMap();

        protected Object getAttribute(String key) {
            return map.get(key);
        }

        protected Iterator getAttributeNames() {
            return map.keySet().iterator();
        }

        protected void removeAttribute(String key) {
            map.remove(key);
        }

        protected void setAttribute(String key, Object value) {
            map.put(key, value);
        }

    }

    /**
     * 
     */
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 1L;

        String name;

        /**
         * @param name
         */
        public Foo(String name) {
            this.name = name;
        }

    }

}
