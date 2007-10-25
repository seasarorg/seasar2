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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 入力された項目をandでつなげていくような検索条件を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class SimpleWhere implements Where {

    private StringBuilder criteriaSb = new StringBuilder(100);

    private LinkedList<StringBuilder> criteriaList = CollectionsUtil
            .newLinkedList(Arrays.asList(criteriaSb));

    private List<Object> paramList = new ArrayList<Object>();

    private boolean ignoreWhitespace;

    /**
     * {@link SimpleWhere}を作成します。
     */
    public SimpleWhere() {
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
    protected void addCondition(ConditionType conditionType,
            String propertyName, Object value) {
        if (criteriaSb.length() > 0) {
            criteriaSb.append(" and ");
        }
        criteriaSb.append(conditionType.getCondition(propertyName, value));
        conditionType.addValue(paramList, value);
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>を、
     * それ以外なら<code>value</code>をそのまま返します。
     * 
     * @param value
     *            パラメータ値
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>、
     *         それ以外なら<code>value</code>
     */
    protected Object normalize(Object value) {
        if (ignoreWhitespace && value instanceof String) {
            if (StringUtil.isEmpty(String.class.cast(value).trim())) {
                return null;
            }
        }
        return value;
    }

    /**
     * {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>を、
     * それ以外なら<code>value</code>をそのまま返します。
     * 
     * @param values
     *            パラメータ値の配列
     * @return {@link #ignoreWhitespace()}が呼び出された場合でパラメータ値が空文字列または空白のみの文字列なら<code>null</code>、
     *         それ以外なら元の値からなる配列
     */
    protected Object[] normalize(Object... values) {
        if (values == null) {
            return null;
        }
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; ++i) {
            result[i] = normalize(values[i]);
        }
        return result;
    }

    /**
     * {@link #eq(String, Object)}等で渡されたパラメータ値が空文字列または空白のみの文字列なら<code>null</code>として扱い、条件に加えないことを指定します。
     * 
     * @return このインスタンス自身
     */
    public SimpleWhere ignoreWhitespace() {
        ignoreWhitespace = true;
        return this;
    }

    /**
     * これまでに追加された条件とこれから追加される条件をORで結合します。
     * 
     * @return このインスタンス自身
     */
    public SimpleWhere or() {
        if (criteriaSb.length() > 0) {
            criteriaSb = new StringBuilder(100);
            criteriaList.addLast(criteriaSb);
        }
        return this;
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere eq(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.EQ.isTarget(value)) {
            addCondition(ConditionType.EQ, propertyName, value);
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
    public SimpleWhere ne(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.NE.isTarget(value)) {
            addCondition(ConditionType.NE, propertyName, value);
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
    public SimpleWhere lt(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.LT.isTarget(value)) {
            addCondition(ConditionType.LT, propertyName, value);
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
    public SimpleWhere le(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.LE.isTarget(value)) {
            addCondition(ConditionType.LE, propertyName, value);
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
    public SimpleWhere gt(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.GT.isTarget(value)) {
            addCondition(ConditionType.GT, propertyName, value);
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
    public SimpleWhere ge(String propertyName, Object value) {
        value = normalize(value);
        if (ConditionType.GE.isTarget(value)) {
            addCondition(ConditionType.GE, propertyName, value);
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
    public SimpleWhere in(String propertyName, Object... values) {
        values = normalize(values);
        if (ConditionType.IN.isTarget(values)) {
            addCondition(ConditionType.IN, propertyName, values);
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
    public SimpleWhere notIn(String propertyName, Object... values) {
        values = normalize(values);
        if (ConditionType.NOT_IN.isTarget(values)) {
            addCondition(ConditionType.NOT_IN, propertyName, values);
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
    public SimpleWhere like(String propertyName, String value) {
        Object normalizedValue = normalize(value);
        if (ConditionType.LIKE.isTarget(normalizedValue)) {
            addCondition(ConditionType.LIKE, propertyName, normalizedValue);
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
    public SimpleWhere starts(String propertyName, String value) {
        Object normalizedValue = normalize(value);
        if (ConditionType.STARTS.isTarget(normalizedValue)) {
            addCondition(ConditionType.STARTS, propertyName, normalizedValue);
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
    public SimpleWhere ends(String propertyName, String value) {
        Object normalizedValue = normalize(value);
        if (ConditionType.ENDS.isTarget(normalizedValue)) {
            addCondition(ConditionType.ENDS, propertyName, normalizedValue);
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
    public SimpleWhere contains(String propertyName, String value) {
        Object normalizedValue = normalize(value);
        if (ConditionType.CONTAINS.isTarget(normalizedValue)) {
            addCondition(ConditionType.CONTAINS, propertyName, normalizedValue);
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
    public SimpleWhere isNull(String propertyName, Boolean value) {
        if (ConditionType.IS_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NULL, propertyName, value);
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
    public SimpleWhere isNotNull(String propertyName, Boolean value) {
        if (ConditionType.IS_NOT_NULL.isTarget(value)) {
            addCondition(ConditionType.IS_NOT_NULL, propertyName, value);
        }
        return this;
    }

    public String getCriteria() {
        if (criteriaSb.length() == 0) {
            criteriaList.removeLast();
        }
        if (criteriaList.isEmpty()) {
            return "";
        }
        if (criteriaList.size() == 1) {
            return new String(criteriaList.getFirst());
        }

        int size = 0;
        for (StringBuilder buf : criteriaList) {
            size += buf.length();
        }
        StringBuilder buf = new StringBuilder(size + 50);
        for (StringBuilder criteria : criteriaList) {
            buf.append("(").append(criteria).append(") or ");
        }
        buf.setLength(buf.length() - 4);
        return new String(buf);
    }

    public Object[] getParams() {
        return paramList.toArray();
    }

}