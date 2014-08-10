/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
import java.util.List;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class BeanListResultSetHandlerTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testHandle() throws Exception {
        ResultSetHandler handler = new BeanListResultSetHandler(Employee.class);
        String sql = "select * from emp";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        List ret = null;
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ret = (List) handler.handle(rs);
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
        assertNotNull("1", ret);
        for (int i = 0; i < ret.size(); ++i) {
            Employee emp = (Employee) ret.get(i);
            System.out.println(emp.getEmpno() + "," + emp.getEname());
        }
    }

    public void setUp() {
        include("j2ee.dicon");
    }
}