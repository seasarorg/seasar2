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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameters;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.util.TransactionManagerUtil;

public class Seasar2Test extends S2TestCase {

    private static String log;

    private static int count;

    private static boolean txActive;

    public void setUp() {
        log = "";
        count = 0;
        txActive = false;
        Seasar2.configure();
    }

    @RunWith(Seasar2.class)
    public static class AnnotationTest {

        @BeforeClass
        public static void aaa() {
            log += "a";
        }

        @AfterClass
        public static void bbb() {
            log += "b";
        }

        @Before
        public void ccc() {
            log += "c";
        }

        @After
        public void ddd() {
            log += "d";
        }

        @Test
        public void eee() {
            log += "e";
        }

        @Ignore
        @Test
        public void fff() {
            log = "f";
        }
    }

    public void testAnnotationTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AnnotationTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("acedb", log);
    }

    @RunWith(Seasar2.class)
    public static class ConventionTest {

        public static void beforeClass() {
            log += "a";
        }

        public static void afterClass() {
            log += "b";
        }

        public void before() {
            log += "c";
        }

        public void after() {
            log += "d";
        }

        public void aaa() {
            log += "e";
        }

        @Ignore
        public void bbb() {
            log = "f";
        }
    }

    public void testConventionTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(ConventionTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("acedb", log);
    }

    @RunWith(Seasar2.class)
    public static class InvalidMethodsTest {

        public void aaa() {
            log += "a";
        }

        public static void bbb() {
            log += "b";
        }

        private void ccc() {
            log += "c";
        }

        public String ddd() {
            log += "d";
            return null;
        }

        public void eee(String a) {
            log += "e";
        }

        @Ignore
        private void fff() {
            log += "f";
        }
    }

    public void testInvalidMethodsTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(InvalidMethodsTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("a", log);
    }

    @RunWith(Seasar2.class)
    public static class TransactionBehaviorDefaultTest {
        TransactionManager tm;

        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorDefaultTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorDefaultTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class TransactionBehaviorNoneTest {
        TransactionManager tm;

        @TxBehavior(TxBehaviorType.NONE)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorNoneTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorNoneTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(false, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorNoneTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorNoneTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorNoneTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(false, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorCommitTest {
        TransactionManager tm;

        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorCommitTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorCommitTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class TransactionBehaviorCommitTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorCommitTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorCommitTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorRollbackTest {

        TransactionManager tm;

        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }

    }

    public void testTransactionBehaviorRollbackTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorRollbackTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.ROLLBACK)
    public static class TransactionBehaviorRollbackTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }

    }

    public void testTransactionBehaviorRollbackTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorRollbackTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    @RunWith(Seasar2.class)
    public static class ParametarizedTest {

        @Parameters
        public static Collection parameters() {
            return Arrays
                    .asList(new Object[][] { { 1, 1 }, { 2, 4 }, { 3, 9 } });
        }

        private int a;

        private int b;

        public ParametarizedTest(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public void aaa() {
            count++;
            log += a;
            log += b;
        }
    }

    public void testParametarizedTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(ParametarizedTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(3, count);
        assertEquals("112439", log);
    }

    @RunWith(Seasar2.class)
    public static class EachBeforeAndEachAfterTest {

        public void beforeAaa() {
            log += "a";
        }

        public void aaa() {
            log += "b";
        }

        public void afterAaa() {
            log += "c";
        }
    }

    public void testEachBeforeAndEachAfterTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(EachBeforeAndEachAfterTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("abc", log);
    }

    @RunWith(Seasar2.class)
    public static class FieldBindingTest {
        private S2Container container;

        private InternalTestContext internalContext;

        private TestContext context;

        public void before() {
            log += (container != null);
            log += "-";
            log += (internalContext != null);
            log += "-";
            log += (context != null);
            log += "-";
        }

        public void aaa() {
            log += (container != null);
            log += "-";
            log += (internalContext != null);
            log += "-";
            log += (context != null);
        }

    }

    public void testFieldBindingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(FieldBindingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("false-false-true-true-true-true", log);
    }

    @RunWith(Seasar2.class)
    public static class AutoPreparingTest {

        static int aaa_size;

        static int bbb_size;

        static int ccc_size;

        DataAccessor da;

        public void aaa() {
            aaa_size = da.readDbByTable("EMP").getRowSize();
        }

        public void bbb() {
            bbb_size = da.readDbByTable("EMP").getRowSize();
        }

        public void ccc() {
            ccc_size = da.readDbByTable("EMP").getRowSize();
        }
    }

    public void testAutoPreparingTest() {
        AutoPreparingTest.aaa_size = 0;
        AutoPreparingTest.bbb_size = 0;
        AutoPreparingTest.ccc_size = 0;
        JUnitCore core = new JUnitCore();
        Result result = core.run(AutoPreparingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(15, AutoPreparingTest.aaa_size);
        assertEquals(16, AutoPreparingTest.bbb_size);
        assertEquals(16, AutoPreparingTest.ccc_size);
    }

    @RunWith(Seasar2.class)
    public static class NonAutoPreparingTest {

        static int aaa_size;

        static int bbb_size;

        static int ccc_size;

        private TestContext ctx;

        private DataAccessor da;

        public void before() {
            ctx.setAutoPreparing(false);
        }

        public void aaa() {
            aaa_size = da.readDbByTable("EMP").getRowSize();
        }

        public void bbb() {
            bbb_size = da.readDbByTable("EMP").getRowSize();
        }

        public void ccc() {
            ccc_size = da.readDbByTable("EMP").getRowSize();
        }
    }

    public void testNonAutoPreparingTest() {
        NonAutoPreparingTest.aaa_size = 0;
        NonAutoPreparingTest.bbb_size = 0;
        NonAutoPreparingTest.ccc_size = 0;
        JUnitCore core = new JUnitCore();
        Result result = core.run(NonAutoPreparingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(14, NonAutoPreparingTest.aaa_size);
        assertEquals(14, NonAutoPreparingTest.bbb_size);
        assertEquals(14, NonAutoPreparingTest.ccc_size);
    }

    @RunWith(Seasar2.class)
    public static class AutoIncludingTest {

        private S2Container container;

        public void aaa() {
            log += container.getComponent("hoge").toString();
        }
    }

    public void testAutoIncludingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AutoIncludingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("aaa", log);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class NonAutoIncludingTest {

        private TestContext ctx;

        private S2Container container;

        public void before() {
            ctx.setAutoIncluding(false);
        }

        public void aaa() {
            log += container.hasComponentDef("hoge");
            log += "-";
            log += container.hasComponentDef(TransactionManager.class);
        }
    }

    public void testNonAutoIncludingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(NonAutoIncludingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("false-false", log);
    }

    @RunWith(Seasar2.class)
    public static class GetExpectedTest {
        private TestContext ctx;

        public void aaa() {
            log += (ctx.getExpected() != null);
            log += "-";
        }

        public void bbb() {
            log += (ctx.getExpected() == null);
            log += "-";
        }
    }

    public void testGetExpected() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(GetExpectedTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("true-true-", log);
    }

    @RunWith(Seasar2.class)
    public static class BindEJB3ByFieldNameTest {

        TestContext testContext;

        @EJB
        private IHoge hoge;

        public void before() {
            testContext.register(Hoge.class);
            testContext.register(Foo.class);
        }

        public void aaa() {
            log += (hoge != null) + "-";
            log += (hoge.aaa() != null);
        }
    }

    public void testBindEJB3ByFieldName() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByFieldNameTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true-true", log);
    }

    @RunWith(Seasar2.class)
    public static class BindEJB3ByBeanNameTest {

        TestContext testContext;

        @EJB(beanName = "xxx")
        private IHoge yyy;

        public void before() {
            testContext.register(Hoge2.class);
        }

        public void aaa() {
            log += (yyy != null);
        }
    }

    public void testBindEJB3ByBeanName() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByBeanNameTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true", log);
    }

    @RunWith(Seasar2.class)
    public static class BindEJB3ByTypeTest {

        TestContext testContext;

        @EJB
        private IBar zzz;

        public void before() {
            testContext.register(Bar.class);
        }

        public void aaa() {
            log += (zzz != null);
        }
    }

    public void testBindEJB3ByType() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByTypeTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true", log);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeSeasar2Test {

        public void aaa() {
            log += "a";
            count++;
        }

        public void beforeBbb() {
            log += "b";
            count++;
        }

        @Test
        public void bbb() {
            log += "c";
            count++;
        }
    }

    public void testCustomizeSeasar2Test() {
        configure("Seasar2.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeSeasar2Test.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals("c", log);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeS2TestClassMethodsRunnerTest {

        private TransactionManager tm;

        @Test
        public void aaa() {
            count++;
            log = "aaa";
            txActive = TransactionManagerUtil.isActive(tm);
        }

        public void bbb() {
            count++;
            log += "bbb";
            txActive = TransactionManagerUtil.isActive(tm);
        }

        public void ccc() {
            count++;
            log += "ccc";
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testCustomizeS2TestClassMethodsRunnerTest() {
        configure("S2TestClassMethodsRunner.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestClassMethodsRunnerTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("aaa", log);
        assertEquals(1, count);
        assertFalse(txActive);
    }

    public void configure(String name) {
        String path = getClass().getName().replace('.', '/') + "." + name;
        Seasar2.configure(path);
    }

    public static class NormalJunit4 implements Seasar2.Provider {
        public Runner createTestClassRunner(Class<?> clazz) throws Exception {
            return new TestClassRunner(clazz);
        }
    }

    public static class TxDisabledTestMethodRunner extends S2TestMethodRunner {

        public TxDisabledTestMethodRunner(Object test, Method method,
                RunNotifier notifier, Description description,
                S2TestIntrospector introspector) {
            super(test, method, notifier, description, introspector);
        }

        @Override
        protected boolean needsTransaction() {
            return false;
        }
    }

    public void printFailures(List<Failure> failures) {
        for (final Failure failure : failures) {
            System.out.println("---");
            System.out.println(failure.getTestHeader());
            System.out.println(failure.getMessage());
            System.out.println(failure.getTrace());
            System.out.println("---");
        }
    }
}
