package org.seasar.extension.dataset.impl;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableWriter;
import org.seasar.extension.dataset.impl.DataTableImpl;
import org.seasar.extension.dataset.impl.SqlDeleteTableWriter;
import org.seasar.extension.dataset.impl.SqlTableWriter;
import org.seasar.extension.unit.S2TestCase;

public class SqlDeleteTableWriterTest extends S2TestCase {

	public SqlDeleteTableWriterTest(String arg0) {
		super(arg0);
	}

	public void testWriteTx() throws Exception {
		DataTable table = new DataTableImpl("emp");
		table.addColumn("empno");
		table.addColumn("ename");
		DataRow row = table.addRow();
		row.setValue("empno", new Integer(9900));
		row.setValue("ename", "hoge");
		TableWriter writer = new SqlTableWriter(getDataSource());
		writer.write(table);
		TableWriter writer2 = new SqlDeleteTableWriter(getDataSource());
		writer2.write(table);
		DataTable table2 = readDbByTable("emp", "empno = 9900");
		assertEquals("1", 0, table2.getRowSize());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SqlDeleteTableWriterTest.class);
	}

}
