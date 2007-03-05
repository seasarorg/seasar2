/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbsession;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.serializer.Serializer;

/**
 * @author higa
 * 
 */
public class DbSessionStateTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAttribute() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, "hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", baos.toByteArray());
        DbSessionState state = new DbSessionState(binaryData);
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
        DbSessionState state = new DbSessionState(binaryData);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, "hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", baos.toByteArray());
        DbSessionState state = new DbSessionState(binaryData);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, "hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", baos.toByteArray());
        DbSessionState state = new DbSessionState(binaryData);
        state.setAttribute("bbb", "111");
        assertTrue(state.needInsert("bbb"));
        assertFalse(state.needInsert("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testNeedUpdate() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, "hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", baos.toByteArray());
        DbSessionState state = new DbSessionState(binaryData);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, "hoge");
        Map binaryData = new HashMap();
        binaryData.put("aaa", baos.toByteArray());
        DbSessionState state = new DbSessionState(binaryData);
        state.setAttribute("bbb", null);
        assertFalse(state.needDelete("bbb"));
        assertFalse(state.needDelete("aaa"));
        state.setAttribute("aaa", null);
        assertTrue(state.needDelete("aaa"));
    }
}
