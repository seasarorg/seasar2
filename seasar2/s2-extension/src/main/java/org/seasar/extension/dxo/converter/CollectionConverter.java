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
package org.seasar.extension.dxo.converter;

/**
 * 変換元クラスのインスタンスを変換先コレクションクラスのインスタンスに変換するコンバータを表します。
 * 
 * @author koichik
 */
public interface CollectionConverter extends Converter {

    /**
     * <code>source</code>を<code>destElementClass</code>を要素とするコレクション<code>dest</code>に変換します。
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
     * @param destElementClass
     *            変換先の要素の型
     * @param context
     *            変換コンテキスト
     * @throws UnsupportedOperationException
     *             この変換がサポートされていない場合にスローします
     */
    void convert(Object source, Object dest, Class destElementClass,
            ConversionContext context);

    /**
     * <code>source</code>を<code>destElementClass</code>を要素とする<code>destClass</code>型のコレクションに変換したオブジェクトを返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @param destClass
     *            変換先のクラス
     * @param destElementClass
     *            変換先の要素の型
     * @param context
     *            変換コンテキスト
     * @return 変換先のオブジェクト。これは<code>destClass</code>のインスタンスです。
     */
    Object convert(Object source, Class destClass, Class destElementClass,
            ConversionContext context);

}
