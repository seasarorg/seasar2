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

import static javax.persistence.EnumType.STRING;
import static org.seasar.framework.ejb.unit.PersistentStateType.BASIC;
import static org.seasar.framework.ejb.unit.PersistentStateType.EMBEDDED;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_MANY;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_ONE;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescTest extends TestCase {

    public void testGetColumn() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        PersistentColumn column = stateDesc.getColumn();
        assertEquals("1", "hoge", column.getTable().toLowerCase());
        assertEquals("2", "aaa", column.getName().toLowerCase());
    }

    public void testGetColumn2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        PersistentColumn column = stateDesc.getColumn();
        assertEquals("1", "foo2", column.getTable().toLowerCase());
        assertEquals("2", "foo1aaa", column.getName().toLowerCase());
    }

    public void testHasColumnReturnsTrue() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        assertEquals("1", true, stateDesc.hasColumn("foo1aaa"));
    }

    public void testHasColumnReturnsFalse() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        assertEquals("1", false, stateDesc.hasColumn("aaa"));
    }

    public void testGetPersistentClass() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        assertEquals("1", Long.class, stateDesc.getPersistenceTargetClass());
    }

    public void testGetPersistenceTargetClassFromCollection() {
        PersistentClassDesc pc = new EntityClassDesc(Department.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("employees");
        assertEquals("1", Employee.class, stateDesc.getPersistenceTargetClass());
        assertEquals("2", Collection.class, stateDesc.getPersistentStateClass());
    }
    
    public void testGetPersistenceTargetClassFromMap() {
        PersistentClassDesc pc = new EntityClassDesc(Department6.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("employees");
        assertEquals("1", Employee6.class, stateDesc.getPersistenceTargetClass());
        assertEquals("2", Map.class, stateDesc.getPersistentStateClass());
    }
    
    public void testGetPersistenceTargetClassFromManyToOne() {
        PersistentClassDesc pc = new EntityClassDesc(Department7.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("employees");
        assertEquals("1", Employee7.class, stateDesc.getPersistenceTargetClass());
        assertEquals("2", Collection.class, stateDesc.getPersistentStateClass());
    }

    public void testGetPersistenceTargetClassFromOneToMany() {
        PersistentClassDesc pc = new EntityClassDesc(Employee7.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("department");
        assertEquals("1", Department7.class, stateDesc.getPersistenceTargetClass());
        assertEquals("2", Object.class, stateDesc.getPersistentStateClass());
    }

    public void testGetEmbeddedStateDescs() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("period");
        List<PersistentStateDesc> embeddeds = stateDesc.getEmbeddedStateDescs();
        assertEquals("1", 2, embeddeds.size());
    }

    public void testIsIdentifierReturnsTrue() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("aaa");
        assertEquals("1", true, stateDesc.isIdentifier());
    }

    public void testIsIdentifierReturnsFalse() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("bbb");
        assertEquals("1", false, stateDesc.isIdentifier());
    }

    public void testGetPersistentStateType() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("department");
        assertEquals("1", TO_ONE, stateDesc.getPersistentStateType());

        stateDesc = pc.getPersistentStateDesc("period");
        assertEquals("2", EMBEDDED, stateDesc.getPersistentStateType());

        stateDesc = pc.getPersistentStateDesc("id");
        assertEquals("3", BASIC, stateDesc.getPersistentStateType());

        pc = new EntityClassDesc(Department.class);
        stateDesc = pc.getPersistentStateDesc("employees");
        assertEquals("4", TO_MANY, stateDesc.getPersistentStateType());
    }

    public void testSetupForeignKeyColumns() {
        PersistentClassDesc pc1 = new EntityClassDesc(Employee.class);
        PersistentStateDesc stateDesc = pc1
                .getPersistentStateDesc("department");
        PersistentClassDesc pc2 = new EntityClassDesc(Department.class);
        stateDesc.setupForeignKeyColumns(pc2);
        PersistentColumn fk = stateDesc.getForeignKeyColumns().get(0);

        assertEquals("1", "department_id", fk.getName().toLowerCase());
        assertEquals("2", "employee", fk.getTable().toLowerCase());
        assertEquals("3", "id", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumn() {
        PersistentClassDesc pc1 = new EntityClassDesc(Employee2.class);
        PersistentStateDesc stateDesc = pc1
                .getPersistentStateDesc("department");
        PersistentClassDesc pc2 = new EntityClassDesc(Department2.class);
        stateDesc.setupForeignKeyColumns(pc2);
        PersistentColumn fk = stateDesc.getForeignKeyColumns().get(0);

        assertEquals("1", "foo", fk.getName().toLowerCase());
        assertEquals("2", "employee2", fk.getTable().toLowerCase());
        assertEquals("3", "deptid", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumn2() {
        PersistentClassDesc pc1 = new EntityClassDesc(Employee5.class);
        PersistentStateDesc stateDesc = pc1
                .getPersistentStateDesc("department");
        PersistentClassDesc pc2 = new EntityClassDesc(Department5.class);
        stateDesc.setupForeignKeyColumns(pc2);
        PersistentColumn fk = stateDesc.getForeignKeyColumns().get(0);

        assertEquals("1", "department_name", fk.getName().toLowerCase());
        assertEquals("2", "employee5", fk.getTable().toLowerCase());
        assertEquals("3", "name", fk.getReferencedColumnName().toLowerCase());
    }
    
    public void testSetupForeignKeyWithJoinColumns() {
        PersistentClassDesc pc1 = new EntityClassDesc(Employee3.class);
        PersistentStateDesc stateDesc = pc1
                .getPersistentStateDesc("department");
        PersistentClassDesc pc2 = new EntityClassDesc(Department3.class);
        stateDesc.setupForeignKeyColumns(pc2);

        PersistentColumn fk = stateDesc.getForeignKeyColumns().get(0);
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee3", fk.getTable().toLowerCase());
        assertEquals("3", "id1", fk.getReferencedColumnName().toLowerCase());

        fk = stateDesc.getForeignKeyColumns().get(1);
        assertEquals("4", "deptid2", fk.getName().toLowerCase());
        assertEquals("5", "employee3", fk.getTable().toLowerCase());
        assertEquals("6", "id2", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumns2() {
        PersistentClassDesc pc1 = new EntityClassDesc(Employee4.class);
        PersistentStateDesc stateDesc = pc1
                .getPersistentStateDesc("department");
        PersistentClassDesc pc2 = new EntityClassDesc(Department4.class);
        stateDesc.setupForeignKeyColumns(pc2);

        PersistentColumn fk = stateDesc.getForeignKeyColumns().get(0);
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee4", fk.getTable().toLowerCase());
        assertEquals("3", "id1", fk.getReferencedColumnName().toLowerCase());

        fk = stateDesc.getForeignKeyColumns().get(1);
        assertEquals("4", "deptid2", fk.getName().toLowerCase());
        assertEquals("5", "employee4", fk.getTable().toLowerCase());
        assertEquals("6", "id2", fk.getReferencedColumnName().toLowerCase());
    }
    
    public void testGetEnumByOrdinal() {
        Hoge3 hoge3 = new Hoge3();
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("bbb");
        assertEquals("1", 1, stateDesc.getValue(hoge3));
        assertEquals("2", int.class, stateDesc.getPersistenceTargetClass());
    }
    
    public void testGetEnumByString() {
        Hoge3 hoge3 = new Hoge3();
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        PersistentStateDesc stateDesc = pc.getPersistentStateDesc("ccc");
        assertEquals("1", "BBB", stateDesc.getValue(hoge3));
        assertEquals("2", String.class, stateDesc.getPersistenceTargetClass());
    }
    
    @Entity(name = "Hoge")
    public static class Hoge {
        @Id
        private Long aaa;

        private String bbb;
    }

    @Entity
    @Table(name = "Foo1")
    @SecondaryTable(name = "Foo2")
    public static class Hoge2 {
        @Id
        @Column(name = "Foo1aaa", table = "Foo2")
        private Long aaa;
    }
    
    @Entity(name = "Hoge3")
    public static class Hoge3 {
        @Id
        private Long aaa;

        private HogeType bbb = HogeType.BBB;
        
        @Enumerated(STRING)
        private HogeType ccc = HogeType.BBB;
    }
    
    public static enum HogeType {
        AAA, BBB
    }

    @Entity(name = "Department")
    public static class Department {

        @Id
        private Long id;

        @OneToMany(mappedBy = "department")
        private Collection<Employee> employees = new HashSet<Employee>();
    }

    @Entity(name = "Department2")
    public static class Department2 {

        @Id
        @Column(name = "DEPTID")
        private Long id;

        public Department2() {
        }
    }

    @Entity(name = "Department3")
    @IdClass(Department3PK.class)
    public static class Department3 {

        @Id
        private Long id1;

        @Id
        private Long id2;
    }

    @Entity(name = "Department4")
    @IdClass(Department4PK.class)
    public static class Department4 {

        @Id
        private Long id1;

        @Id
        private Long id2;
    }

    @Entity(name = "Department5")
    public static class Department5 {

        @Id
        private Long id;

        private String name;
    }
    
    @Entity(name = "Department6")
    public static class Department6 {

        @Id
        private Long id;

        @OneToMany(mappedBy = "department")
        private Map<Long, Employee6> employees = new HashMap<Long, Employee6>();
    }

    @Entity(name = "Department7")
    public static class Department7 {

        @Id
        private Long id;

        @OneToMany(targetEntity=Employee7.class, mappedBy = "department")
        private Collection employees = new HashSet();
    }
    
    public static class Department3PK {
        private Long id1;

        private Long id2;
    }

    public static class Department4PK {
        private Long id1;

        private Long id2;
    }

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        @ManyToOne
        private Department department;

        @OneToOne(mappedBy = "employee")
        private Address address;

        @Embedded
        @AttributeOverrides( {
                @AttributeOverride(name = "startDate", column = @Column(name = "beginDate", table = "Hoge")),
                @AttributeOverride(name = "endDate", column = @Column(name = "finishDate")) })
        private EmployeePeriod period;

    }

    @Entity(name = "Employee2")
    public static class Employee2 {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(name = "FOO", referencedColumnName = "DEPTID")
        private Department2 department;
    }

    @Entity(name = "Employee3")
    public static class Employee3 {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumns( {
                @JoinColumn(name = "deptId1", referencedColumnName = "id1"),
                @JoinColumn(name = "deptId2", referencedColumnName = "id2") })
        private Department3 department;

    }

    @Entity(name = "Employee4")
    public static class Employee4 {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumns( {
                @JoinColumn(name = "deptId1"),
                @JoinColumn(name = "deptId2") })
        private Department3 department;
        
    }
    
    @Entity(name = "Employee5")
    public static class Employee5 {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(referencedColumnName = "name")
        private Department department;
    }

    @Entity(name = "Employee6")
    public static class Employee6 {
        @Id
        private Long id;

        @ManyToOne
        private Department department;
    }
    
    @Entity(name = "Employee7")
    public static class Employee7 {
        @Id
        private Long id;

        @ManyToOne(targetEntity=Department7.class)
        private Object department;
    }
    
    @Embeddable
    public static class EmployeePeriod {

        private java.util.Date startDate;

        private java.util.Date endDate;
    }

    @Entity
    public static class Address {
        @OneToOne
        private Employee employee;
    }

}
