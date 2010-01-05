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
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

/**
 * @author taichi
 * 
 */
public class URLUtilTest extends TestCase {

    /**
     * Test method for
     * 'org.seasar.framework.util.URLUtil.disableURLCaches(ClassLoader)'
     * 
     * @throws Exception
     */
    public void testDisableURLCaches() throws Exception {
        String root = ResourceUtil.getBuildDir(getClass()).getCanonicalPath();
        String srcJar = root + "/org/seasar/framework/util/test.jar";
        String destJar = root + "/org/seasar/framework/util/test2.jar";
        File dest = new File(destJar);
        if (dest.exists()) {
            dest.delete();
        }
        dest.createNewFile();
        FileUtil.copy(new File(srcJar), dest);
        new URL("http://a").openConnection().setDefaultUseCaches(true);

        URLUtil.disableURLCaches();

        URL url = new URL("jar:" + dest.toURI().toURL()
                + "!/META-INF/MANIFEST.MF");
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();
        stream.close();

        assertTrue(dest.delete());

    }

    /**
     * @throws Exception
     */
    public void testEncode() throws Exception {
        assertEquals("Program+Files", URLUtil.encode("Program Files", "UTF-8"));
    }

    /**
     * @throws Exception
     */
    public void testDecode() throws Exception {
        assertEquals("Program Files", URLUtil.decode("Program+Files", "UTF-8"));
    }

    /**
     * @throws Exception
     */
    public void testToCanonicalProtocol() throws Exception {
        assertEquals("jar", URLUtil.toCanonicalProtocol("wsjar"));
        assertEquals("jar", URLUtil.toCanonicalProtocol("jar"));
        assertEquals("zip", URLUtil.toCanonicalProtocol("zip"));
        assertEquals("file", URLUtil.toCanonicalProtocol("file"));
    }

    /**
     * @throws Exception
     */
    public void testToFile() throws Exception {
        File file = new File("Program Files/hoge.txt");
        URL url = file.toURL();
        assertEquals(file.getAbsoluteFile(), URLUtil.toFile(url));
        assertEquals(file.getAbsoluteFile(), URLUtil.toFile(new URL(
                "file:Program%20Files/hoge.txt")));
    }

}
