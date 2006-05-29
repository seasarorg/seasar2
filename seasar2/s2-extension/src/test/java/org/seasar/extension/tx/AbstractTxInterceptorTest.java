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
package org.seasar.extension.tx;

import java.rmi.AccessException;
import java.rmi.RemoteException;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class AbstractTxInterceptorTest extends S2TestCase {
    private static final String PATH = "AbstractTxInterceptorTest.dicon";

    private ExceptionBean exBean_;

    private TransactionManager tm_;

    private TestInterceptor testTx_;

    public AbstractTxInterceptorTest() {
    }

    public AbstractTxInterceptorTest(String name) {
        super(name);
    }

    public void testType() throws Exception {
        try {
            testTx_.addCommitRule(Throwable.class);
            testTx_.addCommitRule(Error.class);
            testTx_.addCommitRule(Exception.class);
            testTx_.addCommitRule(RuntimeException.class);
            testTx_.addCommitRule(Object.class);
            fail("1");
        } catch (IllegalArgumentException expected) {
            System.out.println(expected);
        }

        try {
            testTx_.addRollbackRule(Throwable.class);
            testTx_.addRollbackRule(Error.class);
            testTx_.addRollbackRule(Exception.class);
            testTx_.addRollbackRule(RuntimeException.class);
            testTx_.addRollbackRule(String.class);
            fail("2");
        } catch (IllegalArgumentException expected) {
            System.out.println(expected);
        }
    }

    public void testNoRule() throws Exception {
        try {
            exBean_.invoke();
            fail("1");
        } catch (Exception expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("3", testTx_.result);
    }

    public void testCommitRule() throws Exception {
        testTx_.addRollbackRule(RuntimeException.class);
        testTx_.addRollbackRule(RemoteException.class);
        testTx_.addCommitRule(Exception.class);
        try {
            exBean_.invoke(new SystemException());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertTrue("3", testTx_.result);
    }

    public void testRollbackRule1() throws Exception {
        testTx_.addRollbackRule(RuntimeException.class);
        testTx_.addRollbackRule(RemoteException.class);
        testTx_.addCommitRule(Exception.class);
        try {
            exBean_.invoke(new UnsupportedOperationException());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("3", testTx_.result);
    }

    public void testRollbackRule2() throws Exception {
        testTx_.addRollbackRule(RuntimeException.class);
        testTx_.addRollbackRule(RemoteException.class);
        testTx_.addCommitRule(Exception.class);
        try {
            exBean_.invoke(new AccessException(""));
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("3", testTx_.result);
    }

    public void testRollbackRule3() throws Exception {
        testTx_.addRollbackRule(RuntimeException.class);
        testTx_.addRollbackRule(RemoteException.class);
        testTx_.addCommitRule(Exception.class);
        try {
            exBean_.invoke(new Throwable());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("3", testTx_.result);
    }

    public void testRollbackRule4() throws Exception {
        testTx_.addRollbackRule(RuntimeException.class);
        testTx_.addRollbackRule(RemoteException.class);
        testTx_.addCommitRule(Exception.class);
        try {
            exBean_.invoke(new OutOfMemoryError());
            fail("1");
        } catch (Throwable expected) {
            System.out.println(expected);
        }
        assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
        assertFalse("3", testTx_.result);
    }

    protected void setUp() throws Exception {
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        return new TestSuite(AbstractTxInterceptorTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner
                .main(new String[] { AbstractTxInterceptorTest.class.getName() });
    }

    public static class TestInterceptor extends AbstractTxInterceptor {
        boolean result;

        public TestInterceptor(TransactionManager transactionManager) {
            super(transactionManager);
        }

        public Object invoke(MethodInvocation invocation) throws Throwable {
            boolean began = false;
            if (!hasTransaction()) {
                begin();
                began = true;
            }
            Object ret = null;
            try {
                ret = invocation.proceed();
                if (began) {
                    commit();
                }
                return ret;
            } catch (Throwable t) {
                if (began) {
                    result = complete(t);
                }
                throw t;
            }
        }
    }
}
