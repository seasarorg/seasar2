/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 *
 */
public class RequiresNewInterceptorTest extends S2TestCase {

    private static final String PATH = "RequiresNewInterceptorTest.dicon";

    private TxBean txBean_;

    private ExceptionBean exBean_;

    private TransactionManager tm_;

    protected void setUp() throws Exception {
        include(PATH);
    }

    /**
     * @throws Exception
     */
    public void testInvoke() throws Exception {
        assertEquals("1", true, txBean_.hasTransaction());
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testInvoke2() throws Exception {
        tm_.begin();
        Transaction tx = tm_.getTransaction();
        System.out.println("preTx:" + tx);
        assertEquals("1", true, txBean_.hasTransaction());
        assertEquals("2", Status.STATUS_ACTIVE, tm_.getStatus());
        assertEquals("3", tx, tm_.getTransaction());
        tm_.commit();
    }

    /**
     * @throws Exception
     */
    public void testMarkRollback() throws Exception {
        tm_.begin();
        Transaction tx = tm_.getTransaction();
        System.out.println("preTx:" + tx);
        Transaction newTx = txBean_.markRollback();
        assertNotSame(tx, newTx);
    }

    /**
     * @throws Exception
     */
    public void testInvokeException() throws Exception {
        try {
            exBean_.invoke();
            fail("1");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
    }

}
