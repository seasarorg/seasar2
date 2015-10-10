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
import org.seasar.extension.jdbc.util.LikeUtil;
import org.seasar.framework.util.StringUtil;

/**
 * <code>LIKE</code>演算子です。
 * 
 * @author koichik
 */
public class LikeOperator extends SingleValueOperator {

    /** エスケープ文字です。 */
    protected String escapeChar;

    /** パラメータの値をエスケープする必要がある場合は<code>true</code>です。 */
    protected boolean needEscape;

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
    public LikeOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Object value) {
        super(conditionType, propertyName, value);
        switch (conditionType) {
        case STARTS:
        case NOT_STARTS:
        case ENDS:
        case NOT_ENDS:
        case CONTAINS:
        case NOT_CONTAINS:
            needEscape = true;
            break;
        }
    }

    /**
     * インスタンスを構築します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            パラメータのプロパティ名
     * @param value
     *            パラメータの値
     * @param escapeChar
     *            エスケープ文字
     */
    public LikeOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Object value,
            final String escapeChar) {
        super(conditionType, propertyName, value);
        this.escapeChar = escapeChar;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            パラメータのプロパティ名
     * @param value
     *            パラメータの値
     * @param needEscape
     *            パラメータの値をエスケープする必要がある場合は<code>true</code>
     */
    public LikeOperator(final ConditionType conditionType,
            final CharSequence propertyName, final Object value,
            final boolean needEscape) {
        super(conditionType, propertyName, value);
        this.needEscape = needEscape;
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
    @Override
    protected Object normalize(final Object value) {
        final Object param = super.normalize(value);
        if (param == null) {
            return null;
        } else if (!StringUtil.isEmpty(escapeChar)) {
            return new Object[] { param, escapeChar };
        } else if (!needEscape) {
            return param;
        }

        final String normalizedValue = (String) param;
        if (!LikeUtil.containsWildcard(normalizedValue)) {
            return normalizedValue;
        }
        switch (conditionType) {
        case STARTS:
            conditionType = ConditionType.STARTS_ESCAPE;
            break;
        case NOT_STARTS:
            conditionType = ConditionType.NOT_STARTS_ESCAPE;
            break;
        case ENDS:
            conditionType = ConditionType.ENDS_ESCAPE;
            break;
        case NOT_ENDS:
            conditionType = ConditionType.NOT_ENDS_ESCAPE;
            break;
        case CONTAINS:
            conditionType = ConditionType.CONTAINS_ESCAPE;
            break;
        case NOT_CONTAINS:
            conditionType = ConditionType.NOT_CONTAINS_ESCAPE;
            break;
        }
        return LikeUtil.escapeWildcard(String.class.cast(normalizedValue));
    }

}
