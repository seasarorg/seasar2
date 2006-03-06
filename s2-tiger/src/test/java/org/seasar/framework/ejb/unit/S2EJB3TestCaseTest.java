package org.seasar.framework.ejb.unit;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.framework.ejb.unit.annotation.Rollback;

/**
 * @author taedium
 * 
 */
public class S2EJB3TestCaseTest extends S2EJB3TestCase {

    @Override
    public void setUp() {
        include("j2ee.dicon");
    }

    public void testAssertEntityEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("EMPLOYEE");
        table.addColumn("ID");
        table.addColumn("EMPNO");
        table.addColumn("NAME");
        DataRow row = table.addRow();
        row.setValue("ID", new Long(1));
        row.setValue("EMPNO", 7782);
        row.setValue("NAME", "CLARK");

        Employee clark = new Employee(new Long(1), 7782, "CLARK", null);

        assertEntityEquals("1", expected, clark);
    }

    public void testAssertEntityListEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("EMPLOYEE");
        table.addColumn("ID");
        table.addColumn("EMPNO");
        table.addColumn("NAME");
        DataRow row1 = table.addRow();
        row1.setValue("ID", new Long(1));
        row1.setValue("EMPNO", 7782);
        row1.setValue("NAME", "CLARK");
        DataRow row2 = table.addRow();
        row2.setValue("ID", new Long(2));
        row2.setValue("EMPNO", 7839);
        row2.setValue("NAME", "KING");

        Employee clark = new Employee(new Long(1), 7782, "CLARK", null);
        Employee king = new Employee(new Long(2), 7839, "KING", null);
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(clark);
        employees.add(king);

        assertEntityListEquals("1", expected, employees);
    }

    @Rollback
    public void testRollback() {
        assertTrue("1", needTransaction());
    }

}
