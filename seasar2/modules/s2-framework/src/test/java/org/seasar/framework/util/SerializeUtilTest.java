package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.SerializeUtil;

public class SerializeUtilTest extends TestCase {

	public SerializeUtilTest(String name) {
		super(name);
	}

	public void testSerialize() throws Exception {
		String[] a = new String[] { "1", "2" };
		String[] b = (String[]) SerializeUtil.serialize(a);
		assertEquals("1", b.length, a.length);
		assertEquals("2", "1", b[0]);
		assertEquals("3", "2", b[1]);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(SerializeUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { SerializeUtilTest.class.getName()});
	}
}
