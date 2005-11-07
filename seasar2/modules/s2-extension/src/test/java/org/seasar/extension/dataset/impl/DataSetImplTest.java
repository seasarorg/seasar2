package org.seasar.extension.dataset.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableNotFoundRuntimeException;
import org.seasar.extension.dataset.impl.DataSetImpl;

public class DataSetImplTest extends TestCase {

	public DataSetImplTest(String name) {
		super(name);
	}

	public void testRemoveTable() throws Exception {
		DataSet dataSet = new DataSetImpl();
		DataTable table = dataSet.addTable("hoge"); 
		assertEquals("1", table, dataSet.removeTable("hoge"));
		assertEquals("2", 0, dataSet.getTableSize());
		dataSet.addTable(table);
		assertEquals("3", table, dataSet.removeTable(table));
		assertEquals("4", 0, dataSet.getTableSize());
		dataSet.addTable(table);
		assertEquals("5", table, dataSet.removeTable(0));
		assertEquals("6", 0, dataSet.getTableSize());
		try {
			dataSet.removeTable("hoge");
			fail("7");
		} catch (TableNotFoundRuntimeException ex) {
			System.out.println(ex);
		}
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DataSetImplTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { DataSetImplTest.class.getName()});
	}
}