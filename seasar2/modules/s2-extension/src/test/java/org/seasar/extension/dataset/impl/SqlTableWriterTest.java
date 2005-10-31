package org.seasar.extension.dataset.impl;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataTableImpl;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.impl.SqlTableWriter;
import org.seasar.extension.unit.S2TestCase;

public class SqlTableWriterTest extends S2TestCase {

	private DataSource ds_;

	public SqlTableWriterTest(String arg0) {
		super(arg0);
	}

	public void testCreatedTx() throws Exception {
		DataTable table = new DataTableImpl("emp");
		table.addColumn("empno");
		table.addColumn("ename");
		table.addColumn("dname");
		DataRow row = table.addRow();
		row.setValue("empno", new Integer(9900));
		row.setValue("ename", "hoge");
		row.setValue("dname", "aaa");
		SqlTableWriter writer = new SqlTableWriter(ds_);
		writer.write(table);
		SqlTableReader reader = new SqlTableReader(ds_);
		reader.setTable("emp", "empno = 9900");
		DataTable ret = reader.read();
		System.out.println(ret);
		assertNotNull("1", ret);
	}
	
	public void testModifiedTx() throws Exception {
		SqlTableReader reader = new SqlTableReader(ds_);
		String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
		reader.setSql(sql, "emp");
		DataTable table = reader.read();
		DataRow row = table.getRow(0);
		row.setValue("ename", "hoge");
		row.setValue("dname", "aaa");
		SqlTableWriter writer = new SqlTableWriter(ds_);
		writer.write(table);
		SqlTableReader reader2 = new SqlTableReader(ds_);
		reader2.setTable("emp", "empno = 7788");
		DataTable table2 = reader2.read();
		System.out.println(table2);
		assertEquals("1", "hoge", table2.getRow(0).getValue("ename"));
	}
	
	public void testRemovedTx() throws Exception {
		SqlTableReader reader = new SqlTableReader(ds_);
		String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
		reader.setSql(sql, "emp");
		DataTable table = reader.read();
		DataRow row = table.getRow(0);
		row.remove();
		SqlTableWriter writer = new SqlTableWriter(ds_);
		writer.write(table);
		SqlTableReader reader2 = new SqlTableReader(ds_);
		reader2.setTable("emp", "empno = 7788");
		DataTable table2 = reader2.read();
		assertEquals("1", 0, table2.getRowSize());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SqlTableWriterTest.class);
	}

}
