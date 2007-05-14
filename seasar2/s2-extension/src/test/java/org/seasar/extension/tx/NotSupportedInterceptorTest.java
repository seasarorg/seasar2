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

/**
 * 
 * @author taichi S.
 */
public class NotSupportedInterceptorTest extends S2TestCase {

    private static final String PATH = "NotSupportedInterceptorTest.dicon";

    private TxBean txBean;

    private ExceptionBean exBean;

    private TransactionManager tm;

    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
    }

    /**
     * @throws Exception
     */
    public void testInvoke() throws Exception {
        assertEquals("1", false, txBean.hasTransaction());
    }

    /**
     * @throws Exception
     */
    public void testInvokeTx() throws Exception {
        assertEquals("1", false, txBean.hasTransaction());
        assertEquals("2", Status.STATUS_ACTIVE, tm.getStatus());
    }

    /**
     * @throws Exception
     */
    public void testInvokeExceptionTx() throws Exception {
        try {
            exBean.invoke();
            fail("1");
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals("2", Status.STATUS_ACTIVE, tm.getStatus());
    }

}
