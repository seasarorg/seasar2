package org.seasar.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.DateConversionUtil;

public class DateConversionUtilTest extends TestCase {

	public DateConversionUtilTest(String name) {
		super(name);
	}

	public void testRemoveDelimiter() throws Exception {
		assertEquals("1", "yyyyMMdd", DateConversionUtil
				.removeDelimiter("yyyy/MM/dd"));
	}
	
	public void testGetDateFormat() throws Exception {
		SimpleDateFormat sdf = DateConversionUtil.getDateFormat("2004/11/7", Locale.JAPAN);
		assertEquals("1", "yyyy/MM/dd", sdf.toPattern());
	}
	
	public void testGetDateFormat2() throws Exception {
		SimpleDateFormat sdf = DateConversionUtil.getDateFormat("04/11/7", Locale.JAPAN);
		assertEquals("1", "yy/MM/dd", sdf.toPattern());
	}
	
	public void testGetDateFormat3() throws Exception {
		SimpleDateFormat sdf = DateConversionUtil.getDateFormat("20041107", Locale.JAPAN);
		assertEquals("1", "yyyyMMdd", sdf.toPattern());
	}
	
	public void testGetDateFormat4() throws Exception {
		SimpleDateFormat sdf = DateConversionUtil.getDateFormat("041107", Locale.JAPAN);
		assertEquals("1", "yyMMdd", sdf.toPattern());
	}
	
	public void testGetPattern() throws Exception {
		System.out.println(DateConversionUtil.getPattern(Locale.JAPAN));
	}
	
	public void testGetDefaultPattern() throws Exception {
		System.out.println(DateConversionUtil.getY4Pattern(Locale.JAPAN));
	}
	
	public void testSpike() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		System.out.println(formatter.parse("041115"));
	}
	
	public void testSpike2() throws Exception {
		SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
		System.out.println(formatter.toPattern());
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(DateConversionUtilTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { DateConversionUtilTest.class.getName() });
	}
}