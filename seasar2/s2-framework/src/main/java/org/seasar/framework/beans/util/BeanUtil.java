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
package org.seasar.framework.beans.util;

import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * JavaBeans用のユーティリティです。
 * 
 * @author Kimura Satoshi
 * @author higa
 * 
 */
public class BeanUtil {

    protected BeanUtil() {
    }

    /**
     * {@link Map}の値をJavaBeansにコピーします。
     * 
     * @param src
     * @param dest
     */
    public static void copyProperties(Map src, Object dest) {
        if (src == null || dest == null) {
            return;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(dest.getClass());
        for (Iterator i = src.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            if (!beanDesc.hasPropertyDesc(key)) {
                continue;
            }
            PropertyDesc pd = beanDesc.getPropertyDesc(key);
            if (pd.hasWriteMethod()) {
                pd.setValue(dest, src.get(key));
            }
        }
    }

    /**
     * JavaBeansの値を{@link Map}にコピーします。
     * 
     * @param src
     * @param dest
     */
    public static void copyProperties(Object src, Map dest) {
        if (src == null || dest == null) {
            return;
        }
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        final int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; ++i) {
            final PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (pd.hasReadMethod() && pd.hasWriteMethod()) {
                final Object value = pd.getValue(src);
                dest.put(pd.getPropertyName(), value);
            }
        }
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     * @param dest
     */
    public static void copyProperties(final Object src, final Object dest) {
        copyProperties(src, dest, true);
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     * @param dest
     * @param includeNull
     */
    public static void copyProperties(final Object src, final Object dest,
            final boolean includeNull) {
        final BeanDesc srcBeanDesc = BeanDescFactory
                .getBeanDesc(src.getClass());
        final BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest
                .getClass());

        final int propertyDescSize = destBeanDesc.getPropertyDescSize();
        for (int i = 0; i < propertyDescSize; i++) {
            final PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(i);
            final String propertyName = destPropertyDesc.getPropertyName();
            if (srcBeanDesc.hasPropertyDesc(propertyName)) {
                final PropertyDesc srcPropertyDesc = srcBeanDesc
                        .getPropertyDesc(propertyName);
                if (destPropertyDesc.hasWriteMethod()
                        && srcPropertyDesc.hasReadMethod()) {
                    final Object value = srcPropertyDesc.getValue(src);
                    if (includeNull || value != null) {
                        destPropertyDesc.setValue(dest, srcPropertyDesc
                                .getValue(src));
                    }
                }
            }
        }
    }
}