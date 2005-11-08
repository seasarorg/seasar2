package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.impl.SqlReloadReader;
import org.seasar.extension.unit.S2TestCase;

public class SqlReloadReaderTest extends S2TestCase {

	private DataSource ds_;

	public SqlReloadReaderTest(String arg0) {
		super(arg0);
	}

	public void testRead() throws Exception {
		DataSet dataSet = new DataSetImpl();
		DataTable table = dataSet.addTable("emp");
		table.addColumn("empno");
		table.addColumn("ename");
		DataRow row = table.addRow();
		row.setValue("empno", new BigDecimal(7788));
		row.setValue("ename", "SCOTT");
		SqlReloadReader reader = new SqlReloadReader(ds_, dataSet);
		DataSet ret = reader.read();
		assertEquals("1", dataSet, ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SqlReloadReaderTest.class);
	}

}