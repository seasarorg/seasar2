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
package org.seasar.framework.beans.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class CopyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testExecute_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = new MyBean();
        new Copy(src, dest).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testExecute_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        Map<String, Object> dest = new HashMap<String, Object>();
        new Copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testExecute_mapToBean() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        MyBean dest = new MyBean();
        new Copy(src, dest).execute();
        assertEquals("aaa", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testExecute_mapToMap() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        Map<String, Object> dest = new HashMap<String, Object>();
        new Copy(src, dest).execute();
        assertEquals("aaa", dest.get("aaa"));
    }
}
