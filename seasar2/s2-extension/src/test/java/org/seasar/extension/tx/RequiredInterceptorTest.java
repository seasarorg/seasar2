package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class RequiredInterceptorTest extends S2TestCase {

	private static final String PATH =
		"RequiredInterceptorTest.dicon";
	private TxBean txBean_;
	private ExceptionBean exBean_;
	private TransactionManager tm_;

	public RequiredInterceptorTest(String name) {
		super(name);
	}

	public void testInvoke() throws Exception {
		assertEquals("1", true, txBean_.hasTransaction());
		assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
	}
	
	public void testInvoke2() throws Exception {
		tm_.begin();
		assertEquals("1", true, txBean_.hasTransaction());
		assertEquals("2", Status.STATUS_ACTIVE, tm_.getStatus());
		tm_.commit();
	}
	
	public void testInvokeException() throws Exception {
		try {
			exBean_.invoke();
			fail("1");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
	}

	protected void setUp() throws Exception {
		include(PATH);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(RequiredInterceptorTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { RequiredInterceptorTest.class.getName()});
	}
}