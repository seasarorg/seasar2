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

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.extension.dxo.converter.ConverterFactory;

/**
 * @author koichik
 * 
 */
public abstract class AbstractDxoCommandBuilder implements DxoCommandBuilder {

    protected ConverterFactory converterFactory;

    protected AnnotationReaderFactory annotationReaderFactory;

    public void setConverterFactory(final ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public void setAnnotationReaderFactory(
            final AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

    protected AnnotationReader getAnnotationReader() {
        return annotationReaderFactory.getAnnotationReader();
    }

}
