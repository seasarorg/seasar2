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
package org.seasar.framework.beans.util;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ModifierUtil;

/**
 * JavaBeansやMapを作成し、プロパティをコピーするクラスです。
 * 
 * @author higa
 * @param <T>
 *            作成するタイプ
 * 
 */
public class CreateAndCopy<T> extends AbstractCopy<CreateAndCopy<T>> {

    /**
     * 作成対象クラス
     */
    protected Class<T> destClass;

    /**
     * コピー元です。
     */
    protected Object src;

    /**
     * インスタンスを構築します。
     * 
     * @param destClass
     *            作成対象クラス
     * @param src
     *            コピー元
     * @throws NullPointerException
     *             引数が<code>null</code>だった場合
     */
    public CreateAndCopy(Class<T> destClass, Object src)
            throws NullPointerException {
        if (destClass == null) {
            throw new NullPointerException("destClass");
        }
        if (src == null) {
            throw new NullPointerException("src");
        }
        this.destClass = destClass;
        this.src = src;
    }

    /**
     * JavaBeansやMapを作成し、プロパティをコピーします。
     * 
     * @return 作成結果
     */
    @SuppressWarnings("unchecked")
    public T execute() {
        if (Map.class.isAssignableFrom(destClass)) {
            Map dest = null;
            if (ModifierUtil.isAbstract(destClass)) {
                dest = new HashMap();
            } else {
                dest = (Map) ClassUtil.newInstance(destClass);
            }
            if (src instanceof Map) {
                copyMapToMap((Map) src, dest);
            } else {
                copyBeanToMap(src, dest);
            }
            return (T) dest;
        }
        T dest = (T) ClassUtil.newInstance(destClass);
        if (src instanceof Map) {
            copyMapToBean((Map) src, dest);
        } else {
            copyBeanToBean(src, dest);
        }
        return dest;
    }
}