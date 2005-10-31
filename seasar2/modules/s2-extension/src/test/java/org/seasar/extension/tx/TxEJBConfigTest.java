package org.seasar.extension.tx;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class TxEJBConfigTest extends S2TestCase {

	private static final String PATH = "ejbtx.dicon";

	public TxEJBConfigTest(String name) {
		super(name);
	}

	public void testConfig() throws Exception {
		assertNotNull("1", getComponent("ejbtx.requiredTx"));
		assertNotNull("2", getComponent("ejbtx.requiresNewTx"));
		assertNotNull("3", getComponent("ejbtx.mandatoryTx"));
		assertNotNull("4", getComponent("ejbtx.notSupportedTx"));
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
