package org.seasar.framework.util;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import junit.framework.TestCase;

public class DecimalFormatSymbolsUtilTest extends TestCase {

    public void testGetDecimalFormatSymbols() throws Exception {
        DecimalFormatSymbols symbols = DecimalFormatSymbolsUtil
                .getDecimalFormatSymbols(Locale.GERMAN);
        System.out.println("DecimalSeparator:" + symbols.getDecimalSeparator());
        System.out.println("GroupingSeparator:"
                + symbols.getGroupingSeparator());
    }
}