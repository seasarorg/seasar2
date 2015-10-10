/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.sql.SQLException;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.mock.sql.MockConnection;
import org.seasar.framework.mock.sql.MockXAConnection;

/**
 * @author higa
 * 
 */
public class ConnectionWrapperImplTest extends S2TestCase {

    private static final String PATH = "connection.dicon";

    private ConnectionWrapper con_;

    private DummyConnectionPool dummyPool_;

    /**
     * @throws Exception
     */
    public void testCloseReally() throws Exception {
        con_.closeReally();
        assertEquals("1", true, con_.isClosed());
    }

    /**
     * @throws Exception
     */
    public void testCloseReallyWithTransaction() throws Exception {
        MockXAConnection xaCon = new MockXAConnection();
        ConnectionWrapperImpl wrapper = new ConnectionWrapperImpl(xaCon, xaCon
                .getConnection(), dummyPool_, null);
        MockConnection con = (MockConnection) wrapper.getPhysicalConnection();
        wrapper.setAutoCommit(false);
        wrapper.closeReally();
        assertTrue(con.isRolledback());
        assertTrue(con.getAutoCommit());
        assertTrue(con.isClosed());
    }

    /**
     * @throws Exception
     */
    public void testClose() throws Exception {
        try {
            con_.close();
            assertEquals("1", true, con_.isClosed());
            assertEquals("2", true, dummyPool_.isCheckIned());
        } finally {
            con_.closeReally();
        }
    }

    /**
     * @throws Exception
     */
    public void testRelease() throws Exception {
        try {
            con_.setTransactionIsolation(100);
            fail("1");
        } catch (SQLException ex) {
            System.out.println(ex);
            assertEquals("2", true, dummyPool_.isReleased());
        } finally {
            con_.closeReally();
        }
    }

    /**
     * @throws Exception
     */
    public void testInit() throws Exception {
        TransactionManager tm = new TransactionManagerImpl();
        try {
            tm.begin();
            Transaction tx = tm.getTransaction();
            XAResource xares = con_.getXAConnection().getXAResource();
            tx.enlistResource(xares);
            tx.commit();
            con_.close();
            con_.init(null);
        } finally {
            con_.closeReally();
        }
    }

    /**
     * @throws Exception
     */
    public void testCleanup() throws Exception {
        TransactionManager tm = new TransactionManagerImpl();
        try {
            tm.begin();
            Transaction tx = tm.getTransaction();
            XAResource xares = con_.getXAConnection().getXAResource();
            tx.enlistResource(xares);
            tx.commit();
            con_.cleanup();
            assertTrue("1", con_.isClosed());
            con_.init(null);
        } finally {
            con_.closeReally();
        }
    }

    /**
     * @throws Exception
     */
    public void testRestrictedOperations() throws Exception {
        TransactionManager tm = new TransactionManagerImpl();
        try {
            tm.begin();
            Transaction tx = tm.getTransaction();
            con_.init(tx);
            XAResource xares = con_.getXAConnection().getXAResource();
            tx.enlistResource(xares);
            try {
                con_.setAutoCommit(true);
                fail("1");
            } catch (SQLException expected) {
            }
            try {
                con_.commit();
                fail("2");
            } catch (SQLException expected) {
            }
            try {
                con_.rollback();
                fail("3");
            } catch (SQLException expected) {
            }
            try {
                con_.setSavepoint();
                fail("4");
            } catch (SQLException expected) {
            }
            try {
                con_.setSavepoint(null);
                fail("5");
            } catch (SQLException expected) {
            }
            assertFalse("6", con_.isClosed());
            tx.commit();
            con_.cleanup();
            assertTrue("7", con_.isClosed());
            con_.init(null);
        } finally {
            con_.closeReally();
        }
    }

    /**
     * @throws Exception
     */
    public void testConnectionErrorOccurred() throws Exception {
        try {
            ((ConnectionEventListener) con_)
                    .connectionErrorOccurred(new ConnectionEvent(con_
                            .getXAConnection()));
            assertTrue(dummyPool_.isReleased());
        } finally {
            con_.closeReally();
        }
    }

    protected void setUp() throws Exception {
        include(PATH);
    }
}