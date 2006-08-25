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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Method;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.dxo.converter.impl.ConversionContextImpl;
import org.seasar.extension.dxo.util.BeanUtil;

/**
 * @author koichik
 * 
 */
public class BeanToBeanDxoCommand extends AbstractDxoCommand {

    protected ConverterFactory converterFactory;

    protected AnnotationReader annotationReader;

    protected Class dxoClass;

    protected Method method;

    protected Class sourceClass;

    protected Class destClass;

    protected Converter converter;

    public BeanToBeanDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader, final Class sourceClass,
            final Class destClass) {
        super(method);
        this.dxoClass = dxoClass;
        this.method = method;
        this.converterFactory = converterFactory;
        this.annotationReader = annotationReader;
        this.sourceClass = sourceClass;
        this.destClass = destClass;
        converter = converterFactory.getConverter(sourceClass, destClass);
    }

    protected Object convertScalar(final Object source) {
        return converter.convert(source, destClass, createContext());
    }

    protected void copy(final Object src, final Object dest) {
        BeanUtil.copyProperties(src, dest);
    }

    protected Class getDestElementType() {
        return destClass;
    }

    protected ConversionContext createContext() {
        return new ConversionContextImpl(dxoClass, method, converterFactory,
                annotationReader);
    }

}
