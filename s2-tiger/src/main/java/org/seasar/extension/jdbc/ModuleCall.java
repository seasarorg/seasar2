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
 * 永続格納モジュール(ストアドプロシージャまたはストアドファンクション)の呼び出しのベースとなるインターフェースです。
 * 
 * @author higa
 * @param <S>
 *            <code>ModuleCall</code>のサブタイプです。
 */
public interface ModuleCall<S extends ModuleCall<S>> extends Query<S> {

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     * @return このインスタンス自身
     */
    S maxRows(int maxRows);

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     * @return このインスタンス自身
     */
    S fetchSize(int fetchSize);

}