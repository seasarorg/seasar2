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
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.MapConversion;

/**
 * @author koichik
 * 
 */
public class TigerAnnotationReaderTest extends TestCase {

    AnnotationReader reader;

    protected void setUp() throws Exception {
        super.setUp();
        reader = new AnnotationReaderFactoryImpl().getAnnotationReader();
    }

    public void testGetDateFormat() throws Exception {
        assertEquals("class default", reader.getDatePattern(Dxo.class,
                Dxo.class.getMethod("converted", new Class[] { List.class })));
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

    public void testGetMapConversion() throws Exception {
        assertEquals("map1", reader.getMapConversion(Dxo.class, Dxo.class
                .getMethod("convert", new Class[] { Integer.class })));
        assertEquals("map2", reader
                .getMapConversion(Dxo.class, Dxo.class.getMethod("convert",
                        new Class[] { Integer.class, Map.class })));
        assertNull(reader.getMapConversion(Dxo.class, Dxo.class.getMethod(
                "convert", new Class[] { Integer[].class })));
    }

    @DatePattern("class default")
    public interface Dxo {

        List converted(List src);

        @DatePattern("foo")
        Object convert(Object src);

        @DatePattern("bar")
        void convert(Number src, String date);

        @DatePattern("baz")
        Object[] convert(Object[] src);

        @MapConversion("map1")
        Map convert(Integer src);

        @MapConversion("map2")
        void convert(Integer src, Map dest);

        Map[] convert(Integer[] src);
    }

    public interface Dxo2 {
        Object convert(Object src);
    }
}
