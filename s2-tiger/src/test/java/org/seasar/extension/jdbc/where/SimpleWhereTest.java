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
package org.seasar.extension.jdbc.where;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ConditionType;

/**
 * @author higa
 * 
 */
public class SimpleWhereTest extends TestCase {

    /**
     * 
     */
    public void testAddCondition() {
        SimpleWhere w = new SimpleWhere();
        w.addCondition(ConditionType.EQ, "id", 1);
        w.addCondition(ConditionType.EQ, "name", "hoge");
        assertEquals("id = ? and name = ?", w.getCriteria());
        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals(1, params[0]);
        assertEquals("hoge", params[1]);
    }

    /**
     * 
     */
    public void testEq() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.eq("id", 1));
        assertEquals("id = ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testEq_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.eq("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNe() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ne("id", 1));
        assertEquals("id <> ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testNe_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ne("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLt() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.lt("id", 1));
        assertEquals("id < ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLt_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.lt("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLe() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.le("id", 1));
        assertEquals("id <= ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLe_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.le("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGt() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.gt("id", 1));
        assertEquals("id > ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testGt_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.gt("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGe() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ge("id", 1));
        assertEquals("id >= ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testGe_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ge("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.in("id", 1, 2));
        assertEquals("id in (?, ?)", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.in("id", (Object[]) null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn_zero() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.in("id"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.notIn("id", 1, 2));
        assertEquals("id not in (?, ?)", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.notIn("id", (Object[]) null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn_zero() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.notIn("id"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.like("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.like("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testStarts() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.starts("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testStarts_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.starts("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEnds() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ends("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testEnds_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ends("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testContains() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.contains("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testContains_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.contains("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNull("name", true));
        assertEquals("name is null", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNull("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull_false() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNull("name", false));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNotNull() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNotNull("name", true));
        assertEquals("name is not null", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNotNull_null() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNotNull("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNotNull_false() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.isNotNull("name", false));
        assertEquals("", w.getCriteria());
    }
}