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
package org.seasar.extension.httpsession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.util.SerializeUtil;

/**
 * @author higa
 * 
 */
public class SessionStateTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAttribute() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        SessionState state = new SessionState(binaryData);
        assertEquals("hoge", state.getAttribute("aaa"));
        Enumeration e = state.getAccessedAttributeNames();
        assertEquals("aaa", e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     */
    public void testSetAttribute() throws Exception {
        Map binaryData = new HashMap();
        SessionState state = new SessionState(binaryData);
        state.setAttribute("aaa", "hoge");
        assertEquals("hoge", state.getAttribute("aaa"));
        Enumeration e = state.getAccessedAttributeNames();
        assertEquals("aaa", e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     */
    public void testGetAttributeNames() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", "111");
        Enumeration e = state.getAttributeNames();
        assertEquals("bbb", e.nextElement());
        assertEquals("aaa", e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     */
    public void testNeedInsert() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", "111");
        assertTrue(state.needInsert("bbb"));
        assertFalse(state.needInsert("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testNeedInsertAndUpdate() throws Exception {
        Map binaryData = new HashMap();
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", "111");
        state.persisted();
        assertFalse(state.needInsert("bbb"));
        state.setAttribute("bbb", "222");
        assertFalse(state.needInsert("bbb"));
        assertTrue(state.needUpdate("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testNeedUpdate() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", "111");
        assertFalse(state.needUpdate("bbb"));
        assertFalse(state.needUpdate("aaa"));
        state.getAttribute("aaa");
        assertTrue(state.needUpdate("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testNeedDelete() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", null);
        assertFalse(state.needDelete("bbb"));
        assertFalse(state.needDelete("aaa"));
        state.setAttribute("aaa", null);
        assertTrue(state.needDelete("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testPersisted() throws Exception {
        byte[] binary = SerializeUtil.fromObjectToBinary("hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", binary);
        binaryData.put("ccc", binary);
        SessionState state = new SessionState(binaryData);
        state.setAttribute("bbb", "111");
        state.setAttribute("ccc", null);
        state.persisted();
        assertFalse(state.needInsert("aaa"));
        assertFalse(state.needUpdate("aaa"));
        assertFalse(state.needDelete("aaa"));
        assertFalse(state.needInsert("bbb"));
        assertFalse(state.needUpdate("bbb"));
        assertFalse(state.needDelete("bbb"));
        assertFalse(state.needInsert("ccc"));
        assertFalse(state.needUpdate("ccc"));
        assertFalse(state.needDelete("ccc"));
        assertSame(binary, binaryData.get("aaa"));
        assertEquals("111", state.getPersistedAttribute("bbb"));
        assertNull(binaryData.get("ccc"));

    }
}
