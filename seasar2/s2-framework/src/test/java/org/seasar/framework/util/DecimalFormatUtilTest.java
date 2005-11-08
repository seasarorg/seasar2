package org.seasar.framework.util;

import java.util.Locale;

import junit.framework.TestCase;

public class DecimalFormatUtilTest extends TestCase {

    public void testNormalize() throws Exception {
        assertEquals("1", "1000.00", DecimalFormatUtil.normalize("1,000.00",
                Locale.JAPAN));
        assertEquals("2", "1000", DecimalFormatUtil.normalize("1,000",
                Locale.JAPAN));
        assertEquals("3", "1000.00", DecimalFormatUtil.normalize("1.000,00",
                Locale.GERMAN));
    }
}