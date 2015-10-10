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

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.framework.util.StringUtil;

/**
 * 一つの値を対象とする2項演算子です。
 * 
 * @author koichik
 */
public class SingleValueOperator extends ComposableWhere {

    /** 条件タイプです。 */
    protected ConditionType conditionType;

    /** パラメータのプロパティ名です。 */
    protected CharSequence propertyName;

    /** パラメータの値です。 */
    protected Object value;

    /**
     * パラメータ値が空文字列または空白のみの文字列なら <code>null</code>として扱い、 条件に加えない場合は
     * <code>true</code>
     */
    protected boolean excludesWhitespace;

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
    public SingleValueOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Object value) {
        this.conditionType = conditionType;
        this.propertyName = propertyName;
        this.value = value;
    }

    /**
     * パラメータ値が空文字列または空白のみの文字列なら <code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     * @see #ignoreWhitespace()
     */
    public SingleValueOperator excludesWhitespace() {
        excludesWhitespace = true;
        return this;
    }

    @Override
    protected void visit(final ComposableWhereContext context) {
        final Object param = normalize(value);
        if (!conditionType.isTarget(param)) {
            return;
        }
        final String name = propertyName.toString();
        context.append(conditionType.getCondition(name, param));
        final int size = context.addParam(conditionType, param);
        for (int i = 0; i < size; i++) {
            context.addPropertyName(name);
        }
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>を、 それ以外なら元の値をそのまま返します。
     * 
     * @param value
     *            パラメータ値
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら
     *         <code>null</code>、 それ以外なら元の値
     */
    protected Object normalize(final Object value) {
        if (excludesWhitespace && value instanceof String) {
            if (StringUtil.isEmpty(String.class.cast(value).trim())) {
                return null;
            }
        }
        return value;
    }

}
