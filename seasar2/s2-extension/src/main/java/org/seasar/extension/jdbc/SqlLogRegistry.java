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
 * {@link SqlLog}のレジストリを表すインターフェースです。
 * 
 * @author taedium
 */
public interface SqlLogRegistry {

    /**
     * {@link SqlLog}を登録できる上限サイズを返します。
     * 
     * @return 上限サイズ
     */
    int getLimitSize();

    /**
     * このインスタンスが登録している{@link SqlLog}のサイズを返します。
     * 
     * @return {@link SqlLog}のサイズ
     */
    int getSize();

    /**
     * このインスタンスに{@link SqlLog}が登録されていない場合<code>true</code>を返します。
     * 
     * @return {@link SqlLog}が登録されていない場合<code>true</code>、登録されている場合<code>false</code>
     */
    boolean isEmpty();

    /**
     * 指定されたインデックスの{@link SqlLog}を返します。
     * 
     * @param index
     *            インデックス
     * @return 指定されたインデックスの{@link SqlLog}
     */
    SqlLog get(int index);

    /**
     * 最後に記録された{@link SqlLog}を返します。
     * 
     * @return 登録されている場合最後に記録された{@link SqlLog}、登録されていない場合<code>null</code>
     */
    SqlLog getLast();

    /**
     * {@link SqlLog}を追加します。
     * 
     * @param sqlLog
     *            SQLのログ
     */
    void add(SqlLog sqlLog);

    /**
     * すべての{@link SqlLog}をこのレジストリから削除します。
     * 
     */
    void clear();

}
