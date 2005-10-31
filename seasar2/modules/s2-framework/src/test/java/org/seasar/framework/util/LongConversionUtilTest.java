package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.LongConversionUtil;

public class LongConversionUtilTest extends TestCase {

	public LongConversionUtilTest(String name) {
		super(name);
	}
	
	public void testToLong() throws Exception {
		assertEquals("1", new Long("1000"), LongConversionUtil.toLong("1,000"));
	}
	
	public void testToPrimitiveLong() throws Exception {
		assertEquals("1", 1000, LongConversionUtil.toPrimitiveLong("1,000"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(LongConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { LongConversionUtilTest.class.getName() });
	}
}