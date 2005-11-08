package org.seasar.extension.dbcp.impl;

import java.sql.SQLException;

import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.dbcp.impl.ConnectionPoolImpl;
import org.seasar.extension.unit.S2TestCase;

public class ConnectionPoolImplTest extends S2TestCase {

	private static final String PATH = "connection.dicon";
    private ConnectionPool pool_;
	private ConnectionPool pool2_;
    private TransactionManager tm_;
    private boolean checkOuted_ = false;

    public ConnectionPoolImplTest(String name) {
        super(name);
    }

    public void testCheckOut() throws Exception {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    ConnectionWrapper con = pool_.checkOut();
                    checkOuted_ = true;
                    Thread.sleep(4000);
                    pool_.checkIn(con);
                    System.out.println("checkIn");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        Thread th = new Thread(r);
        th.start();
        while (!checkOuted_ && th.isAlive()) {
            Thread.sleep(100);
        }
        ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
        assertTrue("1", con.isClosed());
    }

    public void testReuseConnection() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
		ConnectionWrapper con2 = pool_.checkOut();
		pool_.checkIn(con2);
        assertSame("1", con, con2);
    }
	
    public void testCloseLogicalConnection() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        assertEquals("1", 1, pool_.getActivePoolSize());
        assertEquals("2", 0, pool_.getFreePoolSize());
        con.close();
        assertEquals("3", 0, pool_.getActivePoolSize());
        assertEquals("4", 1, pool_.getFreePoolSize());
    }
	
    public void testClose() throws Exception {
		pool_.checkOut();
        pool_.close();
        assertEquals("1", 0, pool_.getActivePoolSize());
        assertEquals("2", 0, pool_.getFreePoolSize());
    }

    public void testTransaction() throws Exception {
    	tm_.begin();
		ConnectionWrapper con = pool_.checkOut();
		assertEquals("1", 0, pool_.getActivePoolSize());
		assertEquals("2", 1, pool_.getTxActivePoolSize());
        con.close();
		assertEquals("3", 0, pool_.getActivePoolSize());
		assertEquals("4", 1, pool_.getTxActivePoolSize());
		ConnectionWrapper con2 = pool_.checkOut();
		assertEquals("5", 1, pool_.getTxActivePoolSize());
		assertEquals("6", 0, pool_.getActivePoolSize());
		con2.close();
		tm_.commit();
		assertEquals("7", 0, pool_.getActivePoolSize());
		assertEquals("8", 0, pool_.getTxActivePoolSize());
		assertEquals("9", 1, pool_.getFreePoolSize());
		assertSame("10", con, con2);
    }
    
	public void testTransaction2() throws Exception {
		tm_.begin();
		ConnectionWrapper con = pool_.checkOut();
		ConnectionWrapper con2 = pool_.checkOut();
		assertEquals("1", 0, pool_.getActivePoolSize());
		assertEquals("2", 1, pool_.getTxActivePoolSize());
		con.close();
		con2.close();
		assertEquals("3", 0, pool_.getActivePoolSize());
		assertEquals("4", 1, pool_.getTxActivePoolSize());		
		tm_.commit();
		assertEquals("5", 0, pool_.getActivePoolSize());
		assertEquals("6", 0, pool_.getTxActivePoolSize());
		assertEquals("7", 1, pool_.getFreePoolSize());
	}
    
	public void testIrregularUsecase() throws Exception {
		ConnectionWrapper con = pool_.checkOut();
		assertEquals("1", 1, pool_.getActivePoolSize());
		assertEquals("2", 0, pool_.getTxActivePoolSize());
		tm_.begin();
		con.close();
		assertEquals("3", 0, pool_.getActivePoolSize());
		assertEquals("4", 0, pool_.getTxActivePoolSize());
		assertEquals("5", 1, pool_.getFreePoolSize());
		tm_.commit();
		assertEquals("6", 0, pool_.getActivePoolSize());
		assertEquals("7", 0, pool_.getTxActivePoolSize());
		assertEquals("8", 1, pool_.getFreePoolSize());
	}
	
	public void test2PC() throws Exception {
		tm_.begin();
		ConnectionWrapper con = pool_.checkOut();
		ConnectionWrapper con2 = pool2_.checkOut();
		assertEquals("1", 0, pool_.getActivePoolSize());
		assertEquals("2", 0, pool2_.getActivePoolSize());
		assertEquals("3", 1, pool_.getTxActivePoolSize());
		assertEquals("4", 1, pool2_.getTxActivePoolSize());
		con.close();
		con2.close();
		assertEquals("5", 0, pool_.getActivePoolSize());
		assertEquals("6", 0, pool2_.getActivePoolSize());
		assertEquals("7", 1, pool_.getTxActivePoolSize());		
		assertEquals("8", 1, pool2_.getTxActivePoolSize());
		tm_.commit();
		assertEquals("9", 0, pool_.getActivePoolSize());
		assertEquals("10", 0, pool2_.getActivePoolSize());
		assertEquals("11", 0, pool_.getTxActivePoolSize());
		assertEquals("12", 0, pool2_.getTxActivePoolSize());
		assertEquals("13", 1, pool_.getFreePoolSize());
		assertEquals("14", 1, pool2_.getFreePoolSize());
	}
		
    public void testTimeout() throws Exception {
		ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
        Thread.sleep(8000);
        assertEquals("1", 0, pool_.getFreePoolSize());
    }
    
	public void testTimeout2() throws Exception {
		ConnectionWrapper con = pool_.checkOut();
		pool_.checkIn(con);
		con = pool_.checkOut();
		Thread.sleep(8000);
		con.getAutoCommit();
	}

	public void testRequiredTransaction() throws Exception {
		((ConnectionPoolImpl) pool_).setAllowLocalTx(false);
		try {
			pool_.checkOut();
			fail("1");
		} catch (IllegalStateException expected) {}
	}

	public void testMaxPoolSize0() throws Exception {
		((ConnectionPoolImpl) pool_).setMaxPoolSize(0);
		ConnectionWrapper con = pool_.checkOut();
		pool_.checkIn(con);
		assertEquals("1", 0, pool_.getFreePoolSize());

		int size = 100;
		ConnectionWrapper[] cons = new ConnectionWrapper[size];
		for (int i = 0; i < size; ++i) {
			cons[i] = pool_.checkOut();
		}
		for (int i = 0; i < size; ++i) {
			pool_.checkIn(cons[i]);
		}
		assertEquals("2", 0, pool_.getFreePoolSize());
	}

	public void testCheckInTxNotify() throws Exception {
        tm_.begin();
        ((ConnectionPoolImpl) pool_).setMaxPoolSize(1);
        ConnectionWrapper con = pool_.checkOut();
        Thread thread = new Thread() {
            public void run() {
                try {
                    pool_.checkOut();
                    synchronized (ConnectionPoolImplTest.this) {
                        checkOuted_ = true;
                    }
                }
                catch (SQLException e) {
                    fail(e.toString());
                }
            }
        };
        thread.start();

        con.close();
        Thread.sleep(100);
        assertFalse("1", checkOuted_);

        tm_.commit();
        Thread.sleep(100);
        assertTrue("2", checkOuted_);
        thread.interrupt();
    }

	public void testExpired() throws Exception {
        ((ConnectionPoolImpl) pool_).setTimeout(0);
        synchronized (pool_) {
	        ConnectionWrapper con = pool_.checkOut();
	        con.close();
	        Thread.sleep(200);
	        con = pool_.checkOut();
	        con.close();
        }
    }
	protected void setUp() throws Exception {
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite ( ) {
        return new TestSuite(ConnectionPoolImplTest.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.main(new String[]{ConnectionPoolImplTest.class.getName()});
    }
}