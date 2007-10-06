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

import java.util.Map;

/**
 * SQLを自動生成する更新です。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public interface AutoUpdate<T> extends Update {

    /**
     * 検索を呼び出すクラスを設定します。デフォルトは {@link AutoSelect}の実装クラスです。
     * 
     * @param callerClass
     *            検索を呼び出すクラス
     * @return 自動検索
     */
    AutoUpdate<T> callerClass(Class<?> callerClass);

    /**
     * 検索を呼び出すメソッド名を設定します。デフォルトはgetResultListあるいはgetSingleResultになります。
     * 
     * @param callerMethodName
     *            検索を呼び出すメソッド名
     * @return 自動検索
     */
    AutoUpdate<T> callerMethodName(String callerMethodName);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return 自動検索
     */
    AutoUpdate<T> queryTimeout(int queryTimeout);

    /**
     * バージョンプロパティを更新対象に含めます。
     * <p>
     * このメソッドが呼び出されると、<code>update</code>文の<code>where</code>句にはバージョンのチェックが含まれなくなり、
     * バージョンプロパティは通常のプロパティと同じように更新対象に含められます ({@link #excludeNull()}や{@link #changedFrom(Object)}等も同じように適用されます)。
     * 
     * </p>
     * 
     * @return このインスタンス自身
     */
    AutoUpdate<T> includeVersion();

    /**
     * <code>null</code>値のプロパティを更新対象から除外します。
     * 
     * @return このインスタンス自身
     */
    AutoUpdate<T> excludeNull();

    /**
     * 指定のプロパティのみを更新対象とします。
     * 
     * @param propertyNames
     *            更新対象とするプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoUpdate<T> include(String... propertyNames);

    /**
     * 指定のプロパティを更新対象から除外します。
     * 
     * @param propertyNames
     *            更新対象から除外するプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoUpdate<T> exclude(String... propertyNames);

    /**
     * <code>before</code>から変更のあったプロパティだけを更新対象とします。
     * 
     * @param before
     *            変更前の状態を持つエンティティ
     * @return このインスタンス自身
     */
    AutoUpdate<T> changedFrom(T before);

    /**
     * <code>before</code>から変更のあったプロパティだけを更新対象とします。
     * 
     * @param before
     *            変更前の状態を持つ{@link Map}
     * @return このインスタンス自身
     */
    AutoUpdate<T> changedFrom(Map<String, Object> before);

}
