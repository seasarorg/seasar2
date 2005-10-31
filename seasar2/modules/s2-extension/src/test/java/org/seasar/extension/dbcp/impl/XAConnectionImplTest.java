package org.seasar.extension.dbcp.impl;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class XAConnectionImplTest extends S2TestCase {

	private static final String PATH = "connection.dicon";
	private XADataSource xads2_;

	public XAConnectionImplTest(String name) {
		super(name);
	}

	public void testGetConnectionAndGetXAResource() throws Exception {
		XAConnection xaCon = null;
		try {
			xaCon = xads2_.getXAConnection();
			assertNotNull("1", xaCon.getConnection());
			assertNotNull("2", xaCon.getXAResource());
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
		return new TestSuite(XAConnectionImplTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { XAConnectionImplTest.class.getName()});
	}
}