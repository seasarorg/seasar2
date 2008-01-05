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

import java.math.BigDecimal;

import org.seasar.extension.dxo.converter.ConversionContext;

/**
 * 任意のオブジェクトから{@link Boolean}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link Boolean}なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Number 数}なら、その値が0より大きければ<code>true</code>、そうでなければ<code>false</code>とします。</li>
 * <li>それ以外の場合は、変換元オブジェクトの文字列表現が<code>yes</code>、<code>y</code>、<code>true</code>、<code>on</code>、<code>1</code>なら<code>true</code>、
 * そうでなければ<code>false</code>とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class BooleanConverter extends AbstractConverter {

    /** <code>true</code>に評価する文字列の配列です。 */
    protected static final String[] TRUES = new String[] { "yes", "y", "true",
            "on", "1", };

    /** 0を示す定数です。 */
    protected static final BigDecimal ZERO = new BigDecimal("0");

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return Boolean.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Boolean) {
            return source;
        }
        if (source instanceof Number) {
            return toBoolean((Number) source);
        }
        return toBoolean(source.toString());
    }

    /**
     * 文字列を{@link Boolean}に変換して返します。
     * 
     * @param value
     *            変換元の文字列表現
     * @return 変換した結果の{@link Boolean}
     */
    protected Object toBoolean(final String value) {
        for (int i = 0; i < TRUES.length; ++i) {
            if (TRUES[i].equalsIgnoreCase(value)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 数を{@link Boolean}に変換して返します。
     * 
     * @param value
     *            変換元の数
     * @return 変換した結果の{@link Boolean}
     */
    protected Object toBoolean(final Number value) {
        final BigDecimal decimal = new BigDecimal(value.toString());
        if (decimal.compareTo(ZERO) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
