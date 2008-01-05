/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.util;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class BeansTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCopy_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = new MyBean();
        Beans.copy(src, dest).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopy_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        BeanMap dest = new BeanMap();
        Beans.copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopy_mapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = new MyBean();
        Beans.copy(src, dest).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopy_mapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = new BeanMap();
        Beans.copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopy_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = Beans.createAndCopy(MyBean.class, src).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopy_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        BeanMap dest = Beans.createAndCopy(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopy_mapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = Beans.createAndCopy(MyBean.class, src).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCreateAndCopy_mapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = Beans.createAndCopy(BeanMap.class, src).execute();
        assertEquals("aaa", dest.get("aaa"));
    }
}