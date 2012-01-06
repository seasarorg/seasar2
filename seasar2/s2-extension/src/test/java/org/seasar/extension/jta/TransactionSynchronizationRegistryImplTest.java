/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author nakamura
 * 
 */
public class TransactionSynchronizationRegistryImplTest extends S2TestCase {

    private TransactionSynchronizationRegistryImpl tsr;

    private TransactionManager tm;

    protected void setUp() throws Exception {
        super.setUp();
        include("j2ee.dicon");
    }

    /**
     * 
     */
    public void testPutResourceForException() {
        try {
            tsr.putResource("hoge", "foo");
            fail();
        } catch (IllegalStateException ignore) {
        }
    }

    /**
     * 
     */
    public void testPutResourceForExceptionTx() {
        tsr.putResource("hoge", "foo");
        try {
            tsr.putResource(null, "foo");
            fail();
        } catch (NullPointerException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testGetResourceForException() throws Exception {
        try {
            tsr.getResource("hoge");
            fail();
        } catch (IllegalStateException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testGetResourceTx() throws Exception {
        assertNull(tsr.getResource("hoge"));
        tsr.putResource("hoge", "foo");
        assertEquals("foo", tsr.getResource("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testSetRollbackOnlyForException() throws Exception {
        try {
            tsr.setRollbackOnly();
            fail();
        } catch (IllegalStateException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSetRollbackOnlyTx() throws Exception {
        tsr.setRollbackOnly();
        assertEquals(Status.STATUS_MARKED_ROLLBACK, tsr.getTransactionStatus());
    }

    /**
     * @throws Exception
     */
    public void testGetRollbackOnlyForException() throws Exception {
        try {
            tsr.getRollbackOnly();
            fail();
        } catch (IllegalStateException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testGetRollbackOnlyTx() throws Exception {
        assertFalse(tsr.getRollbackOnly());
        tsr.setRollbackOnly();
        assertTrue(tsr.getRollbackOnly());
    }

    /**
     * @throws Exception
     */
    public void testGetTransactionKeyForException() throws Exception {
        assertNull(tsr.getTransactionKey());
    }

    /**
     * @throws Exception
     */
    public void testGetTransactionKeyTx() throws Exception {
        assertNotNull(tsr.getTransactionKey());
    }

    /**
     * @throws Exception
     */
    public void testRegisterInterposedSynchronizationForException()
            throws Exception {
        try {
            tsr.registerInterposedSynchronization(new Synchronization() {
                public void beforeCompletion() {
                }

                public void afterCompletion(int arg0) {
                }
            });
            fail();
        } catch (IllegalStateException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testRegisterInterposedSynchronization() throws Exception {
        Sync.result = new StringBuffer();
        tm.begin();
        tsr.registerInterposedSynchronization(new Sync('a', 'b'));
        tsr.registerInterposedSynchronization(new Sync('c', 'd'));
        tm.getTransaction().registerSynchronization(new Sync('e', 'f'));
        tm.commit();
        assertEquals("eacbdf", Sync.result.toString());
    }

    private static class Sync implements Synchronization {

        private static StringBuffer result;

        private char before;

        private char after;

        /**
         * @param before
         * @param after
         */
        public Sync(char before, char after) {
            this.before = before;
            this.after = after;
        }

        public void beforeCompletion() {
            result.append(before);
        }

        public void afterCompletion(int status) {
            result.append(after);
        }
    }
}
