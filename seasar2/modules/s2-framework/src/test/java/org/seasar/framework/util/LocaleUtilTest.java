package org.seasar.framework.util;

import java.util.Locale;

import org.seasar.framework.util.LocaleUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LocaleUtilTest extends TestCase {

	public LocaleUtilTest(String name) {
		super(name);
	}

	public void testGetLocale() throws Exception {
		assertEquals("1", Locale.getDefault(), LocaleUtil.getLocale(null));
		assertEquals("2", Locale.JAPANESE, LocaleUtil.getLocale("ja"));
		assertEquals("3", Locale.JAPAN, LocaleUtil.getLocale("ja_JP"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(LocaleUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { LocaleUtilTest.class.getName()});
	}
}