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

import java.io.File;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

import junit.framework.TestCase;

import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * @author taedium
 * 
 */
public class ResourceTraversalTest extends TestCase {

    private static int count = 0;

    protected void setUp() throws Exception {
        count = 0;
    }

    public void testForEachFile() throws Exception {
        final File rootDir = ResourceUtil.getBuildDir(getClass());
        final String path = ResourceUtil.getResourcePath(getClass());
        final int pos = path.lastIndexOf("/");
        final String baseDirectory = path.substring(0, pos);
        ResourceTraversal.forEach(rootDir, baseDirectory,
                new ResourceHandler() {
                    public void processResource(String path, InputStream is) {
                        try {
                            if (count < 10) {
                                System.out.println(path);
                            }
                            assertNotNull(path);
                            assertNotNull(is);
                            count++;
                        } finally {
                            InputStreamUtil.close(is);
                        }
                    }
                });
        assertTrue(count > 0);
    }

    public void testForEachJarFile() throws Exception {
        final String classFilePath = TestCase.class.getName().replace('.', '/')
                + ".class";
        final URL classURL = ResourceUtil.getResource(classFilePath);
        final JarURLConnection con = (JarURLConnection) classURL
                .openConnection();
        ResourceTraversal.forEach(con.getJarFile(), new ResourceHandler() {
            public void processResource(String path, InputStream is) {
                try {
                    if (count < 10) {
                        System.out.println(path);
                    }
                    assertNotNull(path);
                    assertNotNull(is);
                    count++;
                } finally {
                    InputStreamUtil.close(is);
                }
            }
        });
        assertTrue(count > 0);
    }

}
