/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.sqltype;

import java.sql.Time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class TimeTypeTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testToTime() throws Exception {
        TimeType timeType = new TimeType();
        assertEquals(Time.valueOf("01:02:03"), timeType.toTime("01:02:03"));
        assertEquals(Time.valueOf("13:02:03"), timeType.toTime("13:02:03"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        TimeType timeType = new TimeType();
        assertEquals("01:02:03", timeType.toString(Time.valueOf("01:02:03")));
        assertEquals("13:02:03", timeType.toString(Time.valueOf("13:02:03")));
    }
}
