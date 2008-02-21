/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.util;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author taedium
 * 
 */
public class BeanUtil {

    private BeanUtil() {
    }

    public static void copy(Object src, Object dest) {
        BeanDesc srcBeanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.hasReadMethod()) {
                continue;
            }
            String destPropertyName = srcPropertyName;
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.hasWriteMethod()) {
                continue;
            }
            Object value = srcPropertyDesc.getValue(src);
            if (value == null) {
                continue;
            }
            destPropertyDesc.setValue(dest, value);
        }
    }
}
