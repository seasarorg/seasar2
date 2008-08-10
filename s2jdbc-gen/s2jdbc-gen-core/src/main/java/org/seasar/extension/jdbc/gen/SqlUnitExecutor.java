/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen;

/**
 * SQLの一まとまりを実行するインタフェースです。
 * 
 * @author taedium
 */
public interface SqlUnitExecutor {

    /**
     * 実行します。
     * 
     * @param <T>
     *            実行結果の型
     * @param unit
     *            一まとまり
     * @return 実行結果
     */
    <T> T execute(ExecutionUnit<T> unit);

    /**
     * 一まとまりを表すインタフェースです。
     * 
     * @author taedium
     * 
     * @param <T>
     *            実行結果の型
     */
    interface ExecutionUnit<T> {

        T execute(SqlExecutionContext context);
    }
}
