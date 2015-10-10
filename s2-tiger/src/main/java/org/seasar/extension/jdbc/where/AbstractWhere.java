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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.util.LikeUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 検索条件を組み立てる抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            このクラスのサブクラス
 */
public class AbstractWhere<T extends AbstractWhere<T>> implements Where {

    /** 現在のクライテリアを保持する文字列バッファ */
    protected StringBuilder criteriaSb = new StringBuilder(100);

    /** バインド変数のリスト */
    protected List<Object> paramList = new ArrayList<Object>();

    /** バインド変数に対応するプロパティ名のリスト */
    protected List<String> propertyNameList = new ArrayList<String>();

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>として扱い、 条件に加えない場合は<code>true</code>
     */
    protected boolean excludesWhitespace;

    /**
     * インスタンスを構築します。
     */
    public AbstractWhere() {
    }

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     * @see #ignoreWhitespace()
     */
    @SuppressWarnings("unchecked")
    public T excludesWhitespace() {
        excludesWhitespace = true;
        return (T) this;
    }

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら
     * <code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     * 
     */
    @Deprecated
    public T ignoreWhitespace() {
        return excludesWhitespace();
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T eq(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.EQ.isTarget(value)) {
            addCondition(ConditionType.EQ, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>&lt;&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T ne(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.NE.isTarget(value)) {
            addCondition(ConditionType.NE, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>&lt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T lt(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.LT.isTarget(value)) {
            addCondition(ConditionType.LT, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>&lt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T le(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.LE.isTarget(value)) {
            addCondition(ConditionType.LE, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T gt(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.GT.isTarget(value)) {
            addCondition(ConditionType.GT, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>&gt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T ge(final CharSequence propertyName, Object value) {
        assertPropertyName(propertyName);
        value = normalize(value);
        if (ConditionType.GE.isTarget(value)) {
            addCondition(ConditionType.GE, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T in(final CharSequence propertyName, Object... values) {
        assertPropertyName(propertyName);
        values = normalizeArray(values);
        if (ConditionType.IN.isTarget(values)) {
            addCondition(ConditionType.IN, propertyName.toString(), values);
        }
        return (T) this;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T in(final CharSequence propertyName, Collection<?> values) {
        assertPropertyName(propertyName);
        values = normalizeList(values);
        if (ConditionType.IN.isTarget(values)) {
            addCondition(ConditionType.IN, propertyName.toString(), values);
        }
        return (T) this;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notIn(final CharSequence propertyName, Object... values) {
        assertPropertyName(propertyName);
        values = normalizeArray(values);
        if (ConditionType.NOT_IN.isTarget(values)) {
            addCondition(ConditionType.NOT_IN, propertyName.toString(), values);
        }
        return (T) this;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notIn(final CharSequence propertyName, Collection<?> values) {
        assertPropertyName(propertyName);
        values = normalizeList(values);
        if (ConditionType.NOT_IN.isTarget(values)) {
            addCondition(ConditionType.NOT_IN, propertyName.toString(), values);
        }
        return (T) this;
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T like(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final Object normalizedValue = normalize(value);
        if (ConditionType.LIKE.isTarget(normalizedValue)) {
            addCondition(ConditionType.LIKE, propertyName.toString(),
                    normalizedValue);
        }
        return (T) this;
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @param escape
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T like(final CharSequence propertyName, final String value,
            final char escape) {
        assertPropertyName(propertyName);
        final Object normalizedValue = normalize(value);
        if (ConditionType.LIKE_ESCAPE.isTarget(normalizedValue)) {
            addCondition(ConditionType.LIKE_ESCAPE, propertyName.toString(),
                    new Object[] { normalizedValue, escape });
        }
        return (T) this;
    }

    /**
     * <code>not like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notLike(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final Object normalizedValue = normalize(value);
        if (ConditionType.NOT_LIKE.isTarget(normalizedValue)) {
            addCondition(ConditionType.NOT_LIKE, propertyName.toString(),
                    normalizedValue);
        }
        return (T) this;
    }

    /**
     * <code>not like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @param escape
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notLike(final CharSequence propertyName, final String value,
            final char escape) {
        assertPropertyName(propertyName);
        final Object normalizedValue = normalize(value);
        if (ConditionType.NOT_LIKE_ESCAPE.isTarget(normalizedValue)) {
            addCondition(ConditionType.NOT_LIKE_ESCAPE,
                    propertyName.toString(), new Object[] { normalizedValue,
                            escape });
        }
        return (T) this;
    }

    /**
     * <code>like '?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T starts(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.STARTS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.STARTS, propertyName.toString(),
                        normalizedValue);
            } else {
                addCondition(ConditionType.STARTS_ESCAPE, propertyName
                        .toString(), LikeUtil.escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>not like '?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notStarts(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.NOT_STARTS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.NOT_STARTS, propertyName.toString(),
                        normalizedValue);
            } else {
                addCondition(ConditionType.NOT_STARTS_ESCAPE, propertyName
                        .toString(), LikeUtil.escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>like '%?'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T ends(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.ENDS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.ENDS, propertyName.toString(),
                        normalizedValue);
            } else {
                addCondition(ConditionType.ENDS_ESCAPE,
                        propertyName.toString(), LikeUtil
                                .escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>not like '%?'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notEnds(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.NOT_ENDS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.NOT_ENDS, propertyName.toString(),
                        normalizedValue);
            } else {
                addCondition(ConditionType.NOT_ENDS_ESCAPE, propertyName
                        .toString(), LikeUtil.escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>like '%?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T contains(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.CONTAINS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.CONTAINS, propertyName.toString(),
                        normalizedValue);
            } else {
                addCondition(ConditionType.CONTAINS_ESCAPE, propertyName
                        .toString(), LikeUtil.escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>not like '%?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T notContains(final CharSequence propertyName, final String value) {
        assertPropertyName(propertyName);
        final String normalizedValue = String.class.cast(normalize(value));
        if (ConditionType.NOT_CONTAINS.isTarget(normalizedValue)) {
            if (!LikeUtil.containsWildcard(normalizedValue)) {
                addCondition(ConditionType.NOT_CONTAINS, propertyName
                        .toString(), normalizedValue);
            } else {
                addCondition(ConditionType.NOT_CONTAINS_ESCAPE, propertyName
                        .toString(), LikeUtil.escapeWildcard(normalizedValue));
            }
        }
        return (T) this;
    }

    /**
     * <code>is null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T isNull(final CharSequence propertyName, final Boolean value) {
        assertPropertyName(propertyName);
        if (ConditionType.IS_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NULL, propertyName.toString(), value);
        }
        return (T) this;
    }

    /**
     * <code>is not null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T isNotNull(final CharSequence propertyName, final Boolean value) {
        assertPropertyName(propertyName);
        if (ConditionType.IS_NOT_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NOT_NULL, propertyName.toString(),
                    value);
        }
        return (T) this;
    }

    public String getCriteria() {
        return new String(criteriaSb);
    }

    public Object[] getParams() {
        return paramList.toArray();
    }

    public String[] getPropertyNames() {
        return propertyNameList.toArray(new String[propertyNameList.size()]);
    }

    /**
     * 条件を追加します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            プロパティ名
     * @param value
     *            値
     */
    protected void addCondition(final ConditionType conditionType,
            final String propertyName, final Object value) {
        if (criteriaSb.length() > 0) {
            criteriaSb.append(" and ");
        }
        criteriaSb.append(conditionType.getCondition(propertyName, value));
        int size = conditionType.addValue(paramList, value);
        for (int i = 0; i < size; i++) {
            propertyNameList.add(propertyName);
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

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合で パラメータ値の要素が空文字列または空白のみの文字列なら
     * <code>null</code>、 それ以外なら元の値からなる配列を返します。
     * 
     * @param values
     *            パラメータ値の配列
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値の要素が空文字列または空白のみの文字列なら
     *         <code>null</code>、 それ以外なら元の値からなる配列
     */
    protected Object[] normalizeArray(final Object... values) {
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
     * {@link #ignoreWhitespace()}が呼び出された場合で パラメータ値の要素が空文字列または空白のみの文字列なら
     * <code>null</code>、 それ以外なら元の値からなるリストを返します。
     * 
     * @param values
     *            パラメータ値のコレクション
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値の要素が空文字列または空白のみの文字列なら
     *         <code>null</code>、 それ以外なら元の値からなるリスト
     */
    protected Collection<?> normalizeList(final Collection<?> values) {
        if (!excludesWhitespace || values == null) {
            return values;
        }
        final List<Object> list = CollectionsUtil.newArrayList(values.size());
        for (final Object value : values) {
            final Object normalizedValue = normalize(value);
            if (normalizedValue != null) {
                list.add(normalizedValue);
            }
        }
        return list;
    }

    /**
     * プロパティ名が{@literal null}でないことを確認します。
     * 
     * @param s
     *            文字の列
     */
    protected void assertPropertyName(final CharSequence s) {
        if (s == null) {
            throw new NullPointerException("propertyName");
        }
    }

}
