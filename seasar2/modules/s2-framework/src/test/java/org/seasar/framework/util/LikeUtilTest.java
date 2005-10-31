package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.LikeUtil;

public class LikeUtilTest extends TestCase {

	public LikeUtilTest(String name) {
		super(name);
	}

	public void testMatch() throws Exception {
		assertEquals("1", true, LikeUtil.match("SCOTT", "SCOTT"));
		assertEquals("2", true, LikeUtil.match("_COT_", "SCOTT"));
		assertEquals("3", true, LikeUtil.match("SC%", "SCOTT"));
		assertEquals("4", true, LikeUtil.match("SC%T", "SCOTT"));
		assertEquals("5", true, LikeUtil.match("%TT", "SCOTT"));
		assertEquals("6", true, LikeUtil.match("S_O%T", "SCOTT"));
		assertEquals("7", false, LikeUtil.match("COTT", "SCOTT"));
		assertEquals("8", false, LikeUtil.match("_COT", "SCOTT"));
		assertEquals("9", false, LikeUtil.match("SC%A", "SCOTT"));
		assertEquals("10", false, LikeUtil.match("%OT", "SCOTT"));
		assertEquals("11", true, LikeUtil.match("SCOTT%", "SCOTT"));
		assertEquals("12", false, LikeUtil.match("SCOTT_", "SCOTT"));
		assertEquals("13", true, LikeUtil.match("%SCOTT", "SCOTT"));
		assertEquals("14", true, LikeUtil.match("%SCOTT%", "SCOTT"));
		assertEquals("15", true, LikeUtil.match("S%", "SCOTT"));
		assertEquals("16", true, LikeUtil.match("%abc%abc", "xxxabcyyyabc"));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(LikeUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { LikeUtilTest.class.getName()});
	}
}