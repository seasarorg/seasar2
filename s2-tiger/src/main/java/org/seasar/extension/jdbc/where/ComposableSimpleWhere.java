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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 検索条件を組み立てる抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            このクラスのサブクラス
 */
public class ComposableSimpleWhere extends ComposableWhere {

    /** LIKE 述語で指定される検索条件中のワイルドカードをエスケープするためのパターン */
    protected static final Pattern WILDCARD_PATTERN = Pattern.compile("[$%_]");

    /** LIKE述語で指定される検索条件中のワイルドカード文字をエスケープするための文字 */
    protected static final char WILDCARD_ESCAPE_CHAR = '$';

    public static class Condition {

        public ConditionType conditionType;

        public CharSequence propertyName;

        public Object value;

        /**
         * @param conditionType
         * @param propertyName
         * @param value
         */
        public Condition(ConditionType conditionType,
                CharSequence propertyName, Object value) {
            this.conditionType = conditionType;
            this.propertyName = propertyName;
            this.value = value;
        }

    }

    protected List<Condition> conditionList = CollectionsUtil.newArrayList();

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>として扱い、 条件に加えない場合は<code>true</code>
     */
    protected boolean excludesWhitespace;

    /**
     * インスタンスを構築します。
     */
    public ComposableSimpleWhere() {
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

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合で パラメータ値の要素が空文字列または空白のみの文字列なら
     * <code>null</code>、 それ以外なら元の値からなる配列を返します。
     * 
     * @param values
     *            パラメータ値の配列
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値の要素が空文字列または空白のみの文字列なら
     *         <code>null</code>、 それ以外なら元の値からなる配列
     */
    protected Object[] normalize(final Object... values) {
        if (!excludesWhitespace || values == null) {
            return values;
        }
        final List<Object> list = CollectionsUtil.newArrayList(values.length);
        for (int i = 0; i < values.length; ++i) {
            final Object normalizedValue = normalize(values[i]);
            if (normalizedValue != null) {
                list.add(normalizedValue);
            }
        }
        return list.toArray(new Object[list.size()]);
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

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     * @see #ignoreWhitespace()
     */
    public ComposableSimpleWhere excludesWhitespace() {
        excludesWhitespace = true;
        return this;
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere eq(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.EQ.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.EQ, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>&lt;&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere ne(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.NE.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.NE, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>&lt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere lt(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.LT.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.LT, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>&lt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere le(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.LE.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.LE, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere gt(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.GT.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.GT, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>&gt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere ge(final CharSequence propertyName,
            Object value) {
        value = normalize(value);
        if (ConditionType.GE.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.GE, propertyName,
                    value));
        }
        return this;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere in(final CharSequence propertyName,
            Object... values) {
        values = normalize(values);
        if (ConditionType.IN.isTarget(values)) {
            conditionList.add(new Condition(ConditionType.IN, propertyName,
                    values));
        }
        return this;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere notIn(final CharSequence propertyName,
            Object... values) {
        values = normalize(values);
        if (ConditionType.NOT_IN.isTarget(values)) {
            conditionList.add(new Condition(ConditionType.NOT_IN, propertyName,
                    values));
        }
        return this;
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere like(final CharSequence propertyName,
            final String value) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.LIKE.isTarget(normalizedValue)) {
            conditionList.add(new Condition(ConditionType.LIKE, propertyName,
                    normalizedValue));
        }
        return this;
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @param escape
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere like(final CharSequence propertyName,
            final String value, final char escape) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.LIKE_ESCAPE.isTarget(normalizedValue)) {
            conditionList.add(new Condition(ConditionType.LIKE_ESCAPE,
                    propertyName, new Object[] { normalizedValue, escape }));
        }
        return this;
    }

    /**
     * <code>like '?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere starts(final CharSequence propertyName,
            final String value) {
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.STARTS.isTarget(normalizedValue)) {
            if (normalizedValue.indexOf('%') == -1
                    && normalizedValue.indexOf('_') == -1) {
                conditionList.add(new Condition(ConditionType.STARTS,
                        propertyName, normalizedValue));
            } else {
                conditionList.add(new Condition(ConditionType.STARTS_ESCAPE,
                        propertyName, escapeWildcard(String.class
                                .cast(normalizedValue))));
            }
        }
        return this;
    }

    /**
     * <code>like '%?'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere ends(final CharSequence propertyName,
            final String value) {
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.ENDS.isTarget(normalizedValue)) {
            if (normalizedValue.indexOf('%') == -1
                    && normalizedValue.indexOf('_') == -1) {
                conditionList.add(new Condition(ConditionType.ENDS,
                        propertyName, normalizedValue));
            } else {
                conditionList.add(new Condition(ConditionType.ENDS_ESCAPE,
                        propertyName, escapeWildcard(String.class
                                .cast(normalizedValue))));
            }
        }
        return this;
    }

    /**
     * <code>like '%?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere contains(final CharSequence propertyName,
            final String value) {
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.CONTAINS.isTarget(normalizedValue)) {
            if (normalizedValue.indexOf('%') == -1
                    && normalizedValue.indexOf('_') == -1) {
                conditionList.add(new Condition(ConditionType.CONTAINS,
                        propertyName, normalizedValue));
            } else {
                conditionList.add(new Condition(ConditionType.CONTAINS_ESCAPE,
                        propertyName, escapeWildcard(String.class
                                .cast(normalizedValue))));
            }
        }
        return this;
    }

    /**
     * <code>is null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere isNull(final CharSequence propertyName,
            final Boolean value) {
        if (ConditionType.IS_NULL.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.IS_NULL,
                    propertyName, value));
        }
        return this;
    }

    /**
     * <code>is not null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public ComposableSimpleWhere isNotNull(final CharSequence propertyName,
            final Boolean value) {
        if (ConditionType.IS_NOT_NULL.isTarget(value)) {
            conditionList.add(new Condition(ConditionType.IS_NOT_NULL,
                    propertyName, value));
        }
        return this;
    }

    @Override
    protected void visit(ComposableWhereContext context) {
        if (conditionList.isEmpty()) {
            return;
        }
        for (final Condition condition : conditionList) {
            final String name = condition.propertyName.toString();
            context
                    .append(
                            condition.conditionType.getCondition(name,
                                    condition.value)).append(" and ");
            int size = context.addParam(condition.conditionType,
                    condition.value);
            for (int i = 0; i < size; i++) {
                context.addPropertyName(name);
            }
        }
        context.cutBack(5);
    }

}
