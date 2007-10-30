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
package org.seasar.extension.jdbc.where;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;
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

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら<code>null</code>として扱い、
     * 条件に加えない場合は<code>true</code>
     */
    protected boolean ignoreWhitespace;

    /**
     * インスタンスを構築します。
     */
    public AbstractWhere() {
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
        conditionType.addValue(paramList, value);
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>を、
     * それ以外なら元の値をそのまま返します。
     * 
     * @param value
     *            パラメータ値
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>、
     *         それ以外なら元の値
     */
    protected Object normalize(final Object value) {
        if (ignoreWhitespace && value instanceof String) {
            if (StringUtil.isEmpty(String.class.cast(value).trim())) {
                return null;
            }
        }
        return value;
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合で パラメータ値の要素が空文字列または空白のみの文字列なら<code>null</code>、
     * それ以外なら元の値からなる配列を返します。
     * 
     * @param values
     *            パラメータ値の配列
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値の要素が空文字列または空白のみの文字列なら<code>null</code>、
     *         それ以外なら元の値からなる配列
     */
    protected Object[] normalize(final Object... values) {
        if (!ignoreWhitespace || values == null) {
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
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら<code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T ignoreWhitespace() {
        ignoreWhitespace = true;
        return (T) this;
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public T eq(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.EQ.isTarget(value)) {
            addCondition(ConditionType.EQ, propertyName, value);
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
    public T ne(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.NE.isTarget(value)) {
            addCondition(ConditionType.NE, propertyName, value);
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
    public T lt(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.LT.isTarget(value)) {
            addCondition(ConditionType.LT, propertyName, value);
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
    public T le(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.LE.isTarget(value)) {
            addCondition(ConditionType.LE, propertyName, value);
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
    public T gt(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.GT.isTarget(value)) {
            addCondition(ConditionType.GT, propertyName, value);
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
    public T ge(final String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.GE.isTarget(value)) {
            addCondition(ConditionType.GE, propertyName, value);
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
    public T in(final String propertyName, Object... values) {
        values = normalize(values);
        if (ConditionType.IN.isTarget(values)) {
            addCondition(ConditionType.IN, propertyName, values);
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
    public T notIn(final String propertyName, Object... values) {
        values = normalize(values);
        if (ConditionType.NOT_IN.isTarget(values)) {
            addCondition(ConditionType.NOT_IN, propertyName, values);
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
    public T like(final String propertyName, final String value) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.LIKE.isTarget(normalizedValue)) {
            addCondition(ConditionType.LIKE, propertyName, normalizedValue);
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
    public T starts(final String propertyName, final String value) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.STARTS.isTarget(normalizedValue)) {
            addCondition(ConditionType.STARTS, propertyName, normalizedValue);
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
    public T ends(final String propertyName, final String value) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.ENDS.isTarget(normalizedValue)) {
            addCondition(ConditionType.ENDS, propertyName, normalizedValue);
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
    public T contains(final String propertyName, final String value) {
        final Object normalizedValue = normalize(value);
        if (ConditionType.CONTAINS.isTarget(normalizedValue)) {
            addCondition(ConditionType.CONTAINS, propertyName, normalizedValue);
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
    public T isNull(final String propertyName, final Boolean value) {
        if (ConditionType.IS_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NULL, propertyName, value);
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
    public T isNotNull(final String propertyName, final Boolean value) {
        if (ConditionType.IS_NOT_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NOT_NULL, propertyName, value);
        }
        return (T) this;
    }

    public String getCriteria() {
        return new String(criteriaSb);
    }

    public Object[] getParams() {
        return paramList.toArray();
    }

}
