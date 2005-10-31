package org.seasar.framework.util;

import java.sql.DatabaseMetaData;
import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.DatabaseMetaDataUtil;

public class DatabaseMetaDataUtilTest extends S2TestCase {

	public DatabaseMetaDataUtilTest(String arg0) {
		super(arg0);
	}

	public void testGetColumnList() throws Exception {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "emp");
		System.out.println(columnSet);
		assertTrue("1", columnSet.size() > 0);
	}
	
	public void testGetColumnListForNotExistTable() throws Exception {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "_emp_");
		assertEquals("1", 0, columnSet.size());
	}
	
	public void testGetColumnListForSchema() throws Exception {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		Set columnSet = DatabaseMetaDataUtil.getColumnSet(dbMetaData, "SA.emp");
		System.out.println(columnSet);
		assertTrue("1", columnSet.size() > 0);
	}

	public void testGetPrimaryKeyList() throws Exception {
		DatabaseMetaData dbMetaData = getConnection().getMetaData();
		Set primaryKeySet =
			DatabaseMetaDataUtil.getPrimaryKeySet(dbMetaData, "emp");
		System.out.println(primaryKeySet);
		assertTrue("1", primaryKeySet.size() > 0);
		System.out.println(dbMetaData.getDatabaseProductName());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DatabaseMetaDataUtilTest.class);
	}

}
