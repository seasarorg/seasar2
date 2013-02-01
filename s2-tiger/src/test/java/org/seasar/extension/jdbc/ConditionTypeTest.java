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
package org.seasar.extension.jdbc;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.exception.NonArrayInConditionRuntimeException;
import org.seasar.extension.jdbc.exception.NonBooleanIsNullConditionRuntimeException;

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

    /**
     * 
     */
    public void testMakeCondition() {
        assertEquals("T1_.ID = ?", ConditionType.EQ.makeCondition("T1_", "ID",
                "="));
        assertEquals("ID = ?", ConditionType.EQ.makeCondition(null, "ID", "="));
    }

    /**
     * 
     */
    public void testEQ_getCondition() {
        assertEquals("T1_.ID = ?", ConditionType.EQ
                .getCondition("T1_", "ID", 1));
    }

    /**
     * 
     */
    public void testEQ_getCondition_name() {
        assertEquals("id = ?", ConditionType.EQ.getCondition("id", 1));
    }

    /**
     * 
     */
    public void testEQ_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.EQ.addValue(paramList, 1));
        assertEquals(1, paramList.size());
        assertEquals(1, paramList.get(0));
    }

    /**
     * 
     */
    public void testEQ_isTarget() {
        assertTrue(ConditionType.EQ.isTarget(1));
        assertFalse(ConditionType.EQ.isTarget(null));
    }

    /**
     * 
     */
    public void testNE_getCondition() {
        assertEquals("T1_.ID <> ?", ConditionType.NE.getCondition("T1_", "ID",
                1));
    }

    /**
     * 
     */
    public void testNE_getCondition_name() {
        assertEquals("id <> ?", ConditionType.NE.getCondition("id", 1));
    }

    /**
     * 
     */
    public void testNE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.NE.addValue(paramList, 1));
        assertEquals(1, paramList.size());
        assertEquals(1, paramList.get(0));
    }

    /**
     * 
     */
    public void testNE_isTarget() {
        assertTrue(ConditionType.NE.isTarget(1));
        assertFalse(ConditionType.NE.isTarget(null));
    }

    /**
     * 
     */
    public void testLT_getCondition() {
        assertEquals("T1_.ID < ?", ConditionType.LT
                .getCondition("T1_", "ID", 1));
    }

    /**
     * 
     */
    public void testLT_getCondition_name() {
        assertEquals("id < ?", ConditionType.LT.getCondition("id", 1));
    }

    /**
     * 
     */
    public void testLT_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.LT.addValue(paramList, 1));
        assertEquals(1, paramList.size());
        assertEquals(1, paramList.get(0));
    }

    /**
     * 
     */
    public void testLT_isTarget() {
        assertTrue(ConditionType.LT.isTarget(1));
        assertFalse(ConditionType.LT.isTarget(null));
    }

    /**
     * 
     */
    public void testLE_getCondition() {
        assertEquals("T1_.ID <= ?", ConditionType.LE.getCondition("T1_", "ID",
                1));
    }

    /**
     * 
     */
    public void testLE_getCondition_name() {
        assertEquals("id <= ?", ConditionType.LE.getCondition("id", 1));
    }

    /**
     * 
     */
    public void testLE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.LE.addValue(paramList, 1));
        assertEquals(1, paramList.size());
        assertEquals(1, paramList.get(0));
    }

    /**
     * 
     */
    public void testLE_isTarget() {
        assertTrue(ConditionType.LE.isTarget(1));
        assertFalse(ConditionType.LE.isTarget(null));
    }

    /**
     * 
     */
    public void testGT_getCondition() {
        assertEquals("T1_.ID > ?", ConditionType.GT
                .getCondition("T1_", "ID", 1));
    }

    /**
     * 
     */
    public void testGT_getCondition_name() {
        assertEquals("id > ?", ConditionType.GT.getCondition("id", 1));
    }

    /**
     * 
     */
    public void testGT_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.GT.addValue(paramList, 1));
        assertEquals(1, paramList.size());
        assertEquals(1, paramList.get(0));
    }

    /**
     * 
     */
    public void testGT_isTarget() {
        assertTrue(ConditionType.GT.isTarget(1));
        assertFalse(ConditionType.GT.isTarget(null));
    }

    /**
     * 
     */
    public void testGetInConditionInternal() {
        assertEquals("T1_.ID in (?, ?)", ConditionType.IN.getCondition("T1_",
                "ID", new Object[] { 1, 2 }));
    }

    /**
     * 
     */
    public void testIN_getCondition() {
        assertEquals("T1_.ID in (?, ?)", ConditionType.IN.getCondition("T1_",
                "ID", new Object[] { 1, 2 }));
    }

    /**
     * 
     */
    public void testIN_getCondition_name() {
        assertEquals("id in (?, ?)", ConditionType.IN.getCondition("id",
                new Object[] { 1, 2 }));
    }

    /**
     * 
     */
    public void testIN_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(2, ConditionType.IN.addValue(paramList, new Object[] { 1,
                2 }));
        assertEquals(2, paramList.size());
        assertEquals(1, paramList.get(0));
        assertEquals(2, paramList.get(1));
    }

    /**
     * 
     */
    public void testIN_isTarget() {
        assertTrue(ConditionType.IN.isTarget(new Object[] { 1 }));
        assertFalse(ConditionType.IN.isTarget(null));
        assertFalse(ConditionType.IN.isTarget(new Object[0]));
        assertFalse(ConditionType.IN.isTarget(new Object[] { null }));
        assertFalse(ConditionType.IN.isTarget(new Object[] { null, null }));
    }

    /**
     * 
     */
    public void testIN_isTarget_notArray() {
        try {
            ConditionType.IN.isTarget("hoge");
            fail();
        } catch (NonArrayInConditionRuntimeException e) {
            System.out.println(e);
            assertEquals("in", e.getConditionName());
            assertEquals(String.class, e.getValueClass());
        }
    }

    /**
     * 
     */
    public void testNOT_IN_getCondition() {
        assertEquals("T1_.ID not in (?, ?)", ConditionType.NOT_IN.getCondition(
                "T1_", "ID", new Object[] { 1, 2 }));
    }

    /**
     * 
     */
    public void testNOT_IN_getCondition_name() {
        assertEquals("id not in (?, ?)", ConditionType.NOT_IN.getCondition(
                "id", new Object[] { 1, 2 }));
    }

    /**
     * 
     */
    public void testNOT_IN_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(2, ConditionType.NOT_IN.addValue(paramList, new Object[] {
                1, 2 }));
        assertEquals(2, paramList.size());
        assertEquals(1, paramList.get(0));
        assertEquals(2, paramList.get(1));
    }

    /**
     * 
     */
    public void testNOT_IN_isTarget() {
        assertTrue(ConditionType.NOT_IN.isTarget(new Object[] { 1 }));
        assertFalse(ConditionType.NOT_IN.isTarget(null));
        assertFalse(ConditionType.NOT_IN.isTarget(new Object[0]));
        assertFalse(ConditionType.NOT_IN.isTarget(new Object[] { null }));
        assertFalse(ConditionType.NOT_IN.isTarget(new Object[] { null, null }));
    }

    /**
     * 
     */
    public void testNOT_IN_isTarget_notArray() {
        try {
            ConditionType.NOT_IN.isTarget("hoge");
            fail();
        } catch (NonArrayInConditionRuntimeException e) {
            System.out.println(e);
            assertEquals("not in", e.getConditionName());
            assertEquals(String.class, e.getValueClass());
        }
    }

    /**
     * 
     */
    public void testLIKE_getCondition() {
        assertEquals("T1_.ID like ?", ConditionType.LIKE.getCondition("T1_",
                "ID", "hoge"));
    }

    /**
     * 
     */
    public void testLIKE_getCondition_name() {
        assertEquals("id like ?", ConditionType.LIKE.getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testLIKE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.LIKE.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("hoge", paramList.get(0));
    }

    /**
     * 
     */
    public void testLIKE_isTarget() {
        assertTrue(ConditionType.LIKE.isTarget("hoge"));
        assertFalse(ConditionType.LIKE.isTarget(null));
    }

    /**
     * 
     */
    public void testLIKE_ESCAPE_getCondition() {
        assertEquals("T1_.ID like ? escape ?", ConditionType.LIKE_ESCAPE
                .getCondition("T1_", "ID", "hoge"));
    }

    /**
     * 
     */
    public void testLIKE_ESCAPE_getCondition_name() {
        assertEquals("id like ? escape ?", ConditionType.LIKE_ESCAPE
                .getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testLIKE_ESCAPE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(2, ConditionType.LIKE_ESCAPE.addValue(paramList,
                new Object[] { "hoge", '$' }));
        assertEquals(2, paramList.size());
        assertEquals("hoge", paramList.get(0));
        assertEquals('$', paramList.get(1));
    }

    /**
     * 
     */
    public void testLIKE_ESCAPE_isTarget() {
        assertTrue(ConditionType.LIKE_ESCAPE.isTarget("hoge"));
        assertFalse(ConditionType.LIKE_ESCAPE.isTarget(null));
    }

    /**
     * 
     */
    public void testSTARTS_getCondition() {
        assertEquals("T1_.ID like ?", ConditionType.STARTS.getCondition("T1_",
                "ID", "hoge"));
    }

    /**
     * 
     */
    public void testSTARTS_getCondition_name() {
        assertEquals("id like ?", ConditionType.STARTS.getCondition("id",
                "hoge"));
    }

    /**
     * 
     */
    public void testSTARTS_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.STARTS.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("hoge%", paramList.get(0));
    }

    /**
     * 
     */
    public void testSTARTS_isTarget() {
        assertTrue(ConditionType.STARTS.isTarget("hoge"));
        assertFalse(ConditionType.STARTS.isTarget(null));
    }

    /**
     * 
     */
    public void testSTARTS_ESCAPE_getCondition() {
        assertEquals("T1_.ID like ? escape '$'", ConditionType.STARTS_ESCAPE
                .getCondition("T1_", "ID", "hoge"));
    }

    /**
     * 
     */
    public void testSTARTS_ESCAPE_getCondition_name() {
        assertEquals("id like ? escape '$'", ConditionType.STARTS_ESCAPE
                .getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testSTARTS_ESCAPE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.STARTS_ESCAPE.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("hoge%", paramList.get(0));
    }

    /**
     * 
     */
    public void testSTARTS_ESCAPE_isTarget() {
        assertTrue(ConditionType.STARTS_ESCAPE.isTarget("hoge"));
        assertFalse(ConditionType.STARTS_ESCAPE.isTarget(null));
    }

    /**
     * 
     */
    public void testENDS_getCondition() {
        assertEquals("T1_.ID like ?", ConditionType.ENDS.getCondition("T1_",
                "ID", "hoge"));
    }

    /**
     * 
     */
    public void testENDS_getCondition_name() {
        assertEquals("id like ?", ConditionType.ENDS.getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testENDS_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.ENDS.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("%hoge", paramList.get(0));
    }

    /**
     * 
     */
    public void testENDS_isTarget() {
        assertTrue(ConditionType.ENDS.isTarget("hoge"));
        assertFalse(ConditionType.ENDS.isTarget(null));
    }

    /**
     * 
     */
    public void testENDS_ESCAPE_getCondition() {
        assertEquals("T1_.ID like ? escape '$'", ConditionType.ENDS_ESCAPE
                .getCondition("T1_", "ID", "hoge"));
    }

    /**
     * 
     */
    public void testENDS_ESCAPE_getCondition_name() {
        assertEquals("id like ? escape '$'", ConditionType.ENDS_ESCAPE
                .getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testENDS_ESCAPE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.ENDS_ESCAPE.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("%hoge", paramList.get(0));
    }

    /**
     * 
     */
    public void testENDS_ESCAPE_isTarget() {
        assertTrue(ConditionType.ENDS_ESCAPE.isTarget("hoge"));
        assertFalse(ConditionType.ENDS_ESCAPE.isTarget(null));
    }

    /**
     * 
     */
    public void testCONTAINS_getCondition() {
        assertEquals("T1_.ID like ?", ConditionType.CONTAINS.getCondition(
                "T1_", "ID", "hoge"));
    }

    /**
     * 
     */
    public void testCONTAINS_getCondition_name() {
        assertEquals("id like ?", ConditionType.CONTAINS.getCondition("id",
                "hoge"));
    }

    /**
     * 
     */
    public void testCONTAINS_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.CONTAINS.addValue(paramList, "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("%hoge%", paramList.get(0));
    }

    /**
     * 
     */
    public void testCONTAINS_isTarget() {
        assertTrue(ConditionType.CONTAINS.isTarget("hoge"));
        assertFalse(ConditionType.CONTAINS.isTarget(null));
    }

    /**
     * 
     */
    public void testCONTAINS_ESCAPE_getCondition() {
        assertEquals("T1_.ID like ? escape '$'", ConditionType.CONTAINS_ESCAPE
                .getCondition("T1_", "ID", "hoge"));
    }

    /**
     * 
     */
    public void testCONTAINS_ESCAPE_getCondition_name() {
        assertEquals("id like ? escape '$'", ConditionType.CONTAINS_ESCAPE
                .getCondition("id", "hoge"));
    }

    /**
     * 
     */
    public void testCONTAINS_ESCAPE_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(1, ConditionType.CONTAINS_ESCAPE.addValue(paramList,
                "hoge"));
        assertEquals(1, paramList.size());
        assertEquals("%hoge%", paramList.get(0));
    }

    /**
     * 
     */
    public void testCONTAINS_ESCAPE_isTarget() {
        assertTrue(ConditionType.CONTAINS_ESCAPE.isTarget("hoge"));
        assertFalse(ConditionType.CONTAINS_ESCAPE.isTarget(null));
    }

    /**
     * 
     */
    public void testIS_NULL_getCondition() {
        assertEquals("T1_.ID is null", ConditionType.IS_NULL.getCondition(
                "T1_", "ID", true));
    }

    /**
     * 
     */
    public void testIS_NULL_getCondition_name() {
        assertEquals("id is null", ConditionType.IS_NULL.getCondition("id",
                true));
    }

    /**
     * 
     */
    public void testIS_NULL_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(0, ConditionType.IS_NULL.addValue(paramList, true));
        assertEquals(0, paramList.size());
    }

    /**
     * 
     */
    public void testIS_NULL_isTarget() {
        assertTrue(ConditionType.IS_NULL.isTarget(true));
        assertFalse(ConditionType.IS_NULL.isTarget(null));
        assertFalse(ConditionType.IS_NULL.isTarget(false));
    }

    /**
     * 
     */
    public void testIS_NULL_isTarget_notArray() {
        try {
            ConditionType.IS_NULL.isTarget("hoge");
            fail();
        } catch (NonBooleanIsNullConditionRuntimeException e) {
            System.out.println(e);
            assertEquals("is null", e.getConditionName());
            assertEquals(String.class, e.getValueClass());
        }
    }

    /**
     * 
     */
    public void testIS_NOT_NULL_getCondition() {
        assertEquals("T1_.ID is not null", ConditionType.IS_NOT_NULL
                .getCondition("T1_", "ID", true));
    }

    /**
     * 
     */
    public void testIS_NOT_NULL_getCondition_name() {
        assertEquals("id is not null", ConditionType.IS_NOT_NULL.getCondition(
                "id", true));
    }

    /**
     * 
     */
    public void testIS_NOT_NULL_addValue() {
        List<Object> paramList = new ArrayList<Object>();
        assertEquals(0, ConditionType.IS_NOT_NULL.addValue(paramList, true));
        assertEquals(0, paramList.size());
    }

    /**
     * 
     */
    public void testIS_NOT_NULL_isTarget() {
        assertTrue(ConditionType.IS_NOT_NULL.isTarget(true));
        assertFalse(ConditionType.IS_NOT_NULL.isTarget(null));
        assertFalse(ConditionType.IS_NOT_NULL.isTarget(false));
    }

    /**
     * 
     */
    public void testIS_NOT_NULL_isTarget_notArray() {
        try {
            ConditionType.IS_NOT_NULL.isTarget("hoge");
            fail();
        } catch (NonBooleanIsNullConditionRuntimeException e) {
            System.out.println(e);
            assertEquals("is not null", e.getConditionName());
            assertEquals(String.class, e.getValueClass());
        }
    }

    /**
     * 
     */
    public void testAddCondition() {
        List<Object> valueList = new ArrayList<Object>();
        WhereClause whereClause = new WhereClause();
        ConditionType.EQ.addCondition("id", 1, whereClause, valueList);
        assertEquals(" where id = ?", whereClause.toSql());
        assertEquals(1, valueList.size());
        assertEquals(1, valueList.get(0));
    }
}