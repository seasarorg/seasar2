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
package org.seasar.extension.jdbc.gen.internal.util;

import org.seasar.extension.jdbc.gen.internal.exception.ClassUnmatchRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * リフレクションに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class ReflectUtil {

    /**
     * 
     */
    protected ReflectUtil() {
    }

    /**
     * インスタンス化します。
     * 
     * @param <T>
     *            期待するクラスの型
     * @param expectedClass
     *            期待するクラス
     * @param className
     *            インスタンス化対象のクラス名
     * @return 期待するクラスのインスタンス
     */
    public static <T> T newInstance(Class<T> expectedClass, String className) {
        Class<?> actualClass = ClassUtil.forName(className);
        Object obj = ClassUtil.newInstance(actualClass);
        if (expectedClass.isInstance(obj)) {
            return expectedClass.cast(obj);
        }
        throw new ClassUnmatchRuntimeException(expectedClass, actualClass);
    }
}
