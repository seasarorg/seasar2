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

import junit.framework.TestCase;

public class LikeUtilTest extends TestCase {

    public void testMatch() throws Exception {
        assertEquals("1", true, LikeUtil.match("SCOTT", "SCOTT"));
        assertEquals("2", true, LikeUtil.match("_COT_", "SCOTT"));
        assertEquals("3", true, LikeUtil.match("SC%", "SCOTT"));
        assertEquals("4", true, LikeUtil.match("SC%T", "SCOTT"));
        assertEquals("5", true, LikeUtil.match("%TT", "SCOTT"));
        assertEquals("6", true, LikeUtil.match("S_O%T", "SCOTT"));
        assertEquals("7", false, LikeUtil.match("COTT", "SCOTT"));
        assertEquals("8", false, LikeUtil.match("_COT", "SCOTT"));
        assertEquals("9", false, LikeUtil.match("SC%A", "SCOTT"));
        assertEquals("10", false, LikeUtil.match("%OT", "SCOTT"));
        assertEquals("11", true, LikeUtil.match("SCOTT%", "SCOTT"));
        assertEquals("12", false, LikeUtil.match("SCOTT_", "SCOTT"));
        assertEquals("13", true, LikeUtil.match("%SCOTT", "SCOTT"));
        assertEquals("14", true, LikeUtil.match("%SCOTT%", "SCOTT"));
        assertEquals("15", true, LikeUtil.match("S%", "SCOTT"));
        assertEquals("16", true, LikeUtil.match("%abc%abc", "xxxabcyyyabc"));
    }
}