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
package org.seasar.framework.unit.annotation;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.unit.S2TestClassRunner;

public class RollbackTest extends TestCase {
    static boolean run;
    static boolean txStarted;
    static boolean txEnded;

    @Override
    public void setUp() {
        run = false;
        txStarted = false;
        txEnded = false;
    }

    public void testRollback() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge.class);
        assertTrue("1", run);
        assertTrue("2", txStarted);
        assertTrue("3", txEnded);
    }

    public void testNoRollbackAnnotation() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge2.class);
        assertTrue("1", run);
        assertFalse("2", txStarted);
    }

    @RunWith(S2TestClassRunner.class)
    public static class Hoge extends S2FrameworkTestCase {

        public void test() {
        }

        @Before 
        public void before() throws Exception {
            include("ejb3tx.dicon");
        }
        
        @Test
        @Rollback
        public void aaa() throws Exception {
            include("ejb3tx.dicon");
            run = true;
            txStarted = isTransactionActinve();;
        }

        @After
        public void after() throws Exception {
            txEnded = !isTransactionActinve();
        }

        private boolean isTransactionActinve() throws Exception {
            TransactionManager tm = (TransactionManager)getComponent(TransactionManager.class);
            return tm != null && tm.getStatus() != Status.STATUS_NO_TRANSACTION;
        }

    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge2 extends S2FrameworkTestCase {

        public void test() {
        }

        @Before 
        public void before() throws Exception {
            include("ejb3tx.dicon");
        }
        
        @Test
        public void aaa() throws Exception {
            include("ejb3tx.dicon");
            run = true;
            txStarted = isTransactionActinve();;
        }
        
        @After
        public void after() throws Exception {
            txEnded = !isTransactionActinve();
        }

        private boolean isTransactionActinve() throws Exception {
            TransactionManager tm = (TransactionManager)getComponent(TransactionManager.class);
            return tm != null && tm.getStatus() != Status.STATUS_NO_TRANSACTION;
        }
    }
}
