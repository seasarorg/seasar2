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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class ToManyRelationshipStateDescImplTest extends TestCase {

    public void testGetPersistenceTargetClassFromCollectionGenericType() {
        PersistentClassDesc pc = new EntityClassDescImpl(Department.class);
        Field field = ClassUtil.getDeclaredField(Department.class, "employees");
        FieldAccessor accessor = new FieldAccessor(field);
        ToManyRelationshipStateDescImpl toMany = new ToManyRelationshipStateDescImpl(pc, "employees", accessor);

        assertEquals("1", Employee.class, toMany.getPersistenceTargetClass());
    }
    
    public void testGetPersistenceTargetClassFromMapGenericType() {
        PersistentClassDesc pc = new EntityClassDescImpl(Department6.class);
        Field field = ClassUtil.getDeclaredField(Department6.class, "employees");
        FieldAccessor accessor = new FieldAccessor(field);
        ToManyRelationshipStateDescImpl toMany = new ToManyRelationshipStateDescImpl(pc, "employees", accessor);
        
        assertEquals("1", Employee6.class, toMany.getPersistenceTargetClass());
    }

    public void testGetPersistenceTargetClassFromOneToManyElement() {
        PersistentClassDesc pc = new EntityClassDescImpl(Department7.class);
        Field field = ClassUtil.getDeclaredField(Department7.class, "employees");
        FieldAccessor accessor = new FieldAccessor(field);
        ToManyRelationshipStateDescImpl toMany = new ToManyRelationshipStateDescImpl(pc, "employees", accessor);
        
        assertEquals("1", Employee7.class, toMany.getPersistenceTargetClass());
    }

    @Entity(name = "Department")
    public static class Department {

        @Id
        private Long id;

        @OneToMany(mappedBy = "department")
        private Collection<Employee> employees = new HashSet<Employee>();
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

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        @ManyToOne
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

}
