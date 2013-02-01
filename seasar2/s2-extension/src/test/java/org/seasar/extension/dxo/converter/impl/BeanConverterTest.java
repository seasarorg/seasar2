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
package org.seasar.extension.dxo.converter.impl;

import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.CalendarDto;
import org.seasar.extension.dxo.CircularReferenceDepartment;
import org.seasar.extension.dxo.CircularReferenceDeptDto;
import org.seasar.extension.dxo.CircularReferenceEmployee;
import org.seasar.extension.dxo.DateDto;
import org.seasar.extension.dxo.DateStringDto;
import org.seasar.extension.dxo.DateStringDto2;
import org.seasar.extension.dxo.DateStringDto3;
import org.seasar.extension.dxo.DateUtil;
import org.seasar.extension.dxo.converter.Converter;

/**
 * @author Satsohi Kimura
 * @author koichik
 */
public class BeanConverterTest extends AbsConverterTest {

    private BeanConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new BeanConverter();
        register(SqlDateConverter.class, "sqlDateDxoConverter");
    }

    /**
     * @throws Exception
     */
    public void testConvert1() throws Exception {
        DateDto dateDto = (DateDto) converter.convert(new DateStringDto(),
                DateDto.class, createContext("testConvert1", null));
        assertEquals(10 - 1, getMonth(dateDto.getA()));
        assertEquals(30, getDate(dateDto.getA()));
    }

    /**
     * @throws Exception
     */
    public void testConvert2() throws Exception {
        DateStringDto dateStringDto = (DateStringDto) converter.convert(
                new DateDto(), DateStringDto.class, createContext(
                        "testConvert2", null));
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

    /**
     * 
     */
    public static final String testConvert3_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * @throws Exception
     */
    public void testConvert3() throws Exception {
        DateStringDto2 dateStringDto = (DateStringDto2) converter.convert(
                new DateDto(), DateStringDto2.class, createContext(
                        "testConvert3", null));
        assertEquals("2100/06/22", dateStringDto.getA());
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

    /**
     * 
     */
    public static final String testConvert4_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * @throws Exception
     */
    public void testConvert4() throws Exception {
        DateDto dateDto = (DateDto) converter.convert(new DateStringDto2(),
                DateDto.class, createContext("testConvert4", null));
        assertEquals(2020, getYear(dateDto.getA()));
        assertEquals(3 - 1, getMonth(dateDto.getA()));
        assertEquals(4, getDate(dateDto.getA()));
    }

    /**
     * 
     */
    public static final String testConvert5_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * @throws Exception
     */
    public void testConvert5() throws Exception {
        DateStringDto3 dateStringDto = (DateStringDto3) converter.convert(
                new DateDto(), DateStringDto3.class, createContext(
                        "testConvert5", null));
        assertEquals("21000622", dateStringDto.getA_yyyyMMdd());
    }

    /**
     * 
     */
    public static final String testConvert6_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * @throws Exception
     */
    public void testConvert6() throws Exception {
        DateDto dateDto = (DateDto) converter.convert(new DateStringDto3(),
                DateDto.class, createContext("testConvert6", null));
        assertEquals(2020, getYear(dateDto.getA()));
        assertEquals(3 - 1, getMonth(dateDto.getA()));
        assertEquals(4, getDate(dateDto.getA()));
    }

    /**
     * @throws Exception
     */
    public void testConvert7() throws Exception {
        CalendarDto calendarDto = (CalendarDto) converter.convert(
                new DateStringDto(), CalendarDto.class, createContext(
                        "testConvert7", null));
        assertEquals(2000, calendarDto.getA().get(Calendar.YEAR));
        assertEquals(10 - 1, calendarDto.getA().get(Calendar.MONTH));
        assertEquals(30, calendarDto.getA().get(Calendar.DAY_OF_MONTH));
    }

    /**
     * @throws Exception
     */
    public void testConvert8() throws Exception {
        DateStringDto dateStringDto = (DateStringDto) converter.convert(
                new CalendarDto(), DateStringDto.class, createContext(
                        "testConvert8", null));
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

    /**
     * @throws Exception
     */
    public void testConvert9() throws Exception {
        CircularReferenceEmployee employee = new CircularReferenceEmployee();
        employee.setEname("ename");
        CircularReferenceEmployee employee2 = new CircularReferenceEmployee();
        employee2.setEname("ename2");
        CircularReferenceDepartment department = new CircularReferenceDepartment();
        department.setDname("dname");
        employee.setDept(department);
        employee2.setDept(department);
        department.setEmps(new CircularReferenceEmployee[] { employee,
                employee2 });
        CircularReferenceDeptDto crDeptDto = (CircularReferenceDeptDto) converter
                .convert(department, CircularReferenceDeptDto.class,
                        createContext("testConvert9", null));
        System.out.println(crDeptDto);
    }

    /**
     * @throws Exception
     */
    public void testNestedProperty() throws Exception {
        HogeHoge hogehoge = new HogeHoge();
        Hoge hoge = (Hoge) converter.convert(hogehoge, Hoge.class,
                createContext("testNestedProperty", null));
        assertNotNull(hoge);
        assertNull(hoge.foo);
        assertNull(hoge.bar);

        Hoge hoge2 = new Hoge();
        hoge2.setFoo(DateUtil.newDate(1966, 8, 18));
        hoge2.setBar(DateUtil.newDate(1972, 2, 28));
        hogehoge.setHoge(hoge2);

        hoge = (Hoge) converter.convert(hogehoge, Hoge.class, createContext(
                "testNestedProperty", null));
        assertNotNull(hoge);
        assertEquals(DateUtil.newDate(1966, 8, 18), hoge.foo);
        assertEquals(DateUtil.newDate(1972, 2, 28), hoge.bar);
    }

    /**
     * @throws Exception
     */
    public void testArray() throws Exception {
        Foo foo = new Foo();
        foo.array = new Object[] { "1", "2" };
        converter.setShallowCopy(true);
        Bar bar = (Bar) converter.convert(foo, Bar.class, createContext(
                "testArray", null));
        assertNotNull(bar);
        Integer[] array = bar.array;
        assertNotNull(array);
        assertEquals(2, array.length);
        assertEquals(new Integer(1), array[0]);
        assertEquals(new Integer(2), array[1]);
    }

    /**
     * @throws Exception
     */
    public void testGetConverter() throws Exception {
        Converter fooConverter = converter.getConverter(String.class,
                Hoge.class, "foo", Date.class, createContext(
                        "testGetConverter", null));
        assertNotNull(fooConverter);
        assertTrue(fooConverter instanceof SqlDateConverter);

        Converter barConverter = converter.getConverter(String.class,
                Hoge.class, "bar", Date.class, createContext(
                        "testGetConverter", null));
        assertNotNull(barConverter);
        assertTrue(barConverter instanceof DateConverter);
    }

    private int getDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getMonth(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.MONTH);
    }

    private int getYear(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 
     */
    public static class Hoge {
        /**
         * 
         */
        public static final String foo_sqlDateDxoConverter = null;

        /**
         * 
         */
        protected Date foo;

        /**
         * 
         */
        protected Date bar;

        /**
         * @return
         */
        public Date getFoo() {
            return foo;
        }

        /**
         * @param foo
         */
        public void setFoo(Date foo) {
            this.foo = foo;
        }

        /**
         * @return
         */
        public Date getBar() {
            return bar;
        }

        /**
         * @param bar
         */
        public void setBar(Date bar) {
            this.bar = bar;
        }
    }

    /**
     * 
     */
    public static class HogeHoge {
        /**
         * 
         */
        protected Hoge hoge;

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            this.hoge = hoge;
        }
    }

    /**
     * 
     */
    public static class Foo {
        /**
         * 
         */
        public Object[] array;
    }

    /**
     * 
     */
    public static class Bar {
        /**
         * 
         */
        public Integer[] array;
    }

}
