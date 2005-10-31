package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.BooleanConversionUtil;

public class BooleanConversionUtilTest extends TestCase {

	public BooleanConversionUtilTest(String name) {
		super(name);
	}

	public void testToBoolean() throws Exception {
		assertEquals("1", Boolean.TRUE, BooleanConversionUtil.toBoolean(new Integer(1)));
		assertEquals("2", Boolean.FALSE, BooleanConversionUtil.toBoolean(new Integer(0)));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(BooleanConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { BooleanConversionUtilTest.class.getName() });
	}
}