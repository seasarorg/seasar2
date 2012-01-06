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
package org.seasar.extension.tx;

import java.rmi.AccessException;
import java.rmi.RemoteException;

import javax.transaction.SystemException;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class AbstTxInterceptorTest extends S2TestCase {

    private static final String PATH = "AbstTxInterceptorTest.dicon";

    private ExceptionBean exBean;

    private TestAdapter adapter;

    private TestInterceptor testTx;

    protected void setUp() throws Exception {
        include(PATH);
    }

    /**
     * @throws Exception
     */
    public void testType() throws Exception {
        try {
            testTx.addCommitRule(Throwable.class);
            testTx.addCommitRule(Error.class);
            testTx.addCommitRule(Exception.class);
            testTx.addCommitRule(RuntimeException.class);
            testTx.addCommitRule(Object.class);
            fail("1");
        } catch (IllegalArgumentException expected) {
            System.out.println(expected);
        }

        try {
            testTx.addRollbackRule(Throwable.class);
            testTx.addRollbackRule(Error.class);
            testTx.addRollbackRule(Exception.class);
            testTx.addRollbackRule(RuntimeException.class);
            testTx.addRollbackRule(String.class);
            fail("2");
        } catch (IllegalArgumentException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testNoRule() throws Exception {
        try {
            exBean.invoke();
            fail("1");
        } catch (Exception expected) {
            System.out.println(expected);
        }
        assertTrue(adapter.rollbackOnly);
    }

    /**
     * @throws Exception
     */
    public void testCommitRule() throws Exception {
        testTx.addRollbackRule(RuntimeException.class);
        testTx.addRollbackRule(RemoteException.class);
        testTx.addCommitRule(Exception.class);
        try {
            exBean.invoke(new SystemException());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertFalse(adapter.rollbackOnly);
    }

    /**
     * @throws Exception
     */
    public void testRollbackRule1() throws Exception {
        testTx.addRollbackRule(RuntimeException.class);
        testTx.addRollbackRule(RemoteException.class);
        testTx.addCommitRule(Exception.class);
        try {
            exBean.invoke(new UnsupportedOperationException());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertTrue(adapter.rollbackOnly);
    }

    /**
     * @throws Exception
     */
    public void testRollbackRule2() throws Exception {
        testTx.addRollbackRule(RuntimeException.class);
        testTx.addRollbackRule(RemoteException.class);
        testTx.addCommitRule(Exception.class);
        try {
            exBean.invoke(new AccessException(""));
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertTrue(adapter.rollbackOnly);
    }

    /**
     * @throws Exception
     */
    public void testRollbackRule3() throws Exception {
        testTx.addRollbackRule(RuntimeException.class);
        testTx.addRollbackRule(RemoteException.class);
        testTx.addCommitRule(Exception.class);
        try {
            exBean.invoke(new Throwable());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertTrue(adapter.rollbackOnly);
    }

    /**
     * @throws Exception
     */
    public void testRollbackRule4() throws Exception {
        testTx.addRollbackRule(RuntimeException.class);
        testTx.addRollbackRule(RemoteException.class);
        testTx.addCommitRule(Exception.class);
        try {
            exBean.invoke(new OutOfMemoryError());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertTrue(adapter.rollbackOnly);
    }

    /**
     * 
     */
    public static class TestInterceptor extends AbstractTxInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return transactionManagerAdapter
                    .required(new DefaultTransactionCallback(invocation,
                            txRules));
        }
    }

    /**
     * 
     */
    public static class TestAdapter implements TransactionManagerAdapter {
        private boolean rollbackOnly;

        public void setRollbackOnly() {
            rollbackOnly = true;
        }

        public Object mandatory(TransactionCallback callback) throws Throwable {
            return callback.execute(this);
        }

        public Object never(TransactionCallback callback) throws Throwable {
            return callback.execute(this);
        }

        public Object notSupported(TransactionCallback callback)
                throws Throwable {
            return callback.execute(this);
        }

        public Object required(TransactionCallback callback) throws Throwable {
            return callback.execute(this);
        }

        public Object requiresNew(TransactionCallback callback)
                throws Throwable {
            return callback.execute(this);
        }
    }
}
