package org.seasar.framework.ejb.unit.impl;

import java.util.Collection;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class PersistentFieldDescTest extends TestCase {

    public void testIsProperty() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc aaa = pc.getStateDesc("aaa");
        assertFalse("1", aaa.isProperty());
    }

    public void testGetTableName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc aaa = pc.getStateDesc("aaa");
        assertEquals("1", "HOGE", aaa.getTableName());
        PersistentStateDesc bbb = pc.getStateDesc("bbb");
        assertEquals("2", "HOGE", bbb.getTableName());
        PersistentStateDesc ccc = pc.getStateDesc("ccc");
        assertEquals("3", "HOGE", ccc.getTableName());
    }

    public void testGetTableName2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc aaa = pc.getStateDesc("aaa");
        assertEquals("1", "FOO1", aaa.getTableName());
        PersistentStateDesc bbb = pc.getStateDesc("bbb");
        assertEquals("2", "FOO2", bbb.getTableName());
        PersistentStateDesc ccc = pc.getStateDesc("ccc");
        assertEquals("3", "FOO3", ccc.getTableName());
    }

    public void testGetColumnName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc bbb = pc.getStateDesc("bbb");
        assertEquals("1", "BBB", bbb.getColumnName());
    }

    public void testGetColumnName2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc bbb = pc.getStateDesc("bbb");
        assertEquals("1", "FOO2BBB", bbb.getColumnName());
    }

    public void testGetValue() {
        Hoge hoge = new Hoge();
        hoge.setBbb(10);
        PersistentClassDesc psh = new EntityClassDesc(Hoge.class);
        PersistentStateDesc bbb = psh.getStateDesc("bbb");
        assertEquals("1", 10, bbb.getValue(hoge));
    }

    public void testSetValue() {
        Hoge hoge = new Hoge();
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc bbb = pc.getStateDesc("bbb");
        bbb.setValue(hoge, 10);
        assertEquals("1", new Integer(10), hoge.getBbb());
    }

    public void testToOneRelationship() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        PersistentStateDesc dept = pc.getStateDesc("department");

        assertEquals("1", "EMPLOYEE", dept.getTableName());
        assertEquals("2", "DEPARTMENT_ID", dept.getColumnName());
        assertEquals("3", "department", dept.getStateName());
        assertEquals("4", Department.class, dept.getPersistentStateType());
        assertFalse("5", dept.isEmbedded());
        assertTrue("6", dept.isRelationship());
        assertTrue("7", dept.isPersistent());
    }

    public void testToManyRelationship() {
        PersistentClassDesc pc = new EntityClassDesc(Department.class);
        PersistentStateDesc employees = pc.getStateDesc("employees");

        assertEquals("3", "employees", employees.getStateName());
        assertEquals("4", Collection.class, employees.getPersistentStateType());
        assertEquals("5", Employee.class, employees.getCollectionType());
        assertFalse("6", employees.isEmbedded());
        assertTrue("7", employees.isRelationship());
        assertTrue("8", employees.isPersistent());
    }

    public void testEmbeddedState() {
        PersistentClassDesc pc = new EntityClassDesc(Employee2.class);
        PersistentStateDesc period = pc.getStateDesc("period");

        assertEquals("2", "EMPLOYEE2", period.getTableName());
        assertEquals("4", "period", period.getStateName());
        assertEquals("5", EmployeePeriod.class, period.getPersistentStateType());
        assertTrue("6", period.isEmbedded());
        assertFalse("7", period.isRelationship());
        assertTrue("8", period.isPersistent());
    }

    public void testEmbeddedStateWithAttributeOverride() {
        PersistentClassDesc pc = new EntityClassDesc(Employee4.class);
        PersistentStateDesc period = pc.getStateDesc("period");
        PersistentClassDesc embeddedClass = period.createPersistentClass();

        PersistentStateDesc begin = embeddedClass.getStateDesc("startDate");
        assertEquals("1", "BEGINDATE", begin.getColumnName());
        assertEquals("2", "HOGE", begin.getTableName());
        PersistentStateDesc finish = embeddedClass.getStateDesc("endDate");
        assertEquals("3", "FINISHDATE", finish.getColumnName());
        assertEquals("4", "EMPLOYEE4", finish.getTableName());
    }
}
