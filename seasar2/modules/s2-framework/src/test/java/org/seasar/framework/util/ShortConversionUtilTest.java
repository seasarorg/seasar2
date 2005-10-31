package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.ShortConversionUtil;

public class ShortConversionUtilTest extends TestCase {

	public ShortConversionUtilTest(String name) {
		super(name);
	}
	
	public void testToShort() throws Exception {
		assertEquals("1", new Short("1000"), ShortConversionUtil.toShort("1,000"));
	}
	
	public void testToPrimitiveShort() throws Exception {
		assertEquals("1", 1000, ShortConversionUtil.toPrimitiveShort("1,000"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(ShortConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { ShortConversionUtilTest.class.getName() });
	}
}