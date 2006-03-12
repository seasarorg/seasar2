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

        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("6", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("7", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("8", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("9", "ACCOUNTING", deptRow.getValue("NAME"));
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

        DataTable deptTable = dataSet.getTable("DEPARTMENT");
        assertEquals("6", 1, deptTable.getRowSize());
        DataRow deptRow = deptTable.getRow(0);
        assertEquals("7", new BigDecimal(1), deptRow.getValue("ID"));
        assertEquals("8", new BigDecimal(10), deptRow.getValue("DEPTNO"));
        assertEquals("9", "ACCOUNTING", deptRow.getValue("NAME"));
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
        DataRow empRow2 = empTable.getRow(1);
        assertEquals("6", new BigDecimal(1), empRow2.getValue("ID"));
        assertEquals("7", new BigDecimal(7782), empRow2.getValue("EMPNO"));
        assertEquals("8", "CLARK", empRow2.getValue("NAME"));
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
        DataRow empRow2 = empTable.getRow(1);
        assertEquals("10", new BigDecimal(2), empRow2.getValue("ID"));
        assertEquals("11", new BigDecimal(7839), empRow2.getValue("EMPNO"));
        assertEquals("12", "KING", empRow2.getValue("NAME"));
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
}
