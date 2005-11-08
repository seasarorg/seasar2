package org.seasar.framework.util;

import junit.framework.TestCase;

import org.seasar.framework.exception.NoSuchConstructorRuntimeException;
import org.seasar.framework.exception.NoSuchFieldRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 *
 */
public class ClassUtilTest extends TestCase {

	public static final String HOGE = "hoge";

	public void testGetPrimitiveClass() {
		assertEquals(
			"1",
			int.class,
			ClassUtil.getPrimitiveClass(Integer.class));
		assertEquals("2", null, ClassUtil.getPrimitiveClass(String.class));
		assertEquals(
			"3",
			byte.class,
			ClassUtil.getPrimitiveClass(Byte.class));
	}

	public void testGetPrimitiveClassIfWrapper() {
		assertEquals(
			"1",
			int.class,
			ClassUtil.getPrimitiveClassIfWrapper(Integer.class));
		assertEquals(
			"2",
			String.class,
			ClassUtil.getPrimitiveClassIfWrapper(String.class));
		assertEquals(
			"3",
			byte.class,
			ClassUtil.getPrimitiveClassIfWrapper(Byte.class));
	}

	public void testGetWrapperClass() {
		assertEquals(
			"1",
			Integer.class,
			ClassUtil.getWrapperClass(int.class));
		assertEquals("2", null, ClassUtil.getWrapperClass(String.class));
		assertEquals(
			"3",
			Byte.class,
			ClassUtil.getWrapperClass(byte.class));
	}

	public void testGetWrapperClassIfWrapper() {
		assertEquals(
			"1",
			Integer.class,
			ClassUtil.getWrapperClassIfPrimitive(int.class));
		assertEquals(
			"2",
			String.class,
			ClassUtil.getWrapperClassIfPrimitive(String.class));
		assertEquals(
			"3",
			Byte.class,
			ClassUtil.getWrapperClassIfPrimitive(byte.class));
	}

	public void testIsAssignableFrom() {
		assertEquals(
			"1",
			true,
			ClassUtil.isAssignableFrom(Number.class, Integer.class));
		assertEquals(
			"2",
			false,
			ClassUtil.isAssignableFrom(Integer.class, Number.class));
		assertEquals(
			"3",
			true,
			ClassUtil.isAssignableFrom(int.class, Integer.class));
	}

	public void testGetShortClassName() {
		assertEquals(
			"1",
			"ClassUtilTest",
			ClassUtil.getShortClassName(getClass()));
	}
	
	public void testGetConstructor() {
		try {
			ClassUtil.getConstructor(ClassUtilTest.class, new Class[]{Integer.class});
			fail("1");
		} catch (NoSuchConstructorRuntimeException ex) {
			System.out.println(ex);
		}
	}
	
	public void testGetField() {
		try {
			ClassUtil.getField(getClass(), "aaa");
		} catch (NoSuchFieldRuntimeException ex) {
			System.out.println(ex);
		}
	}

	public void testGetSimpleClassName() {
	    assertEquals("1", "int", ClassUtil.getSimpleClassName(int.class));
	    assertEquals("2", "java.lang.String", ClassUtil.getSimpleClassName(String.class));
	    assertEquals("3", "int[]", ClassUtil.getSimpleClassName(int[].class));
	    assertEquals("4", "java.lang.String[][]", ClassUtil.getSimpleClassName(String[][].class));
	}
    
    public void testConcatName() {
        assertEquals("1", "aaa.bbb", ClassUtil.concatName("aaa", "bbb"));
        assertEquals("2", "bbb", ClassUtil.concatName(null, "bbb"));
    }
    
    public void testGetResourcePath() {
        assertEquals("1", "org/seasar/framework/util/ClassUtilTest.class", ClassUtil.getResourcePath(getClass()));
    }
}
