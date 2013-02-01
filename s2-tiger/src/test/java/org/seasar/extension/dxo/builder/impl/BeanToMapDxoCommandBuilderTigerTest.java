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
package org.seasar.extension.dxo.builder.impl;

import java.util.List;
import java.util.Map;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class BeanToMapDxoCommandBuilderTigerTest extends S2FrameworkTestCase {

    private BeanToMapDxoCommandBuilder builder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    /**
     * @throws Exception
     */
    public void testApplicable() throws Exception {
        assertNotNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { Object[].class })));
        assertNotNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { List.class })));

        assertNull(builder.createDxoCommand(ToListDxo.class, ToListDxo.class
                .getMethod("convert", new Class[] { String[].class })));
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
        @SuppressWarnings("unchecked")
        List<Map> convert(Object[] src);

        /**
         * @param src
         * @return
         */
        @SuppressWarnings("unchecked")
        List<Map> convert(List<Object> src);

        /**
         * @param src
         * @return
         */
        List<Integer> convert(String[] src);
    }

}
