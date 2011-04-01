/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class RestrictedTransactionManagerImplTest extends TestCase {

    TransactionManager underlyingTm;

    UserTransaction userTransaction;

    TransactionSynchronizationRegistry synchronizationRegistry;

    RestrictedTransactionManagerImpl tm;

    protected void setUp() throws Exception {
        super.setUp();
        underlyingTm = new TransactionManagerImpl();
        userTransaction = new UserTransactionImpl(underlyingTm);
        synchronizationRegistry = new TransactionSynchronizationRegistryImpl(
                underlyingTm);
        tm = new RestrictedTransactionManagerImpl(userTransaction,
                synchronizationRegistry);
    }

    /**
     * @throws Exception
     */
    public void testCommit() throws Exception {
        tm.begin();
        assertEquals(Status.STATUS_ACTIVE, tm.getStatus());
        tm.commit();
        assertEquals(Status.STATUS_NO_TRANSACTION, tm.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testRollback() throws Exception {
        tm.begin();
        assertEquals(Status.STATUS_ACTIVE, tm.getStatus());
        tm.rollback();
        assertEquals(Status.STATUS_NO_TRANSACTION, tm.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testResume() throws Exception {
        tm.begin();
        try {
            tm.resume(null);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSuspend() throws Exception {
        tm.begin();
        try {
            tm.suspend();
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

}
