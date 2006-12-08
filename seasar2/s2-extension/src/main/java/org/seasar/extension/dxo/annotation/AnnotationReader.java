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
package org.seasar.extension.dxo.annotation;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Satsohi Kimura
 * @author koichik
 */
public interface AnnotationReader {

    String getDatePattern(Class dxoClass, Method method);

    String getTimePattern(Class dxoClass, Method method);

    String getTimestampPattern(Class dxoClass, Method method);

    String getConversionRule(Class dxoClass, Method method);

    boolean isExcludeNull(Class dxoClass, Method method);

    Map getConverters(Class destClass);

}
