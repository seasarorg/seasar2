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
package org.seasar.framework.util.tiger;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class GenericUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testClass() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap(Foo.class);
        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertEquals(Object.class, map.get(Foo.class.getTypeParameters()[0]));
    }

    /**
     * @throws Exception
     */
    public void testGenericMethod() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap(Fuga.class);
        assertNotNull(map);
        assertTrue(map.isEmpty());
        Method m = Fuga.class.getMethod("getFuga");
        Class<?> returnClass = GenericUtil.getActualClass(m.getGenericReturnType(), map);
        assertEquals(Object.class, returnClass);
    }

    /**
     * @throws Exception
     */
    public void testArray() throws Exception {
        Method m1 = ArrayType.class.getMethod("arrayOfStringClass");
        Type t1 = m1.getGenericReturnType();
        Type t2 = GenericUtil.getElementTypeOfArray(t1);
        assertEquals(Class.class, GenericUtil.getRawClass(t2));
        assertEquals(String.class, GenericUtil.getGenericParameter(t2, 0));
    }

    /**
     * @throws Exception
     */
    public void testList() throws Exception {
        Method m1 = ListType.class.getMethod("listOfString");
        Type t1 = m1.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t1, List.class));
        assertEquals(String.class, GenericUtil.getElementTypeOfList(t1));

        Method m2 = ListType.class.getMethod("listOfClass");
        Type t2 = m2.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t2, List.class));
        assertTrue(GenericUtil.isTypeOf(GenericUtil.getElementTypeOfList(t2),
                Class.class));

        Method m3 = ListType.class.getMethod("listOfWildcard");
        Type t3 = m3.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t3, List.class));
        assertTrue(WildcardType.class.isInstance(GenericUtil
                .getElementTypeOfList(t3)));
    }

    /**
     * @throws Exception
     */
    public void testSet() throws Exception {
        Method m1 = SetType.class.getMethod("setOfString");
        Type t1 = m1.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t1, Set.class));
        assertEquals(String.class, GenericUtil.getElementTypeOfSet(t1));

        Method m2 = SetType.class.getMethod("setOfClass");
        Type t2 = m2.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t2, Set.class));
        assertTrue(GenericUtil.isTypeOf(GenericUtil.getElementTypeOfSet(t2),
                Class.class));

        Method m3 = SetType.class.getMethod("setOfWildcard");
        Type t3 = m3.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t3, Set.class));
        assertTrue(WildcardType.class.isInstance(GenericUtil
                .getElementTypeOfSet(t3)));
    }

    /**
     * @throws Exception
     */
    public void testMap() throws Exception {
        Method m1 = MapType.class.getMethod("mapOfStringToObject");
        Type t1 = m1.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t1, Map.class));
        assertEquals(String.class, GenericUtil.getKeyTypeOfMap(t1));
        assertEquals(Object.class, GenericUtil.getValueTypeOfMap(t1));

        Method m2 = MapType.class.getMethod("mapOfClassToString");
        Type t2 = m2.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t2, Map.class));
        assertTrue(GenericUtil.isTypeOf(GenericUtil.getKeyTypeOfMap(t2),
                Class.class));
        assertEquals(String.class, GenericUtil.getValueTypeOfMap(t2));

        Method m3 = MapType.class.getMethod("mapOfWildcard");
        Type t3 = m3.getGenericReturnType();
        assertTrue(GenericUtil.isTypeOf(t3, Map.class));
        assertTrue(WildcardType.class.isInstance(GenericUtil
                .getKeyTypeOfMap(t3)));
        assertTrue(WildcardType.class.isInstance(GenericUtil
                .getValueTypeOfMap(t3)));
    }

    /**
     * @throws Exception
     */
    public void testGetTypeVariableMap() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        assertEquals(4, map.size());
        Iterator<Entry<TypeVariable<?>, Type>> it = map.entrySet().iterator();
        Entry<TypeVariable<?>, Type> entry = it.next();
        assertEquals("T1", entry.getKey().getName());
        assertEquals(Integer.class, entry.getValue());
        entry = it.next();
        assertEquals("T2", entry.getKey().getName());
        assertEquals(Long.class, entry.getValue());
        entry = it.next();
        assertEquals("T1", entry.getKey().getName());
        assertEquals(String.class, entry.getValue());
        entry = it.next();
        assertEquals("T2", entry.getKey().getName());
        assertEquals(Boolean.class, entry.getValue());
    }

    /**
     * @throws Exception
     */
    public void testGetActualClass() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);

        Method method = Hoge.class.getMethod("foo", Object.class);
        assertEquals(Integer.class, GenericUtil.getActualClass(method
                .getGenericParameterTypes()[0], map));
        assertEquals(Long.class, GenericUtil.getActualClass(method
                .getGenericReturnType(), map));

        method = Hoge.class.getMethod("array");
        assertEquals(String[].class, GenericUtil.getActualClass(method
                .getGenericReturnType(), map));

        method = Hoge.class.getMethod("list");
        assertEquals(List.class, GenericUtil.getActualClass(method
                .getGenericReturnType(), map));

        method = Hoge.class.getMethod("set");
        assertEquals(Set.class, GenericUtil.getActualClass(method
                .getGenericReturnType(), map));

        method = Hoge.class.getMethod("map");
        assertEquals(Map.class, GenericUtil.getActualClass(method
                .getGenericReturnType(), map));
    }

    /**
     * @throws Exception
     */
    public void testGetActualElementClassOfArray() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        Method method = Hoge.class.getMethod("array");
        assertEquals(String.class, GenericUtil.getActualElementClassOfArray(
                method.getGenericReturnType(), map));
    }

    /**
     * @throws Exception
     */
    public void testGetActualElementClassOfList() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        Method method = Hoge.class.getMethod("list");
        assertEquals(Boolean.class, GenericUtil.getActualElementClassOfList(
                method.getGenericReturnType(), map));
    }

    /**
     * @throws Exception
     */
    public void testGetActualElementClassOfSet() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        Method method = Hoge.class.getMethod("set");
        assertEquals(String.class, GenericUtil.getActualElementClassOfSet(
                method.getGenericReturnType(), map));
    }

    /**
     * @throws Exception
     */
    public void testGetActualKeyClassOfMap() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        Method method = Hoge.class.getMethod("map");
        assertEquals(String.class, GenericUtil.getActualKeyClassOfMap(method
                .getGenericReturnType(), map));
    }

    /**
     * @throws Exception
     */
    public void testGetActualValueClassOfMap() throws Exception {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(Hoge.class);
        Method method = Hoge.class.getMethod("map");
        assertEquals(Boolean.class, GenericUtil.getActualValueClassOfMap(method
                .getGenericReturnType(), map));
    }

    /**
     * 
     */
    public interface ArrayType {

        /**
         * @return
         */
        Class<String>[] arrayOfStringClass();
    }

    /**
     * 
     */
    public interface ListType {

        /**
         * @return
         */
        List<String> listOfString();

        /**
         * @return
         */
        List<Class<?>> listOfClass();

        /**
         * @return
         */
        List<?> listOfWildcard();
    }

    /**
     * 
     */
    public interface SetType {

        /**
         * @return
         */
        Set<String> setOfString();

        /**
         * @return
         */
        Set<Class<?>> setOfClass();

        /**
         * @return
         */
        Set<?> setOfWildcard();
    }

    /**
     * 
     */
    public interface MapType {

        /**
         * @return
         */
        Map<String, Object> mapOfStringToObject();

        /**
         * @return
         */
        Map<Class<?>, String> mapOfClassToString();

        /**
         * @return
         */
        Map<?, ?> mapOfWildcard();
    }

    /**
     * @param <T1>
     * @param <T2>
     * 
     */
    public interface Foo<T1, T2> {

        /**
         * @param foo
         * @return
         */
        T2 foo(T1 foo);
    }

    /**
     * 
     */
    public interface Bar extends Foo<Integer, Long> {
    }

    /**
     * @param <T1>
     * @param <T2>
     * 
     */
    public interface Baz<T1, T2> {

        /**
         * @return
         */
        T1[] array();

        /**
         * @return
         */
        List<T2> list();

        /**
         * @return
         */
        Set<T1> set();

        /**
         * @return
         */
        Map<T1, T2> map();
    }

    /**
     * 
     */
    public static abstract class Hoge implements Bar, Baz<String, Boolean> {
    }

    /**
     *
     */
    public interface Fuga {
        /**
         * @param <T>
         * @return
         */
        <T> T getFuga();
    }

}
