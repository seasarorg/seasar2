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

import java.util.Arrays;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ConditionType;

import static org.seasar.extension.jdbc.where.EmployeeNames.*;

/**
 * @author koichik
 */
public class CompositeWhereTest extends TestCase {

    /**
     * 
     */
    public void testArray() {
        CompositeWhere w = new CompositeWhere("and", new SingleValueOperator(
                ConditionType.EQ, name(), "111"), new SingleValueOperator(
                ConditionType.EQ, id(), null), new SimpleWhere().eq("ccc",
                "333"));
        assertEquals("(name = ?) and (ccc = ?)", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals("111", params[0]);
        assertEquals("333", params[1]);

        String[] names = w.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("name", names[0]);
        assertEquals("ccc", names[1]);
    }

    /**
     * 
     */
    public void testCollection() {
        CompositeWhere w = new CompositeWhere("and", Arrays.asList(
                new SingleValueOperator(ConditionType.EQ, name(), "111"),
                new SingleValueOperator(ConditionType.EQ, id(), null),
                new SimpleWhere().eq("ccc", "333")));
        assertEquals("(name = ?) and (ccc = ?)", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals("111", params[0]);
        assertEquals("333", params[1]);

        String[] names = w.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("name", names[0]);
        assertEquals("ccc", names[1]);
    }

    /**
     * 
     */
    public void testEmpty() {
        CompositeWhere w = new CompositeWhere("and");
        assertEquals("", w.getCriteria());
    }

    /**
     * 
     */
    public void testEmpty2() {
        CompositeWhere w = new CompositeWhere("and", new SingleValueOperator(
                ConditionType.EQ, "aaa", null));
        assertEquals("", w.getCriteria());
    }

}
