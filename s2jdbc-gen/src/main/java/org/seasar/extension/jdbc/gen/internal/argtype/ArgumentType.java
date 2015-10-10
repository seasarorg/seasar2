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
package org.seasar.extension.jdbc.gen.internal.argtype;

/**
 * コマンドラインの引数の型を表すインタフェースです。
 * 
 * @author taedium
 * @param <T>
 *            引数の型
 */
public interface ArgumentType<T> {

    /**
     * 引数の文字列を適切な型のオブジェクトに変換します。
     * 
     * @param value
     *            引数の文字列
     * @return オブジェクト
     */
    T toObject(String value);

    /**
     * 引数のオブジェクトを文字列に変換します。
     * 
     * @param value
     *            引数のオブジェクト
     * @return 文字列
     */
    String toText(T value);

}
