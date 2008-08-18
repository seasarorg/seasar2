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
package org.seasar.extension.jdbc.gen.command;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.exception.CommandFailedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;

/**
 * コマンドを表すインタフェースです。
 * 
 * @author taedium
 */
public interface Command {

    /**
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    String getConfigPath();

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    void setConfigPath(String configPath);

    /**
     * 環境名を返します。
     * 
     * @return 環境名
     */
    String getEnv();

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    void setEnv(String env);

    /**
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    String getJdbcManagerName();

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    void setJdbcManagerName(String jdbcManagerName);

    /**
     * {@link Factory}の実装クラス名を返します。
     * 
     * @return {@link Factory}の実装クラス名
     */
    String getFactoryClassName();

    /**
     * {@link Factory}の実装クラス名を設定します。
     * 
     * @param factoryClassName
     *            {@link Factory}の実装クラス名
     */
    void setFactoryClassName(String factoryClassName);

    /**
     * 実行します。
     * 
     * @throws CommandFailedRuntimeException
     *             コマンドの実行に失敗した場合
     */
    void execute() throws CommandFailedRuntimeException;

}
