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
package org.seasar.framework.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.beans.ParameterizedClassDesc;

/**
 * @author koichik
 */
public class ParameterizedClassDescFactoryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testFieldType() throws Exception {
        @SuppressWarnings("unchecked")
        Map<TypeVariable<?>, Type> map = ParameterizedClassDescFactory
                .getTypeVariables(Hoge.class);
        Field field = Hoge.class.getField("foo");
        ParameterizedClassDesc desc = ParameterizedClassDescFactory
                .createParameterizedClassDesc(field, map);
        assertEquals(Map.class, desc.getRawClass());

        ParameterizedClassDesc[] args = desc.getArguments();
        assertEquals(2, args.length);

        ParameterizedClassDesc arg1 = args[0];
        assertEquals(String.class, arg1.getRawClass());
        assertNull(arg1.getArguments());

        ParameterizedClassDesc arg2 = args[1];
        assertEquals(Set[].class, arg2.getRawClass());

        ParameterizedClassDesc[] args2 = arg2.getArguments();
        assertEquals(1, args2.length);

        ParameterizedClassDesc arg2_1 = args2[0];
        assertEquals(Integer.class, arg2_1.getRawClass());
    }

    /**
     * @throws Exception
     */
    public void testMethodParameterType() throws Exception {
        @SuppressWarnings("unchecked")
        Map<TypeVariable<?>, Type> map = ParameterizedClassDescFactory
                .getTypeVariables(Hoge.class);
        Method method = Hoge.class.getMethod("foo", Set.class, Map.class);
        ParameterizedClassDesc desc = ParameterizedClassDescFactory
                .createParameterizedClassDesc(method, 0, map);
        assertEquals(Set.class, desc.getRawClass());
        ParameterizedClassDesc[] args = desc.getArguments();
        assertEquals(1, args.length);
        assertEquals(Integer.class, args[0].getRawClass());

        desc = ParameterizedClassDescFactory.createParameterizedClassDesc(
                method, 1, map);
        assertEquals(Map.class, desc.getRawClass());
        args = desc.getArguments();
        assertEquals(2, args.length);
        assertEquals(String.class, args[0].getRawClass());
        assertNull(args[0].getArguments());
        assertEquals(Integer.class, args[1].getRawClass());
        assertNull(args[1].getArguments());
    }

    /**
     * @throws Exception
     */
    public void testMethodReturnType() throws Exception {
        @SuppressWarnings("unchecked")
        Map<TypeVariable<?>, Type> map = ParameterizedClassDescFactory
                .getTypeVariables(Hoge.class);
        Method method = Hoge.class.getMethod("foo", Set.class, Map.class);
        ParameterizedClassDesc desc = ParameterizedClassDescFactory
                .createParameterizedClassDesc(method, map);
        assertEquals(List.class, desc.getRawClass());
        ParameterizedClassDesc[] args = desc.getArguments();
        assertEquals(1, args.length);
        assertEquals(String.class, args[0].getRawClass());
        assertNull(args[0].getArguments());
    }

    /**
     * @author koichik
     */
    public interface Hoge {

        /** */
        public static Map<String, Set<Integer>[]> foo = null;

        /**
         * @param arg1
         * @param arg2
         * @return
         */
        List<String> foo(Set<Integer> arg1, Map<String, Integer> arg2);
    }

}
