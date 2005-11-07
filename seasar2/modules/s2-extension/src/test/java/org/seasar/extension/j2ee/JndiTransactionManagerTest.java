package org.seasar.extension.j2ee;

import org.seasar.extension.unit.S2TestCase;

public class JndiTransactionManagerTest extends S2TestCase {

	public JndiTransactionManagerTest(String arg0) {
		super(arg0);
	}

	public void testTransactionManager() throws Exception {
		assertNotNull("1", getContainer().getComponent("j2ee.transactionManager"));
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(JndiTransactionManagerTest.class);
	}

}
