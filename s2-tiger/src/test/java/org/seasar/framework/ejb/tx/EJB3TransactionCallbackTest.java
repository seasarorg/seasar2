/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.tx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

import javax.ejb.ApplicationException;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class EJB3TransactionCallbackTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsRollingBack() throws Exception {
        assertFalse(EJB3TransactionCallback.isRollingBack(new IOException()));
        assertFalse(EJB3TransactionCallback
                .isRollingBack(new FileNotFoundException()));

        assertFalse(EJB3TransactionCallback.isRollingBack(new Exception1()));
        assertFalse(EJB3TransactionCallback.isRollingBack(new Exception2()));
        assertTrue(EJB3TransactionCallback.isRollingBack(new Exception3()));

        assertTrue(EJB3TransactionCallback.isRollingBack(new RemoteException()));
        assertTrue(EJB3TransactionCallback.isRollingBack(new ConnectException(
                "")));

        assertTrue(EJB3TransactionCallback
                .isRollingBack(new RuntimeException()));
        assertTrue(EJB3TransactionCallback
                .isRollingBack(new ArrayIndexOutOfBoundsException()));

        assertTrue(EJB3TransactionCallback.isRollingBack(new Throwable()));
        assertTrue(EJB3TransactionCallback.isRollingBack(new Error()));
        assertTrue(EJB3TransactionCallback
                .isRollingBack(new OutOfMemoryError()));
    }

    /**
     * 
     */
    @ApplicationException
    public static class Exception1 extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }

    /**
     * 
     */
    @ApplicationException(rollback = false)
    public static class Exception2 extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }

    /**
     * 
     */
    @ApplicationException(rollback = true)
    public static class Exception3 extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }

}