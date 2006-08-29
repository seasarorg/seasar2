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
package org.seasar.framework.beans.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.SqlDateConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * @author higa
 * 
 */
public final class PropertyDescImpl implements PropertyDesc {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private String propertyName;

    private Class propertyType;

    private Method readMethod;

    private Method writeMethod;

    private BeanDesc beanDesc;

    private Constructor stringConstructor;

    public PropertyDescImpl(String propertyName, Class propertyType,
            Method readMethod, Method writeMethod, BeanDesc beanDesc) {

        if (propertyName == null) {
            throw new EmptyRuntimeException("propertyName");
        }
        if (propertyType == null) {
            throw new EmptyRuntimeException("propertyType");
        }
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.beanDesc = beanDesc;
        setupStringConstructor();
    }

    private void setupStringConstructor() {
        Constructor[] cons = propertyType.getConstructors();
        for (int i = 0; i < cons.length; ++i) {
            Constructor con = cons[i];
            if (con.getParameterTypes().length == 1
                    && con.getParameterTypes()[0].equals(String.class)) {
                stringConstructor = con;
                break;
            }
        }
    }

    public final String getPropertyName() {
        return propertyName;
    }

    public final Class getPropertyType() {
        return propertyType;
    }

    public final Method getReadMethod() {
        return readMethod;
    }

    public final void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public final boolean hasReadMethod() {
        return readMethod != null;
    }

    public final Method getWriteMethod() {
        return writeMethod;
    }

    public final void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public final boolean hasWriteMethod() {
        return writeMethod != null;
    }

    public final Object getValue(Object target) {
        return MethodUtil.invoke(readMethod, target, EMPTY_ARGS);
    }

    public final void setValue(Object target, Object value) {
        try {
            MethodUtil.invoke(writeMethod, target,
                    new Object[] { convertIfNeed(value) });
        } catch (Throwable t) {
            throw new IllegalPropertyRuntimeException(beanDesc.getBeanClass(),
                    propertyName, t);
        }
    }

    public final BeanDesc getBeanDesc() {
        return beanDesc;
    }

    public final String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("propertyName=");
        buf.append(propertyName);
        buf.append(",propertyType=");
        buf.append(propertyType.getName());
        buf.append(",readMethod=");
        buf.append(readMethod != null ? readMethod.getName() : "null");
        buf.append(",writeMethod=");
        buf.append(writeMethod != null ? writeMethod.getName() : "null");
        return buf.toString();
    }

    public Object convertIfNeed(Object arg) {
        if (propertyType.isPrimitive()) {
            return convertPrimitiveWrapper(arg);
        } else if (Number.class.isAssignableFrom(propertyType)) {
            return convertNumber(arg);
        } else if (java.util.Date.class.isAssignableFrom(propertyType)) {
            return convertDate(arg);
        } else if (Boolean.class.isAssignableFrom(propertyType)) {
            return BooleanConversionUtil.toBoolean(arg);
        } else if (arg instanceof String && !String.class.equals(propertyType)) {
            return convertWithStringConstructor(arg);
        }
        return arg;
    }

    private Object convertPrimitiveWrapper(Object arg) {
        return NumberConversionUtil.convertPrimitiveWrapper(propertyType, arg);
    }

    private Object convertNumber(Object arg) {
        return NumberConversionUtil.convertNumber(propertyType, arg);
    }

    private Object convertDate(Object arg) {
        if (propertyType == java.util.Date.class) {
            return DateConversionUtil.toDate(arg);
        } else if (propertyType == Timestamp.class) {
            return TimestampConversionUtil.toTimestamp(arg);
        } else if (propertyType == java.sql.Date.class) {
            return SqlDateConversionUtil.toDate(arg);
        } else if (propertyType == Time.class) {
            return TimeConversionUtil.toTime(arg);
        }
        return arg;
    }

    private Object convertWithStringConstructor(Object arg) {
        if (stringConstructor == null || arg == null) {
            return arg;
        }
        return ConstructorUtil.newInstance(stringConstructor,
                new Object[] { arg });
    }

}
