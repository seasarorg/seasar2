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

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.seasar.framework.exception.NoSuchConstructorRuntimeException;
import org.seasar.framework.exception.NoSuchFieldRuntimeException;

/**
 * @author higa
 * 
 */
public class ClassUtilTest extends TestCase {

    /**
     * 
     */
    public static final String HOGE = "hoge";

    /**
     * 
     */
    public void testGetPrimitiveClass() {
        assertEquals("1", int.class, ClassUtil.getPrimitiveClass(Integer.class));
        assertEquals("2", null, ClassUtil.getPrimitiveClass(String.class));
        assertEquals("3", byte.class, ClassUtil.getPrimitiveClass(Byte.class));
    }

    /**
     * 
     */
    public void testGetPrimitiveClassIfWrapper() {
        assertEquals("1", int.class, ClassUtil
                .getPrimitiveClassIfWrapper(Integer.class));
        assertEquals("2", String.class, ClassUtil
                .getPrimitiveClassIfWrapper(String.class));
        assertEquals("3", byte.class, ClassUtil
                .getPrimitiveClassIfWrapper(Byte.class));
    }

    /**
     * 
     */
    public void testGetWrapperClass() {
        assertEquals("1", Integer.class, ClassUtil.getWrapperClass(int.class));
        assertEquals("2", null, ClassUtil.getWrapperClass(String.class));
        assertEquals("3", Byte.class, ClassUtil.getWrapperClass(byte.class));
    }

    /**
     * 
     */
    public void testGetWrapperClassIfWrapper() {
        assertEquals("1", Integer.class, ClassUtil
                .getWrapperClassIfPrimitive(int.class));
        assertEquals("2", String.class, ClassUtil
                .getWrapperClassIfPrimitive(String.class));
        assertEquals("3", Byte.class, ClassUtil
                .getWrapperClassIfPrimitive(byte.class));
    }

    /**
     * 
     */
    public void testIsAssignableFrom() {
        assertEquals("1", true, ClassUtil.isAssignableFrom(Number.class,
                Integer.class));
        assertEquals("2", false, ClassUtil.isAssignableFrom(Integer.class,
                Number.class));
        assertEquals("3", true, ClassUtil.isAssignableFrom(int.class,
                Integer.class));
    }

    /**
     * 
     */
    public void testGetPackageName() {
        assertEquals("1", "org.seasar.framework.util", ClassUtil
                .getPackageName(getClass()));
    }

    /**
     * 
     */
    public void testGetShortClassName() {
        assertEquals("1", "ClassUtilTest", ClassUtil
                .getShortClassName(getClass()));
    }

    /**
     * 
     */
    public void testGetConstructor() {
        try {
            ClassUtil.getConstructor(ClassUtilTest.class,
                    new Class[] { Integer.class });
            fail("1");
        } catch (NoSuchConstructorRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * 
     */
    public void testGetField() {
        try {
            ClassUtil.getField(getClass(), "aaa");
        } catch (NoSuchFieldRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * 
     */
    public void testGetSimpleClassName() {
        assertEquals("1", "int", ClassUtil.getSimpleClassName(int.class));
        assertEquals("2", "java.lang.String", ClassUtil
                .getSimpleClassName(String.class));
        assertEquals("3", "int[]", ClassUtil.getSimpleClassName(int[].class));
        assertEquals("4", "java.lang.String[][]", ClassUtil
                .getSimpleClassName(String[][].class));
    }

    /**
     * 
     */
    public void testConcatName() {
        assertEquals("aaa.bbb", ClassUtil.concatName("aaa", "bbb"));
        assertEquals("aaa", ClassUtil.concatName("aaa", null));
        assertEquals("aaa", ClassUtil.concatName("aaa", ""));
        assertEquals("bbb", ClassUtil.concatName(null, "bbb"));
        assertEquals("bbb", ClassUtil.concatName("", "bbb"));
        assertEquals("bbb", ClassUtil.concatName("", "bbb"));
        assertNull(ClassUtil.concatName(null, null));
        assertNull(ClassUtil.concatName(null, ""));
        assertNull(ClassUtil.concatName("", null));
        assertNull(ClassUtil.concatName("", ""));
    }

    /**
     * 
     */
    public void testGetResourcePath() {
        assertEquals("1", "org/seasar/framework/util/ClassUtilTest.class",
                ClassUtil.getResourcePath(getClass()));
    }

    /**
     * 
     */
    public void testSplitPackageAndShortClassName() {
        String[] ret = ClassUtil.splitPackageAndShortClassName("aaa.Hoge");
        assertEquals("aaa", ret[0]);
        assertEquals("Hoge", ret[1]);
        ret = ClassUtil.splitPackageAndShortClassName("Hoge");
        assertNull(ret[0]);
        assertEquals("Hoge", ret[1]);
    }

    /**
     * 
     */
    public void testConvertClass() {
        assertEquals(int.class, ClassUtil.convertClass("int"));
        assertEquals(String.class, ClassUtil.convertClass("java.lang.String"));
    }

    /**
     * @throws Exception
     */
    public void testGetDeclaredFields() throws Exception {
        Field[] fields = ClassUtil.getDeclaredFields(TestClass.class);
        assertEquals(5, fields.length);
        assertEquals("aaa", fields[0].getName());
        assertEquals("bbb", fields[1].getName());
        assertEquals("ccc", fields[2].getName());
        assertEquals("ddd", fields[3].getName());
        assertEquals("eee", fields[4].getName());
    }

    /**
     * @author koichik
     */
    public static class TestClass {
        /** */
        int aaa;

        /** */
        int bbb;

        /** */
        int ccc;

        /** */
        int ddd;

        /** */
        int eee;
    }
}
