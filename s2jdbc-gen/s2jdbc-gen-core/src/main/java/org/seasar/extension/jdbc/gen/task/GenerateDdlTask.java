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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.command.GenerateDdlCommand;

/**
 * DDLのSQLファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateDdlCommand
 */
public class GenerateDdlTask extends AbstractTask {

    /** コマンド */
    protected GenerateDdlCommand command = new GenerateDdlCommand();

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlTask() {
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
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
     * 外部キーを作成するDDLファイル名を設定します。
     * 
     * @param createForeignKeyDdlFileName
     *            外部キーを作成するDDLファイル名
     */
    public void setCreateForeignKeyDdlFileName(
            String createForeignKeyDdlFileName) {
        command.setCreateForeignKeyDdlFileName(createForeignKeyDdlFileName);
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createForeignKeyTemplateFileName
     *            外部キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateForeignKeyTemplateFileName(
            String createForeignKeyTemplateFileName) {
        command
                .setCreateForeignKeyTemplateFileName(createForeignKeyTemplateFileName);
    }

    /**
     * 一意キーを作成するDDLファイル名を設定します。
     * 
     * @param createUniqueKeyDdlFileName
     *            一意キーを作成するDDLファイル名
     */
    public void setCreateUniqueKeyDdlFileName(String createUniqueKeyDdlFileName) {
        command.setCreateUniqueKeyDdlFileName(createUniqueKeyDdlFileName);
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createUniqueKeyTemplateFileName
     *            一意キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateUniqueKeyTemplateFileName(
            String createUniqueKeyTemplateFileName) {
        command
                .setCreateUniqueKeyTemplateFileName(createUniqueKeyTemplateFileName);
    }

    /**
     * 外部キーを削除するDDLファイル名を設定します。
     * 
     * @param dropForeignKeyDdlFileName
     *            外部キーを削除するDDLファイル名
     */
    public void setDropForeignKeyDdlFileName(String dropForeignKeyDdlFileName) {
        command.setDropForeignKeyDdlFileName(dropForeignKeyDdlFileName);
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropForeignKeyTemplateFileName
     *            外部キーを削除するDDLのテンプレートファイル名
     */
    public void setDropForeignKeyTemplateFileName(
            String dropForeignKeyTemplateFileName) {
        command
                .setDropForeignKeyTemplateFileName(dropForeignKeyTemplateFileName);
    }

    /**
     * 一意キーを削除するDDLファイル名を設定します。
     * 
     * @param dropUniqueKeyDdlFileName
     *            一意キーを削除するDDLファイル名
     */
    public void setDropUniqueKeyDdlFileName(String dropUniqueKeyDdlFileName) {
        command.setDropUniqueKeyDdlFileName(dropUniqueKeyDdlFileName);
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropUniqueKeyTemplateFileName
     *            一意キーを削除するDDLのテンプレートファイル名
     */
    public void setDropUniqueKeyTemplateFileName(
            String dropUniqueKeyTemplateFileName) {
        command.setDropUniqueKeyTemplateFileName(dropUniqueKeyTemplateFileName);
    }

    /**
     * シーケンスを作成するDDLファイル名を設定します。
     * 
     * @param createSequenceDdlFileName
     *            シーケンスを作成するDDLのSQLレートファイル名
     */
    public void setCreateSequenceDdlFileName(String createSequenceDdlFileName) {
        command.setCreateSequenceDdlFileName(createSequenceDdlFileName);
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを作成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        command
                .setCreateSequenceTemplateFileName(createSequenceTemplateFileName);
    }

    /**
     * テーブルを作成するDDLファイル名を設定します。
     * 
     * @param createTableDdlFileName
     *            テーブルを作成するDDLファイル名
     */
    public void setCreateTableDdlFileName(String createTableDdlFileName) {
        command.setCreateTableDdlFileName(createTableDdlFileName);
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createTableTemplateFileName
     *            テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        command.setCreateTableTemplateFileName(createTableTemplateFileName);
    }

    /**
     * シーケンスを削除するDDLファイル名を設定します。
     * 
     * @param dropSequenceDdlFileName
     *            シーケンスを削除するDDLファイル名
     */
    public void setDropSequenceDdlFileName(String dropSequenceDdlFileName) {
        command.setDropSequenceDdlFileName(dropSequenceDdlFileName);
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSequenceTemplateFileName
     *            シーケンスを削除するDDLのテンプレートファイル名
     */
    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        command.setDropSequenceTemplateFileName(dropSequenceTemplateFileName);
    }

    /**
     * テーブルを削除するDDLファイル名を設定します。
     * 
     * @param dropTableDdlFileName
     *            テーブルを削除するDDLファイル名
     */
    public void setDropTableDdlFileName(String dropTableDdlFileName) {
        command.setDropTableDdlFileName(dropTableDdlFileName);
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropTableTemplateFileName
     *            テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        command.setDropTableTemplateFileName(dropTableTemplateFileName);
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
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
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
     * マイグレーションのディレクトリを設定します。
     * 
     * @param migrateDir
     *            マイグレーションのディレクトリ
     */
    public void setMigrateDir(File migrateDir) {
        command.setMigrateDir(migrateDir);
    }

    /**
     * DDLファイルのエンコーディングを設定します。
     * 
     * @param ddlFileEncoding
     *            DDLファイルのエンコーディング
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        command.setDdlFileEncoding(ddlFileEncoding);
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlVersionFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
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
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
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
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * 対象とするエンティティ名の正規表現を設定します。
     * 
     * @param entityNamePattern
     *            対象とするエンティティ名の正規表現
     */
    public void setEntityNamePattern(String entityNamePattern) {
        command.setEntityNamePattern(entityNamePattern);
    }

    /**
     * 対象としないエンティティ名の正規表現を設定します。
     * 
     * @param ignoreEntityNamePattern
     *            対象としないエンティティ名の正規表現
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        command.setIgnoreEntityNamePattern(ignoreEntityNamePattern);
    }

    /**
     * @param dumpDirName
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setDumpDirName(java.lang.String)
     */
    public void setDumpDirName(String dumpDirName) {
        command.setDumpDirName(dumpDirName);
    }

    /**
     * @param schemaInfoColumnName
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setSchemaInfoColumnName(java.lang.String)
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        command.setSchemaInfoColumnName(schemaInfoColumnName);
    }

    /**
     * @param schemaInfoFullTableName
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setSchemaInfoFullTableName(java.lang.String)
     */
    public void setSchemaInfoFullTableName(String schemaInfoFullTableName) {
        command.setSchemaInfoFullTableName(schemaInfoFullTableName);
    }

    /**
     * @param tableOption
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setTableOption(java.lang.String)
     */
    public void setTableOption(String tableOption) {
        command.setTableOption(tableOption);
    }

    /**
     * @param versionNoPattern
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setVersionNoPattern(java.lang.String)
     */
    public void setVersionNoPattern(String versionNoPattern) {
        command.setVersionNoPattern(versionNoPattern);
    }

    /**
     * @param ddlInfoFile
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setDdlInfoFile(java.io.File)
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
    }

    /**
     * @param dumpFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setDumpFileEncoding(java.lang.String)
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * @param dump
     * @see org.seasar.extension.jdbc.gen.command.GenerateDdlCommand#setDump(boolean)
     */
    public void setDump(boolean dump) {
        command.setDump(dump);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
