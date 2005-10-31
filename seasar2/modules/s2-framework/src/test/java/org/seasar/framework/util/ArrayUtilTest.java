package org.seasar.framework.util;

import org.seasar.framework.util.ArrayUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ArrayUtilTest extends TestCase {

	public ArrayUtilTest(String name) {
		super(name);
	}

	public void testAdd() throws Exception {
		String[] array = new String[]{"111"};
		String[] newArray = (String[]) ArrayUtil.add(array, "222");
		assertEquals("1", 2, newArray.length);
		assertEquals("2", "111", newArray[0]);
		assertEquals("3", "222", newArray[1]);
	}
	
	public void testAdd2() throws Exception {
        String[] a = new String[]{"1", "2"};
        String[] b = new String[]{"3"};
        a = (String[]) ArrayUtil.add(a, b);
        assertEquals("1", 3, a.length);
        assertEquals("2", "1", a[0]);
        assertEquals("3", "2", a[1]);
        assertEquals("4", "3", a[2]);
    }
	
	public void testIndexOf() throws Exception {
		String[] array = new String[]{"111", "222", "333"};
		assertEquals("1", 1, ArrayUtil.indexOf(array, "222"));
		assertEquals("2", -1, ArrayUtil.indexOf(array, new Object()));
		assertEquals("3", -1, ArrayUtil.indexOf(array, null));
		array[1] = null;
		assertEquals("4", 1, ArrayUtil.indexOf(array, null));
	}
	
	public void testRemoveFirst() throws Exception {
		String[] array = new String[]{"111", "222", "333"};
		String[] newArray = (String[]) ArrayUtil.remove(array, "111");
		assertEquals("1", 2, newArray.length);
		assertEquals("2", "222", newArray[0]);
		assertEquals("3", "333", newArray[1]);
	}
	
	public void testRemoveMiddle() throws Exception {
		String[] array = new String[]{"111", "222", "333"};
		String[] newArray = (String[]) ArrayUtil.remove(array, "222");
		assertEquals("1", 2, newArray.length);
		assertEquals("2", "111", newArray[0]);
		assertEquals("3", "333", newArray[1]);
	}
	
	public void testRemoveLast() throws Exception {
		String[] array = new String[]{"111", "222", "333"};
		String[] newArray = (String[]) ArrayUtil.remove(array, "333");
		assertEquals("1", 2, newArray.length);
		assertEquals("2", "111", newArray[0]);
		assertEquals("3", "222", newArray[1]);
	}
	
	public void testRemoveNothing() throws Exception {
		String[] array = new String[]{"111", "222", "333"};
		String[] newArray = (String[]) ArrayUtil.remove(array, "444");
		assertSame("1", array, newArray);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(ArrayUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(new String[] { ArrayUtilTest.class
				.getName() });
	}
}