package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.ByteConversionUtil;

public class ByteConversionUtilTest extends TestCase {

	public ByteConversionUtilTest(String name) {
		super(name);
	}
	
	public void testToByteConversionUtilTest() throws Exception {
		assertEquals("1", new Byte("100"), ByteConversionUtil.toByte("100"));
	}
	
	public void testToPrimitiveByteConversionUtilTest() throws Exception {
		assertEquals("1", 100, ByteConversionUtil.toPrimitiveByte("100"));
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