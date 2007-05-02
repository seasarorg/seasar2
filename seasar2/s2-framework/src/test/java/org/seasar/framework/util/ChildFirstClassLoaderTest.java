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
package org.seasar.framework.util;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;

/**
 * @author taedium
 */
public class ChildFirstClassLoaderTest extends TestCase {

    private String className = getClass().getPackage().getName() + ".Hoge";

    public void testLoadClass() throws Exception {
        ChildFirstClassLoader loader = new ChildFirstClassLoader(Thread
                .currentThread().getContextClassLoader());

        Class clazz = loader.loadClass(className);
        assertSame(loader, clazz.getClassLoader());
        assertSame(clazz, loader.loadClass(className));

        clazz = loader.loadClass("java.lang.Integer");
        assertNotSame(loader, clazz.getClassLoader());

        clazz = loader.loadClass("junit.framework.TestCase");
        assertSame(TestCase.class, clazz);

        try {
            loader.loadClass(getClass().getPackage().getName() + ".xxx");
            fail();
        } catch (ClassNotFoundException ignore) {
        }
    }

    public void testIsIncludedClass() {
        ChildFirstClassLoader loader = new ChildFirstClassLoader(Thread
                .currentThread().getContextClassLoader());
        assertFalse(loader.isIncludedClass("javax.persistence.EntityManager"));
        assertTrue(loader.isIncludedClass("org.seasar.framework.util.Foo"));
        assertTrue(loader.isIncludedClass("org.seasar.framework.util.Bar"));
        assertTrue(loader.isIncludedClass("org.seasar.framework.util.Baz"));

        Set set = new HashSet();
        set.add("org.seasar.framework.util.Foo");
        set.add("org.seasar.framework.util.Bar");
        loader = new ChildFirstClassLoader(Thread.currentThread()
                .getContextClassLoader(), set);
        assertFalse(loader.isIncludedClass("javax.persistence.EntityManager"));
        assertTrue(loader.isIncludedClass("org.seasar.framework.util.Foo"));
        assertTrue(loader.isIncludedClass("org.seasar.framework.util.Bar"));
        assertFalse(loader.isIncludedClass("org.seasar.framework.util.Baz"));
        assertFalse(loader.isIncludedClass(S2Container.class.getName()));
    }

}
