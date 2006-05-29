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
package org.seasar.framework.util;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import org.seasar.framework.exception.RollbackRuntimeException;
import org.seasar.framework.exception.SystemRuntimeException;

/**
 * @author higa
 * 
 */
public final class TransactionUtil {

    private TransactionUtil() {
    }

    public static void enlistResource(Transaction tx, XAResource xaResource) {
        try {
            tx.enlistResource(xaResource);
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }

    public static void registerSynchronization(Transaction tx,
            Synchronization sync) {

        try {
            tx.registerSynchronization(sync);
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }
}
