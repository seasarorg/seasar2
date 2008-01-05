/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.builder.impl;

import java.util.List;
import java.util.Map;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommandBuilderTest extends S2FrameworkTestCase {

    private BeanToMapDxoCommandBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    /**
     * @throws Exception
     */
    public void testToScalar() throws Exception {
        assertNotNull(builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert",
                        new Class[] { String.class })));
        assertNotNull(builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert2",
                        new Class[] { String.class })));
        assertNotNull(builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert3",
                        new Class[] { String.class })));

        assertNull(builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert",
                        new Class[] { Object[].class })));
        assertNull(builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert",
                        new Class[] { List.class })));
    }

    /**
     * @throws Exception
     */
    public void testToArray() throws Exception {
        assertNotNull(builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert",
                        new Class[] { Object[].class })));
        assertNotNull(builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert",
                        new Class[] { List.class })));

        assertNull(builder.createDxoCommand(ToArrayDxo.class, ToArrayDxo.class
                .getMethod("convert", new Class[] { Object.class })));
    }

    /**
     * @throws Exception
     */
    public void testToList() throws Exception {
        assertNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { Object[].class })));
        assertNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { List.class })));
        assertNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { Object.class })));
    }

    /**
     *
     */
    public interface ToScalarDxo {
        /**
         * 
         */
        String convert_String_CONVERSION_RULE = "";

        /**
         * 
         */
        String convert2_CONVERSION_RULE = "";

        /**
         * @param src
         * @return
         */
        Map convert(String src); // applicable

        /**
         * @param src
         * @return
         */
        Map convert2(String src); // applicable

        /**
         * @param src
         * @return
         */
        Map convert3(String src); // applicabel

        /**
         * @param src
         * @return
         */
        Map convert(Object[] src); // not applicable

        /**
         * @param src
         * @return
         */
        Map convert(List src); // not applicable
    }

    /**
     *
     */
    public interface ToArrayDxo {
        /**
         * 
         */
        String convert_CONVERSION_RULE = "";

        /**
         * @param src
         * @return
         */
        Map[] convert(Object[] src); // applicable

        /**
         * @param src
         * @return
         */
        Map[] convert(List src); // not applicable

        /**
         * @param src
         * @return
         */
        Map[] convert(Object src); // not applicable
    }

    /**
     *
     */
    public interface ToListDxo {
        /**
         * 
         */
        String convert_CONVERSION_RULE = "";

        /**
         * @param src
         * @return
         */
        List convert(Object[] src); // not applicable

        /**
         * @param src
         * @return
         */
        List convert(List src); // not applicable

        /**
         * @param src
         * @return
         */
        List convert(Object src); // not applicable
    }

}
