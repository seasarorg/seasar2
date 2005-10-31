package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.FloatConversionUtil;

public class FloatConversionUtilTest extends TestCase {

	public FloatConversionUtilTest(String name) {
		super(name);
	}
	
	public void testToFloat() throws Exception {
		assertEquals("1", new Float("1000.5"), FloatConversionUtil.toFloat("1,000.5"));
	}
	
	public void testToPrimitiveFloat() throws Exception {
		assertEquals("1", 1000.5, FloatConversionUtil.toPrimitiveFloat("1,000.5"), 0);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(FloatConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { FloatConversionUtilTest.class.getName() });
	}
}