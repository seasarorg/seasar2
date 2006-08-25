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
package org.seasar.extension.dxo.converter.impl;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.IdentityHashMap;
import java.util.Map;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 * 
 */
public class ConversionContextImpl implements ConversionContext {

    protected Class dxoClass;

    protected Method method;

    protected ConverterFactory converterFactory;

    protected Map convertedObjects = new IdentityHashMap();

    protected DateFormat dateFormat;

    protected DateFormat timeFormat;

    protected DateFormat timestampFormat;

    public ConversionContextImpl(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader reader) {
        this.dxoClass = dxoClass;
        this.method = method;
        this.converterFactory = converterFactory;
        dateFormat = toDateFormat(reader.getDatePattern(dxoClass, method));
        timeFormat = toDateFormat(reader.getDatePattern(dxoClass, method));
        timestampFormat = toDateFormat(reader.getDatePattern(dxoClass, method));
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public void addConvertedObject(final Object source, final Object dest) {
        convertedObjects.put(source, dest);
    }

    public Object getConvertedObject(final Object source) {
        return convertedObjects.get(source);
    }

    protected DateFormat toDateFormat(final String format) {
        if (StringUtil.isEmpty(format)) {
            return null;
        }
        return new SimpleDateFormat(format);
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    public DateFormat getTimestampFormat() {
        return timestampFormat;
    }

}
