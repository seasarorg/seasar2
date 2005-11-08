package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataTableImpl;
import org.seasar.extension.dataset.impl.SqlReloadTableReader;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.unit.S2TestCase;

public class SqlReloadTableReaderTest extends S2TestCase {

	private DataSource ds_;

	public SqlReloadTableReaderTest(String arg0) {
		super(arg0);
	}

	public void testRead() throws Exception {
		DataTable table = new DataTableImpl("emp");
		table.addColumn("empno");
		table.addColumn("ename");
		DataRow row = table.addRow();
		row.setValue("empno", new BigDecimal(7788));
		row.setValue("ename", "SCOTT");
		SqlReloadTableReader reader = new SqlReloadTableReader(ds_, table);
		DataTable ret = reader.read();
		System.out.println(ret);
		assertEquals("1", table, ret);
		assertEquals("2", RowStates.UNCHANGED, ret.getRow(0).getState());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SqlReloadTableReaderTest.class);
	}

}