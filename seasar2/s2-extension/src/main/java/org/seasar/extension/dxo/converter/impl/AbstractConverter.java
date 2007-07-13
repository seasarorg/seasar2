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
package org.seasar.extension.dxo.converter.impl;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;

/**
 * {@link Converter}の抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractConverter implements Converter {

    public void convert(Object source, Object dest, ConversionContext context) {
        throw new UnsupportedOperationException();
    }

    /**
     * <code>clazz</code>が列挙である場合に<code>true</code>を返します。
     * 
     * @param clazz
     *            クラス
     * @return <code>clazz</code>が列挙である場合に<code>true</code>
     */
    protected boolean isEnum(Class clazz) {
        return TigerSupport.instance != null
                && TigerSupport.instance.isEnum(clazz);
    }

    /**
     * 列挙<code>o</code>の列挙定数の序数を返します。
     * 
     * @param o
     *            列挙
     * @return 列挙定数の序数
     * @throws ClassCastException
     *             <code>o</code>が列挙でない場合にスローされます
     * @see java.lang.Enum#ordinal()
     */
    protected int getEnumOrdinal(Object o) {
        return TigerSupport.instance.getEnumOrdinal(o);
    }

    /**
     * 列挙<code>o</code>の列挙定数の名前を返します。
     * 
     * @param o
     *            列挙
     * @return 列挙定数の名前
     * @throws ClassCastException
     *             <code>o</code>が列挙でない場合にスローされます
     * @see java.lang.Enum#name()
     */
    protected String getEnumName(Object o) {
        return TigerSupport.instance.getEnumName(o);
    }

}
