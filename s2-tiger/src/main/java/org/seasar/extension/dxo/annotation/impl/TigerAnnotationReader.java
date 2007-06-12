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
package org.seasar.extension.dxo.annotation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.ConversionRule;
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.DxoConverter;
import org.seasar.extension.dxo.annotation.ExcludeNull;
import org.seasar.extension.dxo.annotation.SourcePrefix;
import org.seasar.extension.dxo.annotation.TimePattern;
import org.seasar.extension.dxo.annotation.TimestampPattern;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.tiger.AnnotationUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public class TigerAnnotationReader implements AnnotationReader {

    protected S2Container container;

    protected AnnotationReader next;

    protected Map<Class<?>, Map<String, Converter>> convertersCache = CollectionsUtil
            .newConcurrentHashMap();

    public TigerAnnotationReader(final S2Container container) {
        this(container, null);
    }

    public TigerAnnotationReader(final S2Container container,
            final AnnotationReader next) {
        this.container = container.getRoot();
        this.next = next;
    }

    @SuppressWarnings("unchecked")
    public String getDatePattern(final Class dxoClass, final Method method) {
        final DatePattern datePattern = getAnnotation(dxoClass, method,
                DatePattern.class);
        if (datePattern != null) {
            return datePattern.value();
        }
        if (next != null) {
            return next.getDatePattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getTimePattern(final Class dxoClass, final Method method) {
        final TimePattern timePattern = getAnnotation(dxoClass, method,
                TimePattern.class);
        if (timePattern != null) {
            return timePattern.value();
        }
        if (next != null) {
            return next.getTimePattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getTimestampPattern(final Class dxoClass, final Method method) {
        final TimestampPattern timestampPattern = getAnnotation(dxoClass,
                method, TimestampPattern.class);
        if (timestampPattern != null) {
            return timestampPattern.value();
        }
        if (next != null) {
            return next.getTimestampPattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getConversionRule(final Class dxoClass, final Method method) {
        final ConversionRule mapConversion = method
                .getAnnotation(ConversionRule.class);
        if (mapConversion != null) {
            return mapConversion.value();
        }
        if (next != null) {
            return next.getConversionRule(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean isExcludeNull(final Class dxoClass, final Method method) {
        final ExcludeNull excludeNull = getAnnotation(dxoClass, method,
                ExcludeNull.class);
        if (excludeNull != null) {
            return true;
        }
        if (next != null) {
            return next.isExcludeNull(dxoClass, method);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public String getSourcePrefix(Class dxoClass, Method method) {
        final SourcePrefix sourcePrefix = getAnnotation(dxoClass, method,
                SourcePrefix.class);
        if (sourcePrefix != null) {
            return sourcePrefix.value();
        }
        if (next != null) {
            return next.getSourcePrefix(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map getConverters(final Class destClass) {
        final Map<String, Converter> converters = convertersCache
                .get(destClass);
        if (converters != null) {
            return converters;
        }
        return createConverters(destClass);
    }

    protected <T extends Annotation> T getAnnotation(final Class<?> dxoClass,
            final Method method, final Class<T> annotationType) {
        final T annotation = method.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        return dxoClass.getAnnotation(annotationType);
    }

    protected Map<String, Converter> createConverters(final Class<?> destClass) {
        final Map<String, Converter> converters = CollectionsUtil.newHashMap();
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(destClass);
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (!propertyDesc.hasWriteMethod()) {
                continue;
            }
            final Method method = propertyDesc.getWriteMethod();
            for (final Annotation annotation : method.getDeclaredAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation
                        .annotationType();
                final Annotation metaAnnotation = annotationType
                        .getAnnotation(DxoConverter.class);
                if (metaAnnotation == null) {
                    continue;
                }
                final DxoConverter dxoConverterAnnotation = DxoConverter.class
                        .cast(metaAnnotation);
                final String converterName = dxoConverterAnnotation.value();
                final Converter converter = Converter.class.cast(container
                        .getComponent(converterName));
                final Map<?, ?> props = AnnotationUtil
                        .getProperties(annotation);
                BeanUtil.copyProperties(props, converter);
                converters.put(propertyDesc.getPropertyName(), converter);
                break;
            }
        }
        convertersCache.put(destClass, converters);
        return converters;
    }

}
