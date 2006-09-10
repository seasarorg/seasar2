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
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.seasar.extension.dxo.DxoConstants;
import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.dxo.util.DxoUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 * 
 */
public class ConversionContextImpl implements ConversionContext {

    protected static boolean initialized;

    protected static final Map contextCache = Collections
            .synchronizedMap(new HashMap());

    protected Class dxoClass;

    protected Method method;

    protected ConverterFactory converterFactory;

    protected Map convertedObjects = new IdentityHashMap();

    protected Map contextInfo;

    protected Map evaluatedValues;

    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                destroy();
            }
        });
        initialized = true;
    }

    public static synchronized void destroy() {
        contextCache.clear();
        initialized = false;
    }

    public ConversionContextImpl(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader reader, final Object source) {
        initialize();
        this.dxoClass = dxoClass;
        this.method = method;
        this.converterFactory = converterFactory;
        contextInfo = getContextInfo(reader);

        final Object conversionRule = getContextInfo(DxoConstants.CONVERSION_RULE);
        if (conversionRule != null) {
            evaluatedValues = (Map) OgnlUtil.getValue(conversionRule, source);
        }
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

    public Object getContextInfo(final String key) {
        return contextInfo.get(key);
    }

    public boolean hasEvalueatedValue(final String name) {
        if (evaluatedValues == null) {
            return false;
        }
        return evaluatedValues.containsKey(name);
    }

    public Object getEvaluatedValue(final String name) {
        if (evaluatedValues == null) {
            return null;
        }
        return evaluatedValues.get(name);
    }

    protected Map getContextInfo(final AnnotationReader reader) {
        final Map contextInfo = (Map) contextCache.get(method);
        if (contextInfo != null) {
            return contextInfo;
        }
        return createContextInfo(reader);
    }

    protected Map createContextInfo(final AnnotationReader reader) {
        final Map contextInfo = new HashMap();
        contextInfo.put(DxoConstants.DATE_PATTERN, toDateFormat(reader
                .getDatePattern(dxoClass, method)));
        contextInfo.put(DxoConstants.TIME_PATTERN, toDateFormat(reader
                .getTimePattern(dxoClass, method)));
        contextInfo.put(DxoConstants.TIMESTAMP_PATTERN, toDateFormat(reader
                .getTimestampPattern(dxoClass, method)));
        final String conversionRule = reader
                .getConversionRule(dxoClass, method);
        if (!StringUtil.isEmpty(conversionRule)) {
            contextInfo.put(DxoConstants.CONVERSION_RULE, DxoUtil
                    .parseMap(conversionRule));
        }
        return contextInfo;
    }

    protected DateFormat toDateFormat(final String format) {
        if (StringUtil.isEmpty(format)) {
            return null;
        }
        return new SimpleDateFormat(format);
    }

}
