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

/**
 * SQLを直接指定する更新(insert, update, delete)です。
 * 
 * @author higa
 * 
 */
public interface SqlUpdate extends Update {

    /**
     * 更新を呼び出すクラスを設定します。デフォルトは {@link SqlUpdate}の実装クラスです。
     * 
     * @param callerClass
     *            更新を呼び出すクラス
     * @return SQL更新
     */
    SqlUpdate callerClass(Class<?> callerClass);

    /**
     * 更新を呼び出すメソッド名を設定します。
     * 
     * @param callerMethodName
     *            更新を呼び出すメソッド名
     * @return SQL更新
     */
    SqlUpdate callerMethodName(String callerMethodName);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return SQL更新
     */
    SqlUpdate queryTimeout(int queryTimeout);

    /**
     * パラメータを設定します。
     * 
     * @param params
     *            パラメータの配列
     * @return SQL更新
     */
    SqlUpdate params(Object... params);
}