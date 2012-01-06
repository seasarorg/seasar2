/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class ObjectResultSetHandlerTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testHandle() throws Exception {
        ResultSetHandler handler = new ObjectResultSetHandler();
        String sql = "select ename from emp where empno = 7788";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        String ret = null;
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ret = (String) handler.handle(rs);
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
        assertEquals("1", "SCOTT", ret);
    }

    public void setUp() {
        include("j2ee.dicon");
    }
}