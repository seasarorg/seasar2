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
        String[] names = w.getPropertyNames();
        assertEquals(2, params.length);
        assertEquals("id", names[0]);
        assertEquals("name", names[1]);
    }

    /**
     * 
     */
    public void testNormalize() {
        SimpleWhere w = new SimpleWhere();
        assertNull(w.normalize(null));
        assertEquals("", w.normalize(""));
        assertEquals(" ", w.normalize(" "));
        assertEquals(Integer.valueOf(1), w.normalize(Integer.valueOf(1)));

        assertNull(w.normalize((Object[]) null));
        Object[] normalized = w.normalize(null, "", " ", Integer.valueOf(1));
        assertEquals(4, normalized.length);
        assertNull(normalized[0]);
        assertEquals("", normalized[1]);
        assertEquals(" ", normalized[2]);
        assertEquals(Integer.valueOf(1), normalized[3]);

        w.excludesWhitespace();

        assertNull(w.normalize(null));
        assertNull(w.normalize(""));
        assertNull(w.normalize(" "));
        assertEquals(Integer.valueOf(1), w.normalize(Integer.valueOf(1)));

        assertNull(w.normalize((Object[]) null));
        normalized = w.normalize(null, "", " ", Integer.valueOf(1));
        assertEquals(1, normalized.length);
        assertEquals(Integer.valueOf(1), normalized[0]);
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
    public void testEq_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.eq("id", ""));
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
    public void testNe_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.ne("id", " "));
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
    public void testLt_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.lt("id", "\t"));
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
    public void testLe_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.le("id", "\n"));
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
    public void testGt_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.gt("id", "\r"));
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
    public void testGe_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.ge("id", "\r\n"));
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
    public void testIn_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.in("id", "", " ", "\t"));
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
    public void testNotIn_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.notIn("id", null, "", " ", "\n"));
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
    public void testLike_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.like("name", ""));
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
    public void testStarts_withMetachar() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.starts("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("$$100$%%", w.paramList.get(0));
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
    public void testStarts_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.starts("name", ""));
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
    public void testEnds_withMetachar() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.ends("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("%$$100$%", w.paramList.get(0));
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
    public void testEnds_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.ends("name", ""));
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
    public void testContains_withMetachar() {
        SimpleWhere w = new SimpleWhere();
        assertSame(w, w.contains("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("%$$100$%%", w.paramList.get(0));
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
    public void testContains_excludesWhitespace() {
        SimpleWhere w = new SimpleWhere().excludesWhitespace();
        assertSame(w, w.contains("name", ""));
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

    public void testEscapeWildcard() {
        SimpleWhere w = new SimpleWhere();
        assertEquals("aaa", w.escapeWildcard("aaa"));
        assertEquals("$$$%$_", w.escapeWildcard("$%_"));
    }
}
