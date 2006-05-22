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

import static javax.persistence.InheritanceType.JOINED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;

import org.seasar.framework.ejb.unit.impl.EntityListIntrospector;

import junit.framework.TestCase;

public class EntityListIntrospectorTest extends TestCase {

    public void testGerPersistentClassDescs() throws Exception {
        Department department1 = new Department();
        department1.employees.add(new FulltimeEmployee());
        Department department2 = new Department();
        department2.employees.add(new ParttimeEmployee());

        List<Department> departments = new ArrayList<Department>();
        departments.add(department1);
        departments.add(department2);

        EntityListIntrospector i = new EntityListIntrospector(departments, true,
                new DefaultProxiedObjectResolver());

        assertNotNull("1", i.getEntityClassDesc(Department.class));
        assertNotNull("2", i.getEntityClassDesc(Employee.class));
        assertNotNull("3", i.getEntityClassDesc(FulltimeEmployee.class));
        assertNotNull("4", i.getEntityClassDesc(ParttimeEmployee.class));
    }

    @Entity
    public static class Department {
        @Id
        private Long id;

        @OneToMany
        private Collection<Employee> employees = new HashSet<Employee>();
    }

    @Entity
    @Inheritance(strategy = JOINED)
    public static class Employee {
        @Id
        private Long id;
    }

    @Entity
    public static class FulltimeEmployee extends Employee {
    }

    @Entity
    public static class ParttimeEmployee extends Employee {
    }
}
