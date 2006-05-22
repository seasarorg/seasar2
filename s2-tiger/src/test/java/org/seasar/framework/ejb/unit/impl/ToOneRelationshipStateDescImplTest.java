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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.ForeignKey;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class ToOneRelationshipStateDescImplTest extends TestCase {

    public void testGetPersistenceTargetClass() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee7.class);
        Field field = ClassUtil.getDeclaredField(Employee7.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "hoge", accessor);

        assertEquals("1", Department7.class, toOne.getPersistenceTargetClass());
    }

    public void testSetupForeignKeyColumnsWithManyToOne() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee.class);
        Field field = ClassUtil.getDeclaredField(Employee.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Department.class));
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "department_id", fkColumn.getName().toLowerCase());
        assertEquals("2", "employee", fkColumn.getTable().toLowerCase());
    }

    public void testSetupForeignKeyColumnsWithOneToOne() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee.class);
        Field field = ClassUtil.getDeclaredField(Employee.class, "address");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Address.class));
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "address_id", fkColumn.getName().toLowerCase());
        assertEquals("2", "employee", fkColumn.getTable().toLowerCase());
    }
    
    public void testSetupForeignKeyWithJoinColumn() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee2.class);
        Field field = ClassUtil.getDeclaredField(Employee2.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee2", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Department2.class));
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "foo", fkColumn.getName().toLowerCase());
        assertEquals("2", "employee2", fkColumn.getTable().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumn2() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee5.class);
        Field field = ClassUtil.getDeclaredField(Employee5.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee5", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Department5.class));
        PersistentColumn fk = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "department_name", fk.getName().toLowerCase());
        assertEquals("2", "employee5", fk.getTable().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumns() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee3.class);
        Field field = ClassUtil.getDeclaredField(Employee3.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee3", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Department3.class));

        PersistentColumn fk = toOne.getForeignKeys().get(0).getColumn();
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee3", fk.getTable().toLowerCase());

        fk = toOne.getForeignKeys().get(1).getColumn();
        assertEquals("4", "deptid2", fk.getName().toLowerCase());
        assertEquals("5", "employee3", fk.getTable().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumns2() {
        PersistentClassDesc pc = new EntityClassDescImpl(Employee4.class);
        Field field = ClassUtil.getDeclaredField(Employee4.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDescImpl toOne = new ToOneRelationshipStateDescImpl(pc,
                "employee4", accessor);
        toOne.setupForeignKeys(new EntityClassDescImpl(Department4.class));

        List<ForeignKey> fks = toOne.getForeignKeys();
        PersistentColumn fk = fks.get(0).getColumn();
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee4", fk.getTable().toLowerCase());

        fk = fks.get(1).getColumn();
        assertEquals("3", "deptid2", fk.getName().toLowerCase());
        assertEquals("4", "employee4", fk.getTable().toLowerCase());
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
    public static class Department4 {

        @EmbeddedId
        private Department4PK id;
    }

    @Entity(name = "Department5")
    public static class Department5 {

        @Id
        private Long id;

        private String name;
    }

    @Entity(name = "Department7")
    public static class Department7 {

        @Id
        private Long id;

        @OneToMany(targetEntity = Employee7.class, mappedBy = "department")
        private Collection employees = new HashSet();
    }

    public static class Department3PK implements Serializable {
        private Long id1;

        private Long id2;
    }

    public static class Department4PK implements Serializable {
        private Long id1;

        private Long id2;
    }

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        @ManyToOne
        private Department department;
        
        @OneToOne
        private Address address;
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
            @JoinColumn(name = "deptId1", referencedColumnName = "id1"),
            @JoinColumn(name = "deptId2", referencedColumnName = "id2") })
        private Department4 department;

    }

    @Entity(name = "Employee5")
    public static class Employee5 {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(referencedColumnName = "name")
        private Department department;
    }

    @Entity(name = "Employee7")
    public static class Employee7 {
        @Id
        private Long id;

        @ManyToOne(targetEntity = Department7.class)
        private Object department;
    }
    
    @Entity(name = "Address")
    public static class Address {
        @Id
        private Long id;
    }

}
