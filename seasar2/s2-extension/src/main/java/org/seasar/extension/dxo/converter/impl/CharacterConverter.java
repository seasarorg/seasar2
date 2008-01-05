/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
 * 任意のオブジェクトから{@link Character}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link Character}なら、変換元をそのまま変換先とします。</li>
 * <li>それ以外の場合は、<code>null</code>とします。</li>
 * </ul>
 * <p>
 * <i>もうちょっと頑張ってもいいかも</i>
 * </p>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class CharacterConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return Character.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Character) {
            return source;
        }
        return null;
    }

}
