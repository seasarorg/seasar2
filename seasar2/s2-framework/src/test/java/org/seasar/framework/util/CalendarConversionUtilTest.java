/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class CalendarConversionUtilTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    protected void tearDown() throws Exception {
        TimeZone.setDefault(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testToCalendar() throws Exception {
        Date date = new Date();
        Calendar cal = CalendarConversionUtil.toCalendar(date);
        assertEquals(date, cal.getTime());
    }

    /**
     * 
     * @throws Exception
     */
    public void testLocalize() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        Calendar local = CalendarConversionUtil.localize(calendar);
        assertEquals(TimeZone.getDefault(), local.getTimeZone());
    }
}