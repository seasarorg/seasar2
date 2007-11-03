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
package org.seasar.framework.beans.util;

import java.util.Map;

/**
 * Java5用のJavaBeansのユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public final class TigerBeanUtil {

    /**
     * マップの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(Map<String, Object> src, Object dest) {
        BeanUtil.copyProperties(src, dest);
    }

    /**
     * JavaBeansの値をマップにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(Object src, Map<String, Object> dest) {
        BeanUtil.copyProperties(src, dest);
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(Object src, Object dest) {
        BeanUtil.copyProperties(src, dest, true);
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     * @param includeNull
     *            <code>null</code>を含めるかどうか
     */
    public static void copyProperties(Object src, Object dest,
            boolean includeNull) {
        BeanUtil.copyProperties(src, dest, includeNull);
    }

    /**
     * JavaBeansの値からマップを作成します。
     * 
     * @param src
     *            ソース
     * @return JavaBeansの値を持つマップ
     */
    public static Map<String, Object> createProperties(Object src) {
        return createProperties(src, null);
    }

    /**
     * JavaBeansの値からマップを作成します。
     * 
     * @param src
     *            ソース
     * @param prefix
     *            プレフィックス
     * @return JavaBeansの値を持つマップ
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> createProperties(Object src, String prefix) {
        return BeanUtil.createProperties(src, prefix);
    }
}