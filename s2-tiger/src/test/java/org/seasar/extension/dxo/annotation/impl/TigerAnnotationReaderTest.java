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

    @SuppressWarnings("unchecked")
    public void testGetConverters() throws Exception {
        Map<String, Converter> converters = factory.getAnnotationReader()
                .getConverters(Hoge.class);
        assertNotNull(converters);
        assertEquals(2, converters.size());

        Converter converter = converters.get("foo");
        assertNotNull(converter);
        assertEquals("foo", converter.convert("foo", String.class, null));

        converter = converters.get("bar");
        assertNotNull(converter);
        assertEquals("a_bar_b", converter.convert("bar", String.class, null));
    }

    @DatePattern("class default")
    public interface Dxo {

        List<?> converted(List<?> src);

        @DatePattern("foo")
        Object convert(Object src);

        @DatePattern("bar")
        void convert(Number src, String date);

        @DatePattern("baz")
        Object[] convert(Object[] src);

        @ConversionRule("map1")
        Map<?, ?> convert(Integer src);

        @ConversionRule("map2")
        void convert(Integer src, Map<?, ?> dest);

        Map[] convert(Integer[] src);
    }

    public interface Dxo2 {
        Object convert(Object src);
    }

    public static class Hoge {
        protected String foo;

        protected String bar;

        public String getFoo() {
            return foo;
        }

        @HogeDxoConverter
        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getBar() {
            return bar;
        }

        @HogeDxoConverter(prefix = "a_", suffix = "_b")
        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @DxoConverter("hogeDxoConverter")
    public @interface HogeDxoConverter {
        String prefix() default "";

        String suffix() default "";
    }

    public static class HogeDxoConverterImpl extends AbstractConverter {
        protected String prefix = "";

        protected String suffix = "";

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        @SuppressWarnings("unchecked")
        public Object convert(Object source, Class destClass,
                ConversionContext context) {
            return prefix + source + suffix;
        }
    }

}
