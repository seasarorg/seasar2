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

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * @author higa
 * 
 */
public class AndOrTest extends TestCase {

    /**
     * @return
     */
    public EmployeeNames $n() {
        return new EmployeeNames();
    }

    /**
     * @param children
     * @return
     */
    public And and(Where... children) {
        return new And(children);
    }

    /**
     * @param children
     * @return
     */
    public Or or(Where... children) {
        return new Or(children);
    }

    /**
     * @param name
     * @param param
     * @return
     */
    public Eq eq(PropertyName name, Object param) {
        return new Eq(name, param);
    }

    /**
     * 
     * @throws Exception
     */
    public void testAnd() throws Exception {
        Where w = and(eq($n().id(), 1), eq($n().department().id(), 2));
        assertEquals("(id = ? AND department.id = ?)", w.getCriteria());
        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals(new Integer(1), params[0]);
        assertEquals(new Integer(2), params[1]);
        String[] names = w.getPropertyNames();
        assertEquals(2, params.length);
        assertEquals("id", names[0]);
        assertEquals("department.id", names[1]);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOr() throws Exception {
        Where w = or(eq($n().id(), 1), eq($n().department().id(), 2));
        assertEquals("(id = ? OR department.id = ?)", w.getCriteria());
        Object[] params = w.getParams();
        assertEquals(2, params.length);
        assertEquals(new Integer(1), params[0]);
        assertEquals(new Integer(2), params[1]);
        String[] names = w.getPropertyNames();
        assertEquals(2, params.length);
        assertEquals("id", names[0]);
        assertEquals("department.id", names[1]);
    }

    /**
     * 
     * @throws Exception
     */
    public void testAndOr() throws Exception {
        Where w = and(or(eq($n().id(), 1), eq($n().id(), 2)), or(eq($n()
                .department().id(), 3), eq($n().department().id(), 4)));
        assertEquals(
                "((id = ? OR id = ?) AND (department.id = ? OR department.id = ?))",
                w.getCriteria());
        Object[] params = w.getParams();
        assertEquals(4, params.length);
        assertEquals(new Integer(1), params[0]);
        assertEquals(new Integer(2), params[1]);
        assertEquals(new Integer(3), params[2]);
        assertEquals(new Integer(4), params[3]);
        String[] names = w.getPropertyNames();
        assertEquals(4, params.length);
        assertEquals("id", names[0]);
        assertEquals("id", names[1]);
        assertEquals("department.id", names[2]);
        assertEquals("department.id", names[3]);
    }
}
