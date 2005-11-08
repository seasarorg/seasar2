package org.seasar.framework.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;

public class DatabaseMetaDataUtilTest extends S2FrameworkTestCase {

    public void testGetColumnList() throws Exception {
        DatabaseMetaData dbMetaData = getConnection().getMetaData();
        Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "emp");
        System.out.println(columnSet);
        assertTrue("1", columnSet.size() > 0);
    }

    public void testGetColumnListForNotExistTable() throws Exception {
        DatabaseMetaData dbMetaData = getConnection().getMetaData();
        Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "_emp_");
        assertEquals("1", 0, columnSet.size());
    }

    public void testGetColumnListForSchema() throws Exception {
        DatabaseMetaData dbMetaData = getConnection().getMetaData();
        Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "SA.emp");
        System.out.println(columnSet);
        assertTrue("1", columnSet.size() > 0);
    }

    public void testGetPrimaryKeyList() throws Exception {
        DatabaseMetaData dbMetaData = getConnection().getMetaData();
        Set primaryKeySet = DatabaseMetaDataUtil.getPrimaryKeySet(dbMetaData,
            "emp");
        System.out.println(primaryKeySet);
        assertTrue("1", primaryKeySet.size() > 0);
        System.out.println(dbMetaData.getDatabaseProductName());
    }

    public void setUp() {
        include("DatabaseMetaDataUtilTest.dicon");
    }

    public static class SimpleDataSource implements DataSource {

        private String driverClassName_;
        private String url_;
        private String user_;
        private String password_;

        public Connection getConnection() throws SQLException {
            try {
                Class.forName(driverClassName_);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundRuntimeException(e);
            }
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

        public void setDriverClassName(String driverClassName) {
            driverClassName_ = driverClassName;
        }

        public void setPassword(String password) {
            password_ = password;
        }

        public void setURL(String url) {
            url_ = url;
        }

        public void setUser(String user) {
            user_ = user;
        }
    }

}