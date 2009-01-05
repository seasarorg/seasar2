/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import junit.framework.TestCase;

import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.unit.UnitClassLoader;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * @author manhole
 */
public class MemoryLeakTest extends TestCase {

    private static volatile int counter;

    final String basePath = ClassUtil.getPackageName(getClass()).replace('.',
            '/')
            + "/";

    /**
     * @throws Exception
     */
    public void testFinalize() throws Exception {
        ClassLoader origin = Thread.currentThread().getContextClassLoader();
        for (int i = 0; i < 5; ++i) {
            UnitClassLoader loader = new TestClassLoader(origin);
            Thread.currentThread().setContextClassLoader(loader);

            SingletonS2ContainerFactory.setConfigPath(basePath
                    + "MemoryLeakTest.dicon");
            SingletonS2ContainerFactory.init();
            SingletonS2ContainerFactory.destroy();

            Thread.currentThread().setContextClassLoader(origin);
            loader = null;
            for (int j = 0; j < 5; ++j) {
                System.gc();
                Thread.sleep(10);
            }
            System.out.println();
        }
        for (int j = 0; j < 5; ++j) {
            System.gc();
            Thread.sleep(10);
        }
        assertEquals(0, counter);
    }

    /**
     * @throws Exception
     */
    public void testFinalizeConfigurationContainer() throws Exception {
        for (int i = 0; i < 2; i++) {
            S2ContainerFactory.configure(basePath
                    + "MemoryLeakTest_configure.dicon");
            SingletonS2ContainerFactory.setConfigPath(basePath
                    + "MemoryLeakTest_app.dicon");
            SingletonS2ContainerFactory.init();
            SingletonS2ContainerFactory.destroy();
            for (int j = 0; j < 5; ++j) {
                System.gc();
                Thread.sleep(10);
            }
            System.out.println();
        }
        testFinalize();
    }

    /**
     *
     */
    public static class TestClassLoader extends UnitClassLoader {

        /**
         * @param parent
         */
        public TestClassLoader(ClassLoader parent) {
            super(parent);
            System.out.println("+++ " + toString());
            counter++;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("--- " + toString());
            counter--;
        }

    }

}
