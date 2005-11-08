package org.seasar.extension.tx;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

public class TxJ2EEConfigTest extends S2TestCase {

	private static final String PATH = "j2ee.dicon";

	public TxJ2EEConfigTest(String name) {
		super(name);
	}

	public void testConfig() throws Exception {
		assertNotNull("1", getComponent("j2ee.requiredTx"));
		assertNotNull("2", getComponent("j2ee.requiresNewTx"));
		assertNotNull("3", getComponent("j2ee.mandatoryTx"));
		assertNotNull("4", getComponent("j2ee.notSupportedTx"));
	}

	protected void setUp() throws Exception {
		include(PATH);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(TxJ2EEConfigTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { TxJ2EEConfigTest.class.getName()});
	}
}