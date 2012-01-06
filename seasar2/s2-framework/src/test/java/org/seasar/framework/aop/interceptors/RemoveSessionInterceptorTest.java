/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import javax.servlet.http.HttpSession;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 * 
 */
public class RemoveSessionInterceptorTest extends S2FrameworkTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include(RemoveSessionInterceptorTest.class.getName().replace('.', '/')
                + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void testSessionExists() throws Exception {
        HttpSession session = getRequest().getSession();
        session.setAttribute("foo", "Foo");
        session.setAttribute("bar", "Bar");
        session.setAttribute("baz", "Baz");

        Hoge hoge = (Hoge) getComponent("hoge");
        hoge.begin();
        assertEquals("Foo", session.getAttribute("foo"));
        assertEquals("Bar", session.getAttribute("bar"));
        assertEquals("Foo", session.getAttribute("foo"));

        try {
            hoge.fail();
            fail();
        } catch (RuntimeException expected) {
        }
        assertEquals("Foo", session.getAttribute("foo"));
        assertEquals("Bar", session.getAttribute("bar"));
        assertEquals("Foo", session.getAttribute("foo"));

        hoge.end();
        assertEquals("Foo", session.getAttribute("foo"));
        assertNull(session.getAttribute("bar"));
        assertEquals("Foo", session.getAttribute("foo"));
    }

    /**
     * @throws Exception
     */
    public void testSessionNotExists() throws Exception {
        Hoge hoge = (Hoge) getComponent("hoge");
        hoge.begin();
        hoge.end();
    }

    /**
     */
    public static class Hoge {
        /** */
        public void begin() {
        }

        /** */
        public void end() {
        }

        /** */
        public void fail() {
            throw new RuntimeException();
        }
    }
}