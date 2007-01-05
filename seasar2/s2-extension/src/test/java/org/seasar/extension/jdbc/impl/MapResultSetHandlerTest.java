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
package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class MapResultSetHandlerTest extends S2TestCase {

    public MapResultSetHandlerTest(String arg0) {
        super(arg0);
    }

    public void testHandle() throws Exception {
        ResultSetHandler handler = new MapResultSetHandler();
        String sql = "select * from emp where empno = 7788";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        Map ret = null;
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ret = (Map) handler.handle(rs);
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
        System.out.println(ret);
        assertNotNull("1", ret);
    }

    public void setUp() {
        include("j2ee.dicon");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MapResultSetHandlerTest.class);
    }

}
