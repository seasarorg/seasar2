/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpUtilTest {

    /**
     * 
     */
    @Test
    public void testEncode() {
        assertEquals("aaa", DumpUtil.encode("aaa"));
        assertEquals("\"aa\"\"a\"", DumpUtil.encode("aa\"a"));
        assertEquals("\"aa\r\na\"", DumpUtil.encode("aa\r\na"));
        assertEquals("\"aa\ra\"", DumpUtil.encode("aa\ra"));
        assertEquals("\"aa\na\"", DumpUtil.encode("aa\na"));
        assertEquals("\"aa,a\"", DumpUtil.encode("aa,a"));
        assertEquals("", DumpUtil.encode(""));
        assertEquals("", DumpUtil.encode(null));
    }

    /**
     * 
     */
    @Test
    public void testDecode() {
        assertEquals("aa\"a", DumpUtil.decode("\"aa\"\"a\""));
        assertNull(DumpUtil.decode(""));
        assertNull(DumpUtil.decode(null));
    }

}
