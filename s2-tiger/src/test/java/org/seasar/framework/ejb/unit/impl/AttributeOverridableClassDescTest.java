/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.unit.impl;

import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class AttributeOverridableClassDescTest extends TestCase {

    public void testEmbeddableClass() {
        HashMap<String, PersistentColumn> overrides = new HashMap<String, PersistentColumn>();
        overrides.put("startDate", new PersistentColumnImpl("hoge", "foo"));
        AttributeOverridableClassDesc ao = new AttributeOverridableClassDesc(
                EmployeePeriod.class, "EMPLOYEE2", true, overrides);
        PersistentStateDesc stateDesc = ao
                .getPersistentStateDesc("EmployeePeriod.startDate");
        PersistentColumn column = stateDesc.getColumn();

        assertEquals("1", "HOGE", column.getTableName());
        assertEquals("2", "FOO", column.getName());
    }

    public void testMappedSuperClass() {
        HashMap<String, PersistentColumn> overrides = new HashMap<String, PersistentColumn>();
        overrides.put("empid", new PersistentColumnImpl("hoge", "foo"));
        AttributeOverridableClassDesc ao = new AttributeOverridableClassDesc(
                Employee6.class, "EMPLOYEE2", true, overrides);
        PersistentStateDesc stateDesc = ao
                .getPersistentStateDesc("Employee6.empid");
        PersistentColumn column = stateDesc.getColumn();

        assertEquals("1", "HOGE", column.getTableName());
        assertEquals("2", "FOO", column.getName());
    }

}
