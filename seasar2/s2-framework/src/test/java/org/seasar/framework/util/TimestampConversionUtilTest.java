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
package org.seasar.framework.util;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class TimestampConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetPattern() throws Exception {
        assertEquals("yyyy/MM/dd HH:mm:ss", TimestampConversionUtil
                .getPattern(Locale.JAPANESE));
    }
    
    public void testToTimestamp() {
        Locale defaultLocale = Locale.getDefault();
        
        Locale.setDefault(Locale.JAPANESE);
        assertEquals("2014-12-13 14:15:16.0", TimestampConversionUtil
                .toTimestamp("2014/12/13 14:15:16").toString());
        
        Locale.setDefault(Locale.ENGLISH);
        assertEquals("2014-12-13 14:15:16.0", TimestampConversionUtil
                .toTimestamp("12/13/2014 14:15:16").toString());
        
        Locale.setDefault(defaultLocale);
    }
}