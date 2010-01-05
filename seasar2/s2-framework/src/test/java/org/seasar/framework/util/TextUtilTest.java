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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class TextUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testReadTextCr() throws Exception {
        assertEquals("1", "aaa\rbbb", TextUtil.readText(getPath("hoge_cr.txt")));
    }

    /**
     * @throws Exception
     */
    public void testReadTextLf() throws Exception {
        assertEquals("1", "aaa\nbbb", TextUtil.readText(getPath("hoge_lf.txt")));
    }

    /**
     * @throws Exception
     */
    public void testReadTextCrLf() throws Exception {
        assertEquals("1", "aaa\r\nbbb", TextUtil
                .readText(getPath("hoge_crlf.txt")));
    }

    /**
     * @throws Exception
     */
    public void testReadUTF8() throws Exception {
        assertEquals("1", "あ", TextUtil.readUTF8(getPath("hoge_utf8.txt")));
    }

    private String getPath(String fileName) {
        return getClass().getName().replace('.', '/').replaceFirst(
                "TextUtilTest", fileName);
    }
}