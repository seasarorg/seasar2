package org.seasar.framework.util;

import junit.framework.*;
import java.math.*;

import org.seasar.framework.util.MathUtil;

public class MathUtilTest extends TestCase {

	public MathUtilTest(String name) {
		super(name);
	}

	public void testAdd() throws Exception {
		assertEquals(
			"1",
			new Integer(3),
			MathUtil.add(new Integer(1), new Integer(2)));
		assertEquals(
			"2",
			new Long(12345678902L),
			MathUtil.add(new Integer(1), new Long(12345678901L)));
		assertEquals(
			"3",
			new BigDecimal(2),
			MathUtil.add(new Integer(1), new BigDecimal(1)));
		assertEquals(
			"4",
			new Long(60000000000L),
			MathUtil.add(new Long(30000000000L), new Long(30000000000L)));
		assertEquals(
			"5",
			new Long(30000000002L),
			MathUtil.add(new Long(30000000000L), new Integer(2)));
		assertEquals(
			"6",
			new BigDecimal(30000000002L),
			MathUtil.add(new Long(30000000000L), new BigDecimal(2)));
		assertEquals(
			"7",
			new BigDecimal(3),
			MathUtil.add(new BigDecimal(1), new BigDecimal(2)));
		assertEquals(
			"8",
			new BigDecimal(3),
			MathUtil.add(new BigDecimal(1), new Integer(2)));
		assertEquals(
			"9",
			new BigDecimal(30000000001L),
			MathUtil.add(new BigDecimal(1), new Long(30000000000L)));
		assertEquals("10", null, MathUtil.add(null, new Integer(2)));
		assertEquals(
			"11",
			new BigDecimal(3),
			MathUtil.add(new Float(1), new Integer(2)));
	}

	public void testSubtract() throws Exception {
		assertEquals(
			"1",
			new Integer(-1),
			MathUtil.subtract(new Integer(1), new Integer(2)));
		assertEquals(
			"2",
			new Long(12345678900L),
			MathUtil.subtract(new Long(12345678901L), new Integer(1)));
		assertEquals(
			"3",
			new BigDecimal(2),
			MathUtil.subtract(new Integer(3), new BigDecimal(1)));
		assertEquals(
			"4",
			new Long(1),
			MathUtil.subtract(new Long(30000000001L), new Long(30000000000L)));
		assertEquals(
			"5",
			new Long(30000000000L),
			MathUtil.subtract(new Long(30000000001L), new Integer(1)));
		assertEquals(
			"6",
			new BigDecimal(30000000000L),
			MathUtil.subtract(new Long(30000000001L), new BigDecimal(1)));
		assertEquals(
			"7",
			new BigDecimal(1),
			MathUtil.subtract(new BigDecimal(3), new BigDecimal(2)));
		assertEquals(
			"8",
			new BigDecimal(-1),
			MathUtil.subtract(new BigDecimal(1), new Integer(2)));
		assertEquals(
			"9",
			new BigDecimal(1),
			MathUtil.subtract(
				new BigDecimal(30000000001L),
				new Long(30000000000L)));
		assertEquals("10", null, MathUtil.subtract(null, new Integer(2)));
		assertEquals(
			"11",
			new BigDecimal(-1),
			MathUtil.subtract(new Short((short) 1), new Integer(2)));
	}

	public void testMultiply() throws Exception {
		assertEquals(
			"1",
			new Integer(6),
			MathUtil.multiply(new Integer(2), new Integer(3)));
		assertEquals(
			"2",
			new Long(60000000000L),
			MathUtil.multiply(new Long(30000000000L), new Integer(2)));
		assertEquals(
			"3",
			new BigDecimal(6),
			MathUtil.multiply(new Integer(3), new BigDecimal(2)));
		assertEquals(
			"4",
			new Long(6000000000L),
			MathUtil.multiply(new Long(3000000000L), new Long(2)));
		assertEquals(
			"5",
			new Long(6000000000L),
			MathUtil.multiply(new Long(2), new Long(3000000000L)));
		assertEquals(
			"6",
			new Long(30000000000L),
			MathUtil.multiply(new Long(30000000000L), new Integer(1)));
		assertEquals(
			"7",
			new BigDecimal(30000000000L),
			MathUtil.multiply(new Long(30000000000L), new BigDecimal(1)));
		assertEquals(
			"8",
			new BigDecimal(6),
			MathUtil.multiply(new BigDecimal(3), new BigDecimal(2)));
		assertEquals(
			"9",
			new BigDecimal(6),
			MathUtil.multiply(new BigDecimal(3), new Integer(2)));
		assertEquals(
			"10",
			new BigDecimal(30000000000L),
			MathUtil.multiply(new BigDecimal(1), new Long(30000000000L)));
		assertEquals("11", null, MathUtil.multiply(null, new Integer(2)));
		assertEquals(
			"11",
			new BigDecimal(6),
			MathUtil.multiply(new Short((short) 3), new Integer(2)));
	}

	public void testDivide() throws Exception {
		assertEquals(
			"1",
			new Integer(1),
			MathUtil.divide(new Integer(3), new Integer(2)));
		assertEquals(
			"2",
			new Integer(3),
			MathUtil.divide(new Integer(6), new Integer(2)));
		assertEquals(
			"3",
			new Long(30000000000L),
			MathUtil.divide(new Long(60000000000L), new Integer(2)));
		assertEquals(
			"4",
			new BigDecimal(3),
			MathUtil.divide(new Integer(6), new Double(2)));
		assertEquals(
			"5",
			new BigDecimal(2.5),
			MathUtil.divide(new Integer(5), new BigDecimal(2)));
		assertEquals(
			"6",
			new Long(2),
			MathUtil.divide(new Long(6000000000L), new Long(3000000000L)));
		assertEquals(
			"7",
			new Long(30000000000L),
			MathUtil.divide(new Long(30000000000L), new Integer(1)));
		assertEquals(
			"8",
			new BigDecimal(30000000000L),
			MathUtil.divide(new Long(30000000000L), new BigDecimal(1)));
		assertEquals(
			"9",
			new BigDecimal(1.5),
			MathUtil.divide(new BigDecimal(3), new BigDecimal(2)));
		assertEquals(
			"10",
			new BigDecimal(1.5),
			MathUtil.divide(new BigDecimal(3), new Integer(2)));
		assertEquals(
			"11",
			new BigDecimal(1),
			MathUtil.divide(new BigDecimal(30000000000L), new Long(30000000000L)));
		assertEquals("12", null, MathUtil.divide(null, new Integer(2)));
		assertEquals(
			"12",
			new BigDecimal(1.5),
			MathUtil.divide(new Short((short) 3), new Integer(2)));
	}

	public void testMod() throws Exception {
		assertEquals(
			"1",
			new Integer(1),
			MathUtil.mod(new Integer(5), new Integer(2)));
		assertEquals("2", new Long(1), MathUtil.mod(new Long(5), new Long(2)));
		assertEquals(
			"3",
			new BigDecimal(1),
			MathUtil.mod(new Double(5), new Double(2)));
		assertEquals("4", null, MathUtil.mod(null, new Integer(2)));
		assertEquals(
			"5",
			new Long(1),
			MathUtil.mod(new Integer(1), new Long(2)));
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MathUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { MathUtilTest.class.getName()});
	}
}
