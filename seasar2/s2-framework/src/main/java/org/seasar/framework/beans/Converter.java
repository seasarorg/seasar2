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
package org.seasar.framework.beans;

/**
 * 文字列とオブジェクトの変換を行なうインターフェースです。
 * 
 * @author higa
 * 
 */
public interface Converter {

    /**
     * 値を文字列として返します。
     * 
     * @param value
     *            値
     * @return 文字列としての値
     */
    String getAsString(Object value);

    /**
     * 値をオブジェクトとして返します。
     * 
     * @param value
     *            値
     * @return オブジェクトとしての値
     */
    Object getAsObject(String value);

    /**
     * 対象の型かどうかを返します。 対象のプロパティを指定しない場合に呼び出されます。
     * 
     * @param clazz
     * @return
     */
    boolean isTarget(Class clazz);
}
