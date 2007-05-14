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
package org.seasar.extension.dbcp.impl;

import java.sql.Connection;
import java.sql.SQLException;

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
        assertSame("1", con, con2);
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
        ((ConnectionPoolImpl) pool_).setReadOnly(true);
        ((ConnectionPoolImpl) pool_)
                .setTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
        ConnectionWrapper con = pool_.checkOut();
        assertTrue(con.isReadOnly());
        assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, con
                .getTransactionIsolation());
    }

    protected void setUp() throws Exception {
        include(PATH);
    }
}