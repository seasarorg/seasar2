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

import java.sql.Timestamp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class TimestampTypeTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNanos() throws Exception {
        TimestampType timestampType = new TimestampType();
        Timestamp timestamp = timestampType
                .toTimestamp("2008/01/01 12:11:10.123456");
        assertEquals(123456000, timestamp.getNanos());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNanos_zero() throws Exception {
        TimestampType timestampType = new TimestampType();
        Timestamp timestamp = timestampType
                .toTimestamp("2008/01/01 12:11:10.000");
        assertEquals(0, timestamp.getNanos());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testToTimestamp_hyphen() throws Exception {
        TimestampType timestampType = new TimestampType();
        assertEquals(Timestamp.valueOf("2008-01-01 01:02:03"),
                timestampType.toTimestamp("2008-01-01 01:02:03"));
        assertEquals(Timestamp.valueOf("2008-01-01 01:02:03"),
                timestampType.toTimestamp("2008-01-01 01:02:03"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testToTimestamp_slash() throws Exception {
        TimestampType timestampType = new TimestampType();
        assertEquals(Timestamp.valueOf("2008-01-01 01:02:03"),
                timestampType.toTimestamp("2008/01/01 01:02:03"));
        assertEquals(Timestamp.valueOf("2008-01-01 01:02:03"),
                timestampType.toTimestamp("2008/01/01 01:02:03"));
    }
}
