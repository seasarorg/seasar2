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
package org.seasar.extension.jdbc.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BindVariableUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetCompleteSql1() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        assertEquals(
                "update emp set ename = 'foo', comm = null where empno = 'bar'",
                BindVariableUtil.getCompleteSql(sql, new Object[] { "foo",
                        null, "bar" }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql2() throws Exception {
        final String sql = "update emp set ename = ?, comm = '?' where empno = ?";
        assertEquals(
                "update emp set ename = 'foo', comm = '?' where empno = 'bar'",
                BindVariableUtil.getCompleteSql(sql, new Object[] { "foo",
                        "bar" }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql3() throws Exception {
        final String sql = "update emp set ename = /* ? */, comm = ? where empno = ?/*?*/";
        assertEquals(
                "update emp set ename = /* ? */, comm = null where empno = 'bar'/*?*/",
                BindVariableUtil.getCompleteSql(sql,
                        new Object[] { null, "bar" }));
    }
}