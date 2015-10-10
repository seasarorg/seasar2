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
package org.seasar.framework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

/**
 * {@link Constructor}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class ConstructorUtil {

    /**
     * インスタンスを構築します。
     */
    protected ConstructorUtil() {
    }

    /**
     * 新しいインスタンスを作成します。
     * 
     * @param constructor
     * @param args
     * @return
     * @throws InstantiationRuntimeException
     *             {@link InstantiationException}が起こった場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が起こった場合
     * @throws InvocationTargetRuntimeException
     *             {@link InvocationTargetException}が起こった場合
     */
    public static Object newInstance(Constructor constructor, Object[] args)
            throws InstantiationRuntimeException,
            IllegalAccessRuntimeException, InvocationTargetRuntimeException {

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException ex) {
            throw new InstantiationRuntimeException(constructor
                    .getDeclaringClass(), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(constructor
                    .getDeclaringClass(), ex);
        } catch (InvocationTargetException ex) {
            throw new InvocationTargetRuntimeException(constructor
                    .getDeclaringClass(), ex);
        }
    }
}
