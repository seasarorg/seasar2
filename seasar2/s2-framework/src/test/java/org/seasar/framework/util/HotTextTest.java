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
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class HotTextTest extends TestCase {

    private static final String PATH = StringUtil.replace(ClassUtil
            .getPackageName(HotTextTest.class), ".", "/")
            + "/HotTextTest.txt";

    /**
     * @throws Exception
     */
    public void testSetFileAndGetValue() throws Exception {
        HotText text = new HotText(PATH);
        assertEquals("あ", text.getValue());
    }

    /**
     * @throws Exception
     */
    public void testSetValueAndGetValue() throws Exception {
        HotText text = new HotText();
        text.setValue("aaa");
        assertEquals("aaa", text.getValue());
        assertFalse(text.isModified());
    }

    /**
     * @throws Exception
     */
    public void testIsModified() throws Exception {
        HotText text = new HotText(PATH);
        assertFalse(text.isModified());
        File file = ResourceUtil.getResourceAsFile(PATH);
        Thread.sleep(500);
        file.setLastModified(new Date().getTime());
        assertTrue(text.isModified());
    }

    /**
     * @throws Exception
     */
    public void xxxtestPerformance() throws Exception {
        HotText text = new HotText(PATH);
        final int count = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            text.isModified();
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            text.updateValueByFile();
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}