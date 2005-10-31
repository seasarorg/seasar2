package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class RequiresNewInterceptorTest extends S2TestCase {

	private static final String PATH =
		"RequiresNewInterceptorTest.dicon";
	private TxBean txBean_;
	private ExceptionBean exBean_;
	private TransactionManager tm_;

	public RequiresNewInterceptorTest(String name) {
		super(name);
	}

	public void testInvoke() throws Exception {
		assertEquals("1", true, txBean_.hasTransaction());
		assertEquals("2", Status.STATUS_NO_TRANSACTION, tm_.getStatus());
	}
	
	public void testInvoke2() throws Exception {
		tm_.begin();
		Transaction tx = tm_.getTransaction();
		System.out.println("preTx:" + tx);
		assertEquals("1", true, txBean_.hasTransaction());
		assertEquals("2", Status.STATUS_ACTIVE, tm_.getStatus());
		assertEquals("3", tx, tm_.getTransaction());
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
		return new TestSuite(RequiresNewInterceptorTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { RequiresNewInterceptorTest.class.getName()});
	}
}