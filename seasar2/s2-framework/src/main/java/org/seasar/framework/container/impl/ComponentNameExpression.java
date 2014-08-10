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
package org.seasar.framework.container.impl;

import java.util.Map;

import org.seasar.framework.container.S2Container;

/**
 * ソースがコンポーネント名を表す{@link org.seasar.framework.container.Expression}の実装クラスです。
 * 
 * @author koichik
 */
public class ComponentNameExpression extends AbstractExpression {

    /**
     * <code>ComponentNameExpression</code>のインスタンスを構築します。
     * 
     * @param source
     *            式のソース
     */
    public ComponentNameExpression(final String source) {
        super(source);
    }

    /**
     * <code>ComponentNameExpression</code>のインスタンスを構築します。
     * 
     * @param sourceName
     *            ソースファイル名
     * @param lineNumber
     *            ソースファイル中の行番号
     * @param source
     *            式のソース
     */
    public ComponentNameExpression(final String sourceName,
            final int lineNumber, final String source) {
        super(sourceName, lineNumber, source);
    }

    public Object evaluate(final S2Container container, final Map context) {
        Object result = container.getComponent(source);
        if (result == null && context != null) {
            result = context.get(source);
        }
        return result;
    }

}
