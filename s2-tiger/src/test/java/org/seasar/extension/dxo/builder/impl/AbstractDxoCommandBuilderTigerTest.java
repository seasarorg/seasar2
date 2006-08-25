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
package org.seasar.extension.dxo.builder.impl;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class AbstractDxoCommandBuilderTigerTest extends TestCase {

    public void testGetElementTypeOfListFromParameterType() throws Exception {
        assertEquals(Integer.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromParameterType(Dxo.class.getMethod(
                        "convert", new Class[] { List.class }), 0));
        assertEquals(Double.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromParameterType(Dxo.class.getMethod(
                        "convert", new Class[] { List.class, List.class }), 0));
        assertEquals(BigDecimal.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromParameterType(Dxo.class.getMethod(
                        "convert", new Class[] { List.class, List.class }), 1));
    }

    public void testGetElementTypeOfListFromReturnType() throws Exception {
        assertEquals(String.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromReturnType(Dxo.class.getMethod(
                        "convert", new Class[] { List.class })));
    }

    public void testGetElementTypeOfListFromDestination() throws Exception {
        assertEquals(String.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromDestination(Dxo.class.getMethod(
                        "convert", new Class[] { List.class })));
        assertEquals(BigDecimal.class, AbstractDxoCommandBuilder
                .getElementTypeOfListFromDestination(Dxo.class.getMethod(
                        "convert", new Class[] { List.class, List.class })));
    }

    public interface Dxo {
        List<String> convert(List<Integer> src);

        void convert(List<Double> src, List<BigDecimal> dest);
    }

}
