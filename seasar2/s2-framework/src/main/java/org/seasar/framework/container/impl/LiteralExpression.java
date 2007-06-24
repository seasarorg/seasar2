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
package org.seasar.framework.container.impl;

import java.util.Map;

import org.seasar.framework.container.S2Container;

/**
 * ソースがリテラルを表す{@link org.seasar.framework.container.Expression}の実装クラスです。
 * 
 * @author koichik
 */
public class LiteralExpression extends AbstractExpression {

    /**
     * 値です。
     */
    protected Object value;

    /**
     * <code>LiteralExpression</code>のインスタンスを構築します。
     * 
     * @param source
     *            式のソース
     * @param value
     *            値
     */
    public LiteralExpression(final String source, final Object value) {
        super(source);
        this.value = value;
    }

    /**
     * <code>LiteralExpression</code>のインスタンスを構築します。
     * 
     * @param sourceName
     *            ソースファイル名
     * @param lineNumber
     *            ソースファイル中の行番号
     * @param source
     *            式のソース
     * @param value
     *            値
     */
    public LiteralExpression(final String sourceName, final int lineNumber,
            final String source, final Object value) {
        super(sourceName, lineNumber, source);
        this.value = value;
    }

    public Object evaluate(final S2Container container, final Map context) {
        return value;
    }

}
