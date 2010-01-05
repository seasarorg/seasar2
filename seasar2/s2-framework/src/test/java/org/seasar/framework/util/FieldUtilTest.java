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
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * @author y-komori
 * 
 */
public class FieldUtilTest extends TestCase {
    /**
     * 
     */
    public Object objectField;

    /**
     * 
     */
    public int intField;

    /**
     * 
     */
    public String stringField;

    /**
     * 
     */
    public static final int INT_DATA = 987654321;

    /**
     * 
     */
    public static final String STRING_DATA = "Hello World!";

    /**
     * {@link org.seasar.framework.util.FieldUtil#get(java.lang.reflect.Field, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testGet() {
        Field field;
        try {
            field = getClass().getField("objectField");
            Integer testData = new Integer(123);
            FieldUtil.set(field, this, testData);
            assertEquals("1", testData, FieldUtil.get(field, this));
        } catch (SecurityException e) {
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    /**
     * {@link org.seasar.framework.util.FieldUtil#getInt(java.lang.reflect.Field)}
     * のためのテスト・メソッド。
     */
    public void testGetIntField() {
        Field field;
        try {
            field = getClass().getField("intField");
            int testData = 1234567890;
            FieldUtil.set(field, this, new Integer(testData));
            assertEquals("1", testData, FieldUtil.getInt(field, this));
        } catch (SecurityException e) {
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    /**
     * {@link org.seasar.framework.util.FieldUtil#getInt(java.lang.reflect.Field, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testGetIntFieldObject() {
        Field field;
        try {
            field = getClass().getField("INT_DATA");
            assertEquals("1", INT_DATA, FieldUtil.getInt(field));
        } catch (SecurityException e) {
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    /**
     * {@link org.seasar.framework.util.FieldUtil#getString(java.lang.reflect.Field)}
     * のためのテスト・メソッド。
     */
    public void testGetStringField() {
        Field field;
        try {
            field = getClass().getField("stringField");
            String testData = "Hello World!";
            FieldUtil.set(field, this, testData);
            assertEquals("1", testData, FieldUtil.getString(field, this));
        } catch (SecurityException e) {
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    /**
     * {@link org.seasar.framework.util.FieldUtil#getString(java.lang.reflect.Field, java.lang.Object)}
     * のためのテスト・メソッド。
     */
    public void testGetStringFieldObject() {
        Field field;
        try {
            field = getClass().getField("STRING_DATA");
            assertEquals("1", STRING_DATA, FieldUtil.getString(field));
        } catch (SecurityException e) {
            fail();
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    /**
     * @throws Exception
     */
    public void testSet() throws Exception {
        Field field = getClass().getField("intField");
        try {
            FieldUtil.set(field, this, "abc");
            fail();
        } catch (SIllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromFieldType() throws Exception {
        assertNull(FieldUtil.getElementTypeOfListFromFieldType(Baz.class
                .getField("list")));
    }

    /**
     * 
     */
    public static class Baz {

        /** */
        public List list;
    }
}
