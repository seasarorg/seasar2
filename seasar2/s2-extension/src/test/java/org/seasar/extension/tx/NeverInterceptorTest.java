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
package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

public class NeverInterceptorTest extends S2TestCase {

    private static final String PATH = "NeverInterceptorTest.dicon";

    private TxBean txBean_;

    private TransactionManager tm_;

    protected void setUp() throws Exception {
        include(PATH);
    }

    public void testInvokeWithoutTxn() throws Exception {
        assertEquals("1", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("2", txBean_.hasTransaction());
        assertEquals("3", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
    }

    public void testInvokeWithinTxn() throws Exception {
        tm_.begin();
        try {
            txBean_.hasTransaction();
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
        tm_.commit();
    }

}
