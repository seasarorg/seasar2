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
public class SingleValueOperatorTest extends TestCase {

    /**
     * 
     */
    public void test() {
        ComposableWhere w = new SingleValueOperator(ConditionType.EQ, name(),
                "111");
        assertEquals("name = ?", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(1, params.length);
        assertEquals("111", params[0]);

        String[] names = w.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
    }

    /**
     * 
     */
    public void testNull() {
        ComposableWhere w = new SingleValueOperator(ConditionType.EQ, id(),
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
    public void testExcludeWhitespace() {
        ComposableWhere w = new SingleValueOperator(ConditionType.EQ, name(),
                "   ").excludesWhitespace();
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

}
