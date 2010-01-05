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
package org.seasar.framework.container.hotdeploy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.SessionObjectNotSerializableRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class HotdeployHttpSessionTest extends S2FrameworkTestCase {

    ClassLoader originalClassLoader;

    protected void setUp() throws Exception {
        super.setUp();
        originalClassLoader = Thread.currentThread().getContextClassLoader();
    }

    protected void tearDown() throws Exception {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        ClassLoader oldCl = new ChildFirstClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(oldCl);

        Class fooClass = oldCl.loadClass(Foo.class.getName());
        Object oldFoo = fooClass.newInstance();
        assertEquals(Foo.class.getName(), oldFoo.getClass().getName());
        assertEquals(oldCl, oldFoo.getClass().getClassLoader());

        HotdeployHttpSession session = new HotdeployHttpSession(null,
                getRequest().getSession());
        session.setAttribute("foo", oldFoo);
        assertSame(oldFoo, session.getAttribute("foo"));
        session.flush();

        ClassLoader newCl = new ChildFirstClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(newCl);

        session = new HotdeployHttpSession(null, getRequest().getSession());
        Object newFoo = session.getAttribute("foo");
        assertNotSame(oldFoo, newFoo);
        assertSame(newFoo, session.getAttribute("foo"));
        assertEquals(Foo.class.getName(), newFoo.getClass().getName());
        assertEquals(newCl, newFoo.getClass().getClassLoader());
    }

    /**
     * @throws Exception
     */
    public void testNotSerializable() throws Exception {
        HotdeployHttpSession session = new HotdeployHttpSession(null,
                getRequest().getSession());
        try {
            session.setAttribute("bar", new Bar());
            fail();
        } catch (SessionObjectNotSerializableRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    public void testInvalidate() throws Exception {
        HotdeployHttpServletRequest request = new HotdeployHttpServletRequest(
                getRequest());
        HttpSession s1 = request.getSession();
        s1.setAttribute("a", "1");
        s1.invalidate();
        try {
            s1.getAttribute("a");
            fail();
        } catch (IllegalStateException expected) {
        }

        HttpSession s2 = request.getSession();
        assertNotSame(s1, s2);
    }

    /**
     * @author koichik
     */
    public static class ChildFirstClassLoader extends ClassLoader {

        /**
         * @param parent
         */
        public ChildFirstClassLoader(ClassLoader parent) {
            super(parent);
        }

        protected synchronized Class loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            if (!name.startsWith(HotdeployHttpSession.class.getName())
                    && !name.equals(HotdeployUtil.REBUILDER_CLASS_NAME)) {
                return super.loadClass(name, resolve);
            }

            InputStream is = getParent().getResourceAsStream(
                    name.replace('.', '/') + ".class");
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                Class clazz = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }

    }

    /**
     * @author koichik
     */
    public static class Foo implements Serializable {

        /** */
        private static final long serialVersionUID = 1L;

    }

    /**
     * @author koichik
     */
    public static class Bar {

    }

}
