package org.seasar.extension.dbcp.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class DataSourceImplTest2 extends S2TestCase {

	private static final String PATH = "j2ee.dicon";
	private DataSource ds_;

	public DataSourceImplTest2(String name) {
		super(name);
	}

	public void testGetConnection() throws Exception {
		Connection con = null;
		try {
			con = ds_.getConnection();
			assertNotNull("1", con);
		} finally {
			con.close();
		}
	}

	protected void setUp() throws Exception {
		include(PATH);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DataSourceImplTest2.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { DataSourceImplTest2.class.getName()});
	}
}