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
public class MultiValueOperatorTest extends TestCase {

    /**
     * 
     */
    public void test() {
        ComposableWhere w = new MultiValueOperator(ConditionType.IN,
                department().name(), "111", "222", "333");
        assertEquals("department.name in (?, ?, ?)", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(3, params.length);
        assertEquals("111", params[0]);
        assertEquals("222", params[1]);
        assertEquals("333", params[2]);

        String[] names = w.getPropertyNames();
        assertEquals(3, names.length);
        assertEquals("department.name", names[0]);
        assertEquals("department.name", names[1]);
        assertEquals("department.name", names[2]);
    }

    /**
     * 
     */
    public void testNull() {
        ComposableWhere w = new MultiValueOperator(ConditionType.IN, name());
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
        ComposableWhere w = new MultiValueOperator(ConditionType.IN, name(),
                "   ", "   ").excludesWhitespace();
        assertEquals("", w.getCriteria());

        Object[] params = w.getParams();
        assertEquals(0, params.length);

        String[] names = w.getPropertyNames();
        assertEquals(0, names.length);
    }

}
