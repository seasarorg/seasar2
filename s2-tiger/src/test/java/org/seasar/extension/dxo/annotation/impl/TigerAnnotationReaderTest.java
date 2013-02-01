/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.annotation.ConversionRule;
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.DxoConverter;
import org.seasar.extension.dxo.annotation.ExcludeNull;
import org.seasar.extension.dxo.annotation.ExcludeWhitespace;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.impl.AbstractConverter;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class TigerAnnotationReaderTest extends S2FrameworkTestCase {

    AnnotationReaderFactory factory;

    @Override
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
    public void testGetMapConversion() throws Exception {
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
    @SuppressWarnings("unchecked")
    public void testGetConverters() throws Exception {
        Map<String, Converter> converters = factory.getAnnotationReader()
                .getConverters(Hoge.class);
        assertNotNull(converters);
        assertEquals(3, converters.size());

        Converter converter = converters.get("foo");
        assertNotNull(converter);
        assertEquals("foo", converter.convert("foo", String.class, null));

        converter = converters.get("bar");
        assertNotNull(converter);
        assertEquals("a_bar_b", converter.convert("bar", String.class, null));

        converter = converters.get("baz");
        assertNotNull(converter);
        assertEquals("baz", converter.convert("baz", String.class, null));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testIsExcludeNull() throws Exception {
        assertTrue(factory.getAnnotationReader().isExcludeNull(
                Dxo.class,
                Dxo.class.getMethod("convertExNull",
                        new Class[] { Object.class })));
        assertTrue(factory.getAnnotationReader().isExcludeNull(
                Dxo2.class,
                Dxo2.class.getMethod("convertExNull",
                        new Class[] { Object.class })));
        assertFalse(factory.getAnnotationReader()
                .isExcludeNull(
                        Dxo.class,
                        Dxo.class.getMethod("convertExWs",
                                new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testIsExcludeWs() throws Exception {
        assertTrue(factory.getAnnotationReader()
                .isExcludeWhitespace(
                        Dxo.class,
                        Dxo.class.getMethod("convertExWs",
                                new Class[] { Object.class })));
        assertTrue(factory.getAnnotationReader().isExcludeWhitespace(
                Dxo2.class,
                Dxo2.class.getMethod("convertExWs",
                        new Class[] { Object.class })));
        assertFalse(factory.getAnnotationReader().isExcludeWhitespace(
                Dxo.class,
                Dxo.class.getMethod("convertExNull",
                        new Class[] { Object.class })));
    }

    /**
     * 
     */
    @DatePattern("class default")
    public interface Dxo {

        /**
         * @param src
         * @return
         */
        List<?> converted(List<?> src);

        /**
         * @param src
         * @return
         */
        @DatePattern("foo")
        Object convert(Object src);

        /**
         * @param src
         * @param date
         */
        @DatePattern("bar")
        void convert(Number src, String date);

        /**
         * @param src
         * @return
         */
        @DatePattern("baz")
        Object[] convert(Object[] src);

        /**
         * @param src
         * @return
         */
        @ConversionRule("map1")
        Map<?, ?> convert(Integer src);

        /**
         * @param src
         * @param dest
         */
        @ConversionRule("map2")
        void convert(Integer src, Map<?, ?> dest);

        /**
         * @param src
         * @return
         */
        @SuppressWarnings("unchecked")
        Map[] convert(Integer[] src);

        /**
         * @param src
         * @return
         */
        @ExcludeNull
        Object convertExNull(Object src);

        /**
         * @param src
         * @return
         */
        @ExcludeWhitespace
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
    public static class Hoge {

        /**
         * 
         */
        protected String foo;

        /**
         * 
         */
        protected String bar;

        /**
         * 
         */
        @HogeDxoConverter
        public String baz;

        /**
         * @return
         */
        public String getFoo() {
            return foo;
        }

        /**
         * @param foo
         */
        @HogeDxoConverter
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
        @HogeDxoConverter(prefix = "a_", suffix = "_b")
        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    /**
     * 
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @DxoConverter("hogeDxoConverter")
    public @interface HogeDxoConverter {

        /**
         * 
         */
        String prefix() default "";

        /**
         * 
         */
        String suffix() default "";
    }

    /**
     * 
     */
    public static class HogeDxoConverterImpl extends AbstractConverter {

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

        public Class<?> getDestClass() {
            return Hoge.class;
        }

        public Class<?>[] getSourceClasses() {
            return new Class[] { Hoge.class };
        }

        @SuppressWarnings("unchecked")
        public Object convert(Object source, Class destClass,
                ConversionContext context) {
            return prefix + source + suffix;
        }
    }

}
