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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class ToOneRelationshipStateDescTest extends TestCase {

    public void testGetPersistenceTargetClass() {
        PersistentClassDesc pc = new EntityClassDesc(Employee7.class);
        Field field = ClassUtil.getDeclaredField(Employee7.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "hoge", accessor);

        assertEquals("1", Department7.class, toOne.getPersistenceTargetClass());
        assertEquals("2", Object.class, toOne.getPersistentStateClass());
    }

    public void testSetupForeignKeyColumns() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        Field field = ClassUtil.getDeclaredField(Employee.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "employee", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Department.class));
        PersistentJoinColumn fk = toOne.getForeignKeyColumns().get(0);

        assertEquals("1", "department_id", fk.getName().toLowerCase());
        assertEquals("2", "employee", fk.getTable().toLowerCase());
        assertEquals("3", "id", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumn() {
        PersistentClassDesc pc = new EntityClassDesc(Employee2.class);
        Field field = ClassUtil.getDeclaredField(Employee2.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "employee2", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Department2.class));
        PersistentJoinColumn fk = toOne.getForeignKeyColumns().get(0);

        assertEquals("1", "foo", fk.getName().toLowerCase());
        assertEquals("2", "employee2", fk.getTable().toLowerCase());
        assertEquals("3", "deptid", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumn2() {
        PersistentClassDesc pc = new EntityClassDesc(Employee5.class);
        Field field = ClassUtil.getDeclaredField(Employee5.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "employee5", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Department5.class));
        PersistentJoinColumn fk = toOne.getForeignKeyColumns().get(0);

        assertEquals("1", "department_name", fk.getName().toLowerCase());
        assertEquals("2", "employee5", fk.getTable().toLowerCase());
        assertEquals("3", "name", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumns() {
        PersistentClassDesc pc = new EntityClassDesc(Employee3.class);
        Field field = ClassUtil.getDeclaredField(Employee3.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "employee3", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Department3.class));

        PersistentJoinColumn fk = toOne.getForeignKeyColumns().get(0);
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee3", fk.getTable().toLowerCase());
        assertEquals("3", "id1", fk.getReferencedColumnName().toLowerCase());

        fk = toOne.getForeignKeyColumns().get(1);
        assertEquals("4", "deptid2", fk.getName().toLowerCase());
        assertEquals("5", "employee3", fk.getTable().toLowerCase());
        assertEquals("6", "id2", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithJoinColumns2() {
        PersistentClassDesc pc = new EntityClassDesc(Employee4.class);
        Field field = ClassUtil.getDeclaredField(Employee4.class, "department");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "employee4", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Department4.class));

        PersistentJoinColumn fk = toOne.getForeignKeyColumns().get(0);
        assertEquals("1", "deptid1", fk.getName().toLowerCase());
        assertEquals("2", "employee4", fk.getTable().toLowerCase());
        assertEquals("3", "id1", fk.getReferencedColumnName().toLowerCase());

        fk = toOne.getForeignKeyColumns().get(1);
        assertEquals("4", "deptid2", fk.getName().toLowerCase());
        assertEquals("5", "employee4", fk.getTable().toLowerCase());
        assertEquals("6", "id2", fk.getReferencedColumnName().toLowerCase());
    }

    public void testSetupForeignKeyWithPrimaryKeyJoinColumn() {
        PersistentClassDesc pc = new EntityClassDesc(Body.class);
        Field field = ClassUtil.getDeclaredField(Body.class, "heart");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "body", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Heart.class));

        assertEquals("1", 0, toOne.getForeignKeyColumns().size());
    }

    public void testSetupForeignKeyWithPrimaryKeyJoinColumn2() {
        PersistentClassDesc pc = new EntityClassDesc(Body2.class);
        Field field = ClassUtil.getDeclaredField(Body2.class, "heart");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "body2", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Heart.class));
        PersistentColumn pk = pc.getIdentifiers().get(0).getColumn();

        assertEquals("1", 0, toOne.getForeignKeyColumns().size());
        assertEquals("2", "aaa", pk.getName().toLowerCase());
    }

    public void testSetupForeignKeyWithPrimaryKeyJoinColumns() {
        PersistentClassDesc pc = new EntityClassDesc(Body3.class);
        Field field = ClassUtil.getDeclaredField(Body3.class, "heart");
        FieldAccessor accessor = new FieldAccessor(field);
        ToOneRelationshipStateDesc toOne = new ToOneRelationshipStateDesc(pc,
                "body3", accessor);
        toOne.setupForeignKeyColumns(new EntityClassDesc(Heart.class));
        
        assertEquals("1", 0, toOne.getForeignKeyColumns().size());
        
        PersistentStateDesc id = pc.getIdentifiers().get(0);
        List<PersistentStateDesc> embeddedDescs = id.getEmbeddedStateDescs();
        
        PersistentColumn pk = embeddedDescs.get(0).getColumn();
        assertEquals("2", "aaa", pk.getName().toLowerCase());

        pk = embeddedDescs.get(1).getColumn();
        assertEquals("3", "bbb", pk.getName().toLowerCase());
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
        @JoinColumns( { @JoinColumn(name = "deptId1"),
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

    @Entity(name = "Employee7")
    public static class Employee7 {
        @Id
        private Long id;

        @ManyToOne(targetEntity = Department7.class)
        private Object department;
    }

    @Entity(name = "Body")
    public static class Body {
        @Id
        private Long id;

        @OneToOne
        @PrimaryKeyJoinColumn
        private Heart heart;
    }

    @Entity(name = "Heart")
    public static class Heart {
        @Id
        private Long id;
    }

    @Entity(name = "Body2")
    public static class Body2 {
        @Id
        private Long id;

        @OneToOne
        @PrimaryKeyJoinColumn(name = "aaa")
        private Heart heart;
    }

    @Entity(name = "Heart2")
    public static class Heart2 {
        @Id
        private Long id;
    }

    @Entity(name = "Body3")
    public static class Body3 {
        @EmbeddedId
        private HeartPk3 id;

        @OneToOne
        @PrimaryKeyJoinColumns( {
                @PrimaryKeyJoinColumn(name = "aaa", referencedColumnName = "id1"),
                @PrimaryKeyJoinColumn(name = "bbb", referencedColumnName = "id2") })
        private Heart heart;
    }

    @Entity(name = "Heart3")
    public static class Heart3 {
        @EmbeddedId
        private HeartPk3 id;
    }

    public static class HeartPk3 implements Serializable {
        private Long id1;

        private Long id2;
    }

}
