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
package org.seasar.extension.jdbc;

import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

/**
 * 検索のベースとなるインターフェースです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * @param <S>
 *            <code>Select</code>のサブタイプです。
 */
public interface Select<T, S extends Select<T, S>> extends Query<S> {

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     * @return このインスタンス自身
     * @see Statement#setMaxRows(int)
     */
    S maxRows(int maxRows);

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     * @return このインスタンス自身
     * @see Statement#setFetchSize(int)
     */
    S fetchSize(int fetchSize);

    /**
     * リミットを設定します。
     * 
     * @param limit
     *            リミット
     * @return このインスタンス自身
     */
    S limit(int limit);

    /**
     * オフセットを設定します。
     * 
     * @param offset
     *            オフセット
     * @return このインスタンス自身
     */
    S offset(int offset);

    /**
     * 検索結果がなかった場合、{@link NoResultException}をスローするよう設定します。
     * 
     * @return このインスタンス自身
     */
    S disallowNoResult();

    /**
     * 戻り値またはその要素がLOBであることを指定します。
     * <p>
     * このメソッドの呼び出しは、戻り値またはその要素が{@link String}型の場合に有効です。
     * </p>
     * 
     * @return このインスタンス自身
     */
    S lob();

    /**
     * 戻り値またはその要素の{@link TemporalType}を指定します。
     * <p>
     * このメソッドの呼び出しは、戻り値またはその要素が{@link Date}もしくは{@link Calendar}型の場合に有効です。
     * </p>
     * 
     * @param temporalType
     *            時制の種別
     * @return このインスタンス自身
     */
    S temporal(TemporalType temporalType);

    /**
     * 検索してベースオブジェクトのリストを返します。
     * 
     * @return
     * <p>
     * ベースオブジェクトのリスト。
     * </p>
     * <p>
     * 1件も対象がないときはnullではなく空のリストを返します。
     * </p>
     * @throws OrderByNotFoundRuntimeException
     *             ページング処理で<code>order by</code>が見つからない場合
     * @throws NoResultException
     *             {@link #disallowNoResult()}が呼び出された場合で、検索結果がなかった場合
     */
    List<T> getResultList() throws OrderByNotFoundRuntimeException;

    /**
     * 検索してベースオブジェクトを返します。
     * 
     * @return
     * <p>
     * ベースオブジェクト。
     * </p>
     * <p>
     * 1件も対象がないときはnullを返します。
     * </p>
     * @throws SNonUniqueResultException
     *             検索結果がユニークでない場合。
     * @throws NoResultException
     *             {@link #disallowNoResult()}が呼び出された場合で、検索結果がなかった場合
     */
    T getSingleResult() throws SNonUniqueResultException;

    /**
     * 問い合わせ結果を一件ごとにコールバックに通知します。
     * <p>
     * 問い合わせ結果に含まれる、 基点となるエンティティ({@link JdbcManager#from(Class)}で指定したクラス)またはDTO({@link JdbcManager#selectBySql(Class, String, Object...)}等で指定したクラス)ごとに、
     * {@link IterationCallback#iterate(Object, IterationContext)}メソッドに通知されます。
     * 問い合わせ結果全体のリストを作成しないため、 問い合わせ結果が膨大になる場合でもメモリ消費量を抑えることが出来ます。
     * </p>
     * <h4>SQL自動生成による問い合わせの場合</h4>
     * <p>
     * 問い合わせ結果は基点となるエンティティでソートされている必要があります．
     * 基点となるエンティティがAで、1対多の関連を持つエンティティBを結合した問い合わせの場合、 結果セットは以下のような並びでなくてはなりません。
     * </p>
     * <table border="1">
     * <tr>
     * <th>A</th>
     * <th>B</th>
     * </tr>
     * <tr>
     * <td>A1</td>
     * <td>B1</td>
     * </tr>
     * <tr>
     * <td>A1</td>
     * <td>B2</td>
     * </tr>
     * <tr>
     * <td>A2</td>
     * <td>B1</td>
     * </tr>
     * <tr>
     * <td>A2</td>
     * <td>B2</td>
     * </tr>
     * </table>
     * <p>
     * 上記の問い合わせ結果の場合、
     * {@link IterationCallback#iterate(Object, IterationContext)}メソッドは2回呼び出されます。
     * ただし，A1に関連づけられたB1およびB2と、A1に関連づけられたB1およびB2は同一のインスタンスではなく、 別のインスタンスになります。
     * </p>
     * </p>
     * <table border="1">
     * <tr>
     * <th>A</th>
     * <th>B</th>
     * </tr>
     * <tr>
     * <td>A1</td>
     * <td>B1</td>
     * </tr>
     * <tr>
     * <td>A2</td>
     * <td>B1</td>
     * </tr>
     * <tr>
     * <td>A1</td>
     * <td>B2</td>
     * </tr>
     * <tr>
     * <td>A2</td>
     * <td>B2</td>
     * </tr>
     * </table>
     * <p>
     * 上記の問い合わせ結果の場合、
     * {@link IterationCallback#iterate(Object, IterationContext)}メソッドは4回呼び出されてしまいます。
     * </p>
     * 
     * @param <RESULT>
     *            戻り値の型
     * @param callback
     *            コールバック
     * @return コールバックが最後に返した結果
     */
    <RESULT> RESULT iterate(IterationCallback<T, RESULT> callback);

}