package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataTableImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;

public class DataTableImplTest extends TestCase {

	public DataTableImplTest(String name) {
		super(name);
	}

	public void testRemoveRows() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa", ColumnTypes.STRING);
		DataRow row = table.addRow();
		row.setValue("aaa", "1");
		DataRow row2 = table.addRow();
		row2.setValue("aaa", "2");
		row2.remove();
		DataRow row3 = table.addRow();
		row3.setValue("aaa", "3");
		row3.remove();
		DataRow[] rows = table.removeRows();
		assertEquals("1", 2, rows.length);
		assertEquals("2", 2, table.getRemovedRowSize());
		assertEquals("3", 1, table.getRowSize());
		assertEquals("4", "1", table.getRow(0).getValue("aaa"));
		assertSame("5", row2, table.getRemovedRow(0));
		assertSame("6", row3, table.getRemovedRow(1));
	}
	
	public void testEquals() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		DataTable table2 = new DataTableImpl("hoge");
		table.addColumn("aaa");
		table2.addColumn("aaa");
		DataRow row = table.addRow();
		row.setValue("aaa", "1");
		row = table.addRow();
		row.setValue("aaa", "2");
		row.remove();
		DataRow row2 = table2.addRow();
		row2.setValue("aaa", "1");
		row2 = table2.addRow();
		row2.setValue("aaa", "2");
		row2.remove();
		assertEquals("1", table, table2);
		table.removeRows();
		table2.removeRows();
		assertEquals("2", table, table2);
		table2.getRow(0).setValue(0, "11");
		assertEquals("3", false, table.equals(table2));
	}
	
	public void testEquals2() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa");
		table.addColumn("bbb");
		DataTable table2 = new DataTableImpl("hoge2");
		table2.addColumn("ccc");
		table2.addColumn("aaa");
		table2.addColumn("bbb");
		DataRow row = table.addRow();
		DataRow row2 = table2.addRow();
		row.setValue("aaa", "111");
		row.setValue("bbb", "222");
		row2.setValue("aaa", "111");
		row2.setValue("bbb", "222");
		row2.setValue("ccc", "333");
		assertEquals("1", table, table2);
	}
	
	public void testEquals3() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa");
		DataTable table2 = new DataTableImpl("hoge2");
		table2.addColumn("aaa", ColumnTypes.STRING);
		DataRow row = table.addRow();
		DataRow row2 = table2.addRow();
		row.setValue("aaa", new BigDecimal("111"));
		row2.setValue("aaa", "111");
		assertEquals("1", table, table2);
	}
	
	public void testSetupColumns() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.setupColumns(MyBean.class);
		assertEquals("1", 2, table.getColumnSize());
		assertEquals("2", ColumnTypes.BIGDECIMAL, table.getColumnType("myInt"));
		assertEquals("3", ColumnTypes.STRING, table.getColumnType("myString"));
	}
	
	public void testCopyFromList() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.setupColumns(MyBean.class);
		List list = new ArrayList();
		MyBean bean = new MyBean();
		bean.setMyInt(1);
		bean.setMyString("a");
		list.add(bean);
		bean = new MyBean();
		bean.setMyInt(2);
		bean.setMyString("b");
		list.add(bean);
		table.copyFrom(list);
		assertEquals("1", 2, table.getRowSize());
		assertEquals("2", new BigDecimal(1), table.getRow(0).getValue("myInt"));
		assertEquals("3", "a", table.getRow(0).getValue("myString"));
		assertEquals("4", new BigDecimal(2), table.getRow(1).getValue("myInt"));
		assertEquals("5", "b", table.getRow(1).getValue("myString"));
		assertEquals("6", RowStates.UNCHANGED, table.getRow(0).getState());
	}
	
	public void testCopyFromBeanOrMap() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.setupColumns(MyBean.class);
		MyBean bean = new MyBean();
		bean.setMyInt(1);
		bean.setMyString("a");
		table.copyFrom(bean);
		assertEquals("1", 1, table.getRowSize());
		assertEquals("2", new BigDecimal(1), table.getRow(0).getValue("myInt"));
		assertEquals("3", "a", table.getRow(0).getValue("myString"));
		assertEquals("4", RowStates.UNCHANGED, table.getRow(0).getState());
	}
	
	public void testHasColumn() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa_0");
		table.addColumn("bbbCcc");
		assertEquals("1", true, table.hasColumn("aaa_0"));
		assertEquals("2", true, table.hasColumn("bbb_ccc"));
	}
	
	public void testGetColumn() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa_0");
		table.addColumn("bbbCcc");
		assertNotNull("1", table.getColumn("aaa_0"));
		assertNotNull("2", table.getColumn("bbb_ccc"));
	}
	
	public void testGetColumn2() throws Exception {
		DataTable table = new DataTableImpl("hoge");
		table.addColumn("aaa_bbb");
		assertNotNull("1", table.getColumn("aaaBbb"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DataTableImplTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { DataTableImplTest.class.getName()});
	}
	
	public static class MyBean {
		
		private int myInt_;
		private String myString_;
		
		public int getMyInt() {
			return myInt_;
		}
		
		public void setMyInt(int myInt) {
			myInt_ = myInt;
		}
		
		public String getMyString() {
			return myString_;
		}
		
		public void setMyString(String myString) {
			myString_ = myString;
		}
	}
}