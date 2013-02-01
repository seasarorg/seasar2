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
package org.seasar.extension.jdbc.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.DriverManagerUtil;

/**
 * @author higa
 * 
 */
public class DatabaseMetaDataUtilTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testGetColumnList() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        Map columnMap = DatabaseMetaDataUtil.getColumnMap(dbMetaData, "emp");
        System.out.println(columnMap);
        assertTrue("1", columnMap.size() > 0);
    }

    /**
     * @throws Exception
     */
    public void testSpike() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        System.out.println(dbMetaData.getDatabaseProductName());
        System.out.println(dbMetaData.getDatabaseProductVersion());
    }

    /**
     * @throws Exception
     */
    public void testGetColumnListForNotExistTable() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        Map columnMap = DatabaseMetaDataUtil.getColumnMap(dbMetaData, "_emp_");
        assertEquals("1", 0, columnMap.size());
    }

    /**
     * @throws Exception
     */
    public void testGetColumnListForSchema() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        Map columnMap = DatabaseMetaDataUtil.getColumnMap(dbMetaData, "SA.emp");
        System.out.println(columnMap);
        assertTrue("1", columnMap.size() > 0);
    }

    /**
     * @throws Exception
     */
    public void testGetPrimaryKeyList() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        Set primaryKeySet = DatabaseMetaDataUtil.getPrimaryKeySet(dbMetaData,
                "emp");
        System.out.println(primaryKeySet);
        assertTrue("1", primaryKeySet.size() > 0);
        System.out.println(dbMetaData.getDatabaseProductName());
    }

    /**
     * @throws Exception
     */
    public void testSupportsGetGeneratedKeys() throws Exception {
        DatabaseMetaData dbMetaData = getDatabaseMetaData();
        assertFalse(DatabaseMetaDataUtil.supportsGetGeneratedKeys(dbMetaData));
    }

    public void setUp() {
        include("DatabaseMetaDataUtilTest.dicon");
    }

    /**
     *
     */
    public static class SimpleDataSource implements DataSource {

        private String driverClassName_;

        private String url_;

        private String user_;

        private String password_;

        public Connection getConnection() throws SQLException {
            DriverManagerUtil.registerDriver(driverClassName_);
            return DriverManager.getConnection(url_, user_, password_);
        }

        public Connection getConnection(String username, String password)
                throws SQLException {
            return null;
        }

        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        public void setLogWriter(PrintWriter out) throws SQLException {
        }

        public void setLoginTimeout(int seconds) throws SQLException {
        }

        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        /**
         * @param driverClassName
         */
        public void setDriverClassName(String driverClassName) {
            driverClassName_ = driverClassName;
        }

        /**
         * @param password
         */
        public void setPassword(String password) {
            password_ = password;
        }

        /**
         * @param url
         */
        public void setURL(String url) {
            url_ = url;
        }

        /**
         * @param user
         */
        public void setUser(String user) {
            user_ = user;
        }
    }

}