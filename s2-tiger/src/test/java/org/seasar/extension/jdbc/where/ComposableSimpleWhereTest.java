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
public class ComposableSimpleWhereTest extends TestCase {

    /**
     * 
     */
    public void testAddCondition() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        w.conditionList.add(new ComposableSimpleWhere.Condition(
                ConditionType.EQ, "id", 1));
        w.conditionList.add(new ComposableSimpleWhere.Condition(
                ConditionType.EQ, "name", "hoge"));
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
        ComposableSimpleWhere w = new ComposableSimpleWhere();
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
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.eq("id", 1));
        assertEquals("id = ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testEq_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.eq("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEq_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.eq("id", ""));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNe() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ne("id", 1));
        assertEquals("id <> ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testNe_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ne("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNe_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.ne("id", " "));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLt() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.lt("id", 1));
        assertEquals("id < ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLt_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.lt("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLt_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.lt("id", "\t"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLe() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.le("id", 1));
        assertEquals("id <= ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLe_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.le("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLe_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.le("id", "\n"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGt() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.gt("id", 1));
        assertEquals("id > ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testGt_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.gt("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGt_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.gt("id", "\r"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGe() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ge("id", 1));
        assertEquals("id >= ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testGe_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ge("id", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testGe_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.ge("id", "\r\n"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.in("id", 1, 2));
        assertEquals("id in (?, ?)", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.in("id", (Object[]) null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.in("id", "", " ", "\t"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIn_zero() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.in("id"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.notIn("id", 1, 2));
        assertEquals("id not in (?, ?)", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.notIn("id", (Object[]) null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.notIn("id", null, "", " ", "\n"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testNotIn_zero() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.notIn("id"));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.like("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.like("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.like("name", ""));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike_escape() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.like("name", "100$%", '$'));
        assertEquals("name like ? escape ?", w.getCriteria());
        assertEquals("100$%", w.getParams()[0]);
        assertEquals('$', w.getParams()[1]);
    }

    /**
     * 
     */
    public void testLike_escape_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.like("name", null, '$'));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testLike_escape_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.like("name", "", '$'));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testStarts() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.starts("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testStarts_withMetachar() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.starts("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("$$100$%%", w.getParams()[0]);
    }

    /**
     * 
     */
    public void testStarts_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.starts("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testStarts_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.starts("name", ""));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEnds() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ends("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testEnds_withMetachar() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ends("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("%$$100$%", w.getParams()[0]);
    }

    /**
     * 
     */
    public void testEnds_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.ends("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEnds_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.ends("name", ""));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testContains() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.contains("name", "hoge"));
        assertEquals("name like ?", w.getCriteria());
    }

    /**
     * 
     */
    public void testContains_withMetachar() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.contains("name", "$100%"));
        assertEquals("name like ? escape '$'", w.getCriteria());
        assertEquals("%$$100$%%", w.getParams()[0]);
    }

    /**
     * 
     */
    public void testContains_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.contains("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testContains_excludesWhitespace() {
        ComposableSimpleWhere w = new ComposableSimpleWhere()
                .excludesWhitespace();
        assertSame(w, w.contains("name", ""));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.isNull("name", true));
        assertEquals("name is null", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.isNull("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNull_false() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.isNull("name", false));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNotNull() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.isNotNull("name", true));
        assertEquals("name is not null", w.getCriteria());
    }

    /**
     * 
     */
    public void testIsNotNull_null() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertSame(w, w.isNotNull("name", null));
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEscapeWildcard() {
        ComposableSimpleWhere w = new ComposableSimpleWhere();
        assertEquals("aaa", w.escapeWildcard("aaa"));
        assertEquals("$$$%$_", w.escapeWildcard("$%_"));
    }
}
