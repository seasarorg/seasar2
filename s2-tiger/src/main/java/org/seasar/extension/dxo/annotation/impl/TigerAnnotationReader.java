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
package org.seasar.extension.dxo.annotation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.MapConversion;
import org.seasar.extension.dxo.annotation.TimePattern;
import org.seasar.extension.dxo.annotation.TimestampPattern;

/**
 * @author koichik
 */
public class TigerAnnotationReader implements AnnotationReader {

    protected AnnotationReader next;

    public TigerAnnotationReader(final AnnotationReader next) {
        this.next = next;
    }

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

    protected <T extends Annotation> T getAnnotation(final Class<?> dxoClass,
            final Method method, final Class<T> annotationType) {
        final T annotation = method.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        return dxoClass.getAnnotation(annotationType);
    }

    public String getMapConversion(final Class dxoClass, final Method method) {
        final MapConversion mapConversion = method
                .getAnnotation(MapConversion.class);
        if (mapConversion != null) {
            return mapConversion.value();
        }
        if (next != null) {
            return next.getMapConversion(dxoClass, method);
        }
        return null;
    }

}
