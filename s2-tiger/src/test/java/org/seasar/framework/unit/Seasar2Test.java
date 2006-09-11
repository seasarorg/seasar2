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
import java.util.ArrayList;
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
import org.seasar.framework.unit.annotation.Prerequisite;
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

        private static List<Object> values;

        private S2Container container;

        private InternalTestContext internalContext;

        private TestContext context;

        public void beforeAaa() {
            set();
        }

        public void aaa() {
            set();
        }

        public void afterAaa() {
            set();
        }

        private void set() {
            values.add(container);
            values.add(internalContext);
            values.add(context);
        }
    }

    public void testFieldBindingTest() {
        FieldBindingTest.values = new ArrayList<Object>();
        JUnitCore core = new JUnitCore();
        Result result = core.run(FieldBindingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        List<Object> values = FieldBindingTest.values;
        assertEquals(9, values.size());
        assertNull(values.get(0));
        assertNull(values.get(1));
        assertNotNull(values.get(2));
        assertNotNull(values.get(3));
        assertNotNull(values.get(4));
        assertNotNull(values.get(5));
        assertNull(values.get(6));
        assertNull(values.get(7));
        assertNull(values.get(8));
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
    public static class PrerequisiteTest {

        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        @Prerequisite("isTrue()")
        public void bbb() {
            count++;
            log += "bbb";
        }

        @Prerequisite("isFalse()")
        public void ccc() {
            count++;
            log += "ccc";
        }

        @Prerequisite("#ENV != null")
        public void ddd() {
            count++;
            log += "ddd";
        }

        public boolean isTrue() {
            return true;
        }

        public boolean isFalse() {
            return false;
        }
    }

    public void testPrerequisite() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(3, count);
        assertTrue(log.contains("aaa"));
        assertTrue(log.contains("bbb"));
        assertTrue(log.contains("ddd"));
    }

    @RunWith(Seasar2.class)
    @Prerequisite("true")
    public static class PrerequisiteTest2 {
        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        @Prerequisite("false")
        public void bbb() {
            count++;
            log += "bbb";
        }
    }

    public void testPrerequisite2() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertTrue(log.contains("aaa"));
    }

    @RunWith(Seasar2.class)
    @Prerequisite("false")
    public static class PrerequisiteTest3 {
        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        public void bbb() {
            count++;
            log += "bbb";
        }
    }

    public void testPrerequisite3() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest3.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(0, count);
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

        @Test
        public void aaa() {
            count++;
        }

        public void bbb() {
            count++;
        }

        public void ccc() {
            count++;
        }
    }

    public void testCustomizeS2TestClassMethodsRunnerTest() {
        configure("S2TestClassMethodsRunner.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestClassMethodsRunnerTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(0, count);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeS2TestIntrospectorTest {

        public static void setUpClass() {
            log += "a";
        }

        public static void tearDownClass() {
            log += "b";
        }

        public void setUp() {
            log += "c";
        }

        public void tearDown() {
            log += "d";
        }

        public void setUpAaa() {
            log += "e";
        }

        public void aaa() {
            log += "f";
        }

        public void tearDownAaa() {
            log += "g";
        }

        public void bbb() {
            log += "h";
        }

    }

    public void testCustomizeS2TestIntrospectorTest() {
        configure("S2TestIntrospector.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestIntrospectorTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(10, log.length());
        assertTrue(log.startsWith("a"));
        assertTrue(log.endsWith("b"));
        assertTrue(log.contains("cefgd"));
        assertTrue(log.contains("chd"));
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

    public static class UnfulfilledTestMethodRunner extends S2TestMethodRunner {

        public UnfulfilledTestMethodRunner(Object test, Method method,
                RunNotifier notifier, Description description,
                S2TestIntrospector introspector) {
            super(test, method, notifier, description, introspector);
        }

        @Override
        protected boolean isFulfilled() {
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
