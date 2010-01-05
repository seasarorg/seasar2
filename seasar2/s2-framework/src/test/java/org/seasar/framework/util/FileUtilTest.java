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

import junit.framework.TestCase;

/**
 * @author taichi
 * 
 */
public class FileUtilTest extends TestCase {

    String root;

    File src;

    File dest;

    protected void setUp() throws Exception {
        super.setUp();
        root = ResourceUtil.getBuildDir(getClass()).getCanonicalPath();
        String srcTxt = root + "/org/seasar/framework/util/src.txt";
        src = new File(srcTxt);
        String destTxt = root + "/org/seasar/framework/util/dest.txt";
        dest = new File(destTxt);
    }

    protected void tearDown() throws Exception {
        if (dest.exists()) {
            dest.delete();
        }
        super.tearDown();
    }

    /**
     * Test method for 'org.seasar.framework.util.FileUtil.copy(File, File,
     * boolean)'
     * 
     * @throws Exception
     */
    public void testCopy_New() throws Exception {
        assertTrue(src.exists());
        assertFalse(dest.exists());

        FileUtil.copy(src, dest);
        assertEquals(TextUtil.readText(src), TextUtil.readText(dest));
    }

    /**
     * Test method for 'org.seasar.framework.util.FileUtil.copy(File, File,
     * boolean)'
     * 
     * @throws Exception
     */
    public void testCopy_Exists() throws Exception {
        assertTrue(src.exists());
        assertFalse(dest.exists());

        dest.createNewFile();
        FileUtil.copy(src, dest);
        assertEquals(TextUtil.readText(src), TextUtil.readText(dest));
    }

}
