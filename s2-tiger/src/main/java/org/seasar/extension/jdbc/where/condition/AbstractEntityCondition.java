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

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * エンティティ固有の問い合わせ条件を構築するための抽象クラスです。
 * 
 * @author koichik
 * @param <CONDITION>
 *            このクラスのサブクラス
 */
public abstract class AbstractEntityCondition<CONDITION extends AbstractEntityCondition<CONDITION>>
        implements Where {

    /** WHERE句を組み立てる{@link ComplexWhere} */
    protected ComplexWhere where;

    /** このエンティティを表す関連名を含む接頭辞 */
    protected String prefix;

    /**
     * インスタンスを構築します。
     */
    public AbstractEntityCondition() {
        this("", new ComplexWhere());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param prefix
     *            このエンティティを表す関連名を含む接頭辞
     * @param where
     *            WHERE句を組み立てる{@link ComplexWhere}
     */
    public AbstractEntityCondition(final String prefix, ComplexWhere where) {
        this.prefix = prefix;
        this.where = where;
    }

    /**
     * これまでに追加された条件とこれから追加される条件をORで結合します。
     * 
     * @return このインスタンス自身
     * @see SimpleWhere#or()
     */
    @SuppressWarnings("unchecked")
    public CONDITION or() {
        where.or();
        return (CONDITION) this;
    }

    /**
     * これまでに追加された条件と、引数で渡された条件全体をANDで結合します。
     * 
     * @param factor
     *            ANDで結合される条件
     * @return このインスタンス自身
     * @see SimpleWhere#and(Where)
     */
    @SuppressWarnings("unchecked")
    public CONDITION and(final Where factor) {
        where.and(factor);
        return (CONDITION) this;
    }

    public String getCriteria() {
        return where.getCriteria();
    }

    public Object[] getParams() {
        return where.getParams();
    }

    public String[] getPropertyNames() {
        return where.getPropertyNames();
    }

}
