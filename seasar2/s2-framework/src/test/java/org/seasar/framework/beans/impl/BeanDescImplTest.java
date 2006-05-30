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
package org.seasar.framework.beans.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.util.MathUtil;

/**
 * @author higa
 * @author manhole
 */
public class BeanDescImplTest extends TestCase {

    public void testPropertyDesc() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals("1", 3, beanDesc.getPropertyDescSize());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertEquals("2", "aaa", propDesc.getPropertyName());
        assertEquals("3", String.class, propDesc.getPropertyType());
        assertNotNull("4", propDesc.getReadMethod());
        assertNull("5", propDesc.getWriteMethod());

        propDesc = beanDesc.getPropertyDesc("CCC");
        assertEquals("6", "CCC", propDesc.getPropertyName());
        assertEquals("7", boolean.class, propDesc.getPropertyType());
        assertNotNull("8", propDesc.getReadMethod());
        assertNull("9", propDesc.getWriteMethod());

        propDesc = beanDesc.getPropertyDesc("eee");
        assertEquals("10", "eee", propDesc.getPropertyName());
        assertEquals("11", String.class, propDesc.getPropertyType());
        assertNotNull("12", propDesc.getReadMethod());
        assertNotNull("13", propDesc.getWriteMethod());
    }

    public void testInvoke() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals("1", new Integer(3), beanDesc.invoke(new MyBean(), "add",
                new Object[] { new Integer(1), new Integer(2) }));
    }

    public void testInvoke2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals("1", new Integer(3), beanDesc.invoke(new MyBean(), "add2",
                new Object[] { new BigDecimal(1), new BigDecimal(2) }));
    }

    public void testInvoke3() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertEquals("1", new Integer(3), beanDesc.invoke(null, "max",
                new Object[] { new Integer(1), new Integer(3) }));
        assertEquals("2", new Long(3), beanDesc.invoke(null, "max",
                new Object[] { new Long(1), new Long(3) }));
    }

    public void testInvoke4() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertEquals("1", new Double(3), beanDesc.invoke(null, "ceil",
                new Object[] { new BigDecimal(2.1) }));
    }

    public void testInvoke5() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals("1", new Integer("3"), beanDesc.invoke(new MyBean(),
                "echo", new Object[] { new Double("3") }));
    }

    public void testInvokeForException() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        try {
            beanDesc.invoke(new MyBean(), "throwException", null);
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    public void testNewInstance() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        Integer i = new Integer(10);
        Object[] args = new Object[] { i };
        assertEquals("1", i, beanDesc.newInstance(args));
        Object[] args2 = new Object[] { "10" };
        assertEquals("2", i, beanDesc.newInstance(args2));
    }

    public void testNewInstance2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        BigDecimal d = new BigDecimal(10);
        Object[] args = new Object[] { d };
        assertEquals("1", new Integer(10), beanDesc.newInstance(args));
    }

    public void testGetFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertTrue("1", beanDesc.hasField("HOGE"));
        Field field = beanDesc.getField("HOGE");
        assertEquals("2", "hoge2", field.get(null));
        assertTrue("3", beanDesc.hasField("aaa"));
        assertFalse("4", beanDesc.hasField("aaA"));
    }

    public void testHasMethod() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals(true, beanDesc.hasMethod("getAaa"));
        assertEquals(false, beanDesc.hasMethod("getaaa"));
    }

    public void testGetMethodNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(getClass());
        String[] names = beanDesc.getMethodNames();
        for (int i = 0; i < names.length; ++i) {
            System.out.println(names[i]);
        }
        assertTrue("1", names.length > 0);
    }

    public void testInvalidProperty() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean2.class);
        assertEquals("1", false, beanDesc.hasPropertyDesc("aaa"));
    }

    public void testAddFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Field eee = beanDesc.getField("eee");
        assertEquals("1", true, eee.isAccessible());
    }

    public void testGetConstructorParameterNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean3.class);
        String[] names = beanDesc.getConstructorParameterNames(new Class[0]);
        assertNotNull(names);
        assertEquals(0, names.length);

        names = beanDesc.getConstructorParameterNames(new Class[] { int.class,
                String.class, MyBean.class, MyBean2.class });
        assertNotNull(names);
        assertEquals(4, names.length);
        assertEquals("num", names[0]);
        assertEquals("text", names[1]);
        assertEquals("bean1", names[2]);
        assertEquals("bean2", names[3]);
    }

    public void testGetMethodParameterNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        String[] names = beanDesc.getMethodParameterNames("getAaa",
                new Class[0]);
        assertNotNull(names);
        assertEquals(0, names.length);

        names = beanDesc.getMethodParameterNames("getBbb",
                new Class[] { Object.class });
        assertNotNull(names);
        assertEquals(1, names.length);
        assertEquals("a", names[0]);

        names = beanDesc.getMethodParameterNames("add", new Class[] {
                Number.class, Number.class });
        assertNotNull(names);
        assertEquals(2, names.length);
        assertEquals("arg1", names[0]);
        assertEquals("arg2", names[1]);

        beanDesc = new BeanDescImpl(MyBean3.class);
        names = beanDesc.getMethodParameterNames("foo", new Class[] {
                MyBean.class, MyBean2.class });
        assertNotNull(names);
        assertEquals(2, names.length);
        assertEquals("foo$bar", names[0]);
        assertEquals("hoge$hoge$hoge", names[1]);
    }

    /*
     * public void testPerformance() { long start = System.currentTimeMillis();
     * for (int i = 0; i < 10000; ++i) { BeanDesc beanDesc = new
     * BeanDescImpl(MyBean.class); beanDesc.getPropertyDesc("aaa"); }
     * System.out.println("time:" + (System.currentTimeMillis() - start)); }
     */
    public static interface MyInterface {
        String HOGE = "hoge";
    }

    public static interface MyInterface2 extends MyInterface {
        String HOGE = "hoge2";
    }

    public static class MyBean implements MyInterface2 {

        private String aaa;

        private String eee;

        public String getAaa() {
            return aaa;
        }

        public String getBbb(Object a) {
            return null;
        }

        public boolean isCCC() {
            return true;
        }

        public Object isDdd() {
            return null;
        }

        public String getEee() {
            return eee;
        }

        public void setEee(String eee) {
            this.eee = eee;
        }

        public Number add(Number arg1, Number arg2) {
            return MathUtil.add(arg1, arg2);
        }

        public int add2(int arg1, int arg2) {
            return arg1 + arg2;
        }

        public Integer echo(Integer arg) {
            return arg;
        }

        public void throwException() {
            throw new IllegalStateException("hoge");
        }
    }

    public static class MyBean2 {
        public void setAaa(int i) {
        }

        public void setAaa(String s) {
        }
    }

    public static class MyBean3 {
        public MyBean3() {
        }

        public MyBean3(int num, String text, MyBean bean1, MyBean2 bean2) {
        }

        public static void foo(MyBean foo$bar, MyBean2 hoge$hoge$hoge) {
        }
    }
}
