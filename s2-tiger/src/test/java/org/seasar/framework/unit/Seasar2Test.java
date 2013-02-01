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
package org.seasar.framework.unit;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameters;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.unit.BeanReader;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.warmdeploy.WarmdeployBehavior;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.env.Env;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.annotation.PostBindFields;
import org.seasar.framework.unit.annotation.PreUnbindFields;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.PublishedTestContext;
import org.seasar.framework.unit.annotation.RegisterNamingConvention;
import org.seasar.framework.unit.annotation.RootDicon;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.unit.annotation.WarmDeploy;
import org.seasar.framework.unit.impl.InternalTestContextImpl;
import org.seasar.framework.util.TransactionManagerUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

import static org.easymock.EasyMock.*;

/**
 * 
 */
public class Seasar2Test extends TestCase {

    private static String log;

    private static int count;

    private static boolean txActive;

    @Override
    public void setUp() {
        log = "";
        count = 0;
        txActive = false;
        Seasar2.configure();
    }

    @Override
    public void tearDown() {
        Seasar2.dispose();
        S2TestMethodRunner.s2junit4Path = S2TestMethodRunner.DEFAULT_S2JUNIT4_PATH;
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class FilterTest {

        /**
         * 
         */
        public void aaa() {
            log += "a";
        }

        /**
         * 
         */
        public void bbb() {
            log += "b";
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testFilter() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(Request.method(FilterTest.class, "bbb"));
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertFalse(log.contains("a"));
        assertTrue(log.contains("b"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class SortTest {

        /**
         * 
         */
        public void aaa() {
            log += "a";
        }

        /**
         * 
         */
        public void bbb() {
            log += "b";
        }

        /**
         * 
         */
        public void ccc() {
            log += "c";
        }

    }

    /**
     * 
     * @throws Exception
     */
    public void testSort() throws Exception {
        JUnitCore core = new JUnitCore();
        Request req = Request.aClass(SortTest.class).sortWith(
                new Comparator<Description>() {

                    public int compare(Description o1, Description o2) {
                        return -1
                                * (o1.getDisplayName().compareTo(o2
                                        .getDisplayName()));
                    }
                });
        Result result = core.run(req);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("cba", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class AnnotationTest {

        private Hello hello;

        /**
         * 
         */
        @BeforeClass
        public static void aaa() {
            log += "a";
        }

        /**
         * 
         */
        @AfterClass
        public static void bbb() {
            log += "b";
        }

        /**
         * 
         */
        @Before
        public void ccc() {
            log += "c";
        }

        /**
         * 
         */
        @After
        public void ddd() {
            log += "d";
        }

        /**
         * 
         */
        @Test
        public void eee() {
            log += "e";
        }

        /**
         * 
         */
        @Ignore
        @Test
        public void fff() {
            log = "f";
        }

        /**
         * 
         */
        @PostBindFields
        public void ggg() {
            assertNotNull(hello);
            log += "g";
        }

        /**
         * 
         */
        @PreUnbindFields
        public void hhh() {
            assertNotNull(hello);
            log += "h";
        }
    }

    /**
     * 
     */
    public void testAnnotationTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AnnotationTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("acgehdb", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @Ignore
    public static class IgnoreAnnotationForClassTest {

        /**
         * 
         */
        @Test
        public void aaa() {
            log += "a";
        }
    }

    /**
     * 
     */
    public void testIgnoreAnnotationForClassTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(IgnoreAnnotationForClassTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getIgnoreCount());
        assertEquals(0, log.length());
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class ConventionTest {

        private Hello hello;

        /**
         * 
         */
        public static void beforeClass() {
            log += "a";
        }

        /**
         * 
         */
        public static void afterClass() {
            log += "b";
        }

        /**
         * 
         */
        public void before() {
            log += "c";
        }

        /**
         * 
         */
        public void after() {
            log += "d";
        }

        /**
         * 
         */
        public void aaa() {
            log += "e";
        }

        /**
         * 
         */
        @Ignore
        public void bbb() {
            log = "f";
        }

        /**
         * 
         */
        public void postBindFields() {
            assertNotNull(hello);
            log += "g";
        }

        /**
         * 
         */
        public void preUnbindFields() {
            assertNotNull(hello);
            log += "h";
        }
    }

    /**
     * 
     */
    public void testConventionTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(ConventionTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("acgehdb", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class InvalidMethodsTest {

        /**
         * 
         */
        public void aaa() {
            log += "a";
        }

        /**
         * 
         */
        public static void bbb() {
            log += "b";
        }

        @SuppressWarnings("unused")
        private void ccc() {
            log += "c";
        }

        /**
         * @return
         */
        public String ddd() {
            log += "d";
            return null;
        }

        /**
         * @param a
         */
        public void eee(@SuppressWarnings("unused") String a) {
            log += "e";
        }

        @SuppressWarnings("unused")
        @Ignore
        private void fff() {
            log += "f";
        }
    }

    /**
     * 
     */
    public void testInvalidMethodsTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(InvalidMethodsTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("a", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class TransactionBehaviorDefaultTest {

        TransactionManager tm;

        /**
         * 
         */
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    /**
     * 
     */
    public void testTransactionBehaviorDefaultTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorDefaultTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class TransactionBehaviorNoneTest {

        TransactionManager tm;

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.NONE)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    /**
     * 
     */
    public void testTransactionBehaviorNoneTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorNoneTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(false, txActive);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorCommitTest {

        TransactionManager tm;

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }
    }

    /**
     * 
     */
    public void testTransactionBehaviorCommitTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorCommitTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class TransactionBehaviorRollbackTest {

        TransactionManager tm;

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void bbb() {
            count++;
            txActive = TransactionManagerUtil.isActive(tm);
        }

    }

    /**
     * 
     */
    public void testTransactionBehaviorRollbackTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TransactionBehaviorRollbackTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals(true, txActive);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class ParameterizedTest {

        /**
         * @return
         */
        @Parameters
        public static Collection<?> parameters() {
            return Arrays
                    .asList(new Object[][] { { 1, 1 }, { 2, 4 }, { 3, 9 } });
        }

        private int a;

        private int b;

        /**
         * @param a
         * @param b
         */
        public ParameterizedTest(int a, int b) {
            this.a = a;
            this.b = b;
        }

        /**
         * 
         */
        public void aaa() {
            count++;
            log += a;
            log += b;
        }
    }

    /**
     * 
     */
    public void testParameterizedTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(ParameterizedTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(3, count);
        assertEquals("112439", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class EachBeforeAndEachAfterTest {

        /**
         * 
         */
        public void beforeAaa() {
            log += "a";
        }

        /**
         * 
         */
        public void aaa() {
            log += "b";
        }

        /**
         * 
         */
        public void afterAaa() {
            log += "c";
        }
    }

    /**
     * 
     */
    public void testEachBeforeAndEachAfterTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(EachBeforeAndEachAfterTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("abc", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class FieldBindingTest {

        private static List<Object> values = new ArrayList<Object>();

        private S2Container container;

        private InternalTestContext internalContext;

        private TestContext context;

        /**
         * 
         */
        public void beforeAaa() {
            set();
        }

        /**
         * 
         */
        public void aaa() {
            set();
        }

        /**
         * 
         */
        public void afterAaa() {
            set();
        }

        private void set() {
            values.add(container);
            values.add(internalContext);
            values.add(context);
        }
    }

    /**
     * 
     */
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

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class AutoPreparingTest {

        static int aaa_size;

        static int bbb_size;

        static int ccc_size;

        DataAccessor da;

        /**
         * 
         */
        public void aaa() {
            aaa_size = da.readDbByTable("EMP").getRowSize();
        }

        /**
         * 
         */
        public void bbb() {
            bbb_size = da.readDbByTable("EMP").getRowSize();
        }

        /**
         * 
         */
        public void ccc() {
            ccc_size = da.readDbByTable("EMP").getRowSize();
        }
    }

    /**
     * 
     */
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

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class PreparationTypeTest {

        TestContext context;

        DataAccessor da;

        /**
         * 
         */
        public void defaultSetting() {
            int size = da.readDbByTable("EMP").getRowSize();
            assertEquals(16, size);
        }

        /**
         * 
         */
        public void beforeNone() {
            context.setPreparationType(PreparationType.NONE);
        }

        /**
         * 
         */
        public void none() {
            int size = da.readDbByTable("EMP").getRowSize();
            assertEquals(14, size);
            log += "a";
        }

        /**
         * 
         */
        public void beforeWrite() {
            context.setPreparationType(PreparationType.WRITE);
        }

        /**
         * 
         */
        public void write() {
            int size = da.readDbByTable("EMP").getRowSize();
            assertEquals(16, size);
            log += "b";
        }

        /**
         * 
         */
        public void beforeReplace() {
            context.setPreparationType(PreparationType.REPLACE);
        }

        /**
         * 
         */
        public void replace() {
            int size = da.readDbByTable("EMP").getRowSize();
            assertEquals(15, size);
            log += "c";
        }

        /**
         * 
         */
        public void beforeAllReplace() {
            context.setPreparationType(PreparationType.ALL_REPLACE);
        }

        /**
         * 
         */
        public void allReplace() {
            int size = da.readDbByTable("EMP").getRowSize();
            assertEquals(2, size);
            log += "d";
        }

    }

    /**
     * 
     */
    public void PreparationTypeTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PreparationType.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log.contains("a"));
        assertTrue(log.contains("b"));
        assertTrue(log.contains("c"));
        assertTrue(log.contains("d"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class TrimStringTest {

        private TestContext context;

        private DataAccessor accessor;

        /**
         * 
         */
        public void before() {
            context.setTrimString(false);
        }

        /**
         * 
         */
        public void aaa() {
            DataTable table = accessor.readDbByTable("EMP", "ENAME = ' '");
            assertEquals(1, table.getRowSize());

            DataSet dataSet = context.getExpected();
            String value = (String) dataSet.getTable("EMP").getRow(0).getValue(
                    "ENAME");
            assertEquals(" ", value);

            count++;
        }
    }

    /**
     * 
     */
    public void testTrimStringTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TrimStringTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class TrimStringBeanReaderTest {

        private TestContext context;

        /**
         * 
         */
        public void beforeAaa() {
            context.setTrimString(true);
        }

        /**
         * 
         */
        public void aaa() {
            DataSet dataSet = new BeanReader(new Emp("hoge   ")).read();
            String actual = (String) dataSet.getTable(0).getRow(0).getValue(0);
            assertEquals("hoge", actual);
            log += "a";
        }

        /**
         * 
         */
        public void beforeBbb() {
            context.setTrimString(false);
        }

        /**
         * 
         */
        public void bbb() {
            DataSet dataSet = new BeanReader(new Emp("hoge   ")).read();
            Object actual = dataSet.getTable(0).getRow(0).getValue(0);
            assertEquals("hoge   ", actual);
            log += "b";
        }

        /**
         * 
         */
        public void beforeCcc() {
            context.setTrimString(false);

            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(String.class));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.CHAR));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.LONGVARCHAR));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.VARCHAR));
            log += "c";
        }

        /**
         * 
         */
        public void ccc() {
            assertEquals(ColumnTypes.NOT_TRIM_STRING, ColumnTypes
                    .getColumnType(String.class));
            assertEquals(ColumnTypes.NOT_TRIM_STRING, ColumnTypes
                    .getColumnType(Types.CHAR));
            assertEquals(ColumnTypes.NOT_TRIM_STRING, ColumnTypes
                    .getColumnType(Types.LONGVARCHAR));
            assertEquals(ColumnTypes.NOT_TRIM_STRING, ColumnTypes
                    .getColumnType(Types.VARCHAR));
            log += "d";
        }

        /**
         * 
         */
        public void AfterCcc() {
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(String.class));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.CHAR));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.LONGVARCHAR));
            assertEquals(ColumnTypes.STRING, ColumnTypes
                    .getColumnType(Types.VARCHAR));
            log += "e";
        }
    }

    /**
     * 
     */
    public static class Emp {

        private String name;

        /**
         * 
         * @param name
         */
        public Emp(String name) {
            this.name = name;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 
     */
    public void testTrimStringBeanReaderTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(TrimStringBeanReaderTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log.contains("a"));
        assertTrue(log.contains("b"));
        assertTrue(log.contains("c"));
        assertTrue(log.contains("d"));
        assertTrue(log.contains("e"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class NonAutoPreparingTest {

        static int aaa_size;

        static int bbb_size;

        static int ccc_size;

        private TestContext ctx;

        private DataAccessor da;

        /**
         * 
         */
        @SuppressWarnings("deprecation")
        public void before() {
            ctx.setAutoPreparing(false);
        }

        /**
         * 
         */
        public void aaa() {
            aaa_size = da.readDbByTable("EMP").getRowSize();
        }

        /**
         * 
         */
        public void bbb() {
            bbb_size = da.readDbByTable("EMP").getRowSize();
        }

        /**
         * 
         */
        public void ccc() {
            ccc_size = da.readDbByTable("EMP").getRowSize();
        }
    }

    /**
     * 
     */
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

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class AutoIncludingTest {

        private S2Container container;

        /**
         * 
         */
        public void aaa() {
            log += container.getComponent("hoge").toString();
        }
    }

    /**
     * 
     */
    public void testAutoIncludingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AutoIncludingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("aaa", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @TxBehavior(TxBehaviorType.NONE)
    public static class NonAutoIncludingTest {

        private TestContext ctx;

        private S2Container container;

        /**
         * 
         */
        public void before() {
            ctx.setAutoIncluding(false);
        }

        /**
         * 
         */
        public void aaa() {
            log += container.hasComponentDef("hoge");
            log += "-";
            log += container.hasComponentDef(TransactionManager.class);
        }
    }

    /**
     * 
     */
    public void testNonAutoIncludingTest() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(NonAutoIncludingTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("false-false", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class GetExpectedTest {

        private TestContext ctx;

        /**
         * 
         */
        public void aaa() {
            log += (ctx.getExpected() != null);
            log += "-";
        }

        /**
         * 
         */
        public void bbb() {
            log += (ctx.getExpected() == null);
            log += "-";
        }
    }

    /**
     * @throws Exception
     */
    public void testGetExpected() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(GetExpectedTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("true-true-", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class BindEJB3ByFieldNameTest {

        TestContext testContext;

        @EJB
        private IHoge hoge;

        /**
         * 
         */
        public void before() {
            testContext.register(Hoge.class);
            testContext.register(Foo.class);
        }

        /**
         * 
         */
        public void aaa() {
            log += (hoge != null) + "-";
            log += (hoge.aaa() != null);
        }
    }

    /**
     * @throws Exception
     */
    public void testBindEJB3ByFieldName() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByFieldNameTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true-true", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class BindEJB3ByBeanNameTest {

        TestContext testContext;

        @EJB(beanName = "xxx")
        private IHoge yyy;

        /**
         * 
         */
        public void before() {
            testContext.register(Hoge2.class);
        }

        /**
         * 
         */
        public void aaa() {
            log += (yyy != null);
        }
    }

    /**
     * @throws Exception
     */
    public void testBindEJB3ByBeanName() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByBeanNameTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class BindEJB3ByTypeTest {

        TestContext testContext;

        @EJB
        private IBar zzz;

        /**
         * 
         */
        public void before() {
            testContext.register(Bar.class);
        }

        /**
         * 
         */
        public void aaa() {
            log += (zzz != null);
        }
    }

    /**
     * @throws Exception
     */
    public void testBindEJB3ByType() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(BindEJB3ByTypeTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result.getRunCount());
        assertEquals("true", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class PrerequisiteTest {

        /**
         * 
         */
        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        /**
         * 
         */
        @Prerequisite("isTrue()")
        public void bbb() {
            count++;
            log += "bbb";
        }

        /**
         * 
         */
        @Prerequisite("isFalse()")
        public void ccc() {
            count++;
            log += "ccc";
        }

        /**
         * 
         */
        @Prerequisite("#ENV != null")
        public void ddd() {
            count++;
            log += "ddd";
        }

        /**
         * @return
         */
        public boolean isTrue() {
            return true;
        }

        /**
         * @return
         */
        public boolean isFalse() {
            return false;
        }
    }

    /**
     * @throws Exception
     */
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

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @Prerequisite("true")
    public static class PrerequisiteTest2 {

        /**
         * 
         */
        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        /**
         * 
         */
        @Prerequisite("false")
        public void bbb() {
            count++;
            log += "bbb";
        }
    }

    /**
     * @throws Exception
     */
    public void testPrerequisite2() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertTrue(log.contains("aaa"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @Prerequisite("false")
    public static class PrerequisiteTest3 {

        /**
         * 
         */
        @Prerequisite("true")
        public void aaa() {
            count++;
            log += "aaa";
        }

        /**
         * 
         */
        public void bbb() {
            count++;
            log += "bbb";
        }
    }

    /**
     * @throws Exception
     */
    public void testPrerequisite3() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest3.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(0, count);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class PrerequisiteTest4 {

        /**
         * 
         */
        @Prerequisite("throwException()")
        public void aaa() {
            count++;
        }

        @SuppressWarnings("unused")
        private void throwException() {
            throw new IllegalStateException();
        }
    }

    /**
     * @throws Exception
     */
    public void testPrerequisite4() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest4.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(0, count);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class PrerequisiteTest5 {

        /**
         * 
         */
        @Prerequisite("bbb(#method)")
        public void aaa() {
        }

        /**
         * @param m
         * @return
         */
        @SuppressWarnings("unused")
        public boolean bbb(Method m) {
            Method m2 = ReflectionUtil
                    .getMethod(PrerequisiteTest5.class, "aaa");
            log += m.equals(m2);
            return false;
        }
    }

    /**
     * @throws Exception
     */
    public void testPrerequisite5() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(PrerequisiteTest5.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("true", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class EnvTest {

        /**
         * 
         */
        public void env() {
            log += Env.getFilePath();
            log += Env.getValue();
        }
    }

    /**
     * @throws Exception
     */
    public void testEnv() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(EnvTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("env_ut.txtut", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class WarmDeployTest {

        private S2Container container;

        /**
         * 
         */
        public void warmDeploy() {
            log += (S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
            log += container.getComponent("fooDao") != null;
        }
    }

    /**
     * @throws Exception
     */
    public void testWarmdeploy() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(WarmDeployTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertFalse(log.contains("false"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class WarmDeployTest2 {

        /**
         * 
         */
        public void warmDeploy() {
            log += (S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
        }

        /**
         * 
         */
        @WarmDeploy(false)
        public void notWarmDeploy() {
            log += !(S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
        }
    }

    /**
     * @throws Exception
     */
    public void testWarmdeploy2() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(WarmDeployTest2.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertFalse(log.contains("false"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @WarmDeploy(false)
    public static class WarmDeployTest3 {

        /**
         * 
         */
        @WarmDeploy(true)
        public void warmDeploy() {
            log += (S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
        }

        /**
         * 
         */
        public void notWarmDeploy() {
            log += !(S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
        }
    }

    /**
     * @throws Exception
     */
    public void testWarmdeploy3() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(WarmDeployTest3.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertFalse(log.contains("false"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class MockTest {

        TestContext ctx;

        Hello hello;

        Hello2 hello2;

        /**
         * 
         */
        @Mock(target = Hello.class, returnValue = "'aa'")
        public void targetIsInterface() {
            log += hello.greeting();
        }

        /**
         * 
         */
        @Mock(target = HelloImpl.class, returnValue = "'bb'")
        public void targetIsClass() {
            log += hello.greeting();
        }

        /**
         * 
         */
        @Mock(target = Hello.class, targetName = "hoge", returnValue = "'cc'")
        public void usesTargetName() {
            log += hello.greeting();
        }

        /**
         * 
         */
        @Mock(target = Hello.class, pointcut = "e.*", returnValue = "'dd'")
        public void usesPointcut() {
            log += hello.greeting() + "-";
            log += hello.echo("hoge");
        }

        /**
         * 
         */
        @Mock(target = Hello2.class, targetName = "foo", returnValue = "'ee'")
        public void overridesOtherInterceptor() {
            log += hello2.greeting();
        }

        /**
         * 
         */
        @Mocks( {
                @Mock(target = Hello.class, pointcut = "greeting", returnValue = "'ff'"),
                @Mock(target = Hello.class, pointcut = "echo", returnValue = "'gg'"),
                @Mock(target = Hello2.class, returnValue = "'hh'") })
        public void usesMultiMocks() {
            log += hello.greeting();
            log += hello.echo("hoge");
            log += hello2.greeting();
        }

        /**
         * 
         */
        @Mock(target = Hello.class, returnValue = "getValue()")
        public void invokesMethod() {
            log += hello.greeting();
        }

        /**
         * @return
         */
        public String getValue() {
            return "ii";
        }

        /**
         * 
         */
        @Mock(target = Hello.class, throwable = "new IllegalArgumentException()")
        public void usesThrowable() {
            try {
                hello.echo("hoge");
            } catch (IllegalArgumentException e) {
                log += "jj";
            }
        }

        /**
         * 
         */
        @Mocks( { @Mock(target = Hello.class, pointcut = "echo"),
                @Mock(target = Hello.class, pointcut = "geeting") })
        public void getMockInterceptor() {
            hello.echo("hoge");
            MockInterceptor mi = ctx.getMockInterceptor(0);
            log += mi.getArgs("echo")[0] + "-";
            if (mi.isInvoked("echo")) {
                log += "kk";
            }
            mi = ctx.getMockInterceptor(1);
            if (mi.isInvoked("geeting")) {
                log += "ll";
            }
        }
    }

    /**
     * @throws Exception
     */
    public void testMock() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(MockTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log, log.contains("aa"));
        assertTrue(log, log.contains("bb"));
        assertTrue(log, log.contains("cc"));
        assertTrue(log, log.contains("hello-dd"));
        assertTrue(log, log.contains("ee"));
        assertTrue(log, log.contains("ff"));
        assertTrue(log, log.contains("gg"));
        assertTrue(log, log.contains("hh"));
        assertTrue(log, log.contains("ii"));
        assertTrue(log, log.contains("jj"));
        assertTrue(log, log.contains("hoge-kk"));
        assertTrue(log, log.contains("ll"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class EasyMockTest {

        private S2Container container;

        @EasyMock
        private Runnable runnable;

        @EasyMock(EasyMockType.STRICT)
        private Map<String, String> map;

        @EasyMock(register = true)
        private DataSource dataSource;

        /**
         * 
         */
        public void runnable() {
            runnable.run();
            log += "a";
        }

        /**
         * 
         */
        public void recordRunnable() {
            runnable.run();
        }

        /**
         * @throws Exception
         */
        public void map() throws Exception {
            map.put("a", "A");
            map.put("b", "B");
            assertEquals(2, map.size());
            log += "b";
        }

        /**
         * @throws Exception
         */
        public void recordMap() throws Exception {
            expect(map.put("a", "A")).andReturn(null);
            expect(map.put("b", "B")).andReturn(null);
            expect(map.size()).andReturn(2);
        }

        /**
         * @throws Exception
         */
        public void register() throws Exception {
            assertSame(dataSource, container.getComponent("dataSource"));
            assertSame(dataSource, container.getComponent(DataSource.class));
            assertFalse(container.hasComponentDef(Runnable.class));
            assertFalse(container.hasComponentDef(Map.class));
            log += "c";
        }
    }

    /**
     * @throws Exception
     */
    public void testEasyMock() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(EasyMockTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log, log.contains("a"));
        assertTrue(log, log.contains("b"));
        assertTrue(log, log.contains("c"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    @RootDicon("aop.dicon")
    public static class RootDiconTest {

        private S2Container container;

        /**
         * 
         */
        public void aaa() {
            assertEquals(container.getRoot(), container);
            assertEquals("aop.dicon", container.getPath());
            log += "a";
        }

        /**
         * 
         */
        @RootDicon("jdbc.dicon")
        public void bbb() {
            assertEquals(container.getRoot(), container);
            assertEquals("jdbc.dicon", container.getPath());
            log += "b";
        }
    }

    /**
     * @throws Exception
     */
    public void testRootDicon() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(RootDiconTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log, log.contains("a"));
        assertTrue(log, log.contains("b"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class SimpleInternalTestContextTest {

        private S2Container container;

        /**
         * 
         */
        public void aaa() {
            assertNotNull(container);
            count++;
        }
    }

    /**
     * @throws Exception
     */
    public void testSimpleInternalTestContext() throws Exception {
        S2TestMethodRunner.s2junit4Path = SimpleInternalTestContextTest.class
                .getName().replace(".", "/")
                + ".s2junit4.dicon";
        JUnitCore core = new JUnitCore();
        Result result = core.run(SimpleInternalTestContextTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class NamingConventionTest {

        private TestContext context;

        private NamingConvention namingConvention;

        /**
         * 
         */
        @RootDicon("org/seasar/framework/unit/Seasar2Test_convention.dicon")
        public void aaa() {
            assertEquals("/", namingConvention.getViewRootPath());
            assertEquals(".htm", namingConvention.getViewExtension());
            log += "a";
        }

        /**
         * 
         */
        public void beforeBbb() {
            context.setAutoIncluding(false);
        }

        /**
         * 
         */
        @RegisterNamingConvention(false)
        public void bbb() {
            assertNull(namingConvention);
            context.register(HashMap.class);
            log += "b";
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testNamingConvention() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(NamingConventionTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(2, log.length());
        assertTrue(log.contains("a"));
        assertTrue(log.contains("b"));
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class PublishedTestContextTest {

        private MyTestContext context;

        /**
         * 
         */
        public void before() {
            log += context != null;
        }

        /**
         * 
         */
        public void aaa() {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testPublishedTestContext() throws Exception {
        S2TestMethodRunner.s2junit4Path = PublishedTestContextTest.class
                .getName().replace(".", "/")
                + ".s2junit4.dicon";
        JUnitCore core = new JUnitCore();
        Result result = core.run(PublishedTestContextTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("true", log);
    }

    /**
     * 
     * @author taedium
     */
    @PublishedTestContext
    public static class MyTestContext extends InternalTestContextImpl {
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class MockHttpServletRequestTest {

        private MockHttpServletRequest request;

        /**
         * 
         */
        public void aaa() {
            assertNotNull(request);
            log += "a";
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMockHttpServletRequest() throws Exception {
        JUnitCore core = new JUnitCore();
        Result result = core.run(MockHttpServletRequestTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals("a", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class CustomizeSeasar2Test {

        /**
         * 
         */
        public void aaa() {
            log += "a";
            count++;
        }

        /**
         * 
         */
        public void beforeBbb() {
            log += "b";
            count++;
        }

        /**
         * 
         */
        @Test
        public void bbb() {
            log += "c";
            count++;
        }
    }

    /**
     * 
     */
    public void testCustomizeSeasar2Test() {
        configure("Seasar2.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeSeasar2Test.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(1, count);
        assertEquals("c", log);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class CustomizeS2TestClassMethodsRunnerTest {

        /**
         * 
         */
        @Test
        public void aaa() {
            count++;
        }

        /**
         * 
         */
        public void bbb() {
            count++;
        }

        /**
         * 
         */
        public void ccc() {
            count++;
        }
    }

    /**
     * 
     */
    public void testCustomizeS2TestClassMethodsRunnerTest() {
        configure("S2TestClassMethodsRunner.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestClassMethodsRunnerTest.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertEquals(0, count);
    }

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class CustomizeS2TestIntrospectorTest {

        /**
         * 
         */
        public static void setUpClass() {
            log += "a";
        }

        /**
         * 
         */
        public static void tearDownClass() {
            log += "b";
        }

        /**
         * 
         */
        public void setUp() {
            log += "c";
        }

        /**
         * 
         */
        public void tearDown() {
            log += "d";
        }

        /**
         * 
         */
        public void setUpAaa() {
            log += "e";
        }

        /**
         * 
         */
        public void aaa() {
            log += "f";
        }

        /**
         * 
         */
        public void tearDownAaa() {
            log += "g";
        }

        /**
         * 
         */
        public void bbb() {
            log += "h";
        }

    }

    /**
     * 
     */
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

    /**
     * 
     */
    @RunWith(Seasar2.class)
    public static class CustomizeS2TestIntrospector2Test {

        /**
         * 
         */
        @Ignore
        public void aaa() {
            log += "a";
        }

        /**
         * 
         */
        @Prerequisite("false")
        public void bbb() {
            log += "b";
        }

    }

    /**
     * 
     */
    public void testCustomizeS2TestIntrospector2Test() {
        configure("S2TestIntrospector2.dicon");
        JUnitCore core = new JUnitCore();
        Result result = core.run(CustomizeS2TestIntrospector2Test.class);
        printFailures(result.getFailures());
        assertTrue(result.wasSuccessful());
        assertTrue(log.contains("a"));
        assertTrue(log.contains("b"));
    }

    /**
     * @param name
     */
    public void configure(String name) {
        String path = getClass().getName().replace('.', '/') + "." + name;
        Seasar2.configure(path);
    }

    /**
     * 
     */
    public static class NormalJunit4 implements Seasar2.Provider {

        public Runner createTestClassRunner(Class<?> clazz) throws Exception {
            return new JUnit4ClassRunner(clazz);
        }
    }

    /**
     * 
     */
    public static class UnfulfilledTestMethodRunner extends S2TestMethodRunner {

        /**
         * @param test
         * @param method
         * @param notifier
         * @param description
         * @param introspector
         */
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

    /**
     * 
     */
    public interface Hello {

        /**
         * @return
         */
        public String greeting();

        /**
         * @param str
         * @return
         */
        public String echo(String str);
    }

    /**
     * 
     */
    public static class HelloImpl implements Hello {

        public String greeting() {
            return "hello";
        }

        public String echo(String str) {
            return str;
        }
    }

    /**
     * 
     */
    public interface Hello2 {

        /**
         * @return
         */
        public String greeting();
    }

    /**
     * 
     */
    public static class Hello2Impl implements Hello2 {

        public String greeting() {
            return "hello";
        }
    }

    /**
     * 
     */
    public static class DummyInteceptor extends AbstractInterceptor {

        static final long serialVersionUID = 0L;

        public Object invoke(MethodInvocation arg0) throws Throwable {
            throw new RuntimeException();
        }
    }

    /**
     * @param failures
     */
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
