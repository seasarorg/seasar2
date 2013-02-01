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
package org.seasar.extension.jdbc.where;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class ComplexWhereTest extends TestCase {

    /**
     * 
     */
    public void testIsNotNull_false() {
        ComplexWhere w = new ComplexWhere();
        assertSame(w, w.isNotNull("name", false));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testOr2() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", 1).eq("b", 2).or().eq("c", 3).eq("d", 4);
        assertEquals("(a = ? and b = ?) or (c = ? and d = ?)", w.getCriteria());
        assertEquals(4, w.getParams().length);
        assertEquals(4, w.getPropertyNames().length);
    }

    /**
     * 
     */
    public void testOr3() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", 1).eq("b", 2).or().eq("c", 3).eq("d", 4).or().eq("e", 5).eq(
                "f", 6);
        assertEquals(
                "(a = ? and b = ?) or (c = ? and d = ?) or (e = ? and f = ?)",
                w.getCriteria());
        assertEquals(6, w.getParams().length);
        assertEquals(6, w.getPropertyNames().length);
    }

    /**
     * 
     */
    public void testOrEnds() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", 1).eq("b", 2).or().eq("c", 3).eq("d", 4).or();
        assertEquals("(a = ? and b = ?) or (c = ? and d = ?)", w.getCriteria());
        assertEquals(4, w.getParams().length);
        assertEquals(4, w.getPropertyNames().length);
    }

    /**
     * 
     */
    public void testOrEmptyTerm() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", null).eq("b", null).or().eq("c", 3).eq("d", 4);
        assertEquals("c = ? and d = ?", w.getCriteria());
        assertEquals(2, w.getParams().length);
        assertEquals(2, w.getPropertyNames().length);
    }

    /**
     * 
     */
    public void testAnd() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", 1).eq("b", 2).and(
                new ComplexWhere().eq("c", 3).or().eq("d", 4)).eq("e", 5).eq(
                "f", 6);
        assertEquals(
                "a = ? and b = ? and ((c = ?) or (d = ?)) and e = ? and f = ?",
                w.getCriteria());
        assertEquals(6, w.getParams().length);
        assertEquals(6, w.getPropertyNames().length);
    }

    /**
     * 
     */
    public void testAnd2() {
        ComplexWhere w = new ComplexWhere();
        w.eq("a", null).eq("b", null).and(
                new ComplexWhere().eq("c", 3).or().eq("d", 4)).eq("e", 5).eq(
                "f", 6);
        assertEquals(" ((c = ?) or (d = ?)) and e = ? and f = ?", w
                .getCriteria());
        assertEquals(4, w.getParams().length);
        assertEquals(4, w.getPropertyNames().length);
    }

}
