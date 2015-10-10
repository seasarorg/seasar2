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

import java.io.File;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.ExecuteSqlCommand;

/**
 * SQLを実行する{@link Task}です。
 * 
 * @author taedium
 * @see ExecuteSqlCommand
 */
public class ExecuteSqlTask extends AbstractTask {

    /** コマンド */
    protected ExecuteSqlCommand command = new ExecuteSqlCommand();

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
     * すでに値が設定された{@link FileList}を追加します。
     * 
     * @param sqlFileList
     *            すでに値が設定された{@link FileList}
     */
    public void addConfiguredSqlFileList(FileList sqlFileList) {
        File dir = sqlFileList.getDir(getProject());
        for (String fileName : sqlFileList.getFiles(getProject())) {
            File file = new File(dir, fileName);
            command.getSqlFileList().add(file);
        }
    }

    /**
     * SQLブロックの区切り文字を設定します。
     * 
     * @param blockDelimiter
     *            SQLブロックの区切り文字
     */
    public void setBlockDelimiter(String blockDelimiter) {
        command.setBlockDelimiter(blockDelimiter);
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
    }

    /**
     * エラー発生時に処理を中止する場合{@code true}を設定します。
     * 
     * @param haltOnError
     *            エラー発生時に処理を中止する場合{@code true}
     */
    public void setHaltOnError(boolean haltOnError) {
        command.setHaltOnError(haltOnError);
    }

    /**
     * SQLファイルのエンコーディングを設定します。
     * 
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        command.setSqlFileEncoding(sqlFileEncoding);
    }

    /**
     * すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        command.setTransactional(transactional);
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
