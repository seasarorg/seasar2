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
package org.seasar.framework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ognl.ClassResolver;
import ognl.OgnlContext;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.OgnlRuntimeException;

/**
 * @author YOKOTA Takehiko
 * @author manhole
 */
public class OgnlUtilTest extends TestCase {

    /**
     * 
     */
    public void testAddClassResolverIfNecessary() {
        S2Container container = new S2ContainerImpl() {
            public ClassLoader getClassLoader() {
                return null;
            }
        };
        Map ctx = OgnlUtil.addClassResolverIfNecessary(null, container);
        assertNull("1", ctx);

        Map origCtx = new HashMap();
        ctx = OgnlUtil.addClassResolverIfNecessary(origCtx, container);
        assertSame("2", origCtx, ctx);

        container = new S2ContainerImpl() {
            public ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        };
        ctx = OgnlUtil.addClassResolverIfNecessary(null, container);
        assertNotNull("3", ctx);
        assertTrue("4", ctx instanceof OgnlContext);
        OgnlContext octx = (OgnlContext) ctx;
        assertNotNull("5", octx.getClassResolver());

        origCtx = new HashMap();
        origCtx.put("a", "A");
        ctx = OgnlUtil.addClassResolverIfNecessary(origCtx, container);
        assertNotNull("6", ctx);
        assertTrue("7", ctx instanceof OgnlContext);
        octx = (OgnlContext) ctx;
        assertNotNull("8", octx.getClassResolver());
        assertEquals("9", "A", origCtx.get("a"));
    }

    /**
     * @throws Exception
     */
    public void testClassResolverImpl() throws Exception {
        ClassResolver resolver = new OgnlUtil.ClassResolverImpl(getClass()
                .getClassLoader());
        Class clazz = resolver.classForName(
                "org.seasar.framework.container.impl.S2ContainerImpl", null);
        assertSame("1", S2ContainerImpl.class, clazz);
        try {
            clazz = resolver.classForName("Integer", null);
        } catch (ClassNotFoundException ex) {
            fail("2");
        }
        assertNotNull("3", clazz);
    }

    /**
     * @throws Exception
     */
    public void testGetValueException() throws Exception {
        final RuntimeException runtimeException = new RuntimeException(
                "test error message");
        final Object exp = OgnlUtil.parseExpression("foo.getBar()");
        final Map root = new HashMap();
        root.put("foo", new Foo() {
            public String getBar() {
                throw runtimeException;
            }
        });
        try {
            OgnlUtil.getValue(exp, root);
            fail();
        } catch (OgnlRuntimeException e) {
            e.printStackTrace();
            final Throwable cause = e.getCause();
            assertSame(runtimeException, cause);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetValueException2() throws Exception {
        final Object exp = OgnlUtil.parseExpression("hoge");
        try {
            OgnlUtil.getValue(exp, new Object());
            fail();
        } catch (OgnlRuntimeException e) {
            e.printStackTrace();
            final Throwable cause = e.getCause();
            assertNotNull(cause);
        }
    }

    private static interface Foo {
        /**
         * @return
         */
        String getBar();
    }

    /**
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        {
            final Object exp = OgnlUtil
                    .parseExpression("new java.lang.String(\"abc\")");
            final Object value = OgnlUtil.getValue(exp, null);
            assertEquals("abc", value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("\"aaaa\"");
            final Object value = OgnlUtil.getValue(exp, null);
            assertEquals("aaaa", value);
        }
        {
            // 12354 is Japanese Hiragana "a".
            final Character a = new Character((char) 12354);
            final Object exp = OgnlUtil.parseExpression("\"" + a + "\"");
            final Object value = OgnlUtil.getValue(exp, null);
            assertEquals(a.toString(), value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("a.get(\"b\")");
            Map a = new HashMap();
            a.put("b", new Integer(123));
            Map root = new HashMap();
            root.put("a", a);
            final Object value = OgnlUtil.getValue(exp, root);
            assertEquals(new Integer(123), value);
        }
        {
            final Character a = new Character((char) 12354);
            final Object exp = OgnlUtil.parseExpression(a + ".get(\"b\")");
            Map aaa = new HashMap();
            aaa.put("b", new Integer(123));
            Map root = new HashMap();
            root.put(a.toString(), aaa);
            final Object value = OgnlUtil.getValue(exp, root);
            assertEquals(new Integer(123), value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("\"aaa\" != null");
            final Object value = OgnlUtil.getValue(exp, null);
            assertEquals(Boolean.TRUE, value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("\"aaa\" == null");
            final Object value = OgnlUtil.getValue(exp, null);
            assertEquals(Boolean.FALSE, value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("a != null");
            final Map root = new HashMap();
            final Object value = OgnlUtil.getValue(exp, root);
            assertEquals(Boolean.FALSE, value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("a$ == null");
            final Map root = new HashMap();
            final Object value = OgnlUtil.getValue(exp, root);
            assertEquals(Boolean.TRUE, value);
        }
        {
            final Object exp = OgnlUtil.parseExpression("a_ == null");
            final Map root = new HashMap();
            final Object value = OgnlUtil.getValue(exp, root);
            assertEquals(Boolean.TRUE, value);
        }
    }

    /**
     * @throws Exception
     */
    public void testHiragana() throws Exception {
        final Character a = new Character((char) 12354);
        System.out.println(a);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(a + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void testKatakana() throws Exception {
        final Character a = new Character((char) 12450);
        System.out.println(a);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(a + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void todo_testDigit() throws Exception {
        final Character one = new Character((char) 65297);
        System.out.println(one);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression("A" + one + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void todo_testSmallLetter() throws Exception {
        final Character smallA = new Character((char) 65345);
        System.out.println(smallA);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(smallA + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void todo_testBigLetter() throws Exception {
        final Character largeA = new Character((char) 65313);
        System.out.println(largeA);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(largeA + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void todo_testUndescore() throws Exception {
        final Character underscore = new Character((char) 65343);
        System.out.println(underscore);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(underscore + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

    /**
     * @throws Exception
     */
    public void todo_testDollor() throws Exception {
        final Character dollor = new Character((char) 65284);
        System.out.println(dollor);
        final Map root = new HashMap();
        final Object exp = OgnlUtil.parseExpression(dollor + " != null");
        final Object value = OgnlUtil.getValue(exp, root);
        assertEquals(Boolean.FALSE, value);
    }

}
