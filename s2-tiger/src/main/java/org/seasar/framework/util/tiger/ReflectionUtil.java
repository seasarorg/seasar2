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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchConstructorRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.FieldUtil;

/**
 * @author koichik
 */
public abstract class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static Class<?> forName(final String className)
            throws ClassNotFoundRuntimeException {
        return forName(className, Thread.currentThread()
                .getContextClassLoader());
    }

    public static Class<?> forName(final String className,
            final ClassLoader loader) throws ClassNotFoundRuntimeException {
        try {
            return Class.forName(className, true, loader);
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
            final Class... argTypes) {
        try {
            return clazz.getConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, e);
        }
    }

    public static <T> Constructor<T> getDeclaredConstructor(
            final Class<T> clazz, final Class... argTypes) {
        try {
            return clazz.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, e);
        }
    }

    public static Method getMethod(final Class<?> clazz, final String name,
            final Class... argTypes) {
        try {
            return clazz.getMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes, e);
        }
    }

    public static Method getDeclaredMethod(final Class<?> clazz,
            final String name, final Class... argTypes) {
        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes, e);
        }
    }

    public static <T> T newInstance(final Class<T> clazz)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(clazz, e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(clazz, e);
        }
    }

    public static <T> T newInstance(final Constructor<T> constructor,
            final Object... args) throws InstantiationRuntimeException,
            IllegalAccessRuntimeException {
        try {
            return constructor.newInstance(args);
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(constructor
                    .getDeclaringClass(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(final Field field, final Object target) {
        return (T) FieldUtil.get(field, target);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getStaticValue(final Field field) {
        return (T) FieldUtil.get(field, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Method method, final Object target,
            final Object... args) {
        try {
            return (T) method.invoke(target, args);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(method.getDeclaringClass(),
                    e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(method
                    .getDeclaringClass(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeStatic(final Method method, final Object... args) {
        return (T) invoke(method, null, args);
    }

}
