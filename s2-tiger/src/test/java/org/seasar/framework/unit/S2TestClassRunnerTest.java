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
package org.seasar.framework.unit;

import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.annotation.Rollback;
import org.seasar.framework.util.TransactionManagerUtil;

public class S2TestClassRunnerTest extends TestCase {

    private static String log;

    private static boolean txStarted;

    private static boolean txEnded;

    @Override
    public void setUp() {
        log = "";
        txStarted = false;
        txEnded = false;
    }

    public void testSetUpAndTearDownPrefix() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.run(SetUpAndTearDownPrefixTest.class);
        assertEquals("1", "ABCDE", log);
    }

    public void testTxSuffix() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.run(TxSuffixTest.class);
        assertEquals("1", true, txStarted);
        assertEquals("2", true, txEnded);
    }

    public void testNoTxSuffix() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.run(NoTxSuffixTest.class);
        assertEquals("1", false, txStarted);
    }

    public void testRollbackAnnotation() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.run(RollbackAnnotationTest.class);
        assertEquals("1", true, txStarted);
        assertEquals("2", true, txEnded);
    }

    public void testNoRollbackAnnotation() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.run(NoRollbackAnnotationTest.class);
        assertEquals("1", false, txStarted);
    }

    public static class TransactionTest extends S2FrameworkTestCase {

        public void test() {
        }

        @Before
        public void before() throws Exception {
            include("ejb3tx.dicon");
        }

        @After
        public void after() throws Exception {
            txEnded = !isTransactionActinve();
        }

        protected boolean isTransactionActinve() throws Exception {
            TransactionManager tm = (TransactionManager) getComponent(TransactionManager.class);
            return TransactionManagerUtil.isActive(tm);
        }
    }

    @RunWith(S2TestClassRunner.class)
    public static class SetUpAndTearDownPrefixTest extends S2FrameworkTestCase {

        public void test() {
        }

        @Before
        public void before() {
            log += "A";
        }

        public void setUpAaa() throws Exception {
            log += "B";
        }

        @Test
        public void aaa() throws Exception {
            log += "C";
        }

        public void tearDownAaa() throws Exception {
            log += "D";
        }

        @After
        public void after() {
            log += "E";
        }

    }

    @RunWith(S2TestClassRunner.class)
    public static class TxSuffixTest extends TransactionTest {
        @Test
        public void aaaTx() throws Exception {
            txStarted = isTransactionActinve();
        }
    }

    @RunWith(S2TestClassRunner.class)
    public static class NoTxSuffixTest extends TransactionTest {
        @Test
        public void aaa() throws Exception {
            txStarted = isTransactionActinve();
        }
    }

    @RunWith(S2TestClassRunner.class)
    public static class RollbackAnnotationTest extends TransactionTest {
        @Test
        @Rollback
        public void aaa() throws Exception {
            txStarted = isTransactionActinve();
        }
    }

    @RunWith(S2TestClassRunner.class)
    public static class NoRollbackAnnotationTest extends TransactionTest {
        @Test
        public void aaa() throws Exception {
            txStarted = isTransactionActinve();
        }
    }

}
