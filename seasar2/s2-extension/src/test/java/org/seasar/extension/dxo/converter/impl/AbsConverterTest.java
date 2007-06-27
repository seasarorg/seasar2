/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public abstract class AbsConverterTest extends S2FrameworkTestCase {

    private ConverterFactory converterFactory;

    private AnnotationReaderFactory annotationReaderFactory;

    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    /**
     * @param methodName
     * @param source
     * @return
     * @throws Exception
     */
    protected ConversionContext createContext(String methodName, Object source)
            throws Exception {
        return new ConversionContextImpl(getClass(), getClass().getMethod(
                methodName, null), converterFactory, annotationReaderFactory
                .getAnnotationReader(), source);
    }

}
