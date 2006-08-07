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

import junit.framework.TestCase;

/**
 * @author taichi
 * 
 */
public class FileUtilTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * Test method for 'org.seasar.framework.util.FileUtil.copy(File, File,
     * boolean)'
     */
    public void testCopy() throws Exception {
        String root = ResourceUtil.getBuildDir(getClass()).getCanonicalPath();
        String srcTxt = root + "/org/seasar/framework/util/src.txt";
        File src = new File(srcTxt);
        assertTrue(src.exists());

        String destTxt = root + "/org/seasar/framework/util/dest.txt";
        File dest = new File(destTxt);
        assertFalse(dest.exists());
        dest.createNewFile();
        FileUtil.copy(src, dest);

        assertEquals(TextUtil.readText(src), TextUtil.readText(dest));
        assertTrue(dest.delete());
    }

}
