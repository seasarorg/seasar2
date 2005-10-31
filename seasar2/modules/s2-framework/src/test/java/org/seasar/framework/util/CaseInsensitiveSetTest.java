package org.seasar.framework.util;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.CaseInsensitiveSet;

public class CaseInsensitiveSetTest extends TestCase {

	public CaseInsensitiveSetTest(String name) {
		super(name);
	}

	public void testContains() throws Exception {
		Set set = new CaseInsensitiveSet();
		set.add("one");
		assertEquals("1", true, set.contains("ONE"));
	}

	public static Test suite() {
		return new TestSuite(CaseInsensitiveSetTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { CaseInsensitiveSetTest.class.getName()});
	}
}
