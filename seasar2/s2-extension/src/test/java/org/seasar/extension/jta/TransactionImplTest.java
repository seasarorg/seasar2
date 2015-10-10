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
package org.seasar.extension.jta;

import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import junit.framework.TestCase;

import org.seasar.extension.jta.xa.DefaultXAResource;
import org.seasar.framework.util.TransactionUtil;

/**
 * @author higa
 * 
 */
public class TransactionImplTest extends TestCase {

    private TransactionImpl tx_;

    protected void setUp() throws Exception {
        tx_ = new TransactionImpl();
    }

    /**
     * @throws Exception
     */
    public void testEquals() throws Exception {
        assertEquals("1", tx_, tx_);
        assertTrue("2", !tx_.equals(null));
        assertTrue("3", !tx_.equals(new TransactionImpl()));
        assertTrue("4", !tx_.equals("test"));
    }

    /**
     * @throws Exception
     */
    public void testBegin() throws Exception {
        tx_.begin();
        assertEquals("2", Status.STATUS_ACTIVE, tx_.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testSuspend() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.suspend();
        assertEquals("1", true, tx_.isSuspended());
        assertEquals("2", DefaultXAResource.RS_SUSPENDED, xaRes.getStatus());
        try {
            tx_.suspend();
            fail("3");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tx_.resume();
        tx_.commit();
        try {
            tx_.suspend();
            fail("4");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testSuspend_MarkedRollback() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.setRollbackOnly();
        tx_.suspend();
        assertEquals("1", true, tx_.isSuspended());
        assertEquals("2", DefaultXAResource.RS_SUSPENDED, xaRes.getStatus());
        try {
            tx_.suspend();
            fail("3");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tx_.resume();
        tx_.rollback();
        try {
            tx_.suspend();
            fail("4");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testResume() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.suspend();
        tx_.resume();
        assertEquals("1", false, tx_.isSuspended());
        assertEquals("2", DefaultXAResource.RS_ACTIVE, xaRes.getStatus());
        try {
            tx_.resume();
            fail("3");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testOnePhaseCommit() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Sync sync = new Sync(tx_);
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.registerSynchronization(sync);
        tx_.commit();
        assertTrue("1", sync.beforeCompleted_);
        assertTrue("2", sync.afterCompleted_);
        assertEquals("3", Status.STATUS_COMMITTED, sync.completedStatus_);
        assertEquals("4", DefaultXAResource.RS_NONE, xaRes.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testTwoPhaseCommit() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        DefaultXAResource xaRes2 = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.enlistResource(xaRes2);
        tx_.commit();
        assertEquals("1", Status.STATUS_NO_TRANSACTION, tx_.getStatus());
        assertEquals("2", DefaultXAResource.RS_NONE, xaRes.getStatus());
        assertEquals("3", DefaultXAResource.RS_NONE, xaRes2.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testLastResourceCommitOptimization() throws Exception {
        final boolean[] onePhaseFlags = new boolean[3];
        DefaultXAResource[] xaRes = new DefaultXAResource[3];
        for (int i = 0; i < 3; ++i) {
            final int index = i;
            xaRes[i] = new DefaultXAResource() {
                protected void doCommit(Xid xid, boolean onePhase)
                        throws XAException {
                    onePhaseFlags[index] = onePhase;
                }
            };
        }
        tx_.begin();
        for (int i = 0; i < 3; ++i) {
            tx_.enlistResource(xaRes[i]);
        }
        tx_.commit();
        assertEquals("1", Status.STATUS_NO_TRANSACTION, tx_.getStatus());
        assertTrue("2", onePhaseFlags[0]);
        assertFalse("3", onePhaseFlags[1]);
        assertFalse("4", onePhaseFlags[2]);
    }

    /**
     * @throws Exception
     */
    public void testCommitForException() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.suspend();
        try {
            tx_.commit();
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tx_.begin();
        tx_.commit();
        try {
            tx_.commit();
            fail("2");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testOnePhaseCommitFail() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource() {
            protected int doPrepare(Xid xid) throws XAException {
                TransactionImplTest.fail("0");
                return XAResource.XA_OK;
            }

            protected void doCommit(Xid xid, boolean onePhase)
                    throws XAException {
                throw new XAException();
            }
        };
        tx_.begin();
        tx_.enlistResource(xaRes);
        try {
            tx_.commit();
            fail("1");
        } catch (RollbackException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testTwoPhaseCommitFail() throws Exception {
        final boolean[] result = new boolean[4];
        DefaultXAResource xaRes0 = new DefaultXAResource() {
            protected int doPrepare(Xid xid) throws XAException {
                return XAResource.XA_RDONLY;
            }

            protected void doCommit(Xid xid, boolean onePhase)
                    throws XAException {
                TransactionImplTest.fail("0");
            }

            protected void doRollback(Xid xid) throws XAException {
                result[0] = true;
            }
        };
        DefaultXAResource xaRes1 = new DefaultXAResource() {
            protected int doPrepare(Xid xid) throws XAException {
                return XAResource.XA_OK;
            }

            protected void doCommit(Xid xid, boolean onePhase)
                    throws XAException {
                TransactionImplTest.fail("1");
            }

            protected void doRollback(Xid xid) throws XAException {
                result[1] = true;
            }
        };
        DefaultXAResource xaRes2 = new DefaultXAResource() {
            protected int doPrepare(Xid xid) throws XAException {
                throw new XAException();
            }

            protected void doCommit(Xid xid, boolean onePhase)
                    throws XAException {
                TransactionImplTest.fail("2");
            }

            protected void doRollback(Xid xid) throws XAException {
                result[2] = true;
            }
        };
        DefaultXAResource xaRes3 = new DefaultXAResource() {
            protected int doPrepare(Xid xid) throws XAException {
                TransactionImplTest.fail("3");
                return XAResource.XA_OK;
            }

            protected void doCommit(Xid xid, boolean onePhase)
                    throws XAException {
                TransactionImplTest.fail("4");
            }

            protected void doRollback(Xid xid) throws XAException {
                result[3] = true;
            }
        };

        tx_.begin();
        tx_.enlistResource(xaRes3);
        tx_.enlistResource(xaRes2);
        tx_.enlistResource(xaRes1);
        tx_.enlistResource(xaRes0);
        try {
            tx_.commit();
            fail("5");
        } catch (RollbackException ex) {
            System.out.println(ex);
        }
        assertFalse("6", result[0]);
        assertTrue("7", result[1]);
        assertFalse("8", result[2]);
        assertTrue("9", result[3]);
    }

    /**
     * @throws Exception
     */
    public void testRollback() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Sync sync = new Sync(tx_);
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.registerSynchronization(sync);
        tx_.rollback();
        assertFalse("1", sync.beforeCompleted_);
        assertTrue("2", sync.afterCompleted_);
        assertEquals("3", Status.STATUS_ROLLEDBACK, sync.completedStatus_);
        assertEquals("4", DefaultXAResource.RS_NONE, xaRes.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testRollbackForException() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.suspend();
        try {
            tx_.rollback();
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tx_.begin();
        tx_.rollback();
        try {
            tx_.rollback();
            fail("2");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetRollbackOnly() throws Exception {
        tx_.begin();
        tx_.setRollbackOnly();
        assertEquals("1", Status.STATUS_MARKED_ROLLBACK, tx_.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testSetRollbackOnlyForException() throws Exception {
        tx_.begin();
        tx_.commit();
        try {
            tx_.setRollbackOnly();
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testDelistResource() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        tx_.begin();
        tx_.enlistResource(xaRes);
        tx_.delistResource(xaRes, XAResource.TMSUCCESS);
        assertEquals("1", DefaultXAResource.RS_SUCCESS, xaRes.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testDelistResourceForException() throws Exception {
        tx_.begin();
        DefaultXAResource xaRes = new DefaultXAResource();
        try {
            tx_.delistResource(xaRes, XAResource.TMSUCCESS);
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tx_.suspend();
        tx_.resume();
        tx_.commit();
        try {
            tx_.delistResource(xaRes, XAResource.TMSUCCESS);
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testBeforeCompletion() throws Exception {
        tx_.begin();
        Sync sync = new Sync(tx_);
        tx_.registerSynchronization(sync);
        tx_.registerSynchronization(new ExceptionSync());
        try {
            tx_.commit();
            fail("1");
        } catch (RollbackException ex) {
            System.out.println(ex);
        }
        assertTrue(sync.beforeCompleted_);
        assertTrue(sync.afterCompleted_);
    }

    private class Sync implements Synchronization {

        private boolean beforeCompleted_ = false;

        private boolean afterCompleted_ = false;

        private int completedStatus_ = Status.STATUS_UNKNOWN;

        private Transaction tx_;

        /**
         * @param tx
         */
        public Sync(Transaction tx) {
            tx_ = tx;
        }

        public void beforeCompletion() {
            beforeCompleted_ = true;
        }

        public void afterCompletion(int status) {
            assertEquals(Status.STATUS_NO_TRANSACTION, TransactionUtil
                    .getStatus(tx_));
            afterCompleted_ = true;
            completedStatus_ = status;
        }
    }

    private class ExceptionSync implements Synchronization {

        public void beforeCompletion() {
            throw new RuntimeException("hoge");
        }

        public void afterCompletion(int status) {
        }
    }

}
