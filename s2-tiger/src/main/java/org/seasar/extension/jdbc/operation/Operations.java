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
package org.seasar.extension.jdbc.operation;

import java.util.Collection;

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
public class Operations {

    /**
     * <code>=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator eq(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.EQ, propertyName, param);
    }

    /**
     * <code>&lt;&gt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator ne(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.NE, propertyName, param);
    }

    /**
     * <code>&gt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator gt(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.GT, propertyName, param);
    }

    /**
     * <code>&gt;=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator ge(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.GE, propertyName, param);
    }

    /**
     * <code>&lt;</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator lt(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.LT, propertyName, param);
    }

    /**
     * <code>&lt;=</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> SingleValueOperator le(
            final PropertyName<PropType> propertyName, final ParamType param) {
        return new SingleValueOperator(ConditionType.LE, propertyName, param);
    }

    /**
     * <code>IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> MultiValueOperator in(
            final PropertyName<PropType> propertyName,
            final ParamType... params) {
        return new MultiValueOperator(ConditionType.IN, propertyName, params);
    }

    /**
     * <code>IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> MultiValueOperator in(
            final PropertyName<PropType> propertyName,
            final Collection<? extends ParamType> params) {
        return new MultiValueOperator(ConditionType.IN, propertyName, params);
    }

    /**
     * <code>NOT IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> MultiValueOperator notIn(
            final PropertyName<PropType> propertyName,
            final ParamType... params) {
        return new MultiValueOperator(ConditionType.NOT_IN, propertyName,
                params);
    }

    /**
     * <code>NOT IN</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param <PropType>
     *            プロパティの型
     * @param <ParamType>
     *            引数の型
     * @param propertyName
     *            プロパティ名
     * @param params
     *            引数
     * @return 条件
     */
    public static <PropType, ParamType extends PropType> MultiValueOperator notIn(
            final PropertyName<PropType> propertyName,
            final Collection<? extends ParamType> params) {
        return new MultiValueOperator(ConditionType.NOT_IN, propertyName,
                params);
    }

    /**
     * <code>IS NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 条件
     */
    public static SingleValueOperator isNull(final PropertyName<?> propertyName) {
        return new SingleValueOperator(ConditionType.IS_NULL, propertyName,
                true);
    }

    /**
     * <code>IS NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static SingleValueOperator isNull(
            final PropertyName<?> propertyName, final Boolean param) {
        return new SingleValueOperator(ConditionType.IS_NULL, propertyName,
                param);
    }

    /**
     * <code>IS NOT NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 条件
     */
    public static SingleValueOperator isNotNull(
            final PropertyName<?> propertyName) {
        return new SingleValueOperator(ConditionType.IS_NOT_NULL, propertyName,
                true);
    }

    /**
     * <code>IS NOT NULL</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static SingleValueOperator isNotNull(
            final PropertyName<?> propertyName, final Boolean param) {
        return new SingleValueOperator(ConditionType.IS_NOT_NULL, propertyName,
                param);
    }

    /**
     * <code>LIKE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator like(final PropertyName<String> propertyName,
            final String param) {
        return new LikeOperator(ConditionType.LIKE, propertyName, param);
    }

    /**
     * <code>LIKE ～ ESCAPE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @param escapeChar
     *            エスケープ文字
     * @return 条件
     */
    public static LikeOperator like(final PropertyName<String> propertyName,
            final String param, final String escapeChar) {
        return new LikeOperator(ConditionType.LIKE_ESCAPE, propertyName, param,
                escapeChar);
    }

    /**
     * <code>NOT LIKE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator notLike(final PropertyName<String> propertyName,
            final String param) {
        return new LikeOperator(ConditionType.NOT_LIKE, propertyName, param);
    }

    /**
     * <code>NOT LIKE ～ ESCAPE</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @param escapeChar
     *            エスケープ文字
     * @return 条件
     */
    public static LikeOperator notLike(final PropertyName<String> propertyName,
            final String param, final String escapeChar) {
        return new LikeOperator(ConditionType.NOT_LIKE_ESCAPE, propertyName,
                param, escapeChar);
    }

    /**
     * <code>LIKE ～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator starts(final PropertyName<String> propertyName,
            final String param) {
        return new LikeOperator(ConditionType.STARTS, propertyName, param);
    }

    /**
     * <code>NOT LIKE ～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator notStarts(
            final PropertyName<String> propertyName, final String param) {
        return new LikeOperator(ConditionType.NOT_STARTS, propertyName, param);
    }

    /**
     * <code>LIKE %～</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator ends(final PropertyName<String> propertyName,
            final String param) {
        return new LikeOperator(ConditionType.ENDS, propertyName, param);
    }

    /**
     * <code>NOT LIKE %～</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator notEnds(final PropertyName<String> propertyName,
            final String param) {
        return new LikeOperator(ConditionType.NOT_ENDS, propertyName, param);
    }

    /**
     * <code>LIKE %～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator contains(
            final PropertyName<String> propertyName, final String param) {
        return new LikeOperator(ConditionType.CONTAINS, propertyName, param);
    }

    /**
     * <code>NOT LIKE %～%</code>演算子による{@link Where 検索条件}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param param
     *            引数
     * @return 条件
     */
    public static LikeOperator notContains(
            final PropertyName<String> propertyName, final String param) {
        return new LikeOperator(ConditionType.NOT_CONTAINS, propertyName, param);
    }

    /**
     * 子供の{@link Where 検索条件}をANDで結合した検索条件を作成します。
     * 
     * @param children
     *            子供の{@link Where 検索条件}
     * @return 子供の{@link Where 検索条件}をANDで結合した検索条件
     */
    public static CompositeWhere and(final Where... children) {
        return new CompositeWhere("and", children);
    }

    /**
     * 子供の{@link Where 検索条件}をORで結合した検索条件を作成します。
     * 
     * @param children
     *            子供の{@link Where 検索条件}
     * @return 子供の{@link Where 検索条件}をORで結合した検索条件
     */
    public static CompositeWhere or(final Where... children) {
        return new CompositeWhere("or", children);
    }

    /**
     * 昇順の{@link OrderByItem}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 昇順の{@link OrderByItem}
     */
    public static OrderByItem asc(final CharSequence propertyName) {
        return new OrderByItem(propertyName);
    }

    /**
     * 降順の{@link OrderByItem}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 降順の{@link OrderByItem}
     */
    public static OrderByItem desc(final CharSequence propertyName) {
        return new OrderByItem(propertyName, OrderingSpec.DESC);
    }

}
