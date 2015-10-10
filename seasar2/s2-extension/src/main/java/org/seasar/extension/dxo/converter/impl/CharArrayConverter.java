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
package org.seasar.extension.dxo.converter.impl;

import org.seasar.extension.dxo.converter.ConversionContext;

/**
 * 任意のオブジェクトから{@link String}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが<code>char</code>の配列なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが{@link String}なら、変換元の持つ文字配列を変換先とします。</li>
 * <li>それ以外の場合は、変換元オブジェクトの文字列表現が持つ文字配列を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class CharArrayConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return char[].class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof char[]) {
            if (shallowCopy) {
                return source;
            }
            final char[] sourceArray = (char[]) source;
            final char[] dest = new char[sourceArray.length];
            System.arraycopy(sourceArray, 0, dest, 0, sourceArray.length);
            return sourceArray;
        }
        if (source instanceof String) {
            return ((String) source).toCharArray();
        }
        return source.toString().toCharArray();
    }

}
