package org.seasar.framework.util;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.DecimalFormatSymbolsUtil;

public class DecimalFormatSymbolsUtilTest extends TestCase {

	public DecimalFormatSymbolsUtilTest(String name) {
		super(name);
	}

	public void testGetDecimalFormatSymbols() throws Exception {
		DecimalFormatSymbols symbols = DecimalFormatSymbolsUtil.getDecimalFormatSymbols(Locale.GERMAN);
		System.out.println("DecimalSeparator:" + symbols.getDecimalSeparator());
		System.out.println("GroupingSeparator:" + symbols.getGroupingSeparator());
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DecimalFormatSymbolsUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { DecimalFormatSymbolsUtilTest.class.getName() });
	}
}