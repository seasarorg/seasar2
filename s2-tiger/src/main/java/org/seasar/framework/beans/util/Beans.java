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

/**
 * JavaBeans用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class Beans {

    /**
     * インスタンスを構築します。
     */
    protected Beans() {
    }

    /**
     * プロパティをコピーするオブジェクトを作成します。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     * @return コピー用のオブジェクト
     */
    public static Copy copy(Object src, Object dest) {
        return new Copy(src, dest);
    }

    /**
     * JavaBeansやMapを作成しプロパティをコピーするオブジェクトを作成します。
     * 
     * @param <T>
     * 
     * @param destClass
     *            作成対象クラス
     * @param src
     *            コピー元
     * @return 作成用のオブジェクト
     */
    public static <T> CreateAndCopy<T> createAndCopy(Class<T> destClass, Object src) {
        return new CreateAndCopy<T>(destClass, src);
    }
}
