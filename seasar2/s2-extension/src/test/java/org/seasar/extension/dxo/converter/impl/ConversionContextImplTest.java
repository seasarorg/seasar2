/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.converter.impl;

import junit.framework.TestCase;

import org.seasar.extension.dxo.annotation.impl.ConstantAnnotationReader;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 * 
 */
public class ConversionContextImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_noprefix() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert0", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertEquals("aaa", context.getSourcePropertyName("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_src() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert1", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertEquals("srcAaa", context.getSourcePropertyName("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_srcWith_() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert2", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertEquals("src_aaa", context.getSourcePropertyName("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_dest() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert3", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertNull(context.getSourcePropertyName("aaa"));
        assertEquals("aaa", context.getSourcePropertyName("destAaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_destWith_() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert4", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertNull(context.getSourcePropertyName("aaa"));
        assertNull(context.getSourcePropertyName("destAaa"));
        assertEquals("aaa", context.getSourcePropertyName("dest_aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSourcePropertyName_srcDest() throws Exception {
        ConversionContextImpl context = new ConversionContextImpl(Dxo.class,
                Dxo.class.getMethod("convert5", new Class[] { Object.class }),
                new ConverterFactoryImpl(), new ConstantAnnotationReader(
                        new S2ContainerImpl()), new Object());
        assertNull(context.getSourcePropertyName("aaa"));
        assertEquals("srcAaa", context.getSourcePropertyName("destAaa"));
    }

    /**
     */
    public interface Dxo {

        /**
         * @param src
         * @return
         */
        Object convert0(Object src);

        /** */
        public static final String convert1_SOURCE_PREFIX = "src";

        /**
         * @param src
         * @return
         */
        Object convert1(Object src);

        /** */
        public static final String convert2_SOURCE_PREFIX = "src_";

        /**
         * @param src
         * @return
         */
        Object convert2(Object src);

        /** */
        public static final String convert3_DEST_PREFIX = "dest";

        /**
         * @param src
         * @return
         */
        Object convert3(Object src);

        /** */
        public static final String convert4_DEST_PREFIX = "dest_";

        /**
         * @param src
         * @return
         */
        Object convert4(Object src);

        /** */
        public static final String convert5_SOURCE_PREFIX = "src";

        /** */
        public static final String convert5_DEST_PREFIX = "dest";

        /**
         * @param src
         * @return
         */
        Object convert5(Object src);
    }
}
