package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class MandatoryInterceptorTest extends S2TestCase {

	private static final String PATH =
		"MandatoryInterceptorTest.dicon";
	private TxBean txBean_;
	private TransactionManager tm_;

	public MandatoryInterceptorTest(String name) {
		super(name);
	}

	public void testInvoke() throws Exception {
		try {
			txBean_.hasTransaction();
			fail("1");
		} catch (IllegalStateException ex) {
			System.out.println(ex);
		}
	}
	
	public void testInvoke2() throws Exception {
		tm_.begin();
		assertEquals("1", true, txBean_.hasTransaction());
		assertEquals("2", Status.STATUS_ACTIVE, tm_.getStatus());
		tm_.commit();
	}

	protected void setUp() throws Exception {
		include(PATH);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MandatoryInterceptorTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { MandatoryInterceptorTest.class.getName()});
	}
}