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

import javax.persistence.OptimisticLockException;

/**
 * SQLを自動生成するバッチ更新です。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public interface AutoBatchUpdate<T> extends BatchUpdate<AutoBatchUpdate<T>> {

    /**
     * バージョンプロパティを更新対象に含めます。
     * <p>
     * このメソッドが呼び出されると、<code>update</code>文の<code>where</code>句にはバージョンのチェックが含まれなくなり、
     * バージョンプロパティは通常のプロパティと同じように更新対象に含められます。
     * 
     * </p>
     * 
     * @return このインスタンス自身
     */
    AutoBatchUpdate<T> includesVersion();

    /**
     * 指定のプロパティのみを更新対象とします。
     * 
     * @param propertyNames
     *            更新対象とするプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoBatchUpdate<T> includes(CharSequence... propertyNames);

    /**
     * 指定のプロパティを更新対象から除外します。
     * 
     * @param propertyNames
     *            更新対象から除外するプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoBatchUpdate<T> excludes(CharSequence... propertyNames);

    /**
     * バージョンチェックを行った場合に、 更新行数が0行でも{@link OptimisticLockException}をスローしないようにします。
     * 
     * @return このインスタンス自身
     */
    AutoBatchUpdate<T> suppresOptimisticLockException();

}
