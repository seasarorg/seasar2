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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;

import javax.persistence.JoinColumn;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.command.GenerateEntityCommand;

/**
 * エンティティクラスのJavaファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateEntityCommand
 */
public class GenerateEntityTask extends AbstractTask {

    /** コマンド */
    protected GenerateEntityCommand command = new GenerateEntityCommand();

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
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * エンティティクラスのテンプレート名を設定します。
     * 
     * @param entityTemplateFileName
     *            エンティティクラスのテンプレート名
     */
    public void setEntityTemplateFileName(String entityTemplateFileName) {
        command.setEntityTemplateFileName(entityTemplateFileName);
    }

    /**
     * 生成するJavaファイルの出力先ディレクトリを設定します。
     * 
     * @param javaFileDestDir
     *            生成するJavaファイルの出力先ディレクトリ
     */
    public void setJavaFileDestDir(File javaFileDestDir) {
        command.setJavaFileDestDir(javaFileDestDir);
    }

    /**
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    /**
     * @param overwrite
     * @see org.seasar.extension.jdbc.gen.internal.command.GenerateEntityCommand#setOverwrite(boolean)
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
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
        command.setIgnoreTableNamePattern(ignoreTableNamePattern);
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    /**
     * バージョンカラム名のパターンを設定します。
     * 
     * @param versionColumnNamePattern
     *            バージョンカラム名のパターンを
     */
    public void setVersionColumnName(String versionColumnNamePattern) {
        command.setVersionColumnNamePattern(versionColumnNamePattern);
    }

    /**
     * 単語を複数系に変換するための辞書ファイルを設定します。
     * 
     * @param pluralFormFile
     *            単語を複数系に変換するための辞書ファイル
     */
    public void setPluralFormFile(File pluralFormFile) {
        command.setPluralFormFile(pluralFormFile);
    }

    /**
     * カタログ名を表示する場合{@code true}を設定します。
     * 
     * @param showCatalogName
     *            カタログ名を表示する場合{@code true}
     */
    public void setShowCatalogName(boolean showCatalogName) {
        command.setShowCatalogName(showCatalogName);
    }

    /**
     * カラム定義を表示する場合{@code true}を設定します。
     * 
     * @param showColumnDefinition
     *            カラム定義を表示する場合{@code true}を設定します。
     */
    public void setShowColumnDefinition(boolean showColumnDefinition) {
        command.setShowColumnDefinition(showColumnDefinition);
    }

    /**
     * カラム名を表示する場合{@code true}を設定します。
     * 
     * @param showColumnName
     *            カラム名を表示する場合{@code true}
     */
    public void setShowColumnName(boolean showColumnName) {
        command.setShowColumnName(showColumnName);
    }

    /**
     * {@link JoinColumn}を表示する場合{@code true}を設定します。
     * 
     * @param showJoinColumn
     *            {@link JoinColumn}を表示する場合{@code true}
     */
    public void setShowJoinColumn(boolean showJoinColumn) {
        command.setShowJoinColumn(showJoinColumn);
    }

    /**
     * スキーマ名を表示する場合{@code true}を設定します。
     * 
     * @param showSchemaName
     *            スキーマ名を表示する場合{@code true}
     */
    public void setShowSchemaName(boolean showSchemaName) {
        command.setShowSchemaName(showSchemaName);
    }

    /**
     * テーブル名を表示する場合{@code true}を設定します。
     * 
     * @param showTableName
     *            テーブル名を表示する場合{@code true}
     */
    public void setShowTableName(boolean showTableName) {
        command.setShowTableName(showTableName);
    }

}
