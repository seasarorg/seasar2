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

import java.util.Collection;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescImplTest extends TestCase {

    public void testGetTableName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc aaa = pc.getPersistentStateDesc("Hoge.aaa");
        assertEquals("1", "HOGE", aaa.getColumn().getTableName());
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge.bbb");
        assertEquals("2", "HOGE", bbb.getColumn().getTableName());
        PersistentStateDesc ccc = pc.getPersistentStateDesc("Hoge.ccc");
        assertEquals("3", "HOGE", ccc.getColumn().getTableName());
    }

    public void testGetTableName2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc aaa = pc.getPersistentStateDesc("Hoge3.aaa");
        assertEquals("1", "FOO1", aaa.getColumn().getTableName());
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge3.bbb");
        assertEquals("2", "FOO2", bbb.getColumn().getTableName());
        PersistentStateDesc ccc = pc.getPersistentStateDesc("Hoge3.ccc");
        assertEquals("3", "FOO3", ccc.getColumn().getTableName());
    }

    public void testGetColumnName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge.bbb");
        assertEquals("1", "BBB", bbb.getColumn().getName());
    }

    public void testGetColumnName2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge3.bbb");
        assertEquals("1", "FOO2BBB", bbb.getColumn().getName());
    }

    public void testToOneRelationship() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        PersistentStateDesc dept = pc.getPersistentStateDesc("Employee.department");

        assertEquals("1", "department", dept.getStateName());
        assertEquals("2", Department.class, dept.getPersistentStateType());
        assertFalse("3", dept.isEmbedded());
        assertTrue("4", dept.isToOneRelationship());
        assertFalse("5", dept.isToManyRelationship());
    }

    public void testToManyRelationship() {
        PersistentClassDesc pc = new EntityClassDesc(Department.class);
        PersistentStateDesc employees = pc.getPersistentStateDesc("Department.employees");

        assertEquals("1", "employees", employees.getStateName());
        assertEquals("2", Collection.class, employees.getPersistentStateType());
        assertEquals("3", Employee.class, employees.getCollectionType());
        assertFalse("4", employees.isEmbedded());
        assertFalse("5", employees.isToOneRelationship());
        assertTrue("6", employees.isToManyRelationship());
    }

    public void testEmbeddedState() {
        PersistentClassDesc pc = new EntityClassDesc(Employee2.class);
        PersistentStateDesc period = pc.getPersistentStateDesc("Employee2.period");

        assertEquals("2", EmployeePeriod.class, period.getPersistentStateType());
        assertTrue("3", period.isEmbedded());
        assertFalse("4", period.isToOneRelationship());
        assertFalse("5", period.isToManyRelationship());
    }

    public void testEmbeddedStateWithAttributeOverride() {
        PersistentClassDesc pc = new EntityClassDesc(Employee4.class);
        PersistentStateDesc period = pc.getPersistentStateDesc("Employee4.period");
        PersistentClassDesc embeddedClass = period.getEmbeddedClassDesc();

        PersistentStateDesc begin = embeddedClass.getPersistentStateDesc("EmployeePeriod.startDate");
        PersistentColumn beginColumn = begin.getColumn();
        assertEquals("1", "BEGINDATE", beginColumn.getName());
        assertEquals("2", "HOGE", beginColumn.getTableName());
        PersistentStateDesc finish = embeddedClass.getPersistentStateDesc("EmployeePeriod.endDate");
        PersistentColumn finishColumn = finish.getColumn();
        assertEquals("3", "FINISHDATE", finishColumn.getName());
        assertEquals("4", "EMPLOYEE4", finishColumn.getTableName());
    }

}
