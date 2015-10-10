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

import java.util.List;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * 一つのSQLに複数のパラメータを適用してバッチ実行するためのインターフェースです。
 * <p>
 * バッチ実行された各SQLが挿入／更新／削除した行数の配列を取得する場合は{@link ReturningRowsBatchHandler}を使用してください。
 * </p>
 * 
 * @author higa
 * @see ReturningRowsBatchHandler
 */
public interface BatchHandler {

    /**
     * 一つのSQLに複数のパラメータを適用してバッチ実行します。
     * 
     * @param list
     *            バッチ実行する各SQLに渡されるパラメータの配列からなるリスト
     * @return バッチ実行した数
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    int execute(List list) throws SQLRuntimeException;

    /**
     * 一つのSQLに複数のパラメータを適用してバッチ実行します。
     * 
     * @param list
     *            バッチ実行する各SQLに渡されるパラメータの配列からなるリスト
     * @param argTypes
     *            パラメータのタイプの配列
     * @return バッチ実行した数
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    int execute(List list, Class[] argTypes) throws SQLRuntimeException;

}
