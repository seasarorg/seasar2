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
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataSet;

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
        accounting.getEmployees().add(clark);
        accounting.getEmployees().add(king);
        reserch.getEmployees().add(smith);
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(clark);
        employees.add(king);
        employees.add(smith);

        EntityListReader reader = new EntityListReader(employees);
        DataSet dataSet = reader.read();

        assertEquals("1", 2, dataSet.getTableSize());
        assertEquals("2", 3, dataSet.getTable("EMPLOYEE").getRowSize());
        assertEquals("3", 2, dataSet.getTable("DEPARTMENT").getRowSize());
    }

    public void testDuplicatedEntities() {
        Department accounting = new Department(new Long(1), 10, "ACCOUNTING");
        Employee clark = new Employee(new Long(1), 7782, "CLARK", accounting);
        Employee king = new Employee(new Long(2), 7839, "KING", accounting);
        accounting.getEmployees().add(clark);
        accounting.getEmployees().add(king);
        List<Department> departments = new ArrayList<Department>();
        departments.add(accounting);
        departments.add(accounting);

        EntityListReader reader = new EntityListReader(departments);
        DataSet dataSet = reader.read();

        assertEquals("1", 2, dataSet.getTableSize());
        assertEquals("2", 2, dataSet.getTable("DEPARTMENT").getRowSize());
        assertEquals("3", 2, dataSet.getTable("EMPLOYEE").getRowSize());
    }
}
