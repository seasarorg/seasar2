package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataRowReloadResultSetHandler;
import org.seasar.extension.dataset.impl.DataTableImpl;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.unit.S2TestCase;

public class DataRowReloadResultSetHandlerTest extends S2TestCase {

	private DataSource ds_;

	public DataRowReloadResultSetHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandle() throws Exception {
		String sql = "select empno, ename from emp where empno = ?";
		DataTable table = new DataTableImpl("emp");
		table.addColumn("empno").setPrimaryKey(true);
		table.addColumn("ename");
		DataRow row = table.addRow();
		row.setValue("empno", new BigDecimal(7788));
		row.setValue("ename", "SCOTT");
		DataTable newTable = new DataTableImpl("emp");
		newTable.addColumn("empno").setPrimaryKey(true);
		newTable.addColumn("ename");
		DataRow newRow = newTable.addRow();
		SelectHandler handler = new BasicSelectHandler(ds_, sql,
				new DataRowReloadResultSetHandler(row, newRow));
		handler.execute(new Object[] { new Integer(7788) });
		System.out.println(newRow);
		assertEquals("2", row, newRow);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DataRowReloadResultSetHandlerTest.class);
	}

}