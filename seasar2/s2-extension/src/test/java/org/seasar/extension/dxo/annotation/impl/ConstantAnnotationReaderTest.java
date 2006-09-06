/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.dxo.annotation.impl;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dxo.annotation.AnnotationReader;

/**
 * @author koichik
 * 
 */
public class ConstantAnnotationReaderTest extends TestCase {

    AnnotationReader reader;

    protected void setUp() throws Exception {
        super.setUp();
        reader = new AnnotationReaderFactoryImpl().getAnnotationReader();
    }

    public void testGetDateFormat() throws Exception {
        assertEquals("class default", reader.getDatePattern(Dxo.class,
                Dxo.class.getMethod("converted", new Class[] { List.class })));
        assertEquals("method default", reader.getDatePattern(Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { List.class })));
        assertEquals("foo", reader.getDatePattern(Dxo.class, Dxo.class
                .getMethod("convert", new Class[] { Object.class })));
        assertEquals("bar", reader.getDatePattern(Dxo.class, Dxo.class
                .getMethod("convert",
                        new Class[] { Number.class, String.class })));
        assertEquals("baz", reader.getDatePattern(Dxo.class, Dxo.class
                .getMethod("convert", new Class[] { Object[].class })));
        assertNull(reader.getDatePattern(Dxo2.class, Dxo2.class.getMethod(
                "convert", new Class[] { Object.class })));
    }

    public void testGetConversionRule() throws Exception {
        assertEquals("map1", reader.getConversionRule(Dxo.class, Dxo.class
                .getMethod("convert", new Class[] { Integer.class })));
        assertEquals("map2", reader
                .getConversionRule(Dxo.class, Dxo.class.getMethod("convert",
                        new Class[] { Integer.class, Map.class })));
        assertNull(reader.getConversionRule(Dxo.class, Dxo.class.getMethod(
                "convert", new Class[] { Integer[].class })));
    }

    public interface Dxo {
        String DATE_PATTERN = "class default";

        List converted(List src);

        String convert_DATE_PATTERN = "method default";

        List convert(List src);

        String convert_Object_DATE_PATTERN = "foo";

        Object convert(Object src);

        String convert_Number_String_DATE_PATTERN = "bar";

        void convert(Number src, String date);

        String convert_Object$_DATE_PATTERN = "baz";

        Object[] convert(Object[] src);

        String convert_Integer_CONVERSION_RULE = "map1";

        Map convert(Integer src);

        String convert_Integer_Map_CONVERSION_RULE = "map2";

        void convert(Integer src, Map dest);

        Map[] convert(Integer[] src);
    }

    public interface Dxo2 {
        Object convert(Object src);
    }
}
