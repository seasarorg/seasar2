/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TransactionManagerImplTest extends TestCase {

    private TransactionManager tm_;

    public TransactionManagerImplTest(String name) {
        super(name);
    }

    public void testResume() throws Exception {
        tm_.begin();
        Transaction tx = tm_.suspend();
        tm_.begin();
        tm_.commit();
        tm_.resume(tx);
        tm_.commit();
    }

    public void testCommit() throws Exception {
        tm_.begin();
        tm_.commit();
        assertEquals("1", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
    }

    public void testRollback() throws Exception {
        tm_.begin();
        tm_.rollback();
        assertEquals("1", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
    }

    protected void setUp() throws Exception {
        tm_ = new TransactionManagerImpl();
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        return new TestSuite(TransactionManagerImplTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner
                .main(new String[] { TransactionManagerImplTest.class.getName() });
    }
}