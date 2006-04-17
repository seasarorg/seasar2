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

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.ProxiedObjectResolver;
import org.seasar.framework.ejb.unit.impl.EntityIntrospector;

public class EntityIntrospectorTest extends TestCase {

    public void testGetPersistentClassDesc() throws Exception {
        Address address1 = new Address();
        Address address2 = new Address();
        Employee fulltimeEmployee = new FulltimeEmployee();
        fulltimeEmployee.address = address1;
        Employee parttimeEmployee = new ParttimeEmployee();
        parttimeEmployee.address = address2;
        Department department = new Department();
        department.employees.add(fulltimeEmployee);
        department.employees.add(parttimeEmployee);

        EntityIntrospector i = new EntityIntrospector(department, null);

        assertNotNull("1", i.getPersistentClassDesc(Department.class));
        assertNotNull("2", i.getPersistentClassDesc(Employee.class));
        assertNotNull("3", i.getPersistentClassDesc(FulltimeEmployee.class));
        assertNotNull("4", i.getPersistentClassDesc(ParttimeEmployee.class));
        assertNotNull("5", i.getPersistentClassDesc(Address.class));
    }
    
    public void testUnproxy() throws Exception {
        Employee employee = new Employee();
        employee.address = new AddressProxy(new Address());
        ProxiedObjectResolver resolver = new ProxiedObjectResolverTest();
        
        EntityIntrospector i = new EntityIntrospector(employee, resolver);
        
        assertNotNull("1", i.getPersistentClassDesc(Address.class));
    }

    @Entity
    public static class Department {
        @Id
        private Long id;

        @OneToMany(mappedBy = "department")
        private Collection<Employee> employees = new HashSet<Employee>();
    }

    @Entity
    @Inheritance(strategy = JOINED)
    public static class Employee {
        @Id
        private Long id;
        
        @ManyToOne
        private Department department;

        @ManyToOne
        private Address address;
    }

    @Entity
    public static class FulltimeEmployee extends Employee {
    }

    @Entity
    public static class ParttimeEmployee extends Employee {
    }

    @Entity
    public static class Address {
        @Id
        Long id;
    }

    public static interface Proxy {
        public Object getProxiedObject();
    }
    
    public static class AddressProxy extends Address implements Proxy {
        private Object proxied;

        public AddressProxy(Object proxied) {
            this.proxied = proxied;
        }
        
        public Object getProxiedObject() {
            return proxied;
        }
    }

    public static class ProxiedObjectResolverTest implements
            ProxiedObjectResolver {
        
        public Object unproxy(Object proxy) {
            if (proxy instanceof Proxy) {
                return ((Proxy) proxy).getProxiedObject();
            }
            return proxy;
        }
    }

}
