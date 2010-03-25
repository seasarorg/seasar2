/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbcp.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.transaction.Synchronization;
import javax.transaction.TransactionManager;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.timer.TimeoutManager;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class ConnectionPoolImplTest extends S2TestCase {

    private static final String PATH = "connection.dicon";

    private ConnectionPool pool_;

    private ConnectionPool pool2_;

    private TransactionManager tm_;

    private boolean checkOuted_ = false;

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testReuseConnection() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
        ConnectionWrapper con2 = pool_.checkOut();
        pool_.checkIn(con2);
        assertSame(con.getXAConnection(), con2.getXAConnection());
        assertSame(con.getPhysicalConnection(), con2.getPhysicalConnection());
    }

    /**
     * @throws Exception
     */
    public void testCloseLogicalConnection() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        assertEquals("1", 1, pool_.getActivePoolSize());
        assertEquals("2", 0, pool_.getFreePoolSize());
        con.close();
        assertEquals("3", 0, pool_.getActivePoolSize());
        assertEquals("4", 1, pool_.getFreePoolSize());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCloseLogicalConnection_afterCompletion() throws Exception {
        tm_.begin();
        final Connection con = pool_.checkOut();
        tm_.getTransaction().registerSynchronization(new Synchronization() {
            public void beforeCompletion() {
            }

            public void afterCompletion(int status) {
                try {
                    con.close();
                } catch (SQLException e) {
                    fail();
                }
            }
        });
        assertEquals(1, pool_.getTxActivePoolSize());
        assertEquals(0, pool_.getFreePoolSize());
        tm_.commit();
        assertTrue(con.isClosed());
        assertNull(((ConnectionWrapperImpl) con).getXAConnection());
        assertNull(((ConnectionWrapperImpl) con).getPhysicalConnection());
        assertEquals(0, pool_.getTxActivePoolSize());
        assertEquals(1, pool_.getFreePoolSize());
    }

    /**
     * @throws Exception
     */
    public void testClose() throws Exception {
        Thread.sleep(1500);
        assertEquals(2, TimeoutManager.getInstance().getTimeoutTaskCount());
        ConnectionWrapper wrapper = pool_.checkOut();
        pool_.checkIn(wrapper);
        assertEquals(3, TimeoutManager.getInstance().getTimeoutTaskCount());
        pool_.close();
        assertEquals(0, pool_.getActivePoolSize());
        assertEquals(0, pool_.getFreePoolSize());
        pool2_.close();
        Thread.sleep(1500);
        assertEquals(0, TimeoutManager.getInstance().getTimeoutTaskCount());
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testTransaction3() throws Exception {
        ((ConnectionPoolImpl) pool_).setMaxPoolSize(0);
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
        assertEquals("7", 0, pool_.getFreePoolSize());
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testTimeout() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
        Thread.sleep(8000);
        assertEquals("1", 0, pool_.getFreePoolSize());
    }

    /**
     * @throws Exception
     */
    public void testTimeout2() throws Exception {
        ConnectionWrapper con = pool_.checkOut();
        pool_.checkIn(con);
        con = pool_.checkOut();
        Thread.sleep(8000);
        con.getAutoCommit();
    }

    /**
     * @throws Exception
     */
    public void testRequiredTransaction() throws Exception {
        ((ConnectionPoolImpl) pool_).setAllowLocalTx(false);
        try {
            pool_.checkOut();
            fail("1");
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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
                } catch (SQLException e) {
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testConnectionStatus() throws Exception {
        ((ConnectionPoolImpl) pool_).setMaxPoolSize(1);
        ((ConnectionPoolImpl) pool_).setReadOnly(true);
        ((ConnectionPoolImpl) pool_)
                .setTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);

        ConnectionWrapper con = pool_.checkOut();
        assertTrue(con.getAutoCommit());
        assertTrue(con.isReadOnly());
        assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, con
                .getTransactionIsolation());
        con.setAutoCommit(false);
        con.setReadOnly(false);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        con.close();

        ConnectionWrapper con2 = pool_.checkOut();
        assertTrue(con2.getAutoCommit());
        assertTrue(con2.isReadOnly());
        assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, con2
                .getTransactionIsolation());
        con2.close();
    }

    /**
     * 
     * @throws Exception
     */
    public void testValidationQuery() throws Exception {
        ((ConnectionPoolImpl) pool_).setValidationQuery("select * from dual");
        ((ConnectionPoolImpl) pool_).setValidationInterval(100);
        ((ConnectionPoolImpl) pool_).setTimeout(600 * 1000);
        ((ConnectionPoolImpl) pool_).setMaxPoolSize(2);
        ConnectionWrapper con1 = pool_.checkOut();
        XAConnection xaCon1 = con1.getXAConnection();
        pool_.checkIn(con1);
        Thread.sleep(200);
        ConnectionWrapper con2 = pool_.checkOut();
        XAConnection xaCon2 = con2.getXAConnection();
        assertSame(xaCon1, xaCon2);
        pool_.checkIn(con2);
        ((ConnectionPoolImpl) pool_)
                .setValidationQuery("select * from hogehoge");
        con2 = pool_.checkOut();
        xaCon2 = con2.getXAConnection();
        assertSame(xaCon1, xaCon2);
        ConnectionWrapper con3 = pool_.checkOut();
        pool_.checkIn(con3);
        pool_.checkIn(con2);
        Thread.sleep(200);
        con2 = pool_.checkOut();
        xaCon2 = con2.getXAConnection();
        assertNotNull(con2);
        assertNotSame(xaCon1, xaCon2);
        pool_.checkIn(con2);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMaxWait() throws Exception {
        ((ConnectionPoolImpl) pool_).setMaxPoolSize(1);
        ((ConnectionPoolImpl) pool_).setMaxWait(0L);
        Connection con = pool_.checkOut();
        try {
            pool_.checkOut();
            fail();
        } catch (SQLException e) {
            System.out.println(e);
        }

        ((ConnectionPoolImpl) pool_).setMaxWait(1000L);
        final Thread currentThread = Thread.currentThread();
        Thread otherThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(500L);
                    synchronized (pool_) {
                        pool_.notifyAll();
                    }
                } catch (InterruptedException ignore) {
                }
            }
        };
        long t1 = System.currentTimeMillis();
        try {
            otherThread.start();
            pool_.checkOut();
            fail();
        } catch (SQLException e) {
            System.out.println(e);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        assertTrue(t2 - t1 >= 1000);

        ((ConnectionPoolImpl) pool_).setMaxWait(-1L);
        otherThread = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 3; ++i) {
                        Thread.sleep(500L);
                        synchronized (pool_) {
                            pool_.notifyAll();
                        }
                    }
                    Thread.sleep(500L);
                    currentThread.interrupt();
                } catch (InterruptedException ignore) {
                }
            }
        };
        t1 = System.currentTimeMillis();
        try {
            otherThread.start();
            pool_.checkOut();
            fail();
        } catch (SQLException e) {
            System.out.println(e);
        }
        t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        assertTrue(t2 - t1 >= 2000);
    }

    protected void setUp() throws Exception {
        include(PATH);
    }
}