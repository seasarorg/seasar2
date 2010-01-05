/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

import junit.framework.TestCase;

import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * @author taedium
 * 
 */
public class ClassTraversalTest extends TestCase {

    private static int count = 0;

    protected void setUp() throws Exception {
        count = 0;
    }

    /**
     * @throws Exception
     */
    public void testForEachJarFile() throws Exception {
        final String classFilePath = TestCase.class.getName().replace('.', '/')
                + ".class";
        final URL classURL = ResourceUtil.getResource(classFilePath);
        final JarURLConnection con = (JarURLConnection) classURL
                .openConnection();
        ClassTraversal.forEach(con.getJarFile(), new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                if (count < 10) {
                    System.out.println(ClassUtil.concatName(packageName,
                            shortClassName));
                }
                assertNotNull(packageName);
                assertNotNull(shortClassName);
                assertTrue(packageName.startsWith("junit"));
                count++;
            }
        });
        assertTrue(count > 0);
    }

    /**
     * @throws Exception
     */
    public void testForEachJarFile_withPrefix() throws Exception {
        final String classFilePath = TestCase.class.getName().replace('.', '/')
                + ".class";
        final URL classURL = ResourceUtil.getResource(classFilePath);
        final JarURLConnection con = (JarURLConnection) classURL
                .openConnection();
        ClassTraversal.forEach(con.getJarFile(), "junit/", new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                if (count < 10) {
                    System.out.println(ClassUtil.concatName(packageName,
                            shortClassName));
                }
                assertNotNull(packageName);
                assertNotNull(shortClassName);
                assertFalse(packageName.startsWith("junit"));
                count++;
            }
        });
        assertTrue(count > 0);
    }

    /**
     * @throws Exception
     */
    public void testForEachZipInputStream() throws Exception {
        String classFilePath = TestCase.class.getName().replace('.', '/')
                + ".class";
        URL classURL = ResourceUtil.getResource(classFilePath);
        URL jarURL = new File(JarFileUtil.toJarFilePath(classURL)).toURL();
        ClassTraversal.forEach(new ZipInputStream(jarURL.openStream()),
                new ClassHandler() {
                    public void processClass(String packageName,
                            String shortClassName) {
                        if (count < 10) {
                            System.out.println(ClassUtil.concatName(
                                    packageName, shortClassName));
                        }
                        assertNotNull(packageName);
                        assertNotNull(shortClassName);
                        assertTrue(packageName.startsWith("junit"));
                        count++;
                    }
                });
        assertTrue(count > 0);
    }

    /**
     * @throws Exception
     */
    public void testForEachZipInputStream_withPrefix() throws Exception {
        final String classFilePath = TestCase.class.getName().replace('.', '/')
                + ".class";
        final URL classURL = ResourceUtil.getResource(classFilePath);
        URL jarURL = new File(JarFileUtil.toJarFilePath(classURL)).toURL();
        ClassTraversal.forEach(new ZipInputStream(jarURL.openStream()),
                "junit/",
                new ClassHandler() {
                    public void processClass(String packageName,
                            String shortClassName) {
                        if (count < 10) {
                            System.out.println(ClassUtil.concatName(
                                    packageName, shortClassName));
                        }
                        assertNotNull(packageName);
                        assertNotNull(shortClassName);
                        assertFalse(packageName.startsWith("junit"));
                        count++;
                    }
                });
        assertTrue(count > 0);
    }

}
