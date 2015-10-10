/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.ParameterizedClassDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.CalendarConversionUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.SqlDateConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * {@link PropertyDesc}の実装クラスです。
 * 
 * @author higa
 */
public class PropertyDescImpl implements PropertyDesc {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private String propertyName;

    private Class propertyType;

    private Method readMethod;

    private Method writeMethod;

    private Field field;

    private BeanDesc beanDesc;

    private Constructor stringConstructor;

    private Method valueOfMethod;

    private boolean readable = false;

    private boolean writable = false;

    private ParameterizedClassDesc parameterizedClassDesc;

    /**
     * {@link PropertyDescImpl}を作成します。
     * 
     * @param propertyName
     * @param propertyType
     * @param readMethod
     * @param writeMethod
     * @param beanDesc
     */
    public PropertyDescImpl(String propertyName, Class propertyType,
            Method readMethod, Method writeMethod, BeanDesc beanDesc) {

        this(propertyName, propertyType, readMethod, writeMethod, null,
                beanDesc);
    }

    /**
     * {@link PropertyDescImpl}を作成します。
     * 
     * @param propertyName
     * @param propertyType
     * @param readMethod
     * @param writeMethod
     * @param field
     * @param beanDesc
     */
    public PropertyDescImpl(String propertyName, Class propertyType,
            Method readMethod, Method writeMethod, Field field,
            BeanDesc beanDesc) {

        if (propertyName == null) {
            throw new EmptyRuntimeException("propertyName");
        }
        if (propertyType == null) {
            throw new EmptyRuntimeException("propertyType");
        }
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        setReadMethod(readMethod);
        setWriteMethod(writeMethod);
        setField(field);
        this.beanDesc = beanDesc;
        setupStringConstructor();
        setupValueOfMethod();
        setUpParameterizedClassDesc();
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

    private void setupValueOfMethod() {
        Method[] methods = propertyType.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            if (MethodUtil.isBridgeMethod(method)
                    || MethodUtil.isSyntheticMethod(method)) {
                continue;
            }
            if (ModifierUtil.isStatic(method.getModifiers())
                    && method.getName().equals("valueOf")
                    && method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].equals(String.class)) {
                valueOfMethod = method;
                break;
            }
        }
    }

    private void setUpParameterizedClassDesc() {
        final Map typeVariables = ((BeanDescImpl) beanDesc).getTypeVariables();
        if (field != null) {
            parameterizedClassDesc = ParameterizedClassDescFactory
                    .createParameterizedClassDesc(field, typeVariables);
        } else if (readMethod != null) {
            parameterizedClassDesc = ParameterizedClassDescFactory
                    .createParameterizedClassDesc(readMethod, typeVariables);
        } else if (writeMethod != null) {
            parameterizedClassDesc = ParameterizedClassDescFactory
                    .createParameterizedClassDesc(writeMethod, 0, typeVariables);
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
        if (readMethod != null) {
            readable = true;
            readMethod.setAccessible(true);
        }
    }

    public final boolean hasReadMethod() {
        return readMethod != null;
    }

    public final Method getWriteMethod() {
        return writeMethod;
    }

    public final void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
        if (writeMethod != null) {
            writable = true;
            writeMethod.setAccessible(true);
        }
    }

    public final boolean hasWriteMethod() {
        return writeMethod != null;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
        if (field != null && ModifierUtil.isPublic(field)) {
            readable = true;
            writable = true;
        }
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public final Object getValue(Object target) {
        try {
            if (!readable) {
                throw new IllegalStateException(propertyName
                        + " is not readable.");
            } else if (hasReadMethod()) {
                return MethodUtil.invoke(readMethod, target, EMPTY_ARGS);
            } else {
                return FieldUtil.get(field, target);
            }
        } catch (Throwable t) {
            throw new IllegalPropertyRuntimeException(beanDesc.getBeanClass(),
                    propertyName, t);
        }
    }

    public final void setValue(Object target, Object value) {
        try {
            value = convertIfNeed(value);
            if (!writable) {
                throw new IllegalStateException(propertyName
                        + " is not writable.");
            } else if (hasWriteMethod()) {
                try {
                    MethodUtil.invoke(writeMethod, target,
                            new Object[] { value });
                } catch (Throwable t) {
                    Class clazz = writeMethod.getDeclaringClass();
                    Class valueClass = value == null ? null : value.getClass();
                    Class targetClass = target == null ? null : target
                            .getClass();
                    throw new SIllegalArgumentException("ESSR0098",
                            new Object[] {
                                    clazz.getName(),
                                    clazz.getClassLoader(),
                                    propertyType.getName(),
                                    propertyType.getClassLoader(),
                                    propertyName,
                                    valueClass == null ? null : valueClass
                                            .getName(),
                                    valueClass == null ? null : valueClass
                                            .getClassLoader(),
                                    value,
                                    targetClass == null ? null : targetClass
                                            .getName(),
                                    targetClass == null ? null : targetClass
                                            .getClassLoader() }).initCause(t);
                }
            } else {
                FieldUtil.set(field, target, value);
            }
        } catch (Throwable t) {
            throw new IllegalPropertyRuntimeException(beanDesc.getBeanClass(),
                    propertyName, t);
        }
    }

    public BeanDesc getBeanDesc() {
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
        } else if (arg != null && arg.getClass() != String.class
                && String.class == propertyType) {
            return arg.toString();
        } else if (arg instanceof String && !String.class.equals(propertyType)) {
            return convertWithString(arg);
        } else if (java.util.Calendar.class.isAssignableFrom(propertyType)) {
            return CalendarConversionUtil.toCalendar(arg);
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

    private Object convertWithString(Object arg) {
        if (stringConstructor != null) {
            return ConstructorUtil.newInstance(stringConstructor,
                    new Object[] { arg });
        }
        if (valueOfMethod != null) {
            return MethodUtil.invoke(valueOfMethod, null, new Object[] { arg });
        }
        return arg;
    }

    public boolean isParameterized() {
        return parameterizedClassDesc != null
                && parameterizedClassDesc.isParameterizedClass();
    }

    public ParameterizedClassDesc getParameterizedClassDesc() {
        return parameterizedClassDesc;
    }

    public Class getElementClassOfCollection() {
        if (!Collection.class.isAssignableFrom(propertyType)
                || !isParameterized()) {
            return null;
        }
        final ParameterizedClassDesc pcd = parameterizedClassDesc
                .getArguments()[0];
        if (pcd == null) {
            return null;
        }
        return pcd.getRawClass();
    }

    public Class getKeyClassOfMap() {
        if (!Map.class.isAssignableFrom(propertyType) || !isParameterized()) {
            return null;
        }
        final ParameterizedClassDesc pcd = parameterizedClassDesc
                .getArguments()[0];
        if (pcd == null) {
            return null;
        }
        return pcd.getRawClass();
    }

    public Class getValueClassOfMap() {
        if (!Map.class.isAssignableFrom(propertyType) || !isParameterized()) {
            return null;
        }
        final ParameterizedClassDesc pcd = parameterizedClassDesc
                .getArguments()[1];
        if (pcd == null) {
            return null;
        }
        return pcd.getRawClass();
    }

}
