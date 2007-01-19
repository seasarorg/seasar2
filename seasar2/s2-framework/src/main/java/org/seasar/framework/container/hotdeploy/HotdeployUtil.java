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
package org.seasar.framework.container.hotdeploy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerBehavior.Provider;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public abstract class HotdeployUtil {

    private static Boolean hotdeploy;

    protected HotdeployUtil() {
    }

    public static void setHotdeploy(boolean hotdeploy) {
        HotdeployUtil.hotdeploy = Boolean.valueOf(hotdeploy);
    }

    public static void clearHotdeploy() {
        hotdeploy = null;
    }

    public static boolean isHotdeploy() {
        if (hotdeploy != null) {
            return hotdeploy.booleanValue();
        }
        Provider provider = S2ContainerBehavior.getProvider();
        return provider instanceof HotdeployBehavior;
    }

    public static void start() {
        if (isHotdeploy()) {
            ((HotdeployBehavior) S2ContainerBehavior.getProvider()).start();
        }
    }

    public static void stop() {
        if (isHotdeploy()) {
            ((HotdeployBehavior) S2ContainerBehavior.getProvider()).stop();
        }
    }

    public static Object rebuildValue(Object value) {
        if (isHotdeploy()) {
            return rebuildValueInternal(value);
        }
        return value;
    }

    protected static Object rebuildValueInternal(Object value) {
        if (value == null) {
            return null;
        }
        Class valueClass = value.getClass();
        if (isSimpleValueType(valueClass)) {
            return value;
        }
        if (valueClass.isArray()) {
            return rebuildArray(value);
        }
        if (valueClass == ArrayList.class) {
            return rebuildArrayList((ArrayList) value);
        }
        if (Collection.class.isAssignableFrom(valueClass)) {
            return rebuildCollection((Collection) value);
        }
        if (Map.class.isAssignableFrom(valueClass)) {
            return rebuildMap((Map) value);
        }
        return rebuildBean(value);
    }

    protected static Object rebuildArray(Object value) {
        Class clazz = value.getClass().getComponentType();
        if (!clazz.isPrimitive()) {
            clazz = ClassUtil.forName(clazz.getName());
        }
        int size = Array.getLength(value);
        Object array = Array.newInstance(clazz, size);
        for (int i = 0; i < size; ++i) {
            Array.set(array, i, rebuildValueInternal(Array.get(value, i)));
        }
        return array;
    }

    protected static ArrayList rebuildArrayList(ArrayList value) {
        ArrayList arrayList = new ArrayList(value.size());
        for (int i = 0; i < value.size(); ++i) {
            arrayList.add(rebuildValueInternal(value.get(i)));
        }
        return arrayList;
    }

    protected static Collection rebuildCollection(Collection value) {
        Collection collection = (Collection) ClassUtil.newInstance(value
                .getClass());
        for (Iterator i = value.iterator(); i.hasNext();) {
            collection.add(rebuildValueInternal(i.next()));
        }
        return collection;
    }

    protected static Map rebuildMap(Map value) {
        Map map = (Map) ClassUtil.newInstance(value.getClass());
        for (Iterator i = value.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            map.put(rebuildValueInternal(entry.getKey()),
                    rebuildValueInternal(entry.getValue()));
        }
        return map;
    }

    protected static Object rebuildBean(Object value) {
        Object bean = ClassUtil.newInstance(value.getClass().getName());
        BeanDesc srcBeanDesc = BeanDescFactory.getBeanDesc(value.getClass());
        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(bean.getClass());

        int propertyDescSize = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < propertyDescSize; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            if (!srcPropertyDesc.hasReadMethod()) {
                continue;
            }
            String propertyName = srcPropertyDesc.getPropertyName();
            if (!destBeanDesc.hasPropertyDesc(propertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(propertyName);
            if (!destPropertyDesc.hasWriteMethod()) {
                continue;
            }
            destPropertyDesc.setValue(bean,
                    rebuildValueInternal(srcPropertyDesc.getValue(value)));
        }
        return bean;
    }

    public static boolean isSimpleValueType(Class type) {
        return type == String.class || type == Boolean.class
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || Calendar.class.isAssignableFrom(type);
    }
}
