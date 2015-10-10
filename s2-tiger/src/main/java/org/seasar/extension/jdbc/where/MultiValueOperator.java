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
package org.seasar.extension.jdbc.where;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 複数の値を対象とする2項演算子です。
 * 
 * @author koichik
 */
public class MultiValueOperator extends SingleValueOperator {

    /**
     * インスタンスを構築します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            パラメータのプロパティ名
     * @param value
     *            パラメータの値
     */
    public MultiValueOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Object... value) {
        super(conditionType, propertyName, value);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            パラメータのプロパティ名
     * @param value
     *            パラメータとなる値のコレクション
     */
    public MultiValueOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Collection<?> value) {
        super(conditionType, propertyName, value);
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合で パラメータ値の要素が空文字列または空白のみの文字列なら
     * <code>null</code>、 それ以外なら元の値からなる配列を返します。
     * 
     * @param values
     *            パラメータ値の配列
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値の要素が空文字列または空白のみの文字列なら
     *         <code>null</code>、 それ以外なら元の値からなる配列
     */
    @Override
    protected Object normalize(final Object value) {
        if (!excludesWhitespace || value == null) {
            return value;
        }
        final Collection<?> values = value instanceof Collection ? (Collection<?>) value
                : Arrays.asList((Object[]) value);
        final List<Object> list = CollectionsUtil.newArrayList(values.size());
        for (final Object element : values) {
            final Object normalizedValue = super.normalize(element);
            if (normalizedValue != null) {
                list.add(normalizedValue);
            }
        }
        return list;
    }

}
