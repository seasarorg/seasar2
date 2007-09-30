/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.ParameterizedClassDesc;
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
        ParameterizedClassDesc pd = bd.getPropertyDesc("foo")
                .getParameterizedClassDesc();
        assertEquals(List.class, pd.getRawClass());
        assertEquals(1, pd.getArguments().length);
        assertEquals(String.class, pd.getArguments()[0].getRawClass());
    }

    /**
     * @throws Exception
     */
    public void testGetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        ParameterizedClassDesc pd = bd.getPropertyDesc("bar")
                .getParameterizedClassDesc();
        assertEquals(Set.class, pd.getRawClass());
        assertEquals(1, pd.getArguments().length);
        assertEquals(Integer.class, pd.getArguments()[0].getRawClass());
    }

    /**
     * @throws Exception
     */
    public void testSetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        ParameterizedClassDesc pd = bd.getPropertyDesc("baz")
                .getParameterizedClassDesc();
        assertEquals(Collection.class, pd.getRawClass());
        assertEquals(1, pd.getArguments().length);
        assertEquals(Date.class, pd.getArguments()[0].getRawClass());
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
        public void setBaz(Collection<Date> date) {
        }

    }
}
