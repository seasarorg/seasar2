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
public interface AutoUpdate<T> extends Update<AutoUpdate<T>> {

    /**
     * バージョンプロパティを更新対象に含めます。
     * <p>
     * このメソッドが呼び出されると、<code>update</code>文の<code>where</code>句にはバージョンのチェックが含まれなくなり、
     * バージョンプロパティは通常のプロパティと同じように更新対象に含められます ({@link #excludesNull()}や{@link #changedFrom(Object)}等も同じように適用されます)。
     * 
     * </p>
     * 
     * @return このインスタンス自身
     */
    AutoUpdate<T> includesVersion();

    /**
     * <code>null</code>値のプロパティを更新対象から除外します。
     * 
     * @return このインスタンス自身
     */
    AutoUpdate<T> excludesNull();

    /**
     * 指定のプロパティのみを更新対象とします。
     * 
     * @param propertyNames
     *            更新対象とするプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoUpdate<T> includes(String... propertyNames);

    /**
     * 指定のプロパティを更新対象から除外します。
     * 
     * @param propertyNames
     *            更新対象から除外するプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoUpdate<T> excludes(String... propertyNames);

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
    AutoUpdate<T> changedFrom(Map<String, ? extends Object> before);

}
