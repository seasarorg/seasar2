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
import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * @author taedium
 * 
 */
public class EntityReaderTest extends TestCase {

    public void testManyToOneRelationship() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        accounting.getEmployees().add(clark);

        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable empTable = dataSet.getTable("EMPLOYEE");
        assertEquals("2", 1, empTable.getRowSize());
        DataRow empRow = empTable.getRow(0);
        assertEquals("3", new BigDecimal(1), empRow.getValue("ID"));
        assertEquals("4", new BigDecimal(7782), empRow.getValue("EMPNO"));
        assertEquals("5", "CLARK", empRow.getValue("NAME"));
        assertEquals("6", new BigDecimal(1), empRow.getValue("DEPARTMENT_ID"));

        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("7", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("8", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("9", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("10", "ACCOUNTING", deptRow.getValue("NAME"));
    }

    public void testManyToOneRelationship2() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        Employee king = new Employee(new Long(2), 7839, "KING", accounting);
        accounting.getEmployees().add(clark);
        accounting.getEmployees().add(king);

        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable empTable = dataSet.getTable("EMPLOYEE");
        assertEquals("2", 1, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("3", new BigDecimal(1), empRow1.getValue("ID"));
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

    public void testManyToOneRelationship3() {
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

    public void testOneToManyRelationship() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        Employee king = new Employee(new Long(2), 7839, "KING", accounting);
        accounting.getEmployees().add(clark);
        accounting.getEmployees().add(king);

        EntityReader reader = new EntityReader(accounting);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("2", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("3", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("4", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("5", "ACCOUNTING", deptRow.getValue("NAME"));

        DataTable empTable = dataSet.getTable("EMPLOYEE");
        assertEquals("6", 2, empTable.getRowSize());
        DataRow empRow1 = empTable.getRow(0);
        assertEquals("7", new BigDecimal(1), empRow1.getValue("ID"));
        assertEquals("8", new BigDecimal(7782), empRow1.getValue("EMPNO"));
        assertEquals("9", "CLARK", empRow1.getValue("NAME"));
        assertEquals("10", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));
        
        DataRow empRow2 = empTable.getRow(1);
        assertEquals("11", new BigDecimal(2), empRow2.getValue("ID"));
        assertEquals("12", new BigDecimal(7839), empRow2.getValue("EMPNO"));
        assertEquals("13", "KING", empRow2.getValue("NAME"));
        assertEquals("14", new BigDecimal(1), empRow1.getValue("DEPARTMENT_ID"));
    }

    public void testEmbeddableClass() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2006, Calendar.AUGUST, 8);
        long startDate = calendar.getTime().getTime();
        calendar.add(Calendar.YEAR, 1);
        long endDate = calendar.getTime().getTime();
        EmployeePeriod period = new EmployeePeriod(startDate, endDate);
        Employee2 clark = new Employee2(new Long(1), 7782, "CLARK", period);

        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();
        assertEquals("1", 1, dataSet.getTableSize());

        DataTable empTable = dataSet.getTable("EMPLOYEE2");
        assertEquals("2", 1, empTable.getRowSize());
        DataRow empRow = empTable.getRow(0);
        assertEquals("3", new BigDecimal(1), empRow.getValue("ID"));
        assertEquals("4", new BigDecimal(7782), empRow.getValue("EMPNO"));
        assertEquals("5", "CLARK", empRow.getValue("NAME"));
        assertEquals("6", new Timestamp(startDate), empRow
                .getValue("STARTDATE"));
        assertEquals("7", new Timestamp(endDate), empRow.getValue("ENDDATE"));
    }

    public void testManyToManyRelationship() {
        Project p1 = new Project(new Long(1));
        Project p2 = new Project(new Long(2));
        Employee4 e1 = new Employee4(new Long(1));
        Employee4 e2 = new Employee4(new Long(2));

        p1.getEmployees().add(e1);
        p1.getEmployees().add(e2);
        e1.getProjects().add(p1);
        e2.getProjects().add(p1);

        p2.getEmployees().add(e1);
        p2.getEmployees().add(e2);
        e1.getProjects().add(p2);
        e2.getProjects().add(p2);

        EntityReader reader = new EntityReader(p1);
        DataSet dataSet = reader.read();
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable projectTable = dataSet.getTable("PROJECT");
        assertEquals("2", 1, projectTable.getRowSize());
        DataTable empTable = dataSet.getTable("EMPLOYEE4");
        assertEquals("3", 2, empTable.getRowSize());
    }
    
    public void testJoinColumn() {
        Department2 accounting = new Department2(new Long(1), 10, "ACCOUNTING");
        Employee5 clark = new Employee5(new Long(1), 7782, "CLARK", accounting);
        
        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();     
        DataTable dataTable = dataSet.getTable("EMPLOYEE5");
        DataRow row = dataTable.getRow(0);
        assertEquals("1", new BigDecimal(1), row.getValue("DEPARTMENT_DEPT_ID"));
    }
    
    public void testJoinColumn2() {
        Department2 accounting = new Department2(new Long(1), 10, "ACCOUNTING");
        Employee6 clark = new Employee6(new Long(1), 7782, "CLARK", accounting);
        
        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();     
        DataTable dataTable = dataSet.getTable("EMPLOYEE6");
        DataRow row = dataTable.getRow(0);
        assertEquals("1", new BigDecimal(1), row.getValue("FOO"));
    }
    
    public void testJoinColumn3() {
        Department3 accounting = new Department3(new Long(1), new Long(2), 10, "ACCOUNTING");
        Employee7 clark = new Employee7(new Long(1), 7782, "CLARK", accounting);
        
        EntityReader reader = new EntityReader(clark);
        DataSet dataSet = reader.read();     
        DataTable dataTable = dataSet.getTable("EMPLOYEE7");
        DataRow row = dataTable.getRow(0);
        assertEquals("1", new BigDecimal(1), row.getValue("DEPTID1"));
        assertEquals("2", new BigDecimal(2), row.getValue("DEPTID2"));
    }
    
    public void testPkJoinColumn() {
        ValuedCustomer customer = new ValuedCustomer();
        customer.setId(new Long(1));
        customer.setName("A");
        customer.setRank(1);
        
        EntityReader reader = new EntityReader(customer);
        DataSet dataSet = reader.read();     
        DataTable dataTable = dataSet.getTable("VALUEDCUSTOMER");
        DataRow row = dataTable.getRow(0);
        assertEquals("1", new BigDecimal(1), row.getValue("CUST_ID"));
    }
}
