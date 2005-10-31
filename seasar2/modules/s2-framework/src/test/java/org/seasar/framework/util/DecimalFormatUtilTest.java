package org.seasar.framework.util;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.DecimalFormatUtil;

public class DecimalFormatUtilTest extends TestCase {

	public DecimalFormatUtilTest(String name) {
		super(name);
	}

	public void testNormalize() throws Exception {
		assertEquals("1", "1000.00", DecimalFormatUtil.normalize("1,000.00", Locale.JAPAN));
		assertEquals("2", "1000", DecimalFormatUtil.normalize("1,000", Locale.JAPAN));
		assertEquals("3", "1000.00", DecimalFormatUtil.normalize("1.000,00", Locale.GERMAN));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DecimalFormatUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { DecimalFormatUtilTest.class.getName() });
	}
}