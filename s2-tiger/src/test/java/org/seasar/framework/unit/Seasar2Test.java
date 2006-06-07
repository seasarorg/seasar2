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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized.Parameters;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.util.TransactionManagerUtil;

public class Seasar2Test extends S2TestCase {

    private static String log;

    private static int count;

    private static boolean active;

    public void setUp() {
        log = "";
        count = 0;
        active = false;
    }

    public void tearDown() {
        Seasar2.configurationContainer = null;
        new Seasar2.DefaultConfigurator().configure();
    }

    @RunWith(Seasar2.class)
    public static class TestAnnotationTest {
        @Test
        public void aaa() throws Exception {
            log = "a";
            count++;
        }

        @Ignore
        @Test
        public void bbb() throws Exception {
            log = "b";
            count++;
        }
    }

    public void testTestAnnotationTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TestAnnotationTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertTrue(log.contains("a"));
    }

    @RunWith(Seasar2.class)
    public static class PublicNoArgNoReturnValueMethodTest {
        public void aaa() {
            log = "a";
            count++;
        }

        @Ignore
        public void bbb() {
            log = "b";
            count++;
        }
    }

    public void testPublicNoArgNoReturnValueMethodTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PublicNoArgNoReturnValueMethodTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertTrue(log.contains("a"));
    }

    @RunWith(Seasar2.class)
    public static class InvalidMethodsTest {

        public static void bbb() {
            count++;
        }

        private void ccc() {
            count++;
        }

        public String ddd() {
            count++;
            return null;
        }

        public void eee(String a) {
            count++;
        }

        @Ignore
        private void fff() {
            count++;
        }
    }

    public void testInvalidMethodsTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(InvalidMethodsTest.class);
        assertFalse(result.wasSuccessful());
        assertEquals(0, count);
    }

    @RunWith(Seasar2.class)
    public static class BeforeAndAfterTest {

        @Before
        public void before() {
            log += "a";
        }

        public void aaa() {
            log += "b";
        }

        public void bbb() {
            log += "b";
        }

        @After
        public void after() {
            log += "c";
        }
    }

    public void testBeforeAndAfterTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BeforeAndAfterTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals("abcabc", log);
    }

    @RunWith(Seasar2.class)
    public static class BeforeClassAndAfterClassTest {

        @BeforeClass
        public static void before() {
            log += "a";
        }

        public void aaa() {
        }

        public void bbb() {
        }

        @AfterClass
        public static void after() {
            log += "b";
        }
    }

    public void testBeforeClassAndAfterClassTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BeforeClassAndAfterClassTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals("ab", log);
    }

    @RunWith(Seasar2.class)
    public static class TransactionBehaviorDefaultTest {
        TransactionManager tm;

        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorDefaultTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorDefaultTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class TransactionBehaviorNoneTest {
        TransactionManager tm;

        @TxBehavior(TxBehaviorType.NONE)
        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorNoneTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorNoneTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(false, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorNoneTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorNoneTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorNoneTest2.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(false, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorCommitTest {
        TransactionManager tm;

        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorCommitTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorCommitTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class TransactionBehaviorCommitTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorCommitTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorCommitTest2.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorRollbackTest {
        TransactionManager tm;

        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorRollbackTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorRollbackTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, active);
    }

    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.ROLLBACK)
    public static class TransactionBehaviorRollbackTest2 {
        TransactionManager tm;

        public void bbb() {
            count++;
            active = TransactionManagerUtil.isActive(tm);
        }
    }

    public void testTransactionBehaviorRollbackTest2() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorRollbackTest2.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, active);
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
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
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
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("abc", log);
    }

    @RunWith(Seasar2.class)
    public static class FieldBindingTest {
        private S2Container container;

        private DataAccessor dataAccessor;

        public void aaa() {
            log += (container != null);
        }

        public void bbb() {
            log += (dataAccessor != null);
        }
    }

    public void testFieldBindingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(FieldBindingTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(false, log.contains("false"));
    }

    @RunWith(Seasar2.class)
    public static class AutoReadXlsWriteDbTest {

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

    public void testAutoReadXlsWriteDbTest() {
        AutoReadXlsWriteDbTest.aaa_size = 0;
        AutoReadXlsWriteDbTest.bbb_size = 0;
        AutoReadXlsWriteDbTest.ccc_size = 0;
        JUnitCore core = new JUnitCore();
        Result result = core.run(AutoReadXlsWriteDbTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(15, AutoReadXlsWriteDbTest.aaa_size);
        assertEquals(16, AutoReadXlsWriteDbTest.bbb_size);
        assertEquals(16, AutoReadXlsWriteDbTest.ccc_size);
    }

    @RunWith(Seasar2.class)
    public static class AutoIncludeDiconTest {

        S2Container container;

        public void aaa() {
            log += container.getComponent("hoge").toString();
        }
    }

    public void testAutoIncludeDiconTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AutoIncludeDiconTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals("aaa", log);
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
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals("c", log);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeS2TestClassMethodsRunnerTest {
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
        assertFalse(result.wasSuccessful());
        assertEquals(0, count);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeS2TestMethodRunnerTest {
        @Ignore
        public void aaa() {
            count++;
        }
    }

    public void testCustomizeS2TestMethodRunner() {
        configure("S2TestMethodRunner.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestMethodRunnerTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeS2TestIntrospectorTest {

        public void setUpAaa() {
            log += "a";
            count++;
        }

        public void aaa() {
            log += "b";
            count++;
        }

        public void tearDownAaa() {
            log += "c";
            count++;
        }

        @Disable
        public void bbb() {
            log += "d";
            count++;
        }
    }

    public void testCustomizeS2TestIntrospectorTest() {
        configure("S2TestIntrospector.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestIntrospectorTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals(3, count);
        assertEquals("abc", log);
    }

    @RunWith(Seasar2.class)
    public static class CustomizeTestIntrospectorTest {
        public void aaa() {
            log += "a";
            count++;
        }

        @Test
        public void bbb() {
            log += "b";
            count++;
        }
    }

    public void testCustomizeTestIntrospectorTest() {
        configure("TestIntrospector.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeTestIntrospectorTest.class);
        assertTrue(result.getFailures().toString(), result.wasSuccessful());
        assertEquals(1, count);
        assertEquals("b", log);
    }

    @RunWith(Seasar2.class)
    public static class HogeTest {
        public void after() {
            log += "a";
            count++;
        }
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

    public static class EmptyMethodsRunner extends
            S2TestClassMethodsRunner.DefaultProvider {
        @Override
        public List<Method> getTestMethods(Class<?> clazz) {
            return new ArrayList<Method>();
        }
    }

    public static class IgnoreAnnotationDisabledRunner extends
            S2TestMethodRunner.DefaultProvider {
        @Override
        protected boolean isIgnored(TestMethod testMethod) {
            return false;
        }
    }

    public static class NonAnnotatedMethodDisabledIntrospector extends
            S2TestIntrospector {
        @Override
        protected boolean isTestMethod(Method method) {
            return method.isAnnotationPresent(Test.class);
        }
    }

    @Target( { METHOD })
    @Retention(RUNTIME)
    public static @interface Disable {
    }
}
