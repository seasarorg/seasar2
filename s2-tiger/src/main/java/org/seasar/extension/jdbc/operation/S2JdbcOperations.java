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
package org.seasar.extension.jdbc.operation;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.OrderByItem;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.OrderByItem.OrderingSpec;
import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.where.CompositeWhere;
import org.seasar.extension.jdbc.where.LikeOperator;
import org.seasar.extension.jdbc.where.MultiValueOperator;
import org.seasar.extension.jdbc.where.SingleValueOperator;

/**
 * S2JDBCの{@link AutoSelect}で使用する演算子等を組み立てるための操作を定義したクラスです。
 * <p>
 * このクラスはS2JDBC-Genが生成するServiceクラスからstatic importされて使われることを想定しています。
 * </p>
 * 
 * @author koichik
 */
public class S2JdbcOperations {

    /**
     * <code>=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator eq(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.EQ, propertyName, param);
    }

    /**
     * <code>&lt;&gt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator ne(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.NE, propertyName, param);
    }

    /**
     * <code>&gt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator gt(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.GT, propertyName, param);
    }

    /**
     * <code>&gt;=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator ge(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.GE, propertyName, param);
    }

    /**
     * <code>&lt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator lt(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.LT, propertyName, param);
    }

    /**
     * <code>&lt;=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static <T> SingleValueOperator le(PropertyName<T> propertyName,
            T param) {
        return new SingleValueOperator(ConditionType.LE, propertyName, param);
    }

    /**
     * <code>IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return
     */
    public static <T> MultiValueOperator in(PropertyName<T> propertyName,
            T... params) {
        return new MultiValueOperator(ConditionType.IN, propertyName, params);
    }

    /**
     * <code>NOT IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return
     */
    public static <T> MultiValueOperator notIn(PropertyName<T> propertyName,
            T... params) {
        return new MultiValueOperator(ConditionType.NOT_IN, propertyName,
                params);
    }

    /**
     * <code>IS NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static SingleValueOperator isNull(PropertyName<?> propertyName) {
        return new SingleValueOperator(ConditionType.IS_NULL, propertyName,
                true);
    }

    /**
     * <code>IS NOT NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static SingleValueOperator isNotNull(PropertyName<?> propertyName) {
        return new SingleValueOperator(ConditionType.IS_NOT_NULL, propertyName,
                true);
    }

    /**
     * <code>LIKE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static LikeOperator like(PropertyName<String> propertyName,
            String param) {
        return new LikeOperator(ConditionType.LIKE, propertyName, param);
    }

    /**
     * <code>LIKE ～ ESCAPE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @param escapeChar
     *            エスケープ文字
     * @return
     */
    public static LikeOperator like(PropertyName<String> propertyName,
            String param, String escapeChar) {
        return new LikeOperator(ConditionType.LIKE_ESCAPE, propertyName, param,
                escapeChar);
    }

    /**
     * <code>LIKE ～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static LikeOperator starts(PropertyName<String> propertyName,
            String param) {
        return new LikeOperator(ConditionType.STARTS, propertyName, param);
    }

    /**
     * <code>LIKE %～</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static LikeOperator ends(PropertyName<String> propertyName,
            String param) {
        return new LikeOperator(ConditionType.ENDS, propertyName, param);
    }

    /**
     * <code>LIKE %～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <T>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return
     */
    public static LikeOperator contains(PropertyName<String> propertyName,
            String param) {
        return new LikeOperator(ConditionType.CONTAINS, propertyName, param);
    }

    /**
     * 子供の{@link Where 検索条件}をANDで結合した検索条件を作成します。
     * 
     * @param children
     *            子供の{@link Where 検索条件}
     * @return 子供の{@link Where 検索条件}をANDで結合した検索条件
     */
    public static CompositeWhere and(Where... children) {
        return new CompositeWhere("and", children);
    }

    /**
     * 子供の{@link Where 検索条件}をORで結合した検索条件を作成します。
     * 
     * @param children
     *            子供の{@link Where 検索条件}
     * @return 子供の{@link Where 検索条件}をORで結合した検索条件
     */
    public static CompositeWhere or(Where... children) {
        return new CompositeWhere("or", children);
    }

    /**
     * 昇順の{@link OrderByItem}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 昇順の{@link OrderByItem}
     */
    public static OrderByItem asc(PropertyName<?> propertyName) {
        return new OrderByItem(propertyName);
    }

    /**
     * 降順の{@link OrderByItem}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 降順の{@link OrderByItem}
     */
    public static OrderByItem desc(PropertyName<?> propertyName) {
        return new OrderByItem(propertyName, OrderingSpec.DESC);
    }

}
