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
package org.seasar.framework.beans.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author koichik
 */
public class PropertyDescImplTigerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testFieldType() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("foo");
        assertTrue(pd.isParameterized());
        assertEquals(String.class, pd.getElementClassOfCollection());

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertEquals(List.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(String.class, pcd.getArguments()[0].getRawClass());

        pd = bd.getPropertyDesc("hoge");
        assertTrue(pd.isParameterized());
        assertEquals(Object.class, pd.getElementClassOfCollection());

        pcd = pd.getParameterizedClassDesc();
        assertEquals(List.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(Object.class, pcd.getArguments()[0].getRawClass());

        bd = BeanDescFactory.getBeanDesc(Bar.class);
        pd = bd.getPropertyDesc("list");
        assertTrue(pd.isParameterized());
        assertEquals(String.class, pd.getElementClassOfCollection());

        pcd = pd.getParameterizedClassDesc();
        assertEquals(List.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(String.class, pcd.getArguments()[0].getRawClass());
    }

    /**
     * @throws Exception
     */
    public void testGetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("bar");
        assertTrue(pd.isParameterized());
        assertEquals(Integer.class, pd.getElementClassOfCollection());

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertEquals(Set.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(Integer.class, pcd.getArguments()[0].getRawClass());

        pd = bd.getPropertyDesc("fuga");
        assertTrue(pd.isParameterized());
        assertEquals(Enum.class, pd.getElementClassOfCollection());

        pcd = pd.getParameterizedClassDesc();
        assertEquals(Set.class, pcd.getRawClass());
        assertEquals(1, pcd.getArguments().length);
        assertEquals(Enum.class, pcd.getArguments()[0].getRawClass());
    }

    /**
     * @throws Exception
     */
    public void testSetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("baz");
        assertTrue(pd.isParameterized());
        assertEquals(String.class, pd.getKeyClassOfMap());
        assertEquals(Date.class, pd.getValueClassOfMap());

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertEquals(Map.class, pcd.getRawClass());
        assertEquals(2, pcd.getArguments().length);
        assertEquals(String.class, pcd.getArguments()[0].getRawClass());
        assertEquals(Date.class, pcd.getArguments()[1].getRawClass());

        pd = bd.getPropertyDesc("hege");
        assertTrue(pd.isParameterized());
        assertEquals(String.class, pd.getKeyClassOfMap());
        assertEquals(Number.class, pd.getValueClassOfMap());

        pcd = pd.getParameterizedClassDesc();
        assertEquals(Map.class, pcd.getRawClass());
        assertEquals(2, pcd.getArguments().length);
        assertEquals(String.class, pcd.getArguments()[0].getRawClass());
        assertEquals(Number.class, pcd.getArguments()[1].getRawClass());
    }

    /** */
    public static class Hoge {

        /** */
        public List<String> foo;

        /**
         * @return
         */
        public Set<Integer> getBar() {
            return null;
        }

        /**
         * @param date
         */
        public void setBaz(@SuppressWarnings("unused") Map<String, Date> date) {
        }

        /** */
        public List<?> hoge;

        /**
         * @return
         */
        public Set<? extends Enum<?>> getFuga() {
            return null;
        }

        /**
         * @param date
         */
        public void setHege(
                @SuppressWarnings("unused") Map<? extends String, ? extends Number> date) {
        }

    }

    /**
     * @param <T>
     */
    public static class Foo<T> {

        /** */
        public List<T> list;

    }

    /** */
    public static class Bar extends Foo<String> {
    }
}
