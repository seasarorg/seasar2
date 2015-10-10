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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.ConstructorNotFoundRuntimeException;
import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.beans.IllegalDiiguRuntimeException;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.beans.factory.ParameterizedClassDescFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.ClassPoolUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;
import org.seasar.framework.util.DoubleConversionUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.FloatConversionUtil;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.LongConversionUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ShortConversionUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link BeanDesc}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class BeanDescImpl implements BeanDesc {

    private static final Logger logger = Logger.getLogger(BeanDescImpl.class);

    private static final Object[] EMPTY_ARGS = new Object[0];

    private static final Class[] EMPTY_PARAM_TYPES = new Class[0];

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String PARAMETER_NAME_ANNOTATION = "org.seasar.framework.beans.annotation.ParameterName";

    private Class beanClass;

    private Constructor[] constructors;

    private Map typeVariables;

    private CaseInsensitiveMap propertyDescCache = new CaseInsensitiveMap();

    private Map methodsCache = new HashMap();

    private ArrayMap fieldCache = new ArrayMap();

    private transient Set invalidPropertyNames = new HashSet();

    private Map constructorParameterNamesCache;

    private Map methodParameterNamesCache;

    /**
     * {@link BeanDescImpl}を作成します。
     * 
     * @param beanClass
     * @throws EmptyRuntimeException
     */
    public BeanDescImpl(Class beanClass) throws EmptyRuntimeException {
        if (beanClass == null) {
            throw new EmptyRuntimeException("beanClass");
        }
        this.beanClass = beanClass;
        constructors = beanClass.getConstructors();
        typeVariables = ParameterizedClassDescFactory
                .getTypeVariables(beanClass);
        setupPropertyDescs();
        setupMethods();
        setupFields();
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getBeanClass()
     */
    public Class getBeanClass() {
        return beanClass;
    }

    public boolean hasPropertyDesc(String propertyName) {
        return propertyDescCache.get(propertyName) != null;
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getPropertyDesc(java.lang.String)
     */
    public PropertyDesc getPropertyDesc(String propertyName)
            throws PropertyNotFoundRuntimeException {

        PropertyDesc pd = (PropertyDesc) propertyDescCache.get(propertyName);
        if (pd == null) {
            throw new PropertyNotFoundRuntimeException(beanClass, propertyName);
        }
        return pd;
    }

    private PropertyDesc getPropertyDesc0(String propertyName) {
        return (PropertyDesc) propertyDescCache.get(propertyName);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getPropertyDesc(int)
     */
    public PropertyDesc getPropertyDesc(int index) {
        return (PropertyDesc) propertyDescCache.get(index);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getPropertyDescSize()
     */
    public int getPropertyDescSize() {
        return propertyDescCache.size();
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#hasField(java.lang.String)
     */
    public boolean hasField(String fieldName) {
        return fieldCache.get(fieldName) != null;
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getField(java.lang.String)
     */
    public Field getField(String fieldName) {
        Field field = (Field) fieldCache.get(fieldName);
        if (field == null) {
            throw new FieldNotFoundRuntimeException(beanClass, fieldName);
        }
        return field;
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getField(int)
     */
    public Field getField(int index) {
        return (Field) fieldCache.get(index);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getFieldValue(java.lang.String,
     *      java.lang.Object)
     */
    public Object getFieldValue(String fieldName, Object target)
            throws FieldNotFoundRuntimeException {
        Field field = getField(fieldName);
        return FieldUtil.get(field, target);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getFieldSize()
     */
    public int getFieldSize() {
        return fieldCache.size();
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#newInstance(java.lang.Object[])
     */
    public Object newInstance(Object[] args)
            throws ConstructorNotFoundRuntimeException {

        Constructor constructor = getSuitableConstructor(args);
        return ConstructorUtil.newInstance(constructor, args);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#invoke(java.lang.Object,
     *      java.lang.String, java.lang.Object[])
     */
    public Object invoke(Object target, String methodName, Object[] args) {
        Method method = getSuitableMethod(methodName, args);
        return MethodUtil.invoke(method, target, args);
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getSuitableConstructor(java.lang.Object[])
     */
    public Constructor getSuitableConstructor(Object[] args)
            throws ConstructorNotFoundRuntimeException {

        if (args == null) {
            args = EMPTY_ARGS;
        }
        Constructor constructor = findSuitableConstructor(args);
        if (constructor != null) {
            return constructor;
        }
        constructor = findSuitableConstructorAdjustNumber(args);
        if (constructor != null) {
            return constructor;
        }
        throw new ConstructorNotFoundRuntimeException(beanClass, args);
    }

    public Constructor getConstructor(final Class[] paramTypes) {
        for (int i = 0; i < constructors.length; ++i) {
            if (Arrays.equals(paramTypes, constructors[i].getParameterTypes())) {
                return constructors[i];
            }
        }
        throw new ConstructorNotFoundRuntimeException(beanClass, paramTypes);
    }

    public Method getMethod(final String methodName) {
        return getMethod(methodName, EMPTY_PARAM_TYPES);
    }

    public Method getMethodNoException(final String methodName) {
        return getMethodNoException(methodName, EMPTY_PARAM_TYPES);
    }

    public Method getMethod(final String methodName, final Class[] paramTypes) {
        Method method = getMethodNoException(methodName, paramTypes);
        if (method != null) {
            return method;
        }
        throw new MethodNotFoundRuntimeException(beanClass, methodName,
                paramTypes);
    }

    public Method getMethodNoException(final String methodName,
            final Class[] paramTypes) {
        final Method[] methods = (Method[]) methodsCache.get(methodName);
        if (methods == null) {
            return null;
        }
        for (int i = 0; i < methods.length; ++i) {
            if (Arrays.equals(paramTypes, methods[i].getParameterTypes())) {
                return methods[i];
            }
        }
        return null;
    }

    /**
     * @see org.seasar.framework.beans.BeanDesc#getMethods(java.lang.String)
     */
    public Method[] getMethods(String methodName)
            throws MethodNotFoundRuntimeException {

        Method[] methods = (Method[]) methodsCache.get(methodName);
        if (methods == null) {
            throw new MethodNotFoundRuntimeException(beanClass, methodName,
                    null);
        }
        return methods;
    }

    public boolean hasMethod(String methodName) {
        return methodsCache.get(methodName) != null;
    }

    public String[] getMethodNames() {
        return (String[]) methodsCache.keySet().toArray(
                new String[methodsCache.size()]);
    }

    public String[] getConstructorParameterNames(final Class[] parameterTypes) {
        return getConstructorParameterNames(getConstructor(parameterTypes));
    }

    public String[] getConstructorParameterNames(final Constructor constructor) {
        if (constructorParameterNamesCache == null) {
            constructorParameterNamesCache = createConstructorParameterNamesCache();
        }

        if (!constructorParameterNamesCache.containsKey(constructor)) {
            throw new ConstructorNotFoundRuntimeException(beanClass,
                    constructor.getParameterTypes());
        }
        return (String[]) constructorParameterNamesCache.get(constructor);

    }

    public String[] getMethodParameterNamesNoException(final String methodName,
            final Class[] parameterTypes) {
        return getMethodParameterNamesNoException(getMethod(methodName,
                parameterTypes));
    }

    public String[] getMethodParameterNames(final String methodName,
            final Class[] parameterTypes) {
        return getMethodParameterNames(getMethod(methodName, parameterTypes));
    }

    public String[] getMethodParameterNames(final Method method) {
        String[] names = getMethodParameterNamesNoException(method);
        if (names == null || names.length != method.getParameterTypes().length) {
            throw new IllegalDiiguRuntimeException();
        }
        return names;
    }

    public String[] getMethodParameterNamesNoException(final Method method) {
        if (methodParameterNamesCache == null) {
            methodParameterNamesCache = createMethodParameterNamesCache();
        }

        if (!methodParameterNamesCache.containsKey(method)) {
            throw new MethodNotFoundRuntimeException(beanClass, method
                    .getName(), method.getParameterTypes());
        }
        return (String[]) methodParameterNamesCache.get(method);
    }

    private Map createConstructorParameterNamesCache() {
        final Map map = new HashMap();
        final ClassPool pool = ClassPoolUtil.getClassPool(beanClass);
        for (int i = 0; i < constructors.length; ++i) {
            final Constructor constructor = constructors[i];
            if (constructor.getParameterTypes().length == 0) {
                map.put(constructor, EMPTY_STRING_ARRAY);
                continue;
            }
            final CtClass clazz = ClassPoolUtil.toCtClass(pool, constructor
                    .getDeclaringClass());
            final CtClass[] parameterTypes = ClassPoolUtil.toCtClassArray(pool,
                    constructor.getParameterTypes());
            try {
                final String[] names = getParameterNames(clazz
                        .getDeclaredConstructor(parameterTypes));
                map.put(constructor, names);
            } catch (final NotFoundException e) {
                logger.log("WSSR0084", new Object[] { beanClass.getName(),
                        constructor });
            }
        }
        return map;
    }

    private Map createMethodParameterNamesCache() {
        final Map map = new HashMap();
        final ClassPool pool = ClassPoolUtil.getClassPool(beanClass);
        for (final Iterator it = methodsCache.values().iterator(); it.hasNext();) {
            final Method[] methods = (Method[]) it.next();
            for (int i = 0; i < methods.length; ++i) {
                final Method method = methods[i];
                if (method.getParameterTypes().length == 0) {
                    map.put(methods[i], EMPTY_STRING_ARRAY);
                    continue;
                }
                final CtClass clazz = ClassPoolUtil.toCtClass(pool, method
                        .getDeclaringClass());
                final CtClass[] parameterTypes = ClassPoolUtil.toCtClassArray(
                        pool, method.getParameterTypes());
                try {
                    final String[] names = getParameterNames(clazz
                            .getDeclaredMethod(method.getName(), parameterTypes));
                    map.put(methods[i], names);
                } catch (final NotFoundException e) {
                    logger.log("WSSR0085", new Object[] { beanClass.getName(),
                            method });
                }
            }
        }
        return map;
    }

    private String[] getParameterNames(final CtBehavior behavior)
            throws NotFoundException {
        final MethodInfo methodInfo = behavior.getMethodInfo();
        final ParameterAnnotationsAttribute attribute = (ParameterAnnotationsAttribute) methodInfo
                .getAttribute(ParameterAnnotationsAttribute.visibleTag);
        if (attribute == null) {
            return null;
        }
        final int numParameters = behavior.getParameterTypes().length;
        final String[] parameterNames = new String[numParameters];
        final Annotation[][] annotationsArray = attribute.getAnnotations();
        if (annotationsArray == null
                || annotationsArray.length != numParameters) {
            return null;
        }
        for (int i = 0; i < numParameters; ++i) {
            final String parameterName = getParameterName(annotationsArray[i]);
            if (parameterName == null) {
                return null;
            }
            parameterNames[i] = parameterName;
        }
        return parameterNames;
    }

    private String getParameterName(final Annotation[] annotations) {
        Annotation nameAnnotation = null;
        for (int i = 0; i < annotations.length; ++i) {
            final Annotation annotation = annotations[i];
            if (PARAMETER_NAME_ANNOTATION.equals(annotation.getTypeName())) {
                nameAnnotation = annotation;
                break;
            }
        }
        if (nameAnnotation == null) {
            return null;
        }
        return ((StringMemberValue) nameAnnotation.getMemberValue("value"))
                .getValue();
    }

    private Constructor findSuitableConstructor(Object[] args) {
        outerLoop: for (int i = 0; i < constructors.length; ++i) {
            Class[] paramTypes = constructors[i].getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }
            for (int j = 0; j < args.length; ++j) {
                if (args[j] == null
                        || ClassUtil.isAssignableFrom(paramTypes[j], args[j]
                                .getClass())) {
                    continue;
                }
                continue outerLoop;
            }
            return constructors[i];
        }
        return null;
    }

    private Constructor findSuitableConstructorAdjustNumber(Object[] args) {
        outerLoop: for (int i = 0; i < constructors.length; ++i) {
            Class[] paramTypes = constructors[i].getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }
            for (int j = 0; j < args.length; ++j) {
                if (args[j] == null
                        || ClassUtil.isAssignableFrom(paramTypes[j], args[j]
                                .getClass())
                        || adjustNumber(paramTypes, args, j)) {
                    continue;
                }
                continue outerLoop;
            }
            return constructors[i];
        }
        return null;
    }

    private static boolean adjustNumber(Class[] paramTypes, Object[] args,
            int index) {

        if (paramTypes[index].isPrimitive()) {
            if (paramTypes[index] == int.class) {
                args[index] = IntegerConversionUtil.toInteger(args[index]);
                return true;
            } else if (paramTypes[index] == double.class) {
                args[index] = DoubleConversionUtil.toDouble(args[index]);
                return true;
            } else if (paramTypes[index] == long.class) {
                args[index] = LongConversionUtil.toLong(args[index]);
                return true;
            } else if (paramTypes[index] == short.class) {
                args[index] = ShortConversionUtil.toShort(args[index]);
                return true;
            } else if (paramTypes[index] == float.class) {
                args[index] = FloatConversionUtil.toFloat(args[index]);
                return true;
            }
        } else {
            if (paramTypes[index] == Integer.class) {
                args[index] = IntegerConversionUtil.toInteger(args[index]);
                return true;
            } else if (paramTypes[index] == Double.class) {
                args[index] = DoubleConversionUtil.toDouble(args[index]);
                return true;
            } else if (paramTypes[index] == Long.class) {
                args[index] = LongConversionUtil.toLong(args[index]);
                return true;
            } else if (paramTypes[index] == Short.class) {
                args[index] = ShortConversionUtil.toShort(args[index]);
                return true;
            } else if (paramTypes[index] == Float.class) {
                args[index] = FloatConversionUtil.toFloat(args[index]);
                return true;
            }
        }
        return false;
    }

    private void setupPropertyDescs() {
        Method[] methods = beanClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (MethodUtil.isBridgeMethod(m) || MethodUtil.isSyntheticMethod(m)) {
                continue;
            }
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                if (m.getParameterTypes().length != 0
                        || methodName.equals("getClass")
                        || m.getReturnType() == void.class) {
                    continue;
                }
                String propertyName = decapitalizePropertyName(methodName
                        .substring(3));
                setupReadMethod(m, propertyName);
            } else if (methodName.startsWith("is")) {
                if (m.getParameterTypes().length != 0
                        || !m.getReturnType().equals(Boolean.TYPE)
                        && !m.getReturnType().equals(Boolean.class)) {
                    continue;
                }
                String propertyName = decapitalizePropertyName(methodName
                        .substring(2));
                setupReadMethod(m, propertyName);
            } else if (methodName.startsWith("set")) {
                if (m.getParameterTypes().length != 1
                        || methodName.equals("setClass")
                        || m.getReturnType() != void.class) {
                    continue;
                }
                String propertyName = decapitalizePropertyName(methodName
                        .substring(3));
                setupWriteMethod(m, propertyName);
            }
        }
        for (Iterator i = invalidPropertyNames.iterator(); i.hasNext();) {
            propertyDescCache.remove(i.next());
        }
        invalidPropertyNames.clear();
    }

    private static String decapitalizePropertyName(String name) {
        if (StringUtil.isEmpty(name)) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
                && Character.isUpperCase(name.charAt(0))) {

            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private void addPropertyDesc(PropertyDesc propertyDesc)
            throws EmptyRuntimeException {

        if (propertyDesc == null) {
            throw new EmptyRuntimeException("propertyDesc");
        }
        propertyDescCache.put(propertyDesc.getPropertyName(), propertyDesc);
    }

    private void setupReadMethod(Method readMethod, String propertyName) {
        Class propertyType = readMethod.getReturnType();
        PropertyDesc propDesc = getPropertyDesc0(propertyName);
        if (propDesc != null) {
            if (!propDesc.getPropertyType().equals(propertyType)) {
                invalidPropertyNames.add(propertyName);
            } else {
                propDesc.setReadMethod(readMethod);
            }
        } else {
            propDesc = new PropertyDescImpl(propertyName, propertyType,
                    readMethod, null, null, this);
            addPropertyDesc(propDesc);
        }
    }

    private void setupWriteMethod(Method writeMethod, String propertyName) {
        Class propertyType = writeMethod.getParameterTypes()[0];
        PropertyDesc propDesc = getPropertyDesc0(propertyName);
        if (propDesc != null) {
            if (!propDesc.getPropertyType().equals(propertyType)) {
                invalidPropertyNames.add(propertyName);
            } else {
                propDesc.setWriteMethod(writeMethod);
            }
        } else {
            propDesc = new PropertyDescImpl(propertyName, propertyType, null,
                    writeMethod, null, this);
            addPropertyDesc(propDesc);
        }
    }

    private Method getSuitableMethod(String methodName, Object[] args)
            throws MethodNotFoundRuntimeException {

        if (args == null) {
            args = EMPTY_ARGS;
        }
        Method[] methods = getMethods(methodName);
        Method method = findSuitableMethod(methods, args);
        if (method != null) {
            return method;
        }
        method = findSuitableMethodAdjustNumber(methods, args);
        if (method != null) {
            return method;
        }
        throw new MethodNotFoundRuntimeException(beanClass, methodName, args);
    }

    private Method findSuitableMethod(Method[] methods, Object[] args) {
        outerLoop: for (int i = 0; i < methods.length; ++i) {
            Class[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }
            for (int j = 0; j < args.length; ++j) {
                if (args[j] == null
                        || ClassUtil.isAssignableFrom(paramTypes[j], args[j]
                                .getClass())) {
                    continue;
                }
                continue outerLoop;
            }
            return methods[i];
        }
        return null;
    }

    private Method findSuitableMethodAdjustNumber(Method[] methods,
            Object[] args) {

        outerLoop: for (int i = 0; i < methods.length; ++i) {
            Class[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }
            for (int j = 0; j < args.length; ++j) {
                if (args[j] == null
                        || ClassUtil.isAssignableFrom(paramTypes[j], args[j]
                                .getClass())
                        || adjustNumber(paramTypes, args, j)) {
                    continue;
                }
                continue outerLoop;
            }
            return methods[i];
        }
        return null;
    }

    private void setupMethods() {
        ArrayMap methodListMap = new ArrayMap();
        Method[] methods = beanClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (MethodUtil.isBridgeMethod(method)
                    || MethodUtil.isSyntheticMethod(method)) {
                continue;
            }
            String methodName = method.getName();
            List list = (List) methodListMap.get(methodName);
            if (list == null) {
                list = new ArrayList();
                methodListMap.put(methodName, list);
            }
            list.add(method);
        }
        for (int i = 0; i < methodListMap.size(); ++i) {
            List methodList = (List) methodListMap.get(i);
            methodsCache.put(methodListMap.getKey(i), methodList
                    .toArray(new Method[methodList.size()]));
        }
    }

    /*
     * private void setupField() { for (Class clazz = beanClass_; clazz !=
     * Object.class && clazz != null; clazz = clazz.getSuperclass()) {
     * 
     * Field[] fields = clazz.getDeclaredFields(); for (int i = 0; i <
     * fields.length; ++i) { Field field = fields[i]; String fname =
     * field.getName(); if (!fieldCache_.containsKey(fname)) {
     * fieldCache_.put(fname, field); } } } }
     */
    private void setupFields() {
        setupFields(beanClass);
    }

    private void setupFields(Class targetClass) {
        if (targetClass.isInterface()) {
            setupFieldsByInterface(targetClass);
        } else {
            setupFieldsByClass(targetClass);
        }
    }

    private void setupFieldsByInterface(Class interfaceClass) {
        addFields(interfaceClass);
        Class[] interfaces = interfaceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            setupFieldsByInterface(interfaces[i]);
        }
    }

    private void addFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            String fname = field.getName();
            if (!fieldCache.containsKey(fname)) {
                field.setAccessible(true);
                fieldCache.put(fname, field);
                if (FieldUtil.isInstanceField(field)) {
                    if (hasPropertyDesc(fname)) {
                        PropertyDesc pd = getPropertyDesc(field.getName());
                        pd.setField(field);
                    } else if (FieldUtil.isPublicField(field)) {
                        PropertyDesc pd = new PropertyDescImpl(field.getName(),
                                field.getType(), null, null, field, this);
                        propertyDescCache.put(fname, pd);
                    }
                }
            }
        }
    }

    private void setupFieldsByClass(Class targetClass) {
        addFields(targetClass);
        Class[] interfaces = targetClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            setupFieldsByInterface(interfaces[i]);
        }
        Class superClass = targetClass.getSuperclass();
        if (superClass != Object.class && superClass != null) {
            setupFieldsByClass(superClass);
        }
    }
    /*
     * private void setupField() { Field[] fields = beanClass_.getFields(); for
     * (int i = 0; i < fields.length; i++) { if
     * (Modifier.isStatic(fields[i].getModifiers())) {
     * fieldCache_.put(fields[i].getName(), fields[i]); } } }
     */

    Map getTypeVariables() {
        return typeVariables;
    }

}
