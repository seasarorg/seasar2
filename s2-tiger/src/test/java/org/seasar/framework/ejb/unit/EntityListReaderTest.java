package org.seasar.framework.ejb.unit;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class EntityListReaderTest extends S2TestCase {

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
