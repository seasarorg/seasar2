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

import java.lang.reflect.Method;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.MapConversion;

/**
 * @author koichik
 * 
 */
public class TigerAnnotationReader implements AnnotationReader {

    @SuppressWarnings("unchecked")
    public String getDatePattern(Class dxoClass, Method method) {
        DatePattern datePattern = method.getAnnotation(DatePattern.class);
        if (datePattern != null) {
            return datePattern.value();
        }
        datePattern = DatePattern.class.cast(dxoClass
                .getAnnotation(DatePattern.class));
        if (datePattern != null) {
            return datePattern.value();
        }
        return null;
    }

    public String getMapConversion(final Class dxoClass, final Method method) {
        final MapConversion mapConversion = method
                .getAnnotation(MapConversion.class);
        if (mapConversion == null) {
            return null;
        }
        return mapConversion.value();
    }

}
