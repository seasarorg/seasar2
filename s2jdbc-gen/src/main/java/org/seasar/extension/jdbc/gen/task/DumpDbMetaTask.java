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
package org.seasar.extension.jdbc.gen.task;

import org.apache.tools.ant.Task;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.DumpDbMetaCommand;

/**
 * データベースのメタデータをダンプする{@link Task}です。
 * 
 * @author taedium
 * @see DumpDbMetaCommand
 */
public class DumpDbMetaTask extends AbstractTask {

    /** コマンド */
    protected DumpDbMetaCommand command = new DumpDbMetaCommand();

    @Override
    protected Command getCommand() {
        return command;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * {@link Factory}の実装クラス名を設定します。
     * 
     * @param factoryClassName
     *            {@link Factory}の実装クラス名
     */
    public void setFactoryClassName(String factoryClassName) {
        command.setFactoryClassName(factoryClassName);
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        command.setSchemaName(schemaName);
    }

    /**
     * Javaコード生成の対象とするテーブル名の正規表現を設定します。
     * 
     * @param tableNamePattern
     *            Javaコード生成の対象とするテーブル名の正規表現
     */
    public void setTableNamePattern(String tableNamePattern) {
        command.setTableNamePattern(tableNamePattern);
    }

    /**
     * Javaコード生成の対象としないテーブル名の正規表現を設定します。
     * 
     * @param ignoreTableNamePattern
     *            Javaコード生成の対象としないテーブル名の正規表現
     */
    public void setIgnoreTableNamePattern(String ignoreTableNamePattern) {
        this.command.setIgnoreTableNamePattern(ignoreTableNamePattern);
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        command.setGenDialectClassName(genDialectClassName);
    }
}
