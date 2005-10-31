package org.seasar.extension.unit;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.unit.BeanReader;

/**
 * @author higa
 *  
 */
public class BeanReaderTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * 
	 * @param arg0
	 */
	public BeanReaderTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BeanReaderTest.class);
	}

	protected void tearDown() throws Exception {
	}

	public void testRead() throws Exception {
		Employee emp = new Employee();
		emp.setEmpno(7788);
		emp.setEname("SCOTT");
		emp.setDeptno(10);
		emp.setDname("HOGE");
		BeanReader reader = new BeanReader(emp);
		DataSet ds = reader.read();
		DataTable table = ds.getTable(0);
		DataRow row = table.getRow(0);
		assertEquals("1", new BigDecimal(7788), row.getValue("empno"));
		assertEquals("2", "SCOTT", row.getValue("ename"));
		assertEquals("3", new BigDecimal(10), row.getValue("deptno"));
		assertEquals("4", "HOGE", row.getValue("dname"));
		assertEquals("5", RowStates.UNCHANGED, row.getState());
	}
}