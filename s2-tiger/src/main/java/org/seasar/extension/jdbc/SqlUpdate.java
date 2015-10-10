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

import java.io.Serializable;

import org.seasar.extension.jdbc.parameter.Parameter;

/**
 * SQLを直接指定する更新(insert, update, delete)です。
 * 
 * @author higa
 * 
 */
public interface SqlUpdate extends Update<SqlUpdate> {

    /**
     * パラメータを設定します。
     * 
     * @param params
     *            パラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see Parameter
     */
    SqlUpdate params(Object... params);
}