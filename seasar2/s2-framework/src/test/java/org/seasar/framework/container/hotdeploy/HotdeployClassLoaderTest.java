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
package org.seasar.framework.container.hotdeploy;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class HotdeployClassLoaderTest extends TestCase {

    private static String PACKAGE_NAME = HotdeployTestMain.class.getPackage()
            .getName()
            + ".sub";

    private static String AAA_NAME = PACKAGE_NAME + ".Aaa";

    private ClassLoader originalLoader;

    private HotdeployClassLoader hotLoader;

    protected void setUp() {
        originalLoader = Thread.currentThread().getContextClassLoader();
        hotLoader = new HotdeployClassLoader(originalLoader);
        hotLoader.setPackageName(PACKAGE_NAME);
        Thread.currentThread().setContextClassLoader(hotLoader);
    }

    protected void tearDown() {
        Thread.currentThread().setContextClassLoader(originalLoader);
    }

    public void testLoadClass() throws Exception {
        assertSame("1", hotLoader.loadClass(AAA_NAME), hotLoader
                .loadClass(AAA_NAME));
        try {
            hotLoader.loadClass(PACKAGE_NAME + ".xxx");
            fail("2");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }
}