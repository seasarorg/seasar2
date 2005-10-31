package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.DoubleConversionUtil;

public class DoubleConversionUtilTest extends TestCase {

	public DoubleConversionUtilTest(String name) {
		super(name);
	}

	public void testToDouble() throws Exception {
		assertEquals("1", new Double("1000.5"), DoubleConversionUtil
				.toDouble("1,000.5"));
	}

	public void testToPrimitiveDouble() throws Exception {
		assertEquals("1", 1000.5, DoubleConversionUtil
				.toPrimitiveDouble("1,000.5"), 0);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DoubleConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { DoubleConversionUtilTest.class.getName() });
	}
}