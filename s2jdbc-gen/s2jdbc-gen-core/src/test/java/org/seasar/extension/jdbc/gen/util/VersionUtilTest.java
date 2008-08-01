/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.util;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.IllegalVersionValueRuntimeException;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class VersionUtilTest {

    /**
     * 
     */
    @Test
    public void testToInt() {
        assertEquals(10, VersionUtil.toInt("10"));
    }

    /**
     * 
     */
    @Test
    public void testToInt_null() {
        try {
            VersionUtil.toInt(null);
            fail();
        } catch (IllegalVersionValueRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testToInt_notNumber() {
        try {
            VersionUtil.toInt("aaa");
            fail();
        } catch (IllegalVersionValueRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testToInt_minus() {
        try {
            VersionUtil.toInt("-10");
            fail();
        } catch (IllegalVersionValueRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testToInt_greaterThanInteger() {
        long value = Integer.MAX_VALUE + 1;
        try {
            VersionUtil.toInt(String.valueOf(value));
            fail();
        } catch (IllegalVersionValueRuntimeException expected) {
        }
    }
}
