/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.mock.portlet;

import junit.framework.TestCase;

/**
 *
 */
public class MockPortletRequestImplTest extends TestCase {

    private MockPortletContextImpl context_;

    private MockPortletRequestImpl request_;

    /**
     * @throws Exception
     */
    public void testAddParameter() throws Exception {
        request_.addParameter("aaa", "111");
        String[] values = request_.getParameterValues("aaa");
        assertEquals("1", 1, values.length);
        assertEquals("2", "111", values[0]);
        request_.addParameter("aaa", "222");
        values = request_.getParameterValues("aaa");
        assertEquals("3", 2, values.length);
        assertEquals("4", "111", values[0]);
        assertEquals("5", "222", values[1]);
        request_.addParameter("aaa", new String[] { "333", "444" });
        values = request_.getParameterValues("aaa");
        assertEquals("6", 4, values.length);
        assertEquals("7", "111", values[0]);
        assertEquals("8", "222", values[1]);
        assertEquals("9", "333", values[2]);
        assertEquals("10", "444", values[3]);
    }

    protected void setUp() throws Exception {
        context_ = new MockPortletContextImpl("/s2jsf-example");
        request_ = context_.createRequest();
    }
}