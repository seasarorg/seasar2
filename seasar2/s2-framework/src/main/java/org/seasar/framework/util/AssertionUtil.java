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

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * 表明についてのユーティリティクラスです。
 * 
 * @author shot
 */
public class AssertionUtil {

    /**
     * インスタンスを構築します。
     */
    protected AssertionUtil() {
    }

    /**
     * <code>null</code>でないことを表明します。
     * 
     * @param message
     * @param obj
     * @throws NullPointerException
     *             <code>null</code>の場合。
     */
    public static void assertNotNull(String message, Object obj)
            throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 文字列が空あるいは<code>null</code>でないことを表明します。
     * 
     * @param message
     * @param s
     * @throws EmptyRuntimeException
     *             文字列が空あるいは<code>null</code>の場合。
     */
    public static void assertNotEmpty(String message, String s)
            throws EmptyRuntimeException {
        if (StringUtil.isEmpty(s)) {
            throw new EmptyRuntimeException(message);
        }
    }

    /**
     * <code>int</code>が負でないことを表明します。
     * 
     * @param message
     * @param num
     * @throws IllegalArgumentException
     *             <code>int</code>が負の場合。
     */
    public static void assertIntegerNotNegative(String message, int num)
            throws IllegalArgumentException {
        if (num < 0) {
            throw new IllegalArgumentException(message);
        }
    }

}
