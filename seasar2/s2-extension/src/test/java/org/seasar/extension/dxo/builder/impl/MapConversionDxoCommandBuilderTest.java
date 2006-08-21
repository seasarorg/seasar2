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

import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dxo.annotation.impl.AnnotationReaderFactoryImpl;

/**
 * @author koichik
 * 
 */
public class MapConversionDxoCommandBuilderTest extends TestCase {

    private MapConversionDxoCommandBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        builder = new MapConversionDxoCommandBuilder();
        builder.setAnnotationReaderFactory(new AnnotationReaderFactoryImpl());
    }

    public void testApplicable() throws Exception {
        assertNotNull(builder.createDxoCommand(Dxo.class, Dxo.class.getMethod(
                "convert", new Class[] { String.class })));
    }

    public void testNotApplicable() throws Exception {
        assertNull(builder.createDxoCommand(Dxo.class, Dxo.class.getMethod(
                "convert", new Class[] { Object.class })));
    }

    public interface Dxo {
        String convert_String_MAP_CONVERSION = "";

        Map convert(Object src);

        Map convert(String src);
    }
}
