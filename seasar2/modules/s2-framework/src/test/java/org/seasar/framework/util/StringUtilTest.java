package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.StringUtil;

public class StringUtilTest extends TestCase {

	public StringUtilTest(String name) {
		super(name);
	}

	public void testReplace() throws Exception {
		assertEquals("1", "1bc45", StringUtil.replace("12345", "23", "bc"));
		assertEquals("2", "1234ef", StringUtil.replace("12345", "5", "ef"));
		assertEquals("3", "ab2345", StringUtil.replace("12345", "1", "ab"));
		assertEquals("4", "a234a", StringUtil.replace("12341", "1", "a"));
		assertEquals(
			"5",
			"ab234abab234ab",
			StringUtil.replace("1234112341", "1", "ab"));
		assertEquals("6", "a\\nb", StringUtil.replace("a\nb", "\n", "\\n"));
	}

	public void testSplit() throws Exception {
		String[] array = StringUtil.split("aaa\nbbb", "\n");
		assertEquals("1", 2, array.length);
		assertEquals("2", "aaa", array[0]);
		assertEquals("3", "bbb", array[1]);
	}
	
	public void testLtrim() throws Exception {
		assertEquals("1", "trim", StringUtil.ltrim("zzzytrim", "xyz"));
		assertEquals("2", "", StringUtil.ltrim("xyz", "xyz"));
	}

	public void testRtrim() throws Exception {
		assertEquals("1", "trim", StringUtil.rtrim("trimxxxx", "x"));
		assertEquals("2", "", StringUtil.rtrim("xyz", "xyz"));
		assertEquals("1", "trimxxxx", StringUtil.rtrim("trimxxxx", "y"));
	}
	
	public void testStartsWith() throws Exception {
		assertEquals("1", true, StringUtil.startsWith("abcdef", "ABC"));
		assertEquals("2", false, StringUtil.startsWith("ab", "ABC"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(StringUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { StringUtilTest.class.getName()});
	}
}
