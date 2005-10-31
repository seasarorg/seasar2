package org.seasar.extension.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;

public class S2TestCaseTest extends S2TestCase {

	private static final String J2EE_PATH = "j2ee.dicon";
	private boolean testAaaSetUpInvoked_ = false;
	private Date date_;
	private String ccc_;
	private Employee bbb_;
	
	public S2TestCaseTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(S2TestCaseTest.class);
	}

	public void setUpAaa() {
		testAaaSetUpInvoked_ = true;
	}
	
	public void testAaa() {
		assertEquals("1", true, testAaaSetUpInvoked_);
	}
	
	public void tearDownAaa() {
		System.out.println("tearDownAaa");
	}
	
	public void setUpBbbTx() {
		include(J2EE_PATH);
	}

	public void testBbbTx() {
		System.out.println("testBbbTx");
	}
	
	public void setUpGetDataSource() {
		include(J2EE_PATH);
	}

	public void testGetDataSource() {
		assertNotNull("1", getDataSource());
	}
	
	public void testReadXls() {
		DataSet dataSet = readXls("testdata.xls");
		System.out.println(dataSet);
		assertEquals("1", 2, dataSet.getTableSize());
		DataTable table = dataSet.getTable("emp");
		assertEquals("2", 2, table.getRowSize());
		assertEquals("3", 3, table.getColumnSize());
		DataRow row = table.getRow(0);
		assertEquals("4", new BigDecimal(9900), row.getValue("empno"));
		assertEquals("5", "hoge", row.getValue("ename"));
		assertEquals("6", "aaa", row.getValue("dname"));
	}
	
	public void setUpWriteDbTx() {
		include(J2EE_PATH);
	}

	public void testWriteDbTx() {
		DataSet dataSet = readXls("testdata.xls");
		writeDb(dataSet);
	}
	
	public void setUpReadDbByTable() {
		include(J2EE_PATH);
	}
	
	public void testReadDbByTable() {
		DataTable table = readDbByTable("emp", "empno = 7788");
		System.out.println(table);
		assertEquals("1", 1, table.getRowSize());
	}
	
	public void setUpReadDbBySql() {
		include(J2EE_PATH);
	}

	public void testReadDbBySql() {
		DataTable table = readDbBySql("SELECT * FROM emp WHERE empno = 7788", "emp");
		System.out.println(table);
		assertEquals("1", 1, table.getRowSize());
	}

	public void testWriteXls() {
		DataSet dataSet = readXls("testdata.xls");
		writeXls("aaa.xls", dataSet);
		DataSet dataSet2 = readXls("aaa.xls");
		assertEquals("1", dataSet, dataSet2);
	}
	
	public void setUpBindField() {
		include(J2EE_PATH);
		register(Date.class);
	}

	public void testBindField() {
		assertNotNull("1", date_);
	}
	
	public void setUpBindField2() {
		include("bbb.dicon");
	}
	
	public void testBindField2() {
		assertNotNull("1", bbb_);
	}
	
	public void setUpReadXlsReplaceDbTx() {
		include(J2EE_PATH);
	}

	public void testReadXlsReplaceDbTx() {
		readXlsReplaceDb("testdata.xls");
	}
	
	public void setUpReadXlsAllReplaceDbTx() {
		include(J2EE_PATH);
	}

	public void testReadXlsAllReplaceDbTx() {
		readXlsAllReplaceDb("testdata.xls");
	}
	
	public void testInclude() {
		include("aaa.dicon");
		try {
			getComponent(Date.class);
			fail("1");
		} catch (TooManyRegistrationRuntimeException ex) {
			System.out.println(ex);
		}
	}
	
	public void testAssertMapEquals() {
		DataSet expected = new DataSetImpl();
		DataTable table = expected.addTable("emp");
		table.addColumn("empno");
		DataRow row = table.addRow();
		row.setValue("empno", new BigDecimal(7788));
		Map map = new HashMap();
		map.put("EMPNO", new Integer(7788));
		assertEquals("1", expected, map);
	}
	
	public void testAssertMapListEquals() {
		DataSet expected = new DataSetImpl();
		DataTable table = expected.addTable("emp");
		table.addColumn("empno");
		DataRow row = table.addRow();
		row.setValue("empno", new BigDecimal(7788));
		Map map = new HashMap();
		map.put("EMPNO", new Integer(7788));
		List list = new ArrayList();
		list.add(map);
		assertEquals("1", expected, list);
	}
	
	public void testAssertBeanEquals() {
		DataSet expected = new DataSetImpl();
		DataTable table = expected.addTable("emp");
		table.addColumn("aaa");
		DataRow row = table.addRow();
		row.setValue("aaa", "111");
		Hoge bean = new Hoge();
		bean.setAaa("111");
		assertEquals("1", expected, bean);
	}
	
	public void testAssertBeanListEquals() {
		DataSet expected = new DataSetImpl();
		DataTable table = expected.addTable("emp");
		table.addColumn("aaa");
		DataRow row = table.addRow();
		row.setValue("aaa", "111");
		Hoge bean = new Hoge();
		bean.setAaa("111");
		List list = new ArrayList();
		list.add(bean);
		assertEquals("1", expected, list);
	}
	
	public void testAssertEqualsForNull() {
		Object hoge = null;
		assertEquals("1", null, hoge);
	}
	
	public void setUpIsAssignableFrom() {
		include("bbb.dicon");
	}
	
	public void testIsAssignableFrom() {
		assertEquals("1", "hoge", ccc_);
	}
	
	public static class Hoge {
		
		private String aaa;
		
		/**
		 * @return Returns the aaa.
		 */
		public String getAaa() {
			return aaa;
		}
		/**
		 * @param aaa The aaa to set.
		 */
		public void setAaa(String aaa) {
			this.aaa = aaa;
		}
	}
}
