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
package org.seasar.framework.util.tiger;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class GenericUtilTest extends TestCase {

    public void testArray() throws Exception {
        Method m1 = ArrayType.class.getMethod("arrayOfStringClass");
        Type t1 = m1.getGenericReturnType();
        Type t2 = GenericUtil.getElementTypeOfArray(t1);
        assertEquals(Class.class, GenericUtil.getRawClass(t2));
        assertEquals(String.class, GenericUtil.getGenericParameter(t2, 0));
    }

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

    public interface ArrayType {
        Class<String>[] arrayOfStringClass();
    }

    public interface ListType {
        List<String> listOfString();

        List<Class<?>> listOfClass();

        List<?> listOfWildcard();
    }

    public interface SetType {
        Set<String> setOfString();

        Set<Class<?>> setOfClass();

        Set<?> setOfWildcard();
    }

    public interface MapType {
        Map<String, Object> mapOfStringToObject();

        Map<Class<?>, String> mapOfClassToString();

        Map<?, ?> mapOfWildcard();
    }

}
