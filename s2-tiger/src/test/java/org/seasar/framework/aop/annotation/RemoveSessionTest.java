/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.annotation;

import javax.servlet.http.HttpSession;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class RemoveSessionTest extends S2FrameworkTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(RemoveSessionTest.class.getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        HttpSession session = getRequest().getSession();
        session.setAttribute("foo", "Foo");
        session.setAttribute("bar", "Bar");

        Hoge hoge = (Hoge) getComponent("hoge");
        hoge.begin();
        assertEquals("Foo", session.getAttribute("foo"));
        assertEquals("Bar", session.getAttribute("bar"));

        hoge.end();
        assertEquals("Foo", session.getAttribute("foo"));
        assertNull(session.getAttribute("bar"));
    }

    /**
     * @throws Exception
     */
    public void testRemove2() throws Exception {
        HttpSession session = getRequest().getSession();
        session.setAttribute("foo", "Foo");
        session.setAttribute("bar", "Bar");

        Hoge hoge = (Hoge) getComponent("hoge");
        hoge.begin();
        assertEquals("Foo", session.getAttribute("foo"));
        assertEquals("Bar", session.getAttribute("bar"));

        hoge.end2();
        assertNull(session.getAttribute("foo"));
        assertNull(session.getAttribute("bar"));
    }

    /**
     *
     */
    @Component
    public static class Hoge {

        /**
         * 
         */
        public void begin() {
        }

        /**
         * 
         */
        @RemoveSession(name = "bar")
        public void end() {
        }

        /**
         * 
         */
        @RemoveSession(name = { "foo", "bar" })
        public void end2() {
        }
    }

}
