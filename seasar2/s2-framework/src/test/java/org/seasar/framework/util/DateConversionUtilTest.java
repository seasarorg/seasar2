package org.seasar.framework.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import junit.framework.TestCase;

public class DateConversionUtilTest extends TestCase {

    public void testRemoveDelimiter() throws Exception {
        assertEquals("1", "yyyyMMdd", DateConversionUtil
                .removeDelimiter("yyyy/MM/dd"));
    }

    public void testGetDateFormat() throws Exception {
        SimpleDateFormat sdf = DateConversionUtil.getDateFormat("2004/11/7",
                Locale.JAPAN);
        assertEquals("1", "yyyy/MM/dd", sdf.toPattern());
    }

    public void testGetDateFormat2() throws Exception {
        SimpleDateFormat sdf = DateConversionUtil.getDateFormat("04/11/7",
                Locale.JAPAN);
        assertEquals("1", "yy/MM/dd", sdf.toPattern());
    }

    public void testGetDateFormat3() throws Exception {
        SimpleDateFormat sdf = DateConversionUtil.getDateFormat("20041107",
                Locale.JAPAN);
        assertEquals("1", "yyyyMMdd", sdf.toPattern());
    }

    public void testGetDateFormat4() throws Exception {
        SimpleDateFormat sdf = DateConversionUtil.getDateFormat("041107",
                Locale.JAPAN);
        assertEquals("1", "yyMMdd", sdf.toPattern());
    }

    public void testGetPattern() throws Exception {
        System.out.println(DateConversionUtil.getPattern(Locale.JAPAN));
    }

    public void testGetDefaultPattern() throws Exception {
        System.out.println(DateConversionUtil.getY4Pattern(Locale.JAPAN));
    }
}