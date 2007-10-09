/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ConditionTypeTest extends TestCase {

    /**
     * 
     */
    public void testGetSuffix() {
        assertEquals("_EQ", ConditionType.EQ.getSuffix());
    }

    /**
     * 
     */
    public void testHasSuffix() {
        assertTrue(ConditionType.EQ.hasSuffix("aaa_EQ"));
        assertFalse(ConditionType.EQ.hasSuffix("aaa_NE"));
    }

    /**
     * 
     */
    public void testRemoveSuffix() {
        assertEquals("aaa", ConditionType.EQ.removeSuffix("aaa_EQ"));
    }

    /**
     * 
     */
    public void testRemoveSuffix_notSuffix() {
        assertEquals("aaa", ConditionType.EQ.removeSuffix("aaa"));
    }

    /**
     * 
     */
    public void testGetConditionType() {
        assertEquals(ConditionType.EQ, ConditionType.getConditionType("aaa_EQ"));
        assertEquals(ConditionType.NE, ConditionType.getConditionType("aaa_NE"));
        assertEquals(ConditionType.LT, ConditionType.getConditionType("aaa_LT"));
        assertEquals(ConditionType.LE, ConditionType.getConditionType("aaa_LE"));
        assertEquals(ConditionType.GT, ConditionType.getConditionType("aaa_GT"));
        assertEquals(ConditionType.GE, ConditionType.getConditionType("aaa_GE"));
        assertEquals(ConditionType.NOT_IN, ConditionType
                .getConditionType("aaa_NOT_IN"));
        assertEquals(ConditionType.IN, ConditionType.getConditionType("aaa_IN"));
        assertEquals(ConditionType.LIKE, ConditionType
                .getConditionType("aaa_LIKE"));
        assertEquals(ConditionType.STARTS, ConditionType
                .getConditionType("aaa_STARTS"));
        assertEquals(ConditionType.ENDS, ConditionType
                .getConditionType("aaa_ENDS"));
        assertEquals(ConditionType.CONTAINS, ConditionType
                .getConditionType("aaa_CONTAINS"));
        assertEquals(ConditionType.IS_NULL, ConditionType
                .getConditionType("aaa_IS_NULL"));
        assertEquals(ConditionType.IS_NOT_NULL, ConditionType
                .getConditionType("aaa_IS_NOT_NULL"));
    }
}
