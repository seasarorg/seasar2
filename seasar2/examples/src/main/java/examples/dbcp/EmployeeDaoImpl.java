/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package examples.dbcp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class EmployeeDaoImpl implements EmployeeDao {

    private DataSource dataSource_;

    public EmployeeDaoImpl(DataSource dataSource) {
        dataSource_ = dataSource;
    }

    public String getEmployeeName(int empno) throws SQLException {
        String ename = null;
        Connection con = dataSource_.getConnection();
        try {
            PreparedStatement ps = con
                    .prepareStatement("SELECT ename FROM emp WHERE empno = ?");
            try {
                ps.setInt(1, empno);
                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next()) {
                        ename = rs.getString("ename");
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ps.close();
            }
        } finally {
            con.close();
        }
        return ename;
    }

}
