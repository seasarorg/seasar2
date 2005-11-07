package org.seasar.framework.util;

import java.util.Locale;

import junit.framework.TestCase;

public class LocaleUtilTest extends TestCase {

    public void testGetLocale() throws Exception {
        assertEquals("1", Locale.getDefault(), LocaleUtil.getLocale(null));
        assertEquals("2", Locale.JAPANESE, LocaleUtil.getLocale("ja"));
        assertEquals("3", Locale.JAPAN, LocaleUtil.getLocale("ja_JP"));
    }
}