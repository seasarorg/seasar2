package org.seasar.framework.ejb.unit.impl;

import java.util.Collection;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.ejb.unit.PersistentState;

/**
 * @author taedium
 * 
 */
public class PersistentFieldTest extends S2TestCase {

    public void testInstanceOf() {
        PersistentClass pc = new EntityClass(Hoge.class);
        PersistentState aaa = pc.getPersistentState("aaa");
        assertTrue("1", aaa instanceof PersistentField);
    }

    public void testGetTableName() {
        PersistentClass pc = new EntityClass(Hoge.class);
        PersistentState aaa = pc.getPersistentState("aaa");
        assertEquals("1", "HOGE", aaa.getTableName());
        PersistentState bbb = pc.getPersistentState("bbb");
        assertEquals("2", "HOGE", bbb.getTableName());
        PersistentState ccc = pc.getPersistentState("ccc");
        assertEquals("3", "HOGE", ccc.getTableName());
    }

    public void testGetTableName2() {
        PersistentClass pc = new EntityClass(Hoge3.class);
        PersistentState aaa = pc.getPersistentState("aaa");
        assertEquals("1", "FOO1", aaa.getTableName());
        PersistentState bbb = pc.getPersistentState("bbb");
        assertEquals("2", "FOO2", bbb.getTableName());
        PersistentState ccc = pc.getPersistentState("ccc");
        assertEquals("3", "FOO3", ccc.getTableName());
    }

    public void testGetColumnName() {
        PersistentClass pc = new EntityClass(Hoge.class);
        PersistentState bbb = pc.getPersistentState("bbb");
        assertEquals("1", "BBB", bbb.getColumnName());
    }

    public void testGetColumnName2() {
        PersistentClass pc = new EntityClass(Hoge3.class);
        PersistentState bbb = pc.getPersistentState("bbb");
        assertEquals("1", "FOO2BBB", bbb.getColumnName());
    }

    public void testGetValue() {
        Hoge hoge = new Hoge();
        hoge.setBbb(10);
        PersistentClass psh = new EntityClass(Hoge.class);
        PersistentState bbb = psh.getPersistentState("bbb");
        assertEquals("1", 10, bbb.getValue(hoge));
    }

    public void testSetValue() {
        Hoge hoge = new Hoge();
        PersistentClass pc = new EntityClass(Hoge.class);
        PersistentState bbb = pc.getPersistentState("bbb");
        bbb.setValue(hoge, 10);
        assertEquals("1", new Integer(10), hoge.getBbb());
    }

    public void testToOneRelationship() {
        PersistentClass pc = new EntityClass(Employee.class);
        PersistentState dept = pc.getPersistentState("department");

        assertFalse("1", dept.hasTableName());
        assertFalse("2", dept.hasColumnName());
        assertEquals("3", "department", dept.getStateName());
        assertEquals("4", Department.class, dept.getStateType());
        assertFalse("5", dept.isEmbedded());
        assertTrue("6", dept.isRelationship());
        assertTrue("7", dept.isPersistent());
    }

    public void testToManyRelationship() {
        PersistentClass pc = new EntityClass(Department.class);
        PersistentState employees = pc.getPersistentState("employees");

        assertFalse("1", employees.hasTableName());
        assertFalse("2", employees.hasColumnName());
        assertEquals("3", "employees", employees.getStateName());
        assertEquals("4", Collection.class, employees.getStateType());
        assertEquals("5", Employee.class, employees.getCollectionType());
        assertFalse("6", employees.isEmbedded());
        assertTrue("7", employees.isRelationship());
        assertTrue("8", employees.isPersistent());
    }

    public void testEmbeddedState() {
        PersistentClass pc = new EntityClass(Employee2.class);
        PersistentState period = pc.getPersistentState("period");

        assertTrue("1", period.hasTableName());
        assertEquals("2", "EMPLOYEE2", period.getTableName());
        assertFalse("3", period.hasColumnName());
        assertEquals("4", "period", period.getStateName());
        assertEquals("5", EmployeePeriod.class, period.getStateType());
        assertTrue("6", period.isEmbedded());
        assertFalse("7", period.isRelationship());
        assertTrue("8", period.isPersistent());
    }

    public void testEmbeddedStateWithAttributeOverride() {
        PersistentClass pc = new EntityClass(Employee4.class);
        PersistentState period = pc.getPersistentState("period");
        PersistentClass embeddedClass = period.createPersistentClass();

        PersistentState begin = embeddedClass.getPersistentState("beginDate");
        assertEquals("1", "BEGINDATE", begin.getColumnName());
        assertEquals("2", "HOGE", begin.getTableName());
        PersistentState finish = embeddedClass.getPersistentState("finishDate");
        assertEquals("3", "FINISHDATE", finish.getColumnName());
        assertEquals("4", "EMPLOYEE4", finish.getTableName());
    }
}
