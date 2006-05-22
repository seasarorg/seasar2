package org.seasar.extension.component.impl;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.framework.unit.S2FrameworkTestCase;

public class SelectableDataSourceProxyTest extends S2FrameworkTestCase {
    private TestDataSource ds1;

    private TestDataSource ds2;

    private SelectableDataSourceProxy proxy;

    protected void setUp() throws Exception {
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    public void test() throws Exception {
        proxy.setDataSourceName("ds1");
        assertEquals(ds1.con, proxy.getConnection());
        assertEquals("getConnection", ds1.text);
        assertEquals(ds1.con, proxy.getConnection("scott", "tiger"));
        assertEquals("getConnection:scott:tiger", ds1.text);
        assertEquals(ds1.hashCode(), proxy.getLoginTimeout());
        assertEquals("getLoginTimeout", ds1.text);
        assertEquals(ds1.out, proxy.getLogWriter());
        assertEquals("getLogWriter", ds1.text);
        proxy.setLoginTimeout(100);
        assertEquals("setLoginTimeout:100", ds1.text);
        proxy.setLogWriter(ds1.out);
        assertEquals("setLogWriter:true", ds1.text);

        proxy.setDataSourceName("ds2");
        assertEquals(ds2.con, proxy.getConnection());
        assertEquals("getConnection", ds2.text);
        assertEquals(ds2.con, proxy.getConnection("scott", "tiger"));
        assertEquals("getConnection:scott:tiger", ds2.text);
        assertEquals(ds2.hashCode(), proxy.getLoginTimeout());
        assertEquals("getLoginTimeout", ds2.text);
        assertEquals(ds2.out, proxy.getLogWriter());
        assertEquals("getLogWriter", ds2.text);
        proxy.setLoginTimeout(200);
        assertEquals("setLoginTimeout:200", ds2.text);
        proxy.setLogWriter(ds2.out);
        assertEquals("setLogWriter:true", ds2.text);
    }

    public static class TestDataSource implements DataSource, InvocationHandler {
        public String text;

        public Connection con = (Connection) Proxy.newProxyInstance(getClass()
                .getClassLoader(), new Class[] { Connection.class }, this);

        public PrintWriter out = new PrintWriter(System.out);

        public Connection getConnection() throws SQLException {
            text = "getConnection";
            return con;
        }

        public Connection getConnection(String username, String password)
                throws SQLException {
            text = "getConnection:" + username + ":" + password;
            return con;
        }

        public int getLoginTimeout() throws SQLException {
            text = "getLoginTimeout";
            return hashCode();
        }

        public PrintWriter getLogWriter() throws SQLException {
            text = "getLogWriter";
            return out;
        }

        public void setLoginTimeout(int seconds) throws SQLException {
            text = "setLoginTimeout:" + seconds;
        }

        public void setLogWriter(PrintWriter out) throws SQLException {
            text = "setLogWriter:" + (this.out == out);
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if ("equals".equals(method.getName())) {
                return new Boolean(con == args[0]);
            } else if ("toString".equals(method.getName())) {
                return toString();
            }
            return null;
        }
    }
}
