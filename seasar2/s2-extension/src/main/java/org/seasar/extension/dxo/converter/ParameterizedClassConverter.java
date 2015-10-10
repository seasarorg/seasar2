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
package org.seasar.extension.dxo.converter;

import org.seasar.framework.beans.ParameterizedClassDesc;

/**
 * 変換元クラスのインスタンスをパラメタ化された変換先クラスのインスタンスに変換するコンバータです。
 * 
 * @since 2.4.18
 * @author koichik
 */
public interface ParameterizedClassConverter extends Converter {

    /**
     * <code>source</code>をパラメタ化された型の<code>dest</code>に変換します。
     * <p>
     * このメソッドはJavaBeansや配列，{@link java.util.List}など、
     * 可変なオブジェクトへの変換を行うコンバータでサポートされます。 不変なオブジェクトへの変換を行うコンバータはこのメソッドを実装できないため、
     * このメソッドが呼び出されると{@link UnsupportedOperationException}がスローされます。
     * </p>
     * 
     * @param source
     *            変換元のオブジェクト
     * @param dest
     *            変換先のオブジェクト
     * @param parameterizedClassDesc
     *            変換先のパラメタ化された型の情報
     * @param context
     *            変換コンテキスト
     * @throws UnsupportedOperationException
     *             この変換がサポートされていない場合にスローします
     */
    void convert(Object source, Object dest,
            ParameterizedClassDesc parameterizedClassDesc,
            ConversionContext context);

    /**
     * <code>source</code>をパラメタ化された型の<code>destClass</code>に変換したオブジェクトを返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @param destClass
     *            変換先のクラス
     * @param parameterizedClassDesc
     *            変換先のパラメタ化された型の情報
     * @param context
     *            変換コンテキスト
     * @return 変換先のオブジェクト。これは<code>destClass</code>のインスタンスです。
     */
    Object convert(Object source, Class destClass,
            ParameterizedClassDesc parameterizedClassDesc,
            ConversionContext context);

}
