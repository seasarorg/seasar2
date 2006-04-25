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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * @author taedium
 * 
 */
public class EntityReaderTest extends TestCase {

    public void testReadOneToMany() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee king = new Employee(new Long(1), 7839, "KING", accounting);
        Employee clark = new Employee(new Long(2), 7782, "CLARK", accounting);

        accounting.employees.add(king);
        accounting.employees.add(clark);

        EntityReader reader = new EntityReader(accounting);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("2", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("3", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("4", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("5", "ACCOUNTING", deptRow.getValue("NAME"));

        DataTable empTable = dataSet.getTable("Employee");
        assertEquals("6", 2, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("7", new BigDecimal(2), empRow1.getValue("ID"));
        assertEquals("8", new BigDecimal(7782), empRow1.getValue("EMPNO"));
        assertEquals("9", "CLARK", empRow1.getValue("NAME"));
        assertEquals("10", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));

        DataRow empRow2 = empTable.getRow(1);
        assertEquals("11", new BigDecimal(1), empRow2.getValue("ID"));
        assertEquals("12", new BigDecimal(7839), empRow2.getValue("EMPNO"));
        assertEquals("13", "KING", empRow2.getValue("NAME"));
        assertEquals("14", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));
    }

    public void testReadOneToManyUsingMapKey() {
        Department5 accounting = new Department5(new Long(1), 10, "ACCOUNTING");
        Employee5 king = new Employee5(new Long(1), 7839, "KING", accounting);
        Employee5 clark = new Employee5(new Long(2), 7782, "CLARK", accounting);

        accounting.employees.put(king.id, king);
        accounting.employees.put(clark.id, clark);

        EntityReader reader = new EntityReader(accounting);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        
        DataTable deptTable = dataSet.getTable("Department5");
        assertEquals("2", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("3", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("4", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("5", "ACCOUNTING", deptRow.getValue("NAME"));

        DataTable empTable = dataSet.getTable("Employee5");
        assertEquals("6", 2, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("7", new BigDecimal(1), empRow1.getValue("ID"));
        assertEquals("8", new BigDecimal(7839), empRow1.getValue("EMPNO"));
        assertEquals("9", "KING", empRow1.getValue("NAME"));
        assertEquals("10", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));
        
        DataRow empRow2 = empTable.getRow(1);
        assertEquals("11", new BigDecimal(2), empRow2.getValue("ID"));
        assertEquals("12", new BigDecimal(7782), empRow2.getValue("EMPNO"));
        assertEquals("13", "CLARK", empRow2.getValue("NAME"));
        assertEquals("14", new BigDecimal(1), empRow2.getValue("DEPARTMENT_ID"));
    }
    
    public void testReadManyToOne() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee king = new Employee(new Long(1), 7839, "KING", accounting);
        Employee clark = new Employee(new Long(2), 7782, "CLARK", accounting);

        accounting.employees.add(king);
        accounting.employees.add(clark);

        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable empTable = dataSet.getTable("EMPLOYEE");
        assertEquals("2", 1, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("3", new BigDecimal(2), empRow1.getValue("ID"));
        assertEquals("4", new BigDecimal(7782), empRow1.getValue("EMPNO"));
        assertEquals("5", "CLARK", empRow1.getValue("NAME"));
        assertEquals("6", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));

        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("7", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("8", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("9", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("10", "ACCOUNTING", deptRow.getValue("NAME"));
    }

    public void testReadRecursiveManyToOne() {
        Employee3 clark = new Employee3(new Long(1), 7782, "CLARK", null);
        Employee3 king = new Employee3(new Long(2), 7839, "KING", clark);

        EntityReader reader = new EntityReader(king);
        DataSet dataSet = reader.read();
        assertEquals("1", 1, dataSet.getTableSize());
        DataTable empTable = dataSet.getTable("EMPLOYEE3");
        assertEquals("2", 2, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("3", new BigDecimal(2), empRow1.getValue("ID"));
        assertEquals("4", new BigDecimal(7839), empRow1.getValue("EMPNO"));
        assertEquals("5", "KING", empRow1.getValue("NAME"));
        assertEquals("6", new BigDecimal(1), empRow1.getValue("BOSS_ID"));

        DataRow empRow2 = empTable.getRow(1);
        assertEquals("7", new BigDecimal(1), empRow2.getValue("ID"));
        assertEquals("8", new BigDecimal(7782), empRow2.getValue("EMPNO"));
        assertEquals("9", "CLARK", empRow2.getValue("NAME"));
        assertEquals("10", null, empRow2.getValue("BOSS_ID"));
    }

    public void testReadEmbedded() {
        EmployeePeriod period = new EmployeePeriod("2000", "2010");
        Employee2 clark = new Employee2(new Long(1), 7782, "CLARK", period);

        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();
        assertEquals("1", 1, dataSet.getTableSize());

        DataTable empTable = dataSet.getTable("Employee2");
        assertEquals("2", 1, empTable.getRowSize());
        DataRow empRow = empTable.getRow(0);
        assertEquals("3", new BigDecimal(1), empRow.getValue("ID"));
        assertEquals("4", new BigDecimal(7782), empRow.getValue("EMPNO"));
        assertEquals("5", "CLARK", empRow.getValue("NAME"));
        assertEquals("6", "2000", empRow.getValue("STARTDATE"));
        assertEquals("7", "2010", empRow.getValue("ENDDATE"));
    }

    public void testReadManyToMany() {
        Project p1 = new Project(new Long(1));
        Project p2 = new Project(new Long(2));
        Employee4 e1 = new Employee4(new Long(1));
        Employee4 e2 = new Employee4(new Long(2));

        p1.employees.add(e1);
        p1.employees.add(e2);
        e1.projects.add(p1);
        e2.projects.add(p1);

        p2.employees.add(e1);
        p2.employees.add(e2);
        e1.projects.add(p2);
        e2.projects.add(p2);

        EntityReader reader = new EntityReader(p1, null);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable projectTable = dataSet.getTable("Project");
        assertEquals("2", 1, projectTable.getRowSize());
        DataTable empTable = dataSet.getTable("Employee4");
        assertEquals("3", 2, empTable.getRowSize());
    }

    @Entity(name = "Department")
    public static class Department {

        @Id
        private Long id;

        private long deptno;

        private String name;

        @OneToMany(mappedBy = "department")
        private SortedSet<Employee> employees = new TreeSet<Employee>();

        public Department() {
        }

        public Department(Long id, long deptno, String name) {
            this.id = id;
            this.deptno = deptno;
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Department))
                return false;
            Department castOther = (Department) other;
            return this.deptno == castOther.deptno;
        }

        @Override
        public int hashCode() {
            return (int) this.deptno;
        }
    }

    @Entity(name = "Department5")
    public static class Department5 {

        @Id
        private Long id;

        private long deptno;

        private String name;

        @OneToMany(mappedBy = "department")
        @MapKey
        private Map<Long, Employee5> employees = new TreeMap<Long, Employee5>();

        public Department5() {
        }

        public Department5(Long id, long deptno, String name) {
            this.id = id;
            this.deptno = deptno;
            this.name = name;
        }
    }
    
    @Entity(name = "Employee")
    public static class Employee implements Comparable<Employee> {

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

        public int compareTo(Employee other) {
            if (this.empno > other.empno) {
                return 1;
            }
            if (this.empno < other.empno) {
                return -1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Employee))
                return false;
            Employee castOther = (Employee) other;
            return this.empno == castOther.empno;
        }

        @Override
        public int hashCode() {
            return (int) this.empno;
        }
    }

    @Entity(name = "Employee2")
    public static class Employee2 {

        @Id
        private Long id;

        private long empno;

        private String name;

        @Embedded
        private EmployeePeriod period;

        public Employee2() {
        }

        public Employee2(Long id, long empno, String name, EmployeePeriod period) {
            this.id = id;
            this.empno = empno;
            this.name = name;
            this.period = period;
        }
    }

    @Entity(name = "Employee3")
    public static class Employee3 {
        @Id
        private Long id;

        private long empno;

        private String name;

        @ManyToOne
        private Employee3 boss;

        public Employee3() {
        }

        public Employee3(Long id, long empno, String name, Employee3 boss) {
            this.id = id;
            this.empno = empno;
            this.name = name;
            this.boss = boss;
        }
    }

    @Entity(name = "Employee4")
    public static class Employee4 {

        @Id
        private Long id;

        @ManyToMany(mappedBy = "employees")
        private Collection<Project> projects = new HashSet<Project>();

        public Employee4() {
        }

        public Employee4(Long id) {
            this.id = id;
        }
    }

    @Entity(name = "Employee5")
    public static class Employee5 {

        @Id
        private Long id;

        private long empno;

        private String name;

        @ManyToOne
        private Department5 department;

        public Employee5() {
        }

        public Employee5(Long id, long empno, String name, Department5 department) {
            this.id = id;
            this.empno = empno;
            this.name = name;
            this.department = department;
        }
    }
    
    @Entity(name = "Project")
    public static class Project {

        @Id
        private Long id;

        @ManyToMany
        private Collection<Employee4> employees = new HashSet<Employee4>();

        public Project() {
        }

        public Project(Long id) {
            this.id = id;
        }
    }

    @Embeddable
    public static class EmployeePeriod {

        private String startDate;

        private String endDate;

        public EmployeePeriod(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

}
