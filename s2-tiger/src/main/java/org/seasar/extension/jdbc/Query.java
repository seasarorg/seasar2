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

/**
 * 問い合わせのベースとなるインターフェースです。
 * 
 * @author koichik
 * @param <S>
 *            <code>Query</code>のサブタイプです。
 */
public interface Query<S extends Query<S>> {

    /**
     * 検索を呼び出すクラスを設定します。デフォルトは {@link SqlSelect}の実装クラスです。
     * 
     * @param callerClass
     *            検索を呼び出すクラス
     * @return このインスタンス自身
     */
    S callerClass(Class<?> callerClass);

    /**
     * 検索を呼び出すメソッド名を設定します。デフォルトはgetResultListあるいはgetSingleResultになります。
     * 
     * @param callerMethodName
     *            検索を呼び出すメソッド名
     * @return このインスタンス自身
     */
    S callerMethodName(String callerMethodName);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return このインスタンス自身
     */
    S queryTimeout(int queryTimeout);

}
