/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.types;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class TimestampTypeTest extends TestCase {

    private TimestampType timestampType = new TimestampType();

    /**
     * 
     * @throws Exception
     */
    public void testToText() throws Exception {
        Timestamp timestamp = Timestamp
                .valueOf("2007-11-29 13:14:15");
        assertEquals(timestamp, timestampType
                .toTimestamp("2007/11/29 13:14:15"));
    }

}
