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

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * Nullableなプロパティの問い合わせ条件を表現するクラスです。
 * 
 * @author koichik
 * @param <CONDITION>
 *            このプロパティを持つエンティティの問い合わせ条件を構築するクラス
 * @param <VALUETYPE>
 *            このプロパティの型
 */
public class NullableCondition<CONDITION extends AbstractEntityCondition<CONDITION>, VALUETYPE>
        extends NotNullableCondition<CONDITION, VALUETYPE> {

    /**
     * インスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param condition
     *            このプロパティを持つエンティティの問い合わせ条件
     */
    public NullableCondition(String propertyName, CONDITION condition) {
        super(propertyName, condition);
    }

    /**
     * <code>is null</code>の条件を追加します。
     * 
     * @return このインスタンス自身
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#isNull(String, Boolean)
     */
    public CONDITION isNull() {
        return isNull(true);
    }

    /**
     * <code>is null</code>の条件を追加します。
     * <p>
     * <code>value</code>が<code>null</code>でなく、<code>true</code>の場合に限り、問い合わせ条件に含められます。
     * </p>
     * 
     * @param value
     *            問い合わせ条件に加えることを示すフラグ
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#isNull(String, Boolean)
     */
    public CONDITION isNull(Boolean value) {
        condition.where.isNull(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>is not null</code>の条件を追加します。
     * 
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#isNull(String, Boolean)
     */
    public CONDITION isNotNull() {
        return isNotNull(true);
    }

    /**
     * <code>is not null</code>の条件を追加します。
     * <p>
     * <code>value</code>が<code>null</code>でなく、<code>true</code>の場合に限り、問い合わせ条件に含められます。
     * </p>
     * 
     * @param value
     *            問い合わせ条件に加えることを示すフラグ
     * @return このプロパティを持つエンティティの問い合わせ条件
     * @see SimpleWhere#isNull(String, Boolean)
     */
    public CONDITION isNotNull(Boolean value) {
        condition.where.isNotNull(condition.prefix + propertyName, value);
        return condition;
    }

}
