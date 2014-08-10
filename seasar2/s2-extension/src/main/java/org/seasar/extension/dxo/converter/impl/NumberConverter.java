/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import java.math.BigDecimal;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.exception.ConversionRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * {@link Number 数}から{@link Short}への変換を行うコンバータの抽象クラスです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link Number}なら、サブクラスによる変換結果を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link CharSequence}なら、それを値とする{@link BigDecimal}からサブクラスが変換した結果を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Boolean}なら、変換元が<code>true</code>なら1、<code>false</code>なら0を変換先とします。</li>
 * <li>変換元のオブジェクトが列挙なら、その序数を変換先とします。</li>
 * <li>それ以外の場合は、{@link ConversionRuntimeException}をスローします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public abstract class NumberConverter extends AbstractConverter {

    /** 変換元が<code>true</code>だった場合の値です。 */
    protected static final Integer TRUE = new Integer(1);

    /** 変換元が<code>false</code>だった場合の値です。 */
    protected static final Integer FALSE = new Integer(0);

    public Class[] getSourceClasses() {
        return new Class[] { Number.class, CharSequence.class, Boolean.class,
                Object.class };
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Number) {
            return convert((Number) source);
        }
        if (source instanceof CharSequence) {
            return convert(source.toString());
        }
        if (source instanceof Boolean) {
            final boolean b = ((Boolean) source).booleanValue();
            if (b) {
                return convert(TRUE);
            }
            return convert(FALSE);
        }
        if (isEnum(source.getClass())) {
            return convert(new Integer(getEnumOrdinal(source)));
        }
        throw new ConversionRuntimeException(source.getClass());
    }

    /**
     * 数値を変換して返します。
     * 
     * @param number
     *            変換元の数値
     * @return 変換結果の数値
     */
    protected abstract Number convert(Number number);

    /**
     * 数を表す文字列から数値に変換して返します。
     * 
     * @param number
     *            数を表す文字列
     * @return 変換結果の数値
     */
    protected Number convert(final String number) {
        if (StringUtil.isEmpty(number)) {
            return null;
        }
        final BigDecimal decimal = new BigDecimal(number);
        return convert(decimal);
    }

}
