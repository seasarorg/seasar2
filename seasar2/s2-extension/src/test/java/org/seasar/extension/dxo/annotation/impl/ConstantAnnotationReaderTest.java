/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.impl.AbstractConverter;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class ConstantAnnotationReaderTest extends S2FrameworkTestCase {

    AnnotationReaderFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetDateFormat() throws Exception {
        assertEquals("class default", factory.getAnnotationReader()
                .getDatePattern(
                        Dxo.class,
                        Dxo.class.getMethod("converted",
                                new Class[] { List.class })));
        assertEquals("method default", factory.getAnnotationReader()
                .getDatePattern(
                        Dxo.class,
                        Dxo.class.getMethod("convert",
                                new Class[] { List.class })));
        assertEquals("foo", factory.getAnnotationReader().getDatePattern(
                Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { Object.class })));
        assertEquals("bar", factory.getAnnotationReader().getDatePattern(
                Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { Number.class,
                        String.class })));
        assertEquals("baz", factory.getAnnotationReader().getDatePattern(
                Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { Object[].class })));
        assertNull(factory.getAnnotationReader().getDatePattern(Dxo2.class,
                Dxo2.class.getMethod("convert", new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    public void testGetConversionRule() throws Exception {
        assertEquals("map1", factory.getAnnotationReader().getConversionRule(
                Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { Integer.class })));
        assertEquals("map2", factory.getAnnotationReader().getConversionRule(
                Dxo.class,
                Dxo.class.getMethod("convert", new Class[] { Integer.class,
                        Map.class })));
        assertNull(factory.getAnnotationReader()
                .getConversionRule(
                        Dxo.class,
                        Dxo.class.getMethod("convert",
                                new Class[] { Integer[].class })));
    }

    /**
     * @throws Exception
     */
    public void testGetConverters() throws Exception {
        Map converters = factory.getAnnotationReader()
                .getConverters(Hoge.class);
        assertNotNull(converters);
        assertEquals(2, converters.size());

        Converter converter = (Converter) converters.get("foo");
        assertNotNull(converter);
        assertEquals("foo", converter.convert("foo", String.class, null));

        converter = (Converter) converters.get("bar");
        assertNotNull(converter);
        assertEquals("a_bar_b", converter.convert("bar", String.class, null));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePrefix() throws Exception {
        assertEquals("src", factory.getAnnotationReader().getSourcePrefix(
                Dxo3.class,
                Dxo3.class.getMethod("convert", new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    public void testGetDestPrefix() throws Exception {
        assertEquals("dest", factory.getAnnotationReader().getDestPrefix(
                Dxo3.class,
                Dxo3.class.getMethod("convert", new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    public void testGetExcludeNull() throws Exception {
        assertTrue(factory.getAnnotationReader().isExcludeNull(
                Dxo.class,
                Dxo.class.getMethod("convertExNull",
                        new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    public void testGetExcludeWhitespace() throws Exception {
        assertTrue(factory.getAnnotationReader()
                .isExcludeWhitespace(
                        Dxo.class,
                        Dxo.class.getMethod("convertExWs",
                                new Class[] { Object.class })));
    }

    /**
     * 
     */
    public interface Dxo {
        /**
         * 
         */
        String DATE_PATTERN = "class default";

        /**
         * @param src
         * @return
         */
        List converted(List src);

        /**
         * 
         */
        String convert_DATE_PATTERN = "method default";

        /**
         * @param src
         * @return
         */
        List convert(List src);

        /**
         * 
         */
        String convert_Object_DATE_PATTERN = "foo";

        /**
         * @param src
         * @return
         */
        Object convert(Object src);

        /**
         * 
         */
        String convert_Number_String_DATE_PATTERN = "bar";

        /**
         * @param src
         * @param date
         */
        void convert(Number src, String date);

        /**
         * 
         */
        String convert_Object$_DATE_PATTERN = "baz";

        /**
         * @param src
         * @return
         */
        Object[] convert(Object[] src);

        /**
         * 
         */
        String convert_Integer_CONVERSION_RULE = "map1";

        /**
         * @param src
         * @return
         */
        Map convert(Integer src);

        /**
         * 
         */
        String convert_Integer_Map_CONVERSION_RULE = "map2";

        /**
         * @param src
         * @param dest
         */
        void convert(Integer src, Map dest);

        /**
         * @param src
         * @return
         */
        Map[] convert(Integer[] src);

        /**
         * 
         */
        String convertExNull_EXCLUDE_NULL = null;

        /**
         * @param src
         * @return
         */
        Object convertExNull(Object src);

        /**
         * 
         */
        String convertExWs_EXCLUDE_WHITESPACE = null;

        /**
         * @param src
         * @return
         */
        Object convertExWs(Object src);

    }

    /**
     * 
     */
    public interface Dxo2 {
        /**
         * @param src
         * @return
         */
        Object convert(Object src);
    }

    /**
     * 
     * 
     */
    public interface Dxo3 {

        /** */
        public static final String convert_SOURCE_PREFIX = "src";

        /** */
        public static final String convert_DEST_PREFIX = "dest";

        /**
         * @param src
         * @return
         */
        Object convert(Object src);
    }

    /**
     * 
     */
    public static class Hoge {
        /**
         * 
         */
        public static final String foo_hogeDxoConverter = null;

        /**
         * 
         */
        public static final String bar_hogeDxoConverter = "prefix='a_', suffix='_b'";

        /**
         * 
         */
        protected String foo;

        /**
         * 
         */
        protected String bar;

        /**
         * @return
         */
        public String getFoo() {
            return foo;
        }

        /**
         * @param foo
         */
        public void setFoo(String foo) {
            this.foo = foo;
        }

        /**
         * @return
         */
        public String getBar() {
            return bar;
        }

        /**
         * @param bar
         */
        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    /**
     * 
     */
    public static class HogeDxoConverter extends AbstractConverter {
        /**
         * 
         */
        protected String prefix = "";

        /**
         * 
         */
        protected String suffix = "";

        /**
         * @param prefix
         */
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        /**
         * @param suffix
         */
        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public Class[] getSourceClasses() {
            return new Class[] { String.class };
        }

        public Class getDestClass() {
            return String.class;
        }

        public Object convert(Object source, Class destClass,
                ConversionContext context) {
            return prefix + source + suffix;
        }
    }
}
