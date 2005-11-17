package org.seasar.extension.jdbc.types;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class ValueTypesTest extends TestCase {

    public void testGetValueTypes() throws Exception {
        assertEquals("1", ValueTypes.TIMESTAMP, ValueTypes
                .getValueType(GregorianCalendar.class));
    }
}