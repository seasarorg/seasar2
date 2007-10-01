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
package org.seasar.extension.jdbc;

import java.util.List;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

/**
 * SQLを直接指定する検索です。
 * 
 * @author higa
 * 
 */
public interface SqlSelect {

    /**
     * 検索を呼び出すクラスを設定します。デフォルトは {@link SqlSelect}の実装クラスです。
     * 
     * @param callerClass
     *            検索を呼び出すクラス
     * @return SQL検索
     */
    SqlSelect callerClass(Class<?> callerClass);

    /**
     * 検索を呼び出すメソッド名を設定します。デフォルトはgetResultListあるいはgetSingleResultになります。
     * 
     * @param callerMethodName
     *            検索を呼び出すメソッド名
     * @return SQL検索
     */
    SqlSelect callerMethodName(String callerMethodName);

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     * @return SQL検索
     */
    SqlSelect maxRows(int maxRows);

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     * @return SQL検索
     */
    SqlSelect fetchSize(int fetchSize);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return SQL検索
     */
    SqlSelect queryTimeout(int queryTimeout);

    /**
     * リミットを設定します。
     * 
     * @param limit
     *            リミット
     * @return SQL検索
     */
    SqlSelect limit(int limit);

    /**
     * オフセットを設定します。
     * 
     * @param offset
     *            オフセット
     * @return SQL検索
     */
    SqlSelect offset(int offset);

    /**
     * 検索してベースオブジェクトのリストを返します。
     * 
     * @param <T>
     *            ベースの型
     * @return
     *            <p>
     *            ベースオブジェクトのリスト。
     *            </p>
     *            <p>
     *            1件も対象がないときはnullではなく空のリストを返します。
     *            </p>
     * @throws OrderByNotFoundRuntimeException
     *             ページング処理で<code>order by</code>が見つからない場合
     */
    <T> List<T> getResultList() throws OrderByNotFoundRuntimeException;

    /**
     * 検索してベースオブジェクトを返します。
     * 
     * @param <T>
     *            ベースの型
     * @return
     *            <p>
     *            ベースオブジェクト。
     *            </p>
     *            <p>
     *            1件も対象がないときはnullを返します。
     *            </p>
     * @throws SNonUniqueResultException
     *             検索結果がユニークでない場合。
     */
    <T> T getSingleResult() throws SNonUniqueResultException;
}