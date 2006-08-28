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
import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author Satsohi Kimura
 */
public abstract class BeanConverterTest extends S2TestCase {

    private BeanConverter beanConverter;

    private ConverterFactory converterFactory;

    private AnnotationReaderFactory annotationReaderFactory;

    protected void setUp() {
        include("dxo.dicon");
        beanConverter = new BeanConverter();
    }

    public void testConvert1() throws Exception {
        DateDto dateDto = (DateDto) beanConverter.convert(new DateStringDto(),
                DateDto.class, createContext("testConvert1"));
        assertEquals(10 - 1, getMonth(dateDto.getA()));
        assertEquals(30, getDate(dateDto.getA()));
    }

    public void testConvert2() throws Exception {
        DateStringDto dateStringDto = (DateStringDto) beanConverter.convert(
                new DateDto(), DateStringDto.class,
                createContext("testConvert2"));
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

    public static final String testConvert3_DATE_PATTERN = "yyyy/MM/dd";

    public void testConvert3() throws Exception {
        DateStringDto2 dateStringDto = (DateStringDto2) beanConverter.convert(
                new DateDto(), DateStringDto2.class,
                createContext("testConvert3"));
        assertEquals("2100/06/22", dateStringDto.getA());
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

    public static final String testConvert4_DATE_PATTERN = "yyyy/MM/dd";

    public void testConvert4() throws Exception {
        DateDto dateDto = (DateDto) beanConverter.convert(new DateStringDto2(),
                DateDto.class, createContext("testConvert4"));
        assertEquals(2020, getYear(dateDto.getA()));
        assertEquals(3 - 1, getMonth(dateDto.getA()));
        assertEquals(4, getDate(dateDto.getA()));
    }

    public static final String testConvert5_DATE_PATTERN = "yyyy/MM/dd";

    public void testConvert5() throws Exception {
        DateStringDto3 dateStringDto = (DateStringDto3) beanConverter.convert(
                new DateDto(), DateStringDto3.class,
                createContext("testConvert5"));
        assertEquals("21000622", dateStringDto.getA_yyyyMMdd());
    }

    public static final String testConvert6_DATE_PATTERN = "yyyy/MM/dd";

    public void testConvert6() throws Exception {
        DateDto dateDto = (DateDto) beanConverter.convert(new DateStringDto3(),
                DateDto.class, createContext("testConvert6"));
        assertEquals(2020, getYear(dateDto.getA()));
        assertEquals(3 - 1, getMonth(dateDto.getA()));
        assertEquals(4, getDate(dateDto.getA()));
    }

    public void testConvert7() throws Exception {
        CalendarDto calendarDto = (CalendarDto) beanConverter.convert(
                new DateStringDto(), CalendarDto.class,
                createContext("testConvert7"));
        assertEquals(2000, calendarDto.getA().get(Calendar.YEAR));
        assertEquals(10 - 1, calendarDto.getA().get(Calendar.MONTH));
        assertEquals(30, calendarDto.getA().get(Calendar.DAY_OF_MONTH));
    }

    public void testConvert8() throws Exception {
        DateStringDto dateStringDto = (DateStringDto) beanConverter.convert(
                new CalendarDto(), DateStringDto.class,
                createContext("testConvert8"));
        assertEquals("2100", dateStringDto.getA_yyyy());
        assertEquals("06", dateStringDto.getA_MM());
        assertEquals("22", dateStringDto.getA_dd());
    }

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
        CircularReferenceDeptDto crDeptDto = (CircularReferenceDeptDto) beanConverter
                .convert(department, CircularReferenceDeptDto.class,
                        createContext("testConvert9"));
        System.out.println(crDeptDto);
    }

    private ConversionContext createContext(String methodName) throws Exception {
        return new ConversionContextImpl(BeanConverterTest.class,
                BeanConverterTest.class.getMethod(methodName, null),
                converterFactory, annotationReaderFactory.getAnnotationReader());
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

}
