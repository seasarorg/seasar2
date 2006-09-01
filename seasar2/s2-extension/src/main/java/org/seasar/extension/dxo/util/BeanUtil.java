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
package org.seasar.extension.dxo.util;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author Satoshi Kimura
 */
public class BeanUtil {

    public static void copyProperties(final Object src, final Object dest) {
        copyProperties(src, dest, true);
    }

    public static void copyProperties(final Object src, final Object dest,
            final boolean includeNull) {
        final BeanDesc srcBeanDesc = BeanDescFactory
                .getBeanDesc(src.getClass());
        final BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest
                .getClass());

        final int propertyDescSize = destBeanDesc.getPropertyDescSize();
        for (int i = 0; i < propertyDescSize; i++) {
            final PropertyDesc destPropertyDesc = srcBeanDesc
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
