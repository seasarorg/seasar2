package org.seasar.extension.datasource.impl;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.framework.unit.S2FrameworkTestCase;

public class SelectableDataSourceProxyTest extends S2FrameworkTestCase {

    private DataSourceFactory factory;

    private SelectableDataSourceProxy proxy;

    private TestDataSource fooDataSource;

    private TestDataSource barDataSource;

    protected void setUp() throws Exception {
        register(DataSourceFactoryImpl.class);
        register(SelectableDataSourceProxy.class, "dataSource");
        register(TestDataSource.class, "fooDataSource");
        register(TestDataSource.class, "barDataSource");
    }

    public void testAll() throws Exception {
        factory.setSelectableDataSourceName("foo");
        assertEquals(fooDataSource.con, proxy.getConnection());
        assertEquals("getConnection", fooDataSource.text);
        assertEquals(fooDataSource.con, proxy.getConnection("scott", "tiger"));
        assertEquals("getConnection:scott:tiger", fooDataSource.text);
        assertEquals(fooDataSource.hashCode(), proxy.getLoginTimeout());
        assertEquals("getLoginTimeout", fooDataSource.text);
        assertEquals(fooDataSource.out, proxy.getLogWriter());
        assertEquals("getLogWriter", fooDataSource.text);
        proxy.setLoginTimeout(100);
        assertEquals("setLoginTimeout:100", fooDataSource.text);
        proxy.setLogWriter(fooDataSource.out);
        assertEquals("setLogWriter:true", fooDataSource.text);

        factory.setSelectableDataSourceName("bar");
        assertEquals(barDataSource.con, proxy.getConnection());
        assertEquals("getConnection", barDataSource.text);
        assertEquals(barDataSource.con, proxy.getConnection("scott", "tiger"));
        assertEquals("getConnection:scott:tiger", barDataSource.text);
        assertEquals(barDataSource.hashCode(), proxy.getLoginTimeout());
        assertEquals("getLoginTimeout", barDataSource.text);
        assertEquals(barDataSource.out, proxy.getLogWriter());
        assertEquals("getLogWriter", barDataSource.text);
        proxy.setLoginTimeout(200);
        assertEquals("setLoginTimeout:200", barDataSource.text);
        proxy.setLogWriter(barDataSource.out);
        assertEquals("setLogWriter:true", barDataSource.text);
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
