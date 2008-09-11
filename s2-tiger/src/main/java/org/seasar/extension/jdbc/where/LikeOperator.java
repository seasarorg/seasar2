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
package org.seasar.extension.jdbc.where;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.framework.util.StringUtil;

/**
 * <code>LIKE</code>演算子です。
 * 
 * @author koichik
 */
public class LikeOperator extends SingleValueOperator {

    /** LIKE 述語で指定される検索条件中のワイルドカードをエスケープするためのパターン */
    protected static final Pattern WILDCARD_PATTERN = Pattern.compile("[$%_]");

    /** LIKE述語で指定される検索条件中のワイルドカード文字をエスケープするための文字 */
    protected static final char WILDCARD_ESCAPE_CHAR = '$';

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
        case ENDS:
        case CONTAINS:
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
        if (normalizedValue.indexOf('%') == -1
                && normalizedValue.indexOf('_') == -1) {
            return normalizedValue;
        }
        switch (conditionType) {
        case STARTS:
            conditionType = ConditionType.STARTS_ESCAPE;
            break;
        case ENDS:
            conditionType = ConditionType.ENDS_ESCAPE;
            break;
        case CONTAINS:
            conditionType = ConditionType.CONTAINS_ESCAPE;
            break;
        }
        return escapeWildcard(String.class.cast(normalizedValue));
    }

    /**
     * LIKE述語で使用される検索条件のワイルドカードを<code>'$'</code>でエスケープします．
     * 
     * @param likeCondition
     *            LIKE述語で使用される検索条件の文字列
     * @return ワイルドカードを<code>'$'</code>でエスケープした文字列
     */
    protected String escapeWildcard(final String likeCondition) {
        final Matcher matcher = WILDCARD_PATTERN.matcher(likeCondition);
        return matcher.replaceAll("\\$$0");
    }

}
