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
package org.seasar.framework.util.tiger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.MethodUtil;

/**
 * @author higa
 * 
 */
public class AnnotationUtil {

    protected AnnotationUtil() {
    }

    public static Map<String, Object> getProperties(Annotation annotation) {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(annotation
                .annotationType());
        String[] names = beanDesc.getMethodNames();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Method m = beanDesc.getMethodNoException(name);
            if (m == null) {
                continue;
            }
            Object v = MethodUtil.invoke(m, annotation, null);
            if (v != null) {
                map.put(name, v);
            }
        }
        return map;
    }
}