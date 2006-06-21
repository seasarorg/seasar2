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

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchConstructorRuntimeException;
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
        } catch (final ClassNotFoundException ex) {
            throw new ClassNotFoundRuntimeException(ex);
        }
    }

    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
            final Class... argTypes) {
        try {
            return clazz.getConstructor(argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, ex);
        }
    }

    public static <T> Constructor<T> getDeclaredConstructor(
            final Class<T> clazz, final Class... argTypes) {
        try {
            return clazz.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, ex);
        }
    }

    public static <T> T newInstance(final Class<T> clazz)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException ex) {
            throw new InstantiationRuntimeException(clazz, ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(clazz, ex);
        }
    }

    public static <T> T newInstance(final Constructor<T> constructor,
            final Object... args) throws InstantiationRuntimeException,
            IllegalAccessRuntimeException {
        try {
            return constructor.newInstance(args);
        } catch (final InstantiationException ex) {
            throw new InstantiationRuntimeException(constructor
                    .getDeclaringClass(), ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(constructor
                    .getDeclaringClass(), ex);
        } catch (final InvocationTargetException ex) {
            throw new InvocationTargetRuntimeException(constructor
                    .getDeclaringClass(), ex);
        }
    }

    public static <T> T getValue(final Field field, final Object target) {
        return (T) FieldUtil.get(field, target);
    }
}
