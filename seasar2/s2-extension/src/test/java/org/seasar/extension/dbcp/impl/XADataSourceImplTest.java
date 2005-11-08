package org.seasar.extension.dbcp.impl;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class XADataSourceImplTest extends S2TestCase {

	private static final String PATH = "connection.dicon";
	private XADataSource xads2_;

	public XADataSourceImplTest(String name) {
		super(name);
	}

	public void testGetXAConnection() throws Exception {
		XAConnection xaCon = null;
		try {
			xaCon = xads2_.getXAConnection();
			assertNotNull("1", xaCon);
		} finally {
			xaCon.close();
		}
	}

	protected void setUp() throws Exception {
		include(PATH);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(XADataSourceImplTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { XADataSourceImplTest.class.getName()});
	}
}