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
package org.seasar.framework.container.hotdeploy;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.HotdeployUtil.Rebuilder;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class HotdeployClassLoaderTest extends TestCase {

    private static String PACKAGE_NAME = ClassUtil
            .getPackageName(HotdeployClassLoaderTest.class)
            + ".sub";

    private static String AAA_NAME = PACKAGE_NAME + ".Aaa";

    private ClassLoader originalLoader;

    private HotdeployClassLoader hotLoader;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(PACKAGE_NAME);
        convention.addRootPackageName("junit.framework");
        convention.addRootPackageName("javassist", false);
        hotLoader = new HotdeployClassLoader(originalLoader, convention);
        Thread.currentThread().setContextClassLoader(hotLoader);
    }

    protected void tearDown() {
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    /**
     * @throws Exception
     */
    public void testLoadClass() throws Exception {
        assertTrue(hotLoader.isTargetClass("junit.framework.TestCase"));
        assertFalse(hotLoader.isTargetClass("javassist.CtClass"));
        
        assertSame(hotLoader.loadClass(AAA_NAME), hotLoader.loadClass(AAA_NAME));

        Class clazz = hotLoader.loadClass("junit.framework.TestCase");
        assertEquals(TestCase.class, clazz);

        Class.forName(Rebuilder.class.getName());
        clazz = hotLoader.loadClass(HotdeployUtil.REBUILDER_CLASS_NAME);
        assertSame(hotLoader, clazz.getClassLoader());
        assertSame(Rebuilder.class, clazz.getInterfaces()[0]);
        assertTrue(Rebuilder.class.isAssignableFrom(clazz));
        Rebuilder rebuilder = (Rebuilder) clazz.newInstance();
        assertNotNull(rebuilder);
        assertSame(clazz, hotLoader
                .loadClass(HotdeployUtil.REBUILDER_CLASS_NAME));

        try {
            hotLoader.loadClass(PACKAGE_NAME + ".xxx");
            fail();
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }
}