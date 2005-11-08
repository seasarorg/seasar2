package org.seasar.extension.dataset.impl;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.unit.S2TestCase;

public class SqlTableReaderTest extends S2TestCase {

	private DataSource ds_;

	public SqlTableReaderTest(String arg0) {
		super(arg0);
	}

	public void testRead() throws Exception {
		SqlTableReader reader = new SqlTableReader(ds_);
		reader.setTable("emp");
		DataTable ret = reader.read();
		System.out.println(ret);
		assertEquals("1", 14, ret.getRowSize());
		assertEquals("2", RowStates.UNCHANGED, ret.getRow(0).getState());
	}
	
	public void testRead2() throws Exception {
		SqlTableReader reader = new SqlTableReader(ds_);
		reader.setTable("emp", "empno = 7788");
		DataTable ret = reader.read();
		System.out.println(ret);
		assertEquals("1", 1, ret.getRowSize());
	}
	
	public void testRead3() throws Exception {
		SqlTableReader reader = new SqlTableReader(ds_);
		reader.setSql("select * from emp", "emp");
		DataTable ret = reader.read();
		System.out.println(ret);
		assertEquals("1", 14, ret.getRowSize());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SqlTableReaderTest.class);
	}

}
