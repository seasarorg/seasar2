package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.IntegerConversionUtil;

public class IntegerConversionUtilTest extends TestCase {

	public IntegerConversionUtilTest(String name) {
		super(name);
	}
	
	public void testToInteger() throws Exception {
		assertEquals("1", new Integer("1000"), IntegerConversionUtil.toInteger("1,000"));
	}
	
	public void testToPrimitiveInt() throws Exception {
		assertEquals("1", 1000, IntegerConversionUtil.toPrimitiveInt("1,000"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(IntegerConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { IntegerConversionUtilTest.class.getName() });
	}
}