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
package org.seasar.framework.ejb.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * @author taedium
 * 
 */
public class EntityListReaderTest extends TestCase {

    public void test() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Department reserch = new Department(new Long(2), 20, "RESERCH");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        Employee king = new Employee(new Long(2), 7839, "KING", accounting);
        Employee smith = new Employee(new Long(3), 7369, "SMITH", reserch);
        accounting.employees.add(clark);
        accounting.employees.add(king);
        reserch.employees.add(smith);
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(clark);
        employees.add(king);
        employees.add(smith);

        EntityListReader reader = new EntityListReader(employees, true);
        DataSet dataSet = reader.read();

        assertEquals("1", 2, dataSet.getTableSize());
        assertEquals("2", 3, dataSet.getTable("EMPLOYEE").getRowSize());
        assertEquals("3", 2, dataSet.getTable("DEPARTMENT").getRowSize());
    }

    public void testReadDuplicatedEntities() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        Employee king = new Employee(new Long(2), 7839, "KING", accounting);
        accounting.employees.add(clark);
        accounting.employees.add(king);
        List<Department> departments = new ArrayList<Department>();
        departments.add(accounting);
        departments.add(accounting);

        EntityListReader reader = new EntityListReader(departments, true);
        DataSet dataSet = reader.read();

        DataTable dataTable = dataSet.getTable("DEPARTMENT");
        assertEquals("1", 2, dataTable.getRowSize());
        DataTable dataTable2 = dataSet.getTable("EMPLOYEE");
        assertEquals("2", 2, dataTable2.getRowSize());
    }

    public void testReadPolymorphicEntities() {
        Employee2 clark = new Employee2(new Long(1), 7782, "CLARK");
        Employee2 king = new FullTimeEmployee(new Long(2), 7839, "KING");
        Employee2 smith = new PartTimeEmployee(new Long(3), 7369, "SMITH");
        Employee2 ford = new PartTimeEmployee(new Long(4), 7902, "FORD");

        List<Employee2> employees = new ArrayList<Employee2>();
        employees.add(clark);
        employees.add(king);
        employees.add(smith);
        employees.add(ford);

        EntityListReader reader = new EntityListReader(employees, true);
        DataSet dataSet = reader.read();

        DataTable dataTable = dataSet.getTable("Employee2");
        assertEquals("1", 4, dataTable.getRowSize());
        DataTable dataTable2 = dataSet.getTable("FullTimeEmployee");
        assertEquals("2", 1, dataTable2.getRowSize());
        DataTable dataTable3 = dataSet.getTable("PartTimeEmployee");
        assertEquals("3", 2, dataTable3.getRowSize());
    }

    @Entity(name = "Department")
    public static class Department {

        @Id
        private Long id;

        private long deptno;

        private String name;

        @OneToMany(mappedBy = "department")
        private Collection<Employee> employees = new HashSet<Employee>();

        public Department() {
        }

        public Department(Long id, long deptno, String name) {
            this.id = id;
            this.deptno = deptno;
            this.name = name;
        }
    }

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        private long empno;

        private String name;

        @ManyToOne
        private Department department;

        public Employee() {
        }

        public Employee(Long id, long empno, String name, Department department) {
            this.id = id;
            this.empno = empno;
            this.name = name;
            this.department = department;
        }
    }

    @Entity(name = "Employee2")
    @Inheritance(strategy = InheritanceType.JOINED)
    public static class Employee2 {

        @Id
        private Long id;

        private long empno;

        private String name;

        public Employee2() {
        }

        public Employee2(Long id, long empno, String name) {
            this.id = id;
            this.empno = empno;
            this.name = name;
        }
    }

    @Entity(name = "FullTimeEmployee")
    public static class FullTimeEmployee extends Employee2 {

        protected Integer salary;

        public FullTimeEmployee() {
        }

        public FullTimeEmployee(Long id, long empno, String name) {
            super(id, empno, name);
        }
    }

    @Entity(name = "PartTimeEmployee")
    public static class PartTimeEmployee extends Employee2 {

        protected Float hourlyWage;

        public PartTimeEmployee() {
        }

        public PartTimeEmployee(Long id, long empno, String name) {
            super(id, empno, name);
        }
    }

}
