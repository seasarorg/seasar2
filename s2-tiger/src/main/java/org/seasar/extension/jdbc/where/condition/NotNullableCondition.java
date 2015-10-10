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
package org.seasar.extension.jdbc.where.condition;

import java.util.Collection;
import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * Nullableでないプロパティの問い合わせ条件を表現するクラスです。
 * 
 * @author koichik
 * @param <CONDITION>
 *            このプロパティを持つエンティティの問い合わせ条件を構築するクラス
 * @param <VALUETYPE>
 *            このプロパティの型
 */
public class NotNullableCondition<CONDITION extends AbstractEntityCondition<CONDITION>, VALUETYPE> {

    /** プロパティ名 */
    protected String propertyName;

    /** このプロパティを持つエンティティの問い合わせ条件 */
    protected CONDITION condition;

    /**
     * インスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param condition
     *            このプロパティを持つエンティティの問い合わせ条件
     */
    public NotNullableCondition(final String propertyName,
            final CONDITION condition) {
        this.propertyName = propertyName;
        this.condition = condition;
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#eq(String, Object)
     */
    public CONDITION eq(final VALUETYPE value) {
        condition.where.eq(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>&lt;&gt;</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#ne(String, Object)
     */
    public CONDITION ne(final VALUETYPE value) {
        condition.where.ne(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>&lt;</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#lt(String, Object)
     */
    public CONDITION lt(final VALUETYPE value) {
        condition.where.lt(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>&lt;=</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#le(String, Object)
     */
    public CONDITION le(final VALUETYPE value) {
        condition.where.le(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>&gt;</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#gt(String, Object)
     */
    public CONDITION gt(final VALUETYPE value) {
        condition.where.gt(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>&gt;=</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#ge(String, Object)
     */
    public CONDITION ge(final VALUETYPE value) {
        condition.where.ge(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param values
     *            条件となる値の並び
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#in(String, Object...)
     */
    public CONDITION in(final VALUETYPE... values) {
        condition.where.in(condition.prefix + propertyName, values);
        return condition;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param values
     *            条件となる値のコレクション
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#in(String, List)
     */
    public CONDITION in(final Collection<? extends VALUETYPE> values) {
        condition.where.in(condition.prefix + propertyName, values);
        return condition;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param values
     *            条件となる値の並び
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#notIn(String, Object...)
     */
    public CONDITION notIn(final VALUETYPE... values) {
        condition.where.notIn(condition.prefix + propertyName, values);
        return condition;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param values
     *            条件となる値のコレクション
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#notIn(String, List)
     */
    public CONDITION notIn(final Collection<? extends VALUETYPE> values) {
        condition.where.notIn(condition.prefix + propertyName, values);
        return condition;
    }

}
