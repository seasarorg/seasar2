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

import org.seasar.extension.jdbc.ConditionType;

import static org.seasar.extension.jdbc.where.EmployeeNames.*;

/**
 * @author koichik
 */
public class LikeOperatorTest extends TestCase {

    /**
     * 
     */
    public void testLike() {
        ComposableWhere w = new LikeOperator(ConditionType.LIKE, name(), "1%1");
        assertEquals("name like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("1%1", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testLike_Null() {
        ComposableWhere w = new LikeOperator(ConditionType.LIKE, name(), null);
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

    /**
     * 
     */
    public void testLike_ExcludeWhitespace() {
        ComposableWhere w = new LikeOperator(ConditionType.LIKE, name(), "   ")
                .excludesWhitespace();
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

    /**
     * 
     */
    public void testLikeEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.LIKE_ESCAPE,
                department().name(), "1%1", "$");
        assertEquals("department.name like ? escape ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals("1%1", params[0]);
        assertEquals("$", params[1]);

        String[] names = w.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("department.name", names[0]);
        assertEquals("department.name", names[1]);
    }

    /**
     * 
     */
    public void testNotLike() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_LIKE, name(),
                "1%1");
        assertEquals("name not like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("1%1", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotLike_Null() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_LIKE, name(),
                null);
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

    /**
     * 
     */
    public void testNotLike_ExcludeWhitespace() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_LIKE, name(),
                "   ").excludesWhitespace();
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

    /**
     * 
     */
    public void testNotLikeEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_LIKE_ESCAPE,
                department().name(), "1%1", "$");
        assertEquals("department.name not like ? escape ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals("1%1", params[0]);
        assertEquals("$", params[1]);

        String[] names = w.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("department.name", names[0]);
        assertEquals("department.name", names[1]);
    }

    /**
     * 
     */
    public void testStarts() {
        ComposableWhere w = new LikeOperator(ConditionType.STARTS, name(),
                "111");
        assertEquals("name like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("111%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testStartsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.STARTS, name(),
                "$%_");
        assertEquals("name like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("$$$%$_%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotStarts() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_STARTS, name(),
                "111");
        assertEquals("name not like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("111%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotStartsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_STARTS, name(),
                "$%_");
        assertEquals("name not like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("$$$%$_%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testEnds() {
        ComposableWhere w = new LikeOperator(ConditionType.ENDS, name(), "111");
        assertEquals("name like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%111", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testEndsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.ENDS, name(), "$%_");
        assertEquals("name like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%$$$%$_", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotEnds() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_ENDS, name(),
                "111");
        assertEquals("name not like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%111", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotEndsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_ENDS, name(),
                "$%_");
        assertEquals("name not like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%$$$%$_", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testContains() {
        ComposableWhere w = new LikeOperator(ConditionType.CONTAINS, name(),
                "111");
        assertEquals("name like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%111%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testContainsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.CONTAINS, name(),
                "$%_");
        assertEquals("name like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%$$$%$_%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotContains() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_CONTAINS,
                name(), "111");
        assertEquals("name not like ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%111%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNotContainsEscape() {
        ComposableWhere w = new LikeOperator(ConditionType.NOT_CONTAINS,
                name(), "$%_");
        assertEquals("name not like ? escape '$'", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("%$$$%$_%", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

}
