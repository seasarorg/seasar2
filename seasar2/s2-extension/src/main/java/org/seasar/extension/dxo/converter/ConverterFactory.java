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
 * {@link org.seasar.extension.dxo.converter.Converter コンバータ}のファクトリクラスです。
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public interface ConverterFactory {

    /**
     * <code>sourceClass</code>から<code>destClass</code>への変換が可能なコンバータを返します。
     * <p>
     * 該当するコンバータが複数ある場合は、最も適合するコンバータが選択されます。
     * </p>
     * 
     * @param sourceClass
     *            変換元のクラス
     * @param destClass
     *            変換先のクラス
     * @return コンバータ
     */
    Converter getConverter(Class sourceClass, Class destClass);

}
