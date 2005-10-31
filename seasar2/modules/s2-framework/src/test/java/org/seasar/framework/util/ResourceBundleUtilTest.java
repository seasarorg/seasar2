package org.seasar.framework.util;

import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.ResourceBundleUtil;

public class ResourceBundleUtilTest extends TestCase {

	public ResourceBundleUtilTest(String name) {
		super(name);
	}

	public void testConvertMap() throws Exception {
		ResourceBundle bundle = ResourceBundleUtil.getBundle("SSRMessages", null);
		Map map = ResourceBundleUtil.convertMap(bundle);
		String value = (String) map.get("ESSR0001");
		System.out.println(value);
		assertNotNull("1", value);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(ResourceBundleUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { ResourceBundleUtilTest.class.getName()});
	}
}